package xinrui.cloud.service;

import xinrui.cloud.domain.Policy;
import xinrui.cloud.domain.ProblemLimit;
import xinrui.cloud.domain.dto.*;
import xinrui.cloud.dto.PageDto;

import java.util.List;

/**
 * <B>Title:</B>PolicyService.java</br>
 * <B>Description:</B> 政策service</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年3月28日
 */
public interface PolicyService extends BaseService<Policy> {

    /**
     * 查找所有根据单位主体属性{@link #uniSubject}创建了模板的政策所属的政策组
     *
     * @param uniSubject 单位主体
     * @return 政策组数据传输对象集合
     */
    List<PolicyGroupDto> listGroup(String uniSubject);

    /**
     * 根据政策组id查找所属的所有政策，然后根据单位属性查找此政策的匹配模板是否存在;
     * 如果存在，则生成题目，可有多个，最终将生成的题目汇总生成最终的模板
     *
     * @param groupIdList 政策组id
     * @param uniSubject  单位主体属性
     * @return 生成的政策匹配模板
     */
    MatchtemplateDto createTemplate(List<Long> groupIdList, String uniSubject);

    /**
     * 列出政策组
     *
     * @return
     */
    List<PolicyGroupDto> listAllGroup();

    /**
     * 根据政策组
     *
     * @param groupId     政策组id
     * @param currentPage 当前页
     * @param pageSize    一页显示的数量
     * @return 分页对象包含 政策对象集合
     */
    PageDto<List<PolicyDto>> listPolicyByGroupId(Long groupId, int currentPage, int pageSize);

    /**
     * 进行匹配
     *
     * @param dto       前端传递过来的json格式的{@link MatchtemplateDto}对象
     * @param companyId 匹配的企业id
     * @return 最终根据规则生成的政策匹配的结果
     */
    MatchResultDto match(MatchtemplateDto dto, Long companyId);

    /**
     * 根据问题限制对象生成最终的公式
     *
     * @param limit           问题限制对象
     * @param finalLogicInner 公式字符串缓冲对象
     */
    void add2FinalLogicInner(ProblemLimit limit, StringBuffer finalLogicInner);


    /**
     * 根据企业名查询企业列表
     *
     * @param name 企业名称
     * @return 企业简略信息
     */
    List<CompanyInfoDto> findInfoByName(String name);

    /**
     * 添加入驻企业
     *
     * @param companyName 企业名称
     * @param concatName
     * @param concatPhone
     */
    CompanyInfoDto addCompany(String companyName, String concatName, String concatPhone);
}
