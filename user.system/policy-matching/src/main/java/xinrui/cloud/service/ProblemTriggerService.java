package xinrui.cloud.service;

import xinrui.cloud.domain.ProblemLimit;
import xinrui.cloud.domain.ProblemTrigger;
import xinrui.cloud.domain.dto.ProblemTriggerDto;

/**
 * <B>Title:</B>ProblemTriggerService.java</br>
 * <B>Description:</B> 触发结果service </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年4月4日
 */
public interface ProblemTriggerService extends BaseService<ProblemTrigger> {

    /**
     * 添加触发结果
     *
     * @param limitId     {@link ProblemLimit#getId()}问题限制对象id
     * @param parseObject {@link ProblemTriggerDto}对象的json表示格式
     * @return 触发结果数据传输对象
     */
    ProblemTriggerDto add(Long limitId, ProblemTriggerDto parseObject);

    /**
     * 添加之前需要获取到的数据
     *
     * @param limitId 问题限制id
     * @return 触发结果数据传输对象
     */
    ProblemTriggerDto listDataBeForAdd(Long limitId);

    /**
     * 修改触发结果
     *
     * @param triggerId 需要被修改的触发结果的id
     * @param dto       触发结果数据传输对象
     * @return 持久化之后生成的触发结果数据传输对象
     */
    ProblemTriggerDto edit(Long triggerId, ProblemTriggerDto dto);

}
