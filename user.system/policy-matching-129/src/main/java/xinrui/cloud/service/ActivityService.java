package xinrui.cloud.service;

import java.util.List;

import xinrui.cloud.domain.PolicyActivity;
import xinrui.cloud.domain.PolicyTemplate;
import xinrui.cloud.domain.Problem;
import xinrui.cloud.domain.dto.PolicyActivityDto;
import xinrui.cloud.domain.dto.ProblemDto;
import xinrui.cloud.domain.dto.ProblemDto2;

/**
 * <B>Title:</B>ActivityService.java</br>
 * <B>Description:</B> 政策模版内的环节处理接口</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年3月28日
 */
public interface ActivityService extends BaseService<PolicyActivity> {

    /**
     * 添加问题编辑模板，根据{@linkplain Status}参数的不同,判断其是单选、填空、独选、空条件等
     *
     * @param tempId      {@link PolicyTemplate#getId()}对象的唯一主键
     * @param status      状态，用于区分是哪种文本类型 ，<B>0-3</B>依次代表<B>单选、填空、独选、空</B>
     * @param title       此问题的展示标题
     * @param value       单选时,表示 {@link ProblemDto}的json表格式的表示，其他则表示需要传递的值
     * @param max         最高限制的额度
     * @param last        是否针对上一个问题编辑选项作用
     * @param isAmplitude 是否是幅度上涨
     * @return {@link ProblemDto}问题编辑数据对象
     */
    List<ProblemDto> addProblem(Long tempId, Integer status, String title, String value, Integer max, Boolean last, Boolean isAmplitude);

    /**
     * 根据模板id查找主环节实例
     *
     * @param tempId
     * @return
     */
    PolicyActivityDto findByTemp(Long tempId);

    /**
     * 删除此环节及其所有子问题选项
     *
     * @param activityId {@link PolicyActivity#getId()}
     * @return PolicyTemplate
     */
    PolicyTemplate removeActivityById(Long activityId);

    /**
     * 获取特定格式的单选数据类型对象
     *
     * @param problemId {@link Problem#getId()}
     * @return {@link ProblemDto2}特定格式的单选数据对象
     */
    ProblemDto2 beforeEditSingleRadion(Long problemId);


    /**
     * 编辑问题编辑模板，根据{@linkplain Status}参数的不同,判断其是单选、填空、独选、空条件等
     *
     * @param tempId      {@link PolicyTemplate#getId()}对象的唯一主键
     * @param status      状态，用于区分是哪种文本类型 ，<B>0-3</B>依次代表<B>单选、填空、独选、空</B>
     * @param title       此问题的展示标题
     * @param value       单选时,表示 {@link ProblemDto}的json表格式的表示，其他则表示需要传递的值
     * @param isAmplitude
     * @return {@link ProblemDto}问题编辑数据对象
     */
    ProblemDto editProblem(Long problemId, Integer status, String title, String value, Integer max, Boolean last, Boolean isAmplitude);
}
