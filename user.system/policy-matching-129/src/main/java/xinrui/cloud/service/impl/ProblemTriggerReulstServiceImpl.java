package xinrui.cloud.service.impl;

import xinrui.cloud.domain.*;
import xinrui.cloud.domain.dto.ProblemTriggerResultDto;
import xinrui.cloud.service.ActivityService;
import xinrui.cloud.service.ProblemTriggerResultService;
import xinrui.cloud.service.ProblemTriggerService;
import xinrui.cloud.util.DataUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * <B>Title:</B>ProblemTriggerReulstServiceImpl.java</br>
 * <B>Description:</B>触发结果接口实现 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年4月4日
 */
@Service
public class ProblemTriggerReulstServiceImpl extends BaseServiceImpl<ProblemTriggerResult>
        implements ProblemTriggerResultService {

    @Autowired
    ProblemTriggerService triggerService;

    @Autowired
    ActivityService activityService;

    @Override
    public ProblemTriggerResultDto add(Long triggerId, ProblemTriggerResultDto parseObject) {
        ProblemTrigger trigger = triggerService.findById(triggerId);
        Assert.notNull(trigger, "触发结构对象不能为空!");
        ProblemLimit limit = trigger.getLimit();
        Assert.notNull(limit, "问题限制对象不能为空!");
        Problem problem = limit.getProblem();
        Assert.notNull(problem, "问题编辑对象不能为空!");

        //复制普通属性
        ProblemTriggerResult triggerResult = new ProblemTriggerResult();
        BeanUtils.copyProperties(parseObject, triggerResult, "child", "trigger", "limit", "variables");
        triggerResult.setTrigger(trigger);
        ProblemTriggerResult merge = merge(triggerResult);
        trigger.setTriggerResult(merge);
        triggerService.merge(trigger);

        //保存当前填写的公式
        DataUtil.copyProblemTriggerInnerChild(parseObject.getChild(), dao, merge);
        ProblemTriggerResult mergeResult = merge(merge);

        //最终环节将其结束
        PolicyActivity activity = problem.getActivity();
        activity.setEnd(true);
        activityService.merge(activity);
        return ProblemTriggerResultDto.copy(mergeResult);
    }

    @Override
    public void remove(Long id) {
        Object objBySql = dao.getObjBySql("SELECT\n" +
                "\t\tac.id\n" +
                "\tFROM\n" +
                "\t\tpolicy_activity ac\n" +
                "\tLEFT JOIN policy_problem p ON p.activity_id = ac.id\n" +
                "\tLEFT JOIN policy_problem_limit l ON l.problem_id = p.id\n" +
                "\tLEFT JOIN policy_problemtrigger t ON t.limit_id = l.id\n" +
                "\tLEFT JOIN policy_problemtriggerresult rt ON rt.trigger_id = t.id\n" +
                "\tWHERE\n" +
                "\t\trt.id IS NOT NULL\n" +
                "\tAND rt.id =?", id);
        long activityId = Long.parseLong(objBySql.toString());
        super.remove(id);
        Object objBySql1 = dao.getObjBySql("SELECT\n" +
                "\tcount(rt.id)\n" +
                "FROM\n" +
                "\tpolicy_activity ac\n" +
                "LEFT JOIN policy_problem p ON p.activity_id = ac.id\n" +
                "LEFT JOIN policy_problem_limit l ON l.problem_id = p.id\n" +
                "LEFT JOIN policy_problemtrigger t ON t.limit_id = l.id\n" +
                "LEFT JOIN policy_problemtriggerresult rt ON rt.trigger_id = t.id\n" +
                "WHERE\n" +
                "\trt.id IS NOT NULL\n" +
                "AND ac.id =?", activityId);
        if (objBySql1 == null || "0".equals(objBySql1.toString())) {
            PolicyActivity byId = activityService.findById(activityId);
            byId.setEnd(false);
        }

    }

    @Override
    public ProblemTriggerResultDto edit(Long triggerResultId, ProblemTriggerResultDto dto) {
        ProblemTriggerResult triggerResult = findById(triggerResultId);
        Assert.notNull(triggerResult, "问题结果修饰对象不能为空!");
        BeanUtils.copyProperties(dto, triggerResult, "child", "trigger");
        triggerResult.getChild().clear();
        ProblemTriggerResult merge = merge(triggerResult);
        DataUtil.copyProblemTriggerInnerChild(dto.getChild(), dao, merge);
        return ProblemTriggerResultDto.copy(merge(merge));
    }

}
