package xinrui.cloud.service.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import xinrui.cloud.service.*;
import xinrui.cloud.util.AppUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import xinrui.cloud.BootException;
import xinrui.cloud.domain.dto.PolicyActivityDto;
import xinrui.cloud.domain.dto.ProblemDto;
import xinrui.cloud.domain.dto.ProblemDto2;
import xinrui.cloud.domain.dto.ProblemModelDto;
import xinrui.cloud.enums.ProblemStatus;
import xinrui.cloud.util.DataUtil;
import xinrui.cloud.domain.*;

/**
 * <B>Title:</B>ActivityServiceImpl.java</br>
 * <B>Description:</B> 政策匹配的问题编辑等 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年3月28日
 */
@Service
public class ActivityServiceImpl extends BaseServiceImpl<PolicyActivity> implements ActivityService {

	private final static Logger LOGGER = LoggerFactory.getLogger(ActivityServiceImpl.class);

	@Autowired
    PolicyTemplateService policytemplateService;

	@Autowired
    ProblemService problemService;

	@Autowired
    ProblemModelService problemModelService;

	@Autowired
    ProblemLimitService problemLimitService;

	@Autowired
    AppUtils appUtils;

	@Autowired
    PolicyService policyService;

	/**
	 * 根据政策匹配模板对象{@link PolicyTemplate#getId()}的id获取到{@link PolicyActivity}政策环节对象
	 *
	 * @param tempId {@link PolicyTemplate#getId()}政策模板对象的id
	 * @return {@link PolicyActivity}政策环节对象
	 */
	@Transactional(readOnly = true)
	public PolicyActivity getByTemp(Long tempId) {
		PolicyTemplate temp = policytemplateService.findById(tempId);
		if (temp == null) {
			throw new BootException("未曾创建模板!");
		}
		return temp.getActivity();
	}

	@Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = BootException.class)
	public List<ProblemDto> addProblem(Long tempId, Integer status, String title, String value, Integer max,
			Boolean last, Boolean isAmplitude) {
		PolicyActivity activity = getByTemp(tempId);
		Problem result = null;
		switch (status) {
		case 0:
			try {
				ProblemDto2 parseObject = JSON.parseObject(value, ProblemDto2.class);
				result = toProblem(parseObject);
			} catch (Exception e) {
				throw new BootException(e, "json字符串解析错误!");
			}
			break;
		case 1:
			result = new Problem(value, ProblemStatus.SINGLE, title, max, last, isAmplitude);
			break;
		case 2:
			result = new Problem(ProblemStatus.TEXT, title, value);
			break;
		case 3:
			result = new Problem(ProblemStatus.EMPTY, title, value);
			break;
		default:
			break;
		}
		result.setActivity(activity);
		activity.getProblems().add(result);
		return ProblemDto.copyList(merge(activity).getProblems());
	}

	/**
	 * 将{@link ProblemDto2}类型及其子集合转换成javabean对象(父级持久态),子级瞬时态
	 *
	 * @param resource 需要被转换的对象
	 * @return 持久态对象
	 */
	public Problem toProblem(ProblemDto2 resource) {
		Problem problem = toProblem2(resource);
		List<ProblemModelDto> first = resource.getFirst();
		List<ProblemModelDto> second = resource.getSecond();
		List<ProblemModel> entity = ProblemModelDto.toEntity(first);
		List<ProblemModel> mereObjs = Lists.newArrayList();

		for (ProblemModel problemModel : entity) {
			problemModel.setProblem(problem);
			mereObjs.add(problemModelService.merge(problemModel));
		}

		if (!CollectionUtils.isEmpty(second)) {
			ProblemModel problemModel2 = mereObjs.get(0);
			List<ProblemModel> transientList = ProblemModelDto.toEntity(second);
			for (ProblemModel problemModel : transientList) {
				problemModel.setParent(problemModel2);
			}
			problemModel2.getChildren().addAll(transientList);
			problemModelService.merge(problemModel2);
		}
		return problem;
	}

	public Problem toProblem2(ProblemDto2 resource) {
		if (resource == null) {
			return null;
		}
		Assert.notNull(problemModelService, "problemService 不能为空！");
		List<ProblemModelDto> first = resource.getFirst();
		if (CollectionUtils.isEmpty(first)) {
			throw new BootException("一级选项不能玩为空");
		}
		Problem problem = new Problem();
		BeanUtils.copyProperties(resource, problem, "first", "second", "problem", "child");
		return problemService.merge(problem);
	}

	@Override
	@Transactional(readOnly = true)
	public PolicyActivityDto findByTemp(Long tempId) {
		return PolicyActivityDto.copyFrom(getByTemp(tempId));
	}

	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = BootException.class)
	public PolicyTemplate removeActivityById(Long activityId) {
		PolicyActivity findById = findById(activityId);
		if (findById == null) {
			throw new BootException("该环节不存在或者已经被删除！");
		}
		PolicyTemplate template = findById.getTemplate();
		if (template == null) {
			throw new BootException("该环节不存在或者已经被删除！");
		}
		template.setActivity(null);
		remove(findById);
		return template;
	}

	@Override
	public ProblemDto2 beforeEditSingleRadion(Long problemId) {
		Problem findById = problemService.findById(problemId);
		if (findById == null) {
			throw new BootException("该问题模板不存在！");
		}
		return ProblemDto2.copy(findById);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ProblemDto editProblem(Long problemId, Integer status, String title, String value, Integer max, Boolean last,
			Boolean isAmplitude) {
		Problem pb = problemService.findById(problemId);
		int agoStatus = pb.getStatus();
		if (agoStatus != status) {
			clearChildren(pb);
		}
		if (agoStatus == ProblemStatus.SINGLE_RADIO.getStatus() && status != ProblemStatus.SINGLE_RADIO.getStatus()) { // 单选改称其他的
			pb.setStatus(status);
			pb.setName(value);
			pb.setTitle(title);
			return ProblemDto.copy(problemService.merge(pb));
		}
		if (agoStatus != ProblemStatus.SINGLE_RADIO.getStatus() && status == ProblemStatus.SINGLE_RADIO.getStatus()) {
			pb.setName(null);
		}
		Problem result = null;
		switch (status) {
		case 0:
			try {
				ProblemDto2 parseObject = JSON.parseObject(value, ProblemDto2.class);
				if (pb.getChildStatus() != parseObject.getChildStatus()) { // 当二级项的类型修改,也删除关联数据
					clearLimit(pb);
				}
				result = jsonToEntity(parseObject, pb);
				result.setStatus(ProblemStatus.SINGLE_RADIO);
			} catch (Exception e) {
				throw new BootException(e, "json字符串解析错误!");
			}
			break;
		case 1:
			setValue(title, value, pb, ProblemStatus.SINGLE);
			pb.setMax(max);
			pb.setLast(last);
			pb.setAmplitude(isAmplitude);
			break;
		case 2:
			setValue(title, value, pb, ProblemStatus.TEXT);
			break;
		case 3:
			setValue(title, value, pb, ProblemStatus.EMPTY);
			break;
		default:
			break;
		}
		if (result != null) { // 一旦类型发生改变则将以后的内容全部清空
			if (pb.getStatus() != result.getStatus() || pb.getChildStatus() != result.getChildStatus()) {
				pb.setChild(null);
				pb.setProblemLimits(null);
			}
		}
		return ProblemDto.copy(problemService.merge(pb));
	}

	/**
	 * 清除所有子集关联,并且删除子集,当问题对象{@link Problem}的状态修改的时候调用
	 *
	 * @param pb 问题数据模板对象
	 */
	private void clearChildren(Problem pb) {
		List<ProblemModel> child = pb.getChild();
		for (ProblemModel problemModel : child) {
			problemModel.setProblem(null);
			problemModelService.remove(problemModel);
		}
		child.clear();
		clearLimit(pb);
	}

	/**
	 * 清楚所有问题限制关联
	 *
	 * @param pb 问题数据模板对象
	 */
	private void clearLimit(Problem pb) {
		List<ProblemLimit> problemLimits = pb.getProblemLimits();
		for (ProblemLimit problemLimit : problemLimits) {
			problemLimit.setProblem(null);
			problemLimitService.remove(problemLimit);
		}
		problemLimits.clear();
	}

	/**
	 * 将前端传递的json解析对象与数据库当前的对象作比较，并且做删除增加和修改等操作
	 * 1.如果是来自数据库的实体，需要删除则先解除与任何实体的数据关联，并且先删除子集 </br>
	 * 2.父级与子级的级联操作，需要父级清空集合，和子集解除关联
	 *
	 * @param dto 数据源
	 * @param pb  目标数据
	 * @return 修改后的问题编辑数据模板对象{@link Problem}
	 */
	@SuppressWarnings("AlibabaMethodTooLong")
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
	public Problem jsonToEntity(ProblemDto2 dto, Problem pb) {
		BeanUtils.copyProperties(dto, pb, "id", "child", "first", "status", "second");
		List<ProblemModel> child = pb.getChild(); // 一级选项
		ProblemModel firstChild;
		if (CollectionUtils.isEmpty(child)) {
			firstChild = new ProblemModel();
		} else {
			firstChild = child.get(0);
		}

		List<ProblemModel> secondList = firstChild.getChildren(); // 数据库的二级选项
		List<ProblemModelDto> firstListFromDto = dto.getFirst(); // 这是dto的一级选项
		List<ProblemModelDto> secondListFromDto = dto.getSecond(); // 这是dto的二级选项

		List<ProblemModel> finalFirst = Lists.newArrayList(); // 修改了的需要保存的
		CopyOnWriteArrayList<ProblemModel> finalFirstLessListFromDto = new CopyOnWriteArrayList<>(child); // 数据库中需要删除的
		List<ProblemModel> finalLessListFromDto = Lists.newArrayList(); // 需要被添加的
		DataUtil.subselect(firstListFromDto, finalFirst, finalFirstLessListFromDto, finalLessListFromDto);

		List<ProblemModel> finalSecond = Lists.newArrayList(); // 修改了的需要保存的
		CopyOnWriteArrayList<ProblemModel> secondFianlLessList = new CopyOnWriteArrayList<>(secondList); // 最终需要删除的
		List<ProblemModel> finalLessListFromDto2 = Lists.newArrayList(); // 需要被添加的
		DataUtil.subselect(secondListFromDto, finalSecond, secondFianlLessList, finalLessListFromDto2);

		LOGGER.info("一级的被修改的" + finalFirst);
		LOGGER.info("一级的被删除的" + finalFirstLessListFromDto);
		LOGGER.info("一级的被添加的的" + finalLessListFromDto);
		LOGGER.info("二级的被修改的" + finalSecond);
		LOGGER.info("二级的被删除的" + secondFianlLessList);
		LOGGER.info("二级的被添加的的" + finalLessListFromDto2);

		// 删除未曾被匹配的数据库里的(先删除子集,再删除父级)
		for (ProblemModel problemModel : secondFianlLessList) {
			LOGGER.info("删除二级的" + problemModel.getId());
			problemModel.setParent(null);
			problemModelService.remove(problemModel);
			firstChild.getChildren().remove(problemModel);
			removeLimitsByPb(pb);
		}

		for (ProblemModel problemModel : finalFirstLessListFromDto) {
			LOGGER.info("删除一级的" + problemModel.getId());
			problemModel.setProblem(null);
			problemModelService.remove(problemModel);
			pb.getChild().remove(problemModel);
			removeLimitsByPb(pb);
		}

		problemService.merge(pb);
		finalFirst.addAll(finalLessListFromDto);
		int count = 0;
		List<Long> editIdList = Lists.newArrayList();
		for (ProblemModel problemModel : finalFirst) {
			if (problemModel.getId() == null) {
				problemModel.setProblem(pb);
				LOGGER.info("一级存储的:" + (++count));
			} else {
				problemModel.getChildren().clear();
				Long id = problemModel.getId();
				LOGGER.info("一级修改的:" + id);
				editIdList.add(id);
				removeLimitsByPb(pb);
			}
			pb.getChild().add(problemModel);
		}
		Problem merge = problemService.merge(pb);
		List<ProblemModel> newChild = merge.getChild();
		if (CollectionUtils.isEmpty(newChild)) { // 代表没有一级选项
			throw new BootException("一级选项不能为空!");
		}
		ProblemModel first = merge.getChild().get(0);
		first.getChildren().clear();
		count = 0;
		finalSecond.addAll(finalLessListFromDto2);
		for (ProblemModel problemModel : finalSecond) {
			if (problemModel.getId() == null) {
				LOGGER.info("二级存储的:" + (++count));
			} else {
				LOGGER.info("二级修改的:" + problemModel.getId());
				removeLimitsByPb(pb);
			}
			problemModel.setParent(first);
			first.addChild(problemModel);
		}
		return problemService.merge(pb);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void removeLimitsByPb(Problem pb) {
		Problem byId = problemService.findById(pb.getId());
		List<ProblemLimit> problemLimits = byId.getProblemLimits();
		CopyOnWriteArrayList<ProblemLimit> limits = new CopyOnWriteArrayList<>(problemLimits);
		for (ProblemLimit limit : limits) {
			Long id = limit.getId();
			if (id != null) {
				problemLimits.remove(limit);
				problemLimitService.remove(limit);
			}
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void removeByModel(Problem pb, ProblemModel problemModel) {
		List<ProblemLimit> problemLimits = pb.getProblemLimits();
		StringBuffer buffer = new StringBuffer();
		for (ProblemLimit limit : problemLimits) {
			limit = problemLimitService.findById(limit.getId());
			buffer.setLength(0);
			policyService.add2FinalLogicInner(limit, buffer);
			String name = problemModel.getName();
			if (buffer.toString().contains(name)) {
				// 删除所有的限制公式对象
				List<ProblemLimit> children = limit.getChildren();
				for (ProblemLimit limitChild : children) {
					LOGGER.info("删除的限制集中的某一个" + limitChild.getId());
					problemLimitService.remove(limitChild.getId());
				}
//                limit.getChildren().clear();
				// 设空
				Field[] declaredFields = limit.getClass().getDeclaredFields();
				for (Field field : declaredFields) {
					if (DataUtil.isBaseType(field.getType())) {
						DataUtil.invokeSetByObject(limit, field);
					}
				}
				// 删除变量
				ProblemLimitInner variable = limit.getVariables();
				if (variable != null) {
					List<ProblemLimitInner> variables = variable.getChildren();
					for (ProblemLimitInner inner : variables) {
						if (inner.getVariableId().intValue() == problemModel.getId().intValue()) {
							dao.remove(ProblemLimitInner.class, inner);
							break;
						}
					}
				}
			}
			// 删除触发结构的
//            ProblemTrigger trigger = limit.getTrigger();
//            if (trigger != null) {
//                List<ProblemTriggerInner> child = trigger.getChild();
//                for (ProblemTriggerInner inner:child ){
//                    String logicTwo = inner.getLogicTwo();
//                    if(logicTwo.contains(name)){
//
//                    }
//                }
//            }
			// 删除触发结构修饰的
			problemLimitService.merge(limit);
		}
	}

	/**
	 * 当问题编辑数据模板对象的状态发生改变时调用，做到重新赋值,并且修改变量名称做到与后面的与之关联
	 *
	 * @param title  新的标题
	 * @param value  新的变量名称
	 * @param pb     问题编辑数据模板对象
	 * @param status 新的状态
	 */
	private void setValue(String title, String value, Problem pb, ProblemStatus status) {
		pb.setTitle(title);
		if (pb.getStatus() != ProblemStatus.SINGLE.getStatus()) {
			StringBuffer buffer = new StringBuffer();
			List<ProblemLimit> problemLimits = pb.getProblemLimits();
			// 问题限制
			for (ProblemLimit limitFather : problemLimits) {
				List<ProblemLimit> children = limitFather.getChildren();
				buffer.setLength(0);
				policyService.add2FinalLogicInner(limitFather, buffer);
				if (buffer.toString().contains(pb.getName())) {
					for (ProblemLimit limitChild : children) {
						String logicalFormLeft = limitChild.getLogicalFormLeft();
						String logicalFormRight = limitChild.getLogicalFormRight();
						if (StringUtils.isNotBlank(logicalFormLeft)) {
							limitChild.setLogicalFormLeft(logicalFormLeft.replace(pb.getName(), value));
						}
						if (StringUtils.isNotBlank(logicalFormRight)) {
							limitChild.setLogicalFormRight(logicalFormRight.replace(pb.getName(), value));
						}
					}

					ProblemLimitInner variable = limitFather.getVariables();
					if (variable != null) {
						setInner(variable.getChildren(), pb, value);
					}
				}
				setTrigger(value, pb, limitFather);
			}
		}
		pb.setName(value);
		pb.setStatus(status);
	}

	private void setTrigger(String value, Problem pb, ProblemLimit limitFather) {
		if (limitFather == null) {
			return;
		}
		// 更改触发器的
		ProblemTrigger trigger = limitFather.getTrigger();
		if (trigger != null) {
			String logic = trigger.getLogic();
			if (StringUtils.isNotBlank(logic)) {
				trigger.setLogic(logic.replace(pb.getName(), value));
			}
			setInner(trigger.getVariables(), pb, value);
			ProblemTriggerResult triggerResult = trigger.getTriggerResult();
			if (triggerResult != null) {
				String logic2 = triggerResult.getLogic();
				if (StringUtils.isNotBlank(logic2)) {
					triggerResult.setLogic(logic2.replace(pb.getName(), value));
				}
			}
		}
	}

	/**
	 * 设置内部的值
	 *
	 * @param variables
	 * @param pb
	 * @param value
	 */
	private void setInner(List<ProblemLimitInner> variables, Problem pb, String value) {
		for (ProblemLimitInner inner : variables) {
			String name = inner.getName();
			if (StringUtils.isNotBlank(name)) {
				inner.setName(name.replace(pb.getName(), value));
			}
		}
	}

}
