package xinrui.cloud.service.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import xinrui.cloud.BootException;
import xinrui.cloud.common.utils.BeanUtilsEnhance;
import xinrui.cloud.common.utils.DateUtil;
import xinrui.cloud.compoment.Application;
import xinrui.cloud.domain.*;
import xinrui.cloud.domain.dto.*;
import xinrui.cloud.domain.vto.*;
import xinrui.cloud.dto.PageDto;
import xinrui.cloud.dto.UserDto;
import xinrui.cloud.service.TechnologyFinancialAppointmentService;
import xinrui.cloud.service.TechnologyFinancialFileService;
import xinrui.cloud.service.TechnologyFinancialUserService;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


@Service
@Slf4j
@SuppressWarnings("unused")
public class TechnologyFinancialServiceImpl extends BaseServiceImpl<TechnologyFinancial> implements xinrui.cloud.service.TechnologyFinancialService {

    @Resource
    TechnologyFinancialFileService technologyFinancialFileService;

    @Resource
    TechnologyFinancialUserService technologyFinancialUserService;

    @Resource
    TechnologyFinancialAppointmentService technologyFinancialAppointmentService;

    @Override
    public List<TechnologyLoanDateDto> dates() {
        return TechnologyLoanDateDto.copy(dao.listObjBy(TechnologyLoanDate.class));
    }

    @Override
    public List<TechnologyLoanTypeDto> types() {
        return TechnologyLoanTypeDto.copy(dao.listObjBy(TechnologyLoanType.class));
    }

    @Override
    public List<TechnologyLoanAmountDto> amounts() {
        return TechnologyLoanAmountDto.copy(dao.listObjBy(TechnologyLoanAmount.class));
    }

    @Override
    public TechnologyFinancialDto saveByDto(TechnologyFinancialVto technologyFinancialVto) {
        //先持久化基本字段属性
        TechnologyFinancial technologyFinancial = new TechnologyFinancial();
        BeanUtilsEnhance.copyPropertiesEnhance(technologyFinancialVto, technologyFinancial);
        technologyFinancial = merge(technologyFinancial);
        //检查草稿箱数量
        checkDraftCount(technologyFinancialVto);
        //添加其他字段
        addOtherField(technologyFinancialVto, technologyFinancial, false);

        return TechnologyFinancialDto.copy(merge(technologyFinancial));

    }


    @Override
    public List<TechnologyFinancialSimpleDto> publicById(Long id) {
        TechnologyFinancial byId = getTechnologyFinancialAndCheck(id);
        Integer status = byId.getStatus();
        if (status != TechnologyFinancial.TechnologyStatus.DRAFT.value()
                && status != TechnologyFinancial.TechnologyStatus.REFUSED.value()) {
            throw new BootException("只有被拒绝的产品或者草稿箱中的产品可以发布!");
        }
        byId.setStatus(TechnologyFinancial.TechnologyStatus.APPLYING.value());
        return drafts();
    }

    @Override
    public TechnologyFinancial getTechnologyFinancialAndCheck(Long id) {
        TechnologyFinancial byId = findById(id);
        Assert.notNull(byId, "科技金融产品对象找不到!");
        return byId;
    }

    @Override
    public List<TechnologyFinancialSimpleDto> drafts() {
        DetachedCriteria currentUserAndStatus = getCurrentUserAndStatus(TechnologyFinancial.TechnologyStatus.DRAFT.value());
        return TechnologyFinancialSimpleDto.copy(listBydCriteria(currentUserAndStatus));
    }

    @Override
    public TechnologyFinalAuditDto audits() {
        //审核被拒绝的产品
        DetachedCriteria simpleListCriteria = getSimpleListCriteria(getCurrentUserAndStatus(TechnologyFinancial.TechnologyStatus.REFUSED.value()));
        List<TechnologyFinancial> refusedList = listBydCriteria(simpleListCriteria);
        //审核中的产品
        simpleListCriteria = getSimpleListCriteria(getCurrentUserAndStatus(TechnologyFinancial.TechnologyStatus.APPLYING.value()));
        List<TechnologyFinancial> applyingList = listBydCriteria(simpleListCriteria);
        //获取总共的审核被拒绝并且未曾阅读的产品数量
        DetachedCriteria currentUserAndStatus = getCurrentUserAndStatus(TechnologyFinancial.TechnologyStatus.REFUSED.value());
        currentUserAndStatus.add(Restrictions.eq("refusedRead", false));
        currentUserAndStatus.setProjection(Projections.rowCount());
        int refusedUnreadCount = count(currentUserAndStatus);

        return new TechnologyFinalAuditDto(refusedUnreadCount, TechnologyFinancialApplyingSimpleDto.copyApplyings(refusedList),
                TechnologyFinancialApplyingSimpleDto.copyApplyings(applyingList));
    }

    @Override
    public TechnologyFinancialOnlineDto onlines() {
        //信用贷
        DetachedCriteria simpleListCriteria = getSimpleListCriteria(getCurrentUserAndStatus(TechnologyFinancial.TechnologyStatus.ONLINE.value()));
        simpleListCriteria.add(Restrictions.eq("category", TechnologyFinancial.TechnologyType.CREDIT_LOAN.value()));
        List<TechnologyFinancialOnlineSimpleDto> creditList = TechnologyFinancialOnlineSimpleDto.copyOnlies(listBydCriteria(simpleListCriteria));
        //抵押贷
        simpleListCriteria = getSimpleListCriteria(getCurrentUserAndStatus(TechnologyFinancial.TechnologyStatus.ONLINE.value()));
        simpleListCriteria.add(Restrictions.eq("category", TechnologyFinancial.TechnologyType.MORTGAGES.value()));
        List<TechnologyFinancialOnlineSimpleDto> mortgagesList = TechnologyFinancialOnlineSimpleDto.copyOnlies(listBydCriteria(simpleListCriteria));
        return new TechnologyFinancialOnlineDto(creditList, mortgagesList);
    }

    /**
     * 设置离线查询条件仅仅获取简单的几个字段
     *
     * @param statusCriteria statusCriteria的class类型必须是{@link TechnologyFinancial}
     * @return {@link DetachedCriteria}
     */
    private DetachedCriteria getSimpleListCriteria(DetachedCriteria statusCriteria) {
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.property("name"));
        projectionList.add(Projections.property("viewIndexImage"));
        projectionList.add(Projections.property("slogan"));
        projectionList.add(Projections.property("refuseReason"));
        statusCriteria.setProjection(projectionList);
        return statusCriteria;
    }

    @Override
    public PageDto<List<TechnologyFinancialGovDto>> audits(int status, String bank, int currentPage, int pageSize) {
        if (status != TechnologyFinancial.TechnologyStatus.APPLYING.value()
                && status != TechnologyFinancial.TechnologyStatus.ONLINE.value()) {
            throw new BootException("参数错误!");
        }
        DetachedCriteria statusCriteria = getStatusCriteria(status);
        if (!StringUtils.isEmpty(bank)) {
            statusCriteria.add(Restrictions.eq("bank", bank));
        }
        PageDto<List<TechnologyFinancial>> page = new PageDto<>(--currentPage * pageSize, pageSize, statusCriteria);
        dao.pageByCriteria(page);

        List<TechnologyFinancialGovDto> result = TechnologyFinancialGovDto.copy(page.getT());
        return new PageDto<>(page.getTotalPage(), result);
    }

    @Override
    public void audit(Long id, TechnologyFinancial.TechnologyStatus status, String reason) {
        if (status != TechnologyFinancial.TechnologyStatus.ONLINE
                && status != TechnologyFinancial.TechnologyStatus.REFUSED
                && status != TechnologyFinancial.TechnologyStatus.OFFLINE) {
            throw new BootException("参数错误!");
        }
        TechnologyFinancial technologyFinancialAndCheck = getTechnologyFinancialAndCheck(id);
        if (status == TechnologyFinancial.TechnologyStatus.OFFLINE) {
            technologyFinancialAndCheck.setStatus(TechnologyFinancial.TechnologyStatus.REFUSED.value());
            technologyFinancialAndCheck.getTechnologyFinancialAppointments().clear();   //清空
        } else {
            technologyFinancialAndCheck.setStatus(status.value());
            if (status == TechnologyFinancial.TechnologyStatus.REFUSED) {
                technologyFinancialAndCheck.setRefuseReason(reason);
            } else {
                technologyFinancialAndCheck.setRefuseReason(null);
            }

        }
        merge(technologyFinancialAndCheck);
    }

    @Override
    public void appointment(int id, TechnologyFinancialAppointmentUserDto user) {

        Assert.notNull(user, "用户数据不能为空!");
        //拷贝用户属性
        TechnologyFinancialUser technologyFinancialUser = new TechnologyFinancialUser();
        BeanUtils.copyProperties(user, technologyFinancialUser);
        //获取科技金融对象
        TechnologyFinancial technologyFinancialAndCheck = getTechnologyFinancialAndCheck((long) id);

        //将科技金融对象和预约对象关联起来
        TechnologyFinancialAppointment technologyFinancialAppointment
                = new TechnologyFinancialAppointment();
        technologyFinancialAppointment.setTechnologyFinancial(technologyFinancialAndCheck);
        technologyFinancialAndCheck.getTechnologyFinancialAppointments().add(technologyFinancialAppointment);
        //持久化
        merge(technologyFinancialAndCheck);

        //预约对象与用户关联并且持久化
        technologyFinancialAppointment.setTechnologyFinancialUser(technologyFinancialUser);
        technologyFinancialAppointment.setAppointmentUserId(Application.getCurrentUser().getId());
        dao.merge(TechnologyFinancialAppointment.class, technologyFinancialAppointment);
    }

    @Override
    public PageDto<List<TechnologyFinancialAppointmentedDto>> onlines(Long amount, Long limit, String category, String name, int currentPage, int pageSize) {
        DetachedCriteria statusCriteria = getStatusCriteria(TechnologyFinancial.TechnologyStatus.ONLINE.value());
        if (amount != null) {
            statusCriteria.createAlias("loanAmount", "l", JoinType.INNER_JOIN).add(Restrictions.eq("l.id", amount));
        }
        if (limit != null) {
            statusCriteria.createAlias("loanTimeLimit", "loanTime", JoinType.INNER_JOIN).add(Restrictions.eq("loanTime.id", limit));
        }
        if (!StringUtils.isEmpty(category)) {
            checkTechnologyType(category);
            statusCriteria.add(Restrictions.eq("category", category));
        }
        if (!StringUtils.isEmpty(name)) {
            statusCriteria.add(Restrictions.like("name", "%" + name + "%"));
        }
        statusCriteria.addOrder(Order.asc("createDate"));
        PageDto<List<TechnologyFinancial>> pageObj = new PageDto<>(--currentPage * pageSize, pageSize, statusCriteria);
        dao.pageByCriteria(pageObj);

        return new PageDto<>(pageObj.getTotalPage(), TechnologyFinancialAppointmentedDto.copy(pageObj.getT()));
    }

    private void checkTechnologyType(String category) {
        if (!TechnologyFinancial.TechnologyType.CREDIT_LOAN.value().equals(category)
                && !TechnologyFinancial.TechnologyType.MORTGAGES.value().equals(category)) {
            throw new BootException("科技金融类别错误!");
        }
    }

    @Override
    public TechnologyFinancialDto findDtoById(Long id) {
        return TechnologyFinancialDto.copy(findById(id));
    }

    @Override
    public int getPublicCount(Long userId) {
        DetachedCriteria detachedCriteria = getPublicCriteriaByUserId(userId);
        detachedCriteria.setProjection(Projections.rowCount());
        return dao.count(detachedCriteria);
    }

    @Override
    public String getBestNewViewImage(Long userId) {
        DetachedCriteria publicCriteriaByUserId = getPublicCriteriaByUserId(userId);
        publicCriteriaByUserId.addOrder(Order.asc("createDate"));
        Criteria executableCriteria = publicCriteriaByUserId.getExecutableCriteria(dao.getSession());
        executableCriteria.setFirstResult(0);
        executableCriteria.setMaxResults(1);
        Object o = executableCriteria.uniqueResult();
        log.info("o==null ? {}", o == null);
        if (o != null) {
            TechnologyFinancial technologyFinancial = (TechnologyFinancial) o;
            List<TechnologyFinancialFile> viewImages = technologyFinancial.getViewImages();
            log.info("viewImages is empty ? {}", CollectionUtils.isEmpty(viewImages));
            if (!CollectionUtils.isEmpty(viewImages)) {
                TechnologyFinancialFile technologyFinancialFile = viewImages.get(viewImages.size() - 1);
                log.info("technologyFinancialFile is null {} ? ", technologyFinancialFile == null);
                if (technologyFinancialFile != null) {
                    return technologyFinancialFile.getFullPath();
                }
            }
        }
        return null;
    }

    @Override
    public List<ViewImagesVto> listIndexImages() {

        DetachedCriteria detachedCriteria = getOnlineAndOpenImage(true);
        detachedCriteria.setProjection(Projections.property("viewIndexImage"));
        List<TechnologyFinancial> technologyFinancials = listBydCriteria(detachedCriteria);
        List<ViewImagesVto> result = getViewImagesVtos(technologyFinancials);
        if (CollectionUtils.isEmpty(result)) {
            detachedCriteria = getOnlineAndOpenImage(false);
            technologyFinancials = listBydCriteria(detachedCriteria);
            result = getViewImagesVtos(technologyFinancials);
        }
        return result;
    }

    @Override
    public int findAppointmentCount(Long technologyFinancialId) {
        DetachedCriteria add = DetachedCriteria.forClass(TechnologyFinancialAppointment.class)
                .createAlias("technologyFinancial", "t").add(Restrictions.eq("t.id", technologyFinancialId))
                .setProjection(Projections.rowCount());
        return technologyFinancialUserService.count(add);
    }

    @Override
    public void put(Long id, TechnologyFinancialVto technologyFinancialVto) {
        TechnologyFinancial technologyFinancialAndCheck = getTechnologyFinancialAndCheck(id);
        BeanUtilsEnhance.copyPropertiesEnhance(technologyFinancialVto, technologyFinancialAndCheck);
        addOtherField(technologyFinancialVto, technologyFinancialAndCheck, true);
    }

    @Override
    public void readRefused(Long id) {
        TechnologyFinancial technologyFinancialAndCheck = getTechnologyFinancialAndCheck(id);
        technologyFinancialAndCheck.setRefusedRead(true);
        merge(technologyFinancialAndCheck);
    }

    @Override
    public int listUnProcessCountByBank(String bankName) {
        DetachedCriteria statusCriteria = getStatusCriteria(TechnologyFinancial.TechnologyStatus.APPLYING.value());
        statusCriteria.add(Restrictions.eq("bank",bankName));
        statusCriteria.setProjection(Projections.rowCount());
        return count(statusCriteria);
    }


    /**
     * 添加其他非基本字段到{@link TechnologyFinancial }对象
     *
     * @param technologyFinancialVto 前端接收对象
     * @param technologyFinancial    持久化对象
     * @param clearAgoList           标识是否对以前的list进行清空操作
     */
    private void addOtherField(TechnologyFinancialVto technologyFinancialVto, TechnologyFinancial technologyFinancial, boolean clearAgoList) {

        checkProcessData(technologyFinancialVto, technologyFinancial);

        List<TechnologyFinancialFile> viewImagesFromDataBase = technologyFinancial.getViewImages();
        if (clearAgoList) {
            viewImagesFromDataBase.clear();
        }
        //关联存储过的文件对象
        List<ViewImagesVto> viewImages = technologyFinancialVto.getViewImages();
        Assert.notEmpty(viewImages, "展示图片为必选项!");
        for (ViewImagesVto file : viewImages) {
            TechnologyFinancialFile fileObj = getTechnologyFinancialFile(file.getFullPath());
            fileObj.setTechnologyFinancial(technologyFinancial);
            viewImagesFromDataBase.add(fileObj);
            if (technologyFinancial.getViewImages().size() > 6) {
                throw new BootException("只支持传递6张说明图片!");
            }
        }
        //设置展示图片
        TechnologyFinancialFile technologyFinancialFile = getTechnologyFinancialFile(technologyFinancialVto.getViewIndexImage());
        technologyFinancial.setViewIndexImage(technologyFinancialFile);

        //申请条件
        List<TechnologyApplyCondition> technologyApplyConditions = TechnologyApplyConditionsVto.toBean(technologyFinancialVto.getTechnologyApplyConditions());
        List<TechnologyApplyCondition> technologyApplyConditionsFromDatabase = technologyFinancial.getTechnologyApplyConditions();
        if (clearAgoList) {
            technologyApplyConditionsFromDatabase.clear();
        }
        technologyApplyConditionsFromDatabase.addAll(technologyApplyConditions);

        //办理流程
        List<TechnologyProcess> technologyProcesses = TechnologyProcessesVto.toBean(technologyFinancialVto.getTechnologyProcesses());
        List<TechnologyProcess> technologyProcessesFromDatabase = technologyFinancial.getTechnologyProcesses();
        if (clearAgoList) {
            technologyProcessesFromDatabase.clear();
        }
        technologyProcessesFromDatabase.addAll(technologyProcesses);
        if (technologyProcesses.size() > 20) {
            throw new BootException("最多添加20条申请流程!");
        }

        //常见问题
        List<TechnologyUsualProblem> technologyUsualProblems = TechnologyUsualProblemsVto.toBean(technologyFinancialVto.getTechnologyUsualProblems());
        Assert.notNull(technologyUsualProblems, "常见问题不能为空!");
        List<TechnologyUsualProblem> technologyUsualProblemsFromDatabase = technologyFinancial.getTechnologyUsualProblems();
        if (clearAgoList) {
            technologyUsualProblemsFromDatabase.clear();
        }
        Assert.notEmpty(technologyUsualProblems, "问题不能为空!");
        technologyUsualProblemsFromDatabase.addAll(technologyUsualProblems);
        if (technologyUsualProblems.size() > 5) {
            throw new BootException("最多添加5条问题!");
        }

        UserDto currentUser = Application.getCurrentUser();
        technologyFinancial.setUserId(currentUser.getId());
        technologyFinancial.setBank(currentUser.getUniqueGroup().getName());
    }

    private TechnologyFinancialFile getTechnologyFinancialFile(String file) {
        TechnologyFinancialFile fileObj = technologyFinancialFileService.findByPath(file);
        if (fileObj == null) {
            throw new BootException("所选文件已经被删除!");
        }
        return fileObj;
    }

    /**
     * 检查当前用户创建的位于草稿箱中的产品数量
     *
     * @param technologyFinancialVto 前端接收对象
     */
    private void checkDraftCount(TechnologyFinancialVto technologyFinancialVto) {
        if (technologyFinancialVto.getStatus() == TechnologyFinancial.TechnologyStatus.DRAFT.value()) {
            DetachedCriteria countCriteria = DetachedCriteria.forClass(TechnologyFinancial.class)
                    .add(Restrictions.eq("userId", Application.getCurrentUser().getId()))
                    .add(Restrictions.eq("status", TechnologyFinancial.TechnologyStatus.DRAFT.value()));
            if (count(countCriteria) >= 20) {
                throw new BootException("草稿箱最多添加20条!");
            }
        }
    }

    /**
     * 根据用户id获取所有发布产品的离线查询对象
     *
     * @param userId {@link UserDto#getId()}
     * @return {@link DetachedCriteria}获取所有发布产品的离线查询条件对象
     */
    private DetachedCriteria getPublicCriteriaByUserId(Long userId) {
        Assert.notNull(userId, "用户id不能为空!");
        return DetachedCriteria.forClass(TechnologyFinancial.class)
                .add(Restrictions.or(
                        Restrictions.and(Restrictions.eq("userId", userId),
                                Restrictions.eq("status", TechnologyFinancial.TechnologyStatus.ONLINE))/*,
                        Restrictions.and(Restrictions.eq("userId", userId),
                                Restrictions.eq("status", TechnologyFinancial.TechnologyStatus.APPLYING))*/
                ));
    }


    /**
     * 根据{@code List<TechnologyFinancial>}获取到其文件对象{@link TechnologyFinancial#getViewImages()}并且取第一张图片对象，
     * 将其转换成{@link ViewImagesVto}对象
     *
     * @param technologyFinancials {@code List<TechnologyFinancial>}科技金融对象
     * @return 文件下载对象
     */
    private List<ViewImagesVto> getViewImagesVtos(List<TechnologyFinancial> technologyFinancials) {
        List<ViewImagesVto> result = Lists.newArrayList();
        for (TechnologyFinancial technologyFinancial : technologyFinancials) {
//            List<TechnologyFinancialFile> viewImages = technologyFinancial.getViewImages();
//            TechnologyFinancialFile technologyFinancialFile = viewImages.get(0);
            result.add(new ViewImagesVto(technologyFinancial.getViewIndexImage().getFullPath()));
        }
        return result;
    }

    /**
     * 获取{@link DetachedCriteria}离线查询条件对象,查找的是线上的并且createDate升序的.
     * 如果传递了needOpenImage,则添加条件为openImage为true的并且只查询viewImages属性
     *
     * @param needOpenImage 是否需要过滤首页开启图片的标识
     * @return {@link DetachedCriteria}
     */
    private DetachedCriteria getOnlineAndOpenImage(boolean needOpenImage) {
        DetachedCriteria detachedCriteria = getCurrentUserAndStatus(TechnologyFinancial.TechnologyStatus.ONLINE.value())
                .addOrder(Order.asc("createDate"));
        if (needOpenImage) {
            detachedCriteria.add(Restrictions.eq("openImage", true));
        }
        detachedCriteria.setProjection(Projections.property("viewImages"));
        return detachedCriteria;
    }

    /**
     * 根据状态查找{@link TechnologyFinancialDto}列表
     *
     * @param status 详情见{@link TechnologyFinancial.TechnologyStatus#values()}
     * @return {@code List<TechnologyFinancialDto>}
     */
    private List<TechnologyFinancialDto> listByStatus(int status) {
        DetachedCriteria statusCriteria = getCurrentUserAndStatus(status);
        return TechnologyFinancialDto.copy(listBydCriteria(statusCriteria));
    }

    /**
     * 根据当前用户和传递的状态获取{@link DetachedCriteria}
     *
     * @param status 传递的状态
     * @return {@link DetachedCriteria}
     */
    private DetachedCriteria getCurrentUserAndStatus(int status) {
        return getStatusCriteria(status)
                .add(Restrictions.eq("userId", Application.getCurrentUser().getId()));
    }

    /**
     * 根据当前用户获取{@link DetachedCriteria}
     *
     * @param status 传递的状态
     * @return {@link DetachedCriteria}
     */
    @Override
    public DetachedCriteria getStatusCriteria(int status) {
        return DetachedCriteria.forClass(TechnologyFinancial.class)
                .add(Restrictions.eq("status", status));
    }

    /**
     * 从{@link TechnologyFinancialVto}对象提取非基本对象的参数，并且设置到{@link TechnologyFinancial}对象中
     *
     * @param technologyFinancialVto {@link TechnologyFinancialVto} 前端接收对象
     * @param technologyFinancial    {@link TechnologyFinancial}实体保存对象
     */
    private void checkProcessData(TechnologyFinancialVto technologyFinancialVto, TechnologyFinancial technologyFinancial) {

        //贷款期限
        LoanTimeLimitVto loanTimeLimit = technologyFinancialVto.getLoanTimeLimit();
        Assert.notNull(loanTimeLimit, "贷款期限为必选项！");
        TechnologyLoanDate technologyLoanDate = dao.findSingleByProperty(TechnologyLoanDate.class, "id", loanTimeLimit.getId());
        if (technologyLoanDate == null) {
            throw new BootException("错误的贷款期限选项!");
        }
        technologyFinancial.setLoanTimeLimit(technologyLoanDate);

        //贷款额度
        LoanAmountVto loanAmount = technologyFinancialVto.getLoanAmount();
        Assert.notNull(loanAmount, "贷款额度为必选项！");
        TechnologyLoanAmount technologyLoanAmount = dao.findSingleByProperty(TechnologyLoanAmount.class, "id", loanAmount.getId());
        if (technologyLoanAmount == null) {
            throw new BootException("错误的贷款额度选项!");
        }
        technologyFinancial.setLoanAmount(technologyLoanAmount);

        List<TechnologyLoanTypeVto> technologyLoanTypes = technologyFinancialVto.getTechnologyLoanTypes();
        Assert.notEmpty(technologyLoanTypes, "可抵押类型为必选项!");
        for (TechnologyLoanTypeVto type : technologyLoanTypes) {
            TechnologyLoanType technologyLoanType = dao.findSingleByProperty(TechnologyLoanType.class, "id", type.getId());
            if (technologyLoanType == null) {
                throw new BootException("错误的可抵押类型选项!");
            }
            technologyFinancial.getTechnologyLoanTypes().add(technologyLoanType);
        }


        String endDate = technologyFinancialVto.getApplyEndDate();
        if (endDate != null) {
            try {
                Date parse = DateUtil.parse(endDate);
                technologyFinancial.setApplyEndDate(parse);
            } catch (ParseException e) {
                throw new BootException("结束申请时间格式错误!!");
            }
        }

        String category = technologyFinancialVto.getCategory();
        boolean notTrue = !TechnologyFinancial.TechnologyType.CREDIT_LOAN.value().equals(category) &&
                !TechnologyFinancial.TechnologyType.MORTGAGES.value().equals(category);
        if (notTrue) {
            throw new BootException("不支持的贷款类别!");
        }

    }
}
