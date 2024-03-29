package xinrui.cloud.service;

import java.util.List;

import xinrui.cloud.domain.Policy;
import xinrui.cloud.domain.PolicyTemplate;
import xinrui.cloud.domain.dto.PolicyDto;
import xinrui.cloud.domain.dto.PolicyTemplateDto;

/**
 * <B>Title:</B>PolicyTemplateService.java</br>
 * <B>Description:</B> 政策模板service</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年3月28日
 */
public interface PolicyTemplateService extends BaseService<PolicyTemplate> {

    /**
     * 初始化环节(问题编辑，问题限制，触发结果，触发结果修饰),设置最大环节数为3并且初始化当前环节为0
     *
     * @param tem {@link PolicyTemplate}对象
     * @return 初始化环节后的{@link PolicyTemplate}对象
     */
    public PolicyTemplate init(PolicyTemplate tem);

    /**
     * 1.查询所有已经上线的并且未曾创建政策匹配模板并且是科创的政策;</br>
     * 2.如果传递了name参数，按照条件1查询并且根据name模糊搜索政策的短标题</br>
     * 3如果传递了policyId參數，则查询所有已经上线的并且未曾创建政策匹配模板并且是科创的政策，或者id为policyid的政策;
     * 4.如果传递了name并且同时传递了policyId的话，则按照條件3查询
     *
     * @param name     政策的短标题
     * @param policyId 政策id
     * @return {@code List}
     */
    public List<PolicyDto> listCreatedPolicyId(String name, Long policyId);

    /**
     * 创建政策匹配规则模版
     *
     * @param unitSubject 单位主体
     * @param description 模板描述
     * @param policy      政策对象
     * @return
     */
    public PolicyTemplateDto createTemplate(String unitSubject, String description,Long policyId);

    /**
     * 检查当前政策是否创建过模版
     *
     * @param policyId 政策对象
     * @return 没有创建返回true/否则返回false
     */
    public boolean checkNotCreate(Long policyId);

    /**
     * 修改模板主体
     *
     * @param temp   {@link PolicyTemplate}对象
     * @param policy {@link Policy}
     * @return 封装{@link PolicyTemplate}对象后的{@link PolicyTemplateDto}对象
     */
    public PolicyTemplateDto editTemplate(PolicyTemplate temp, Long policyId);

    /**
     * 根据模版id删除所有的环节实例 {@link PolicyTemplate temp}
     *
     * @param tempId
     */
    public void removeAll(Long tempId);

}
