package xinrui.cloud.service;

import xinrui.cloud.domain.ProblemTriggerResult;
import xinrui.cloud.domain.dto.ProblemTriggerResultDto;

/**
 * <B>Title:</B>ProblemTriggerResultService.java</br>
 * <B>Description:</B> 触发结果修饰接口类 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年4月4日
 */
public interface ProblemTriggerResultService extends BaseService<ProblemTriggerResult> {

    /**
     * 添加触发结果修饰
     *
     * @param triggerId   触发结果修饰id
     * @param parseObject 解析的对象
     * @return 持久化后被转换成的数据传输对象
     */
    ProblemTriggerResultDto add(Long triggerId, ProblemTriggerResultDto parseObject);

    /**
     * 编辑触发结果修饰修饰
     *
     * @param triggerResultId 需要被修改的触发结果修饰的id
     * @param parseObject     触发结果修饰数据传输对象
     * @return 持久化之后新生成的触发结果修饰数据传输对象
     */
    ProblemTriggerResultDto edit(Long triggerResultId, ProblemTriggerResultDto parseObject);

}
