package xinrui.cloud.service;

import java.util.List;

import xinrui.cloud.domain.Problem;
import xinrui.cloud.domain.ProblemLimit;
import xinrui.cloud.domain.dto.ProblemLimitDataDto;
import xinrui.cloud.domain.dto.ProblemLimitDto;

/**
 * <B>Title:</B>ProblemLimitService.java</br>
 * <B>Description:</B> 问题限制接口</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年4月2日
 */
public interface ProblemLimitService extends BaseService<ProblemLimit> {

    /**
     * 修改问题限制
     *
     * @param limitId
     * @param list
     * @param ids
     * @return
     */
    ProblemLimitDto edit(Long limitId, List<ProblemLimit> list, String ids);

    /**
     * 添加问题限制
     *
     * @param problemId {@link Problem#getId()}问题编辑项的id
     * @param list      {@link #list}@问题限制对象集合
     * @param ids       {@link #ids}被选中的变量的id(逗号分割)
     * @return 问题限制对象集合
     */
    List<ProblemLimitDto> addLimitData(Long problemId, List<ProblemLimit> list, String ids);

    /**
     * 获取添加问题限制必须的数据项
     *
     * @param problemId {@link Problem#getId()}问题编辑项的id
     * @return {@link ProblemLimitDataDto}问题限制必须项数据传输对象
     */
    ProblemLimitDataDto listBaseData(Long problemId);

}
