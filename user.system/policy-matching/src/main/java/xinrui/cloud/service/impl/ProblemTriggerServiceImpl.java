package xinrui.cloud.service.impl;

import xinrui.cloud.domain.*;
import xinrui.cloud.domain.dto.ProblemLimitInnerDto;
import xinrui.cloud.domain.dto.ProblemModelDto;
import xinrui.cloud.domain.dto.ProblemTriggerDto;
import xinrui.cloud.domain.dto.ProblemTriggerInnerDto;
import xinrui.cloud.dto.TreeDto;
import xinrui.cloud.enums.ProblemStatus;
import xinrui.cloud.service.ActivityService;
import xinrui.cloud.service.ProblemLimitService;
import xinrui.cloud.service.ProblemTriggerService;
import xinrui.cloud.util.AppUtils;
import xinrui.cloud.util.DataUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Random;

/**
 * <B>Title:</B>ProblemTriggerServiceImpl.java</br>
 * <B>Description:</B> 触发结构接口实现类 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年4月4日
 */
@Service
public class ProblemTriggerServiceImpl extends BaseServiceImpl<ProblemTrigger> implements ProblemTriggerService {

    @Autowired
    ProblemLimitService limitService;

    @Autowired
    AppUtils appUtils;

    @Autowired
    ActivityService activityService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ, readOnly = false)
    public ProblemTriggerDto add(Long limitId, ProblemTriggerDto parseObject) {
        ProblemLimit limit = limitService.findById(limitId);
        Assert.notNull(limit, "问题限制对象不能为空!");
        Problem problem = limit.getProblem();
        Assert.notNull(problem, "问题编辑对象不能为空!");
        // 将当前json解析的所有可复制的属性复制到pojo实体中，并且保存此实体
        ProblemTrigger trigger = new ProblemTrigger();
        BeanUtils.copyProperties(parseObject, trigger, "child", "triggerResult", "limit", "variables");
        trigger.setLimit(limit);
        ProblemTrigger mergeTrigger = merge(trigger);
        limit.setTrigger(mergeTrigger);
        limitService.merge(limit);

        return ProblemTriggerDto.copy(merge(setParamas(parseObject, mergeTrigger)));
    }

    /**
     * 将变量集合和子选项集合全部设置进去
     *
     * @param parseObject  前端接收的由json转换的数据传输对象
     * @param mergeTrigger 出发结构持久态对象
     * @return 更新之后的出发结构持久态对象
     */
    private ProblemTrigger setParamas(ProblemTriggerDto parseObject, ProblemTrigger mergeTrigger) {
        //保存当前填写的公式
        DataUtil.copyProblemTriggerInnerChild(parseObject.getChild(), dao, mergeTrigger);
        updateVriables(parseObject, mergeTrigger);

        //将已经持久态的所有关联对象关联到当前触发结构对象
        ProblemTrigger merge = merge(mergeTrigger);
        setFinalLogic(merge);
        return merge;
    }


    /**
     * 将所选变量个更新
     *
     * @param parseObject  前端传递过来的json对象
     * @param mergeTrigger 当前持久态的{@link ProblemTrigger}对象
     */
    private void updateVriables(ProblemTriggerDto parseObject, ProblemTrigger mergeTrigger) {
        // 保存当前选中的变量
        List<ProblemLimitInnerDto> variables = parseObject.getVariables();
        List<ProblemLimitInner> problemLimitInners = TreeDto.copyList(ProblemLimitInner.class, variables);
        for (ProblemLimitInner inner : problemLimitInners) {
            inner.setTrigger(mergeTrigger);
            mergeTrigger.getVariables().add(inner);
        }
    }

    /**
     * 设置最终的公式到{@link #merge}对象中
     *
     * @param merge
     */
    private void setFinalLogic(ProblemTrigger merge) {
        List<ProblemTriggerInner> child = merge.getChild();
        if (!CollectionUtils.isEmpty(child)) {
            if (merge.getStatus() != ProblemStatus.SINGLE_RADIO.getStatus()) {     //代表是单选
                ProblemTriggerInner problemTriggerInner = child.get(0);           //非单选只有一个选项
                merge.setFinalLogic("<h1>" + problemTriggerInner.getLogicTwo() + "</h1>");
            } else {
                StringBuffer buffer = new StringBuffer();
                for (ProblemTriggerInner inner : child) {
                    String logicTwo1 = inner.getLogicTwo();
                    if (!StringUtils.isEmpty(logicTwo1)) {
                        buffer.append("<h1>").append(logicTwo1).append("</h1>");
                    }
                    List<ProblemTriggerInner> children = inner.getChildren();
                    for (ProblemTriggerInner innerChild : children) {
                        String logicTwo = innerChild.getLogicTwo();
                        if (!StringUtils.isEmpty(logicTwo)) {
                            buffer.append("<h2>").append(logicTwo).append("</h2>");
                        }
                    }
                }
                if (buffer.length() != 0) {
                    merge.setFinalLogic(buffer.toString());
                }
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ, readOnly = false)
    public void remove(Long id) {
        ProblemTrigger trigger = findById(id);
        Assert.notNull(trigger, "问题限制对象为空!");
        super.remove(trigger);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public ProblemTriggerDto listDataBeForAdd(Long limitId) {
        ProblemLimit limit = limitService.findById(limitId);
        Assert.notNull(limit, "问题限制对象找不到!");
        Problem problem = limit.getProblem();
        ProblemTriggerDto trigger = new ProblemTriggerDto();
        int status = problem.getStatus();
        trigger.setStatus(status);
        trigger.setChildStatus(problem.getChildStatus());

        //将所有的id设置为随机数
        Random random = new Random();
        List<ProblemModelDto> copyList = ProblemModelDto.copyList(problem.getChild());
        for (ProblemModelDto problemModelDto : copyList) {
            List<ProblemModelDto> children = problemModelDto.getChildren();
            for (ProblemModelDto problemModelDto2 : children) {
                problemModelDto2.setId(random.nextLong());
            }
        }
        trigger.setChild(TreeDto.copyList(ProblemTriggerInnerDto.class, copyList, 2));
        return trigger;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ, readOnly = false)
    public ProblemTriggerDto edit(Long triggerId, ProblemTriggerDto dto) {
        ProblemTrigger trigger = findById(triggerId);
        Assert.notNull(trigger, "问题限制对象找不到!");
        BeanUtils.copyProperties(dto, trigger, "child", "triggerResult", "limit", "variables");

        //将之前的变量集合以及选项集合清空然后再添加新的变量集合和选项集合
        trigger.getVariables().clear();
        trigger.getChild().clear();
        ProblemTrigger merge = merge(trigger);
        return ProblemTriggerDto.copy(merge(setParamas(dto, merge)));
    }


}
