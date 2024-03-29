package xinrui.cloud.service;

import org.hibernate.criterion.DetachedCriteria;
import xinrui.cloud.compoment.Application;
import xinrui.cloud.domain.TechnologyFinancial;
import xinrui.cloud.domain.dto.*;
import xinrui.cloud.domain.vto.TechnologyFinancialVto;
import xinrui.cloud.domain.vto.ViewImagesVto;
import xinrui.cloud.dto.PageDto;

import java.util.List;

/**
 * 科技金融接口
 */
public interface TechnologyFinancialService extends BaseService<TechnologyFinancial> {

    /**
     * 获取贷款期限列表
     *
     * @return {@link TechnologyLoanDateDto}
     */
    List<TechnologyLoanDateDto> dates();

    /**
     * 获取贷款类型列表
     *
     * @return {@link TechnologyLoanTypeDto}
     */
    List<TechnologyLoanTypeDto> types();

    /**
     * 获取贷款额度列表
     *
     * @return {@link TechnologyLoanAmountDto}
     */
    List<TechnologyLoanAmountDto> amounts();

    /**
     * 保存科技金融产品
     *
     * @param technologyFinancialVto
     * @return
     */
    TechnologyFinancialDto saveByDto(TechnologyFinancialVto technologyFinancialVto);

    /**
     * 根据科技金融产品发布草稿箱中的产品，并且返回新的草稿箱列表
     *
     * @param id {@link TechnologyFinancial#getId()}
     * @return {@code List<TechnologyFinancialSimpleDto>}
     */
    List<TechnologyFinancialSimpleDto> publicById(Long id);


    /**
     * <p/>获取草稿箱列表,获取的是{@link TechnologyFinancial#getStatus()}为
     * {@link TechnologyFinancial.TechnologyStatus#APPLYING}的所有{@link TechnologyFinancial}对象
     *
     * @return {@code  List<TechnologyFinancialSimpleDto>}
     */
    List<TechnologyFinancialSimpleDto> drafts();

    /**
     * <p/>获取上线产品列表,获取的是{@link TechnologyFinancial#getStatus()}为
     * {@link TechnologyFinancial.TechnologyStatus#ONLINE}的所有{@link TechnologyFinancial}对象
     *
     * @return {@link TechnologyFinancialOnlineDto}
     */
    TechnologyFinancialOnlineDto onlines();

    /**
     * <p/>根据status获取(包含审核中(待审核)或者审核被拒绝的)当前用户的产品列表,获取的是{@link TechnologyFinancial#getStatus()}为
     * {@link TechnologyFinancial.TechnologyStatus#APPLYING}，{@link TechnologyFinancial.TechnologyStatus#REFUSED}并且
     * {@link TechnologyFinancial#getUserId()}等于当前用户id的{@link Application#getCurrentUser()#getId()}
     * 的所有{@link TechnologyFinancial}对象
     *
     * @return {@link TechnologyFinalAuditDto}
     */
    TechnologyFinalAuditDto audits();

    /**
     * <p/>根据status获取审核的产品列表(包含审核中(待审核)或者审核通过的),获取的是{@link TechnologyFinancial#getStatus()}为
     * {@link TechnologyFinancial.TechnologyStatus#APPLYING}，{@link TechnologyFinancial.TechnologyStatus#ONLINE}
     * 的所有{@link TechnologyFinancial}对象
     *
     * @param status      当前状态值 1.查询待审核的，3查询通过的
     * @param bank        所选银行
     * @param currentPage 当前页码
     * @param pageSize    一页大小
     * @return {@link PageDto <List<TechnologyFinancialDto>> }
     */
    PageDto<List<TechnologyFinancialGovDto>> audits(int status, String bank, int currentPage, int pageSize);

    /**
     * 根据产品id和状态值审核产品
     *
     * @param id     {@link TechnologyFinancial#getId()}
     * @param status {@link TechnologyFinancial.TechnologyStatus }
     * @param reason 只有当传入的status为{@link TechnologyFinancial.TechnologyStatus#REFUSED }时才需要这个参数
     */
    void audit(Long id, TechnologyFinancial.TechnologyStatus status, String reason);

    /**
     * 企业用户预约金融产品
     *
     * @param id   {@link TechnologyFinancial#getId()}
     * @param user {@link TechnologyFinancialAppointmentUserDto}
     */
    void appointment(int id, TechnologyFinancialAppointmentUserDto user);

    /**
     * 根据筛选条件取所有线上的金融产品
     *
     * @param amount      最高贷款额度
     * @param limit       期限
     * @param category    类别,详情为{@link TechnologyFinancial.TechnologyType#values()}
     * @param name        金融产品名称
     * @param currentPage 当前页码
     * @param pageSize    一页大小
     * @return
     */
    PageDto<List<TechnologyFinancialAppointmentedDto>> onlines(Long amount, Long limit, String category, String name, int currentPage, int pageSize);

    /**
     * 根据科技金融产品的id获取{@link TechnologyFinancialDto}对象
     *
     * @param id {@link TechnologyFinancial#getId()}
     * @return {@link TechnologyFinancialDto}
     */
    TechnologyFinancialDto findDtoById(Long id);

    /**
     * 根据用户id获取其发布产品数量
     *
     * @param userId 用户的id
     * @return 最终获得的数量
     */
    int getPublicCount(Long userId);

    /**
     * 根据用户的id获取其发布所有产品的最新的一张展示的图片下载地址
     *
     * @param userId 当前用户的id
     * @return 最新的需要展示图片的下载地址
     */
    String getBestNewViewImage(Long userId);

    /**
     * 获取首页展示的所有文件对象
     *
     * @return {@code  List<ViewImagesVto>}
     */
    List<ViewImagesVto> listIndexImages();

    /**
     * 获取{@link TechnologyFinancial}下的预约人数
     *
     * @return
     */
    int findAppointmentCount(Long technologyFinancialId);


    /**
     * 根据科技金融对象的id{@link TechnologyFinancial#getId()}和{@link TechnologyFinancialVto}对象编辑产品
     *
     * @param id                     科技金融对象的id
     * @param technologyFinancialVto 科技金融对象（接收）
     */
    void put(Long id, TechnologyFinancialVto technologyFinancialVto);

    /**
     * 根据id获取产品对象{@link TechnologyFinancial}
     *
     * @param id {@link TechnologyFinancial#getId()}
     * @return {@link TechnologyFinancial}
     */
    TechnologyFinancial getTechnologyFinancialAndCheck(Long id);

    /**
     * 根据当前用户获取{@link DetachedCriteria}
     *
     * @param status 传递的状态
     * @return
     */
    DetachedCriteria getStatusCriteria(int status);

    /**
     * 阅读被拒绝的新消息
     * @param id    科技金融产品的id{@link TechnologyFinancial#getId()}
     */
    void readRefused(Long id);

    /**
     * 根据银行获取未曾处理的产品数量
     * @param bankName  银行的名称
     * @return  未曾处理的数量
     */
    int listUnProcessCountByBank(String bankName);
}
