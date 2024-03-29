package xinrui.cloud.service.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

import xinrui.cloud.BootException;
import xinrui.cloud.domain.NameEntity;
import xinrui.cloud.domain.Problem;
import xinrui.cloud.domain.ProblemLimit;
import xinrui.cloud.domain.ProblemLimitInner;
import xinrui.cloud.domain.ProblemModel;
import xinrui.cloud.domain.TreeEntity;
import xinrui.cloud.domain.dto.ProblemLimitDataDto;
import xinrui.cloud.domain.dto.ProblemLimitDto;
import xinrui.cloud.domain.dto.ProblemLimitInnerDto;
import xinrui.cloud.enums.ProblemStatus;
import xinrui.cloud.service.ProblemLimitService;
import xinrui.cloud.service.ProblemModelService;
import xinrui.cloud.service.ProblemService;
import xinrui.cloud.util.AppUtils;
import xinrui.cloud.util.DataUtil;

/**
 * <B>Title:</B>ProblemLimitServiceImpl.java</br>
 * <B>Description:</B>问题限制接口实现类 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年4月2日
 */
@Service
@PropertySource("classpath:policy.properties")
@SuppressWarnings("unchecked")
public class ProblemLimitServiceImpl extends BaseServiceImpl<ProblemLimit> implements ProblemLimitService {

	@Autowired
	ProblemService ProblemService;

	@Autowired
	ProblemModelService problemModelService;

	@Autowired
	AppUtils apputils;

	private final static Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ProblemLimitServiceImpl.class);

	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true, rollbackFor = BootException.class)
	public ProblemLimitDataDto listBaseData(Long problemId) {
		Problem problem = ProblemService.findById(problemId);
		if (problem == null) {
			throw new BootException("当前问题编辑模板不存在！");
		}
		return new ProblemLimitDataDto(addInner(new ProblemLimitInnerDto(), problem, false), apputils.getResult());
	}

	/**
	 * @param e       需要被复制的源对象
	 * @param problem 问题编辑对象
	 * @param needIds 为true代表强制从{@link #ids}中的id去获取,为false代表取所有的二级选项
	 * @return 最终设置了子集的对象实体
	 */
	private synchronized <E extends TreeEntity<E>> E addInner(E e, Problem problem, boolean needIds, String... ids) {
		int status = problem.getStatus();
		LOGGER.info("当前的问题编辑所属状态:" + status);
		LOGGER.info("当前的问题编辑二级选项所属状态:" + problem.getChildStatus());
		LOGGER.info("当前的调用者需要强制从ids中查找么:" + needIds);
		LOGGER.info("需要遍历查找的ids为:" + ids);

		List<Map<String, Object>> result = apputils.getResult();
		if (status == ProblemStatus.SINGLE_RADIO.getStatus()
				&& problem.getChildStatus() == ProblemStatus.TEXT.getStatus()) {
			List<ProblemModel> children = getProblemModels(needIds, result, ids);
			if (!needIds) {
				children = problem.getChild().get(0).getChildren();
//                if (CollectionUtils.isEmpty(children))
//                    throw new BootException("未找到填写的变量值");
			}
			add(e, children.toArray(new ProblemModel[children.size()]));
		} else if (status == ProblemStatus.TEXT.getStatus()) {
			LOGGER.info("当前是填空类型的一级选项!");
			add(e, problem); // 添加了当前的
			if (ids.length != 1) {
				String[] objects = (String[]) ArrayUtils.removeElement(ids, problem.getId().toString());
				LOGGER.info("删除之后的个数" + objects.length);
				List<ProblemModel> children = getProblemModels(needIds, result, objects);
				add(e, children.toArray(new ProblemModel[children.size()]));
			}
		} else {
			if (ids != null && ids.length != 0 && StringUtils.isNotBlank(ids[0])) {
				LOGGER.info("当前的状态:" + problem.getStatus());
				List<ProblemModel> children = getProblemModels(needIds, result, ids);
				add(e, children.toArray(new ProblemModel[children.size()]));
			}
		}
		return e;
	}

	private List<ProblemModel> getProblemModels(boolean needIds, List<Map<String, Object>> result, String[] ids) {
		List<ProblemModel> children = Lists.newArrayList();
		ProblemModel findById = null;
		if (needIds) {
			for (String needId : ids) {
				if (StringUtils.isBlank(needId)
						|| (findById = problemModelService.findById(Long.parseLong(needId))) == null) {
					for (Map<String, Object> map : result) {
						Object object = map.get("id");
						LOGGER.info("当前的被遍历的id为:" + object + "\tids的为:" + needId);
						if (object.toString().equals(needId)) {
							children.add(new ProblemModel(Long.parseLong(needId), map.get("name").toString()));
							break;
						}
					}
				} else {
					children.add(findById);
				}
			}
		}
		return children;
	}

	/**
	 * 根据{@link #lists}对象遍历所有的NameEntity,并且将id和名称获取到,并且生成到{@link TreeEntity}树结构对象
	 *
	 * @param inner {@link TreeEntity}类型的树结构对象
	 * @param lists {@link NameEntity}类型的集合对象
	 */
	@SuppressWarnings("rawtypes")
	public <E extends TreeEntity<E>> E add(E inner, NameEntity... lists) {
		LOGGER.info("当前需要遍历的可变参大小:" + lists.length);
		E e = null;
		Constructor<? extends TreeEntity> constructor = null;
		try {
			constructor = inner.getClass().getConstructor(Long.class, String.class);
		} catch (NoSuchMethodException t) {
			t.printStackTrace();
		}
		for (NameEntity nameEntity : lists) {
			try {
				try {
					LOGGER.info("\r\n\t类型为:" + nameEntity.getClass() + "\r\n\t主键为" + nameEntity.getId() + "\r\n\t名称为"
							+ nameEntity.getName());
					e = (E) constructor.newInstance(nameEntity.getId(), nameEntity.getName());
					LOGGER.info("最终产生的结果为:" + e);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e1) {
					e1.printStackTrace();
				}
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
			if (e != null) {
				e.setParent(inner);
				inner.getChildren().add(e);
			}
		}
		return e;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ, readOnly = false, rollbackFor = BootException.class)
	public List<ProblemLimitDto> addLimitData(Long problemId, List<ProblemLimit> list, String ids) {
		Problem problem = ProblemService.findById(problemId);
		Assert.notNull(problem, "问题编辑模板对象不存在 ！");
		if (problem.getProblemLimits().size() >= apputils.getMaxSize()) { // 只需要这里判断即可，因为后面的都是一对一limit对象
			throw new BootException("当前的问题限制模板已经创建了5个！");
		}
		ProblemLimit limit = new ProblemLimit();
		if (CollectionUtils.isEmpty(list)) {
            return ProblemLimitDto.copy(problem.getProblemLimits());
        }
		for (ProblemLimit problemLimit : list) {
			problemLimit.setParent(limit);
			limit.addChild(problemLimit);
		}
		limit.setProblem(problem);
		/**
		 * 先持久化limit对象
		 */
		ProblemLimit limitMerge = merge(limit);

		/**
		 * 先持久化父级对象problemLimitInnerMerge
		 */
		ProblemLimitInner problemLimitInnerMerge = dao.merge(ProblemLimitInner.class, new ProblemLimitInner());
		List<String> split = Lists.newArrayList();
		if (ids != null) {
			split = DataUtil.split(ids);
		}
		ProblemLimitInner addInner = addInner(problemLimitInnerMerge, problem, true,
				split.toArray(new String[split.size()]));
		LOGGER.info("当前这个的子集合" + addInner.getChildren().size());
		problemLimitInnerMerge = dao.merge(ProblemLimitInner.class, addInner);
		problemLimitInnerMerge.setLimit(limitMerge);
		limitMerge.setVariables(problemLimitInnerMerge);
		merge(limitMerge);
		problem.getProblemLimits().add(limitMerge);
		return ProblemLimitDto.copy(ProblemService.merge(problem).getProblemLimits());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ, readOnly = false, rollbackFor = BootException.class)
	public ProblemLimitDto edit(Long limitId, List<ProblemLimit> listFromDto, String ids) {
		ProblemLimit limit = findById(limitId);
		Assert.notNull(limit, "问题限制对象找不到!");
		List<ProblemLimit> needAddList = Lists.newArrayList();
		List<ProblemLimit> needEditList = Lists.newArrayList();
		CopyOnWriteArrayList<ProblemLimit> needRemoveList = new CopyOnWriteArrayList<>(limit.getChildren());
		DataUtil.subselect2(listFromDto, needEditList, needRemoveList, needAddList);
		for (ProblemLimit problemLimit : needRemoveList) {
			problemLimit.setParent(null);
			remove(problemLimit);
			limit.getChildren().remove(problemLimit);
		}
		needEditList.addAll(needAddList);
		for (ProblemLimit problemLimit : needEditList) {
			if (problemLimit.getId() == null) {
				problemLimit.setParent(limit);
				limit.addChild(problemLimit);
			}
			merge(problemLimit);
		}
		return ProblemLimitDto.copy(merge(limit));
	}

}
