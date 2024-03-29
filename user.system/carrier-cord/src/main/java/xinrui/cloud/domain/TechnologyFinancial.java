package xinrui.cloud.domain;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 科技金融实体类
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/8/5 16:26
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class TechnologyFinancial extends IdEntity {

    /**
     * 科技金融产品名称
     */
    @NotEmpty(message = "科技金融产品名称不能为空")
    @Column(columnDefinition = "text")
    private String name;

    /**
     * 展示的图片
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private TechnologyFinancialFile viewIndexImage;

    /**
     * 说明的图片
     */
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, targetEntity = TechnologyFinancialFile.class)
    @OrderBy("createDate asc ")
    private List<TechnologyFinancialFile> viewImages = Lists.newArrayList();

    /**
     * 科技金融类别
     * 只可能为 信用贷/抵押贷
     * 详情见{@link TechnologyType#values()}
     */
    @NotEmpty(message = "科技金融类别不能为空")
    private String category = TechnologyType.CREDIT_LOAN.value();

    /**
     * 产品标语
     */
    @NotEmpty(message = "产品标语不能为空")
    private String slogan;

    /**
     * 产品简介信息
     */
    @NotEmpty(message = "产品简介信息不能为空")
    @Column(columnDefinition = "text")
    private String information;

    /**
     * 贷款期限
     * {@link TechnologyLoanDate}
     */
    @OneToOne(fetch = FetchType.LAZY)
    private TechnologyLoanDate loanTimeLimit;

    /**
     * 申请结束时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull(message = "申请结束时间不能为空")
    private Date applyEndDate;

    /**
     * 贷款额度对象
     * {@link TechnologyLoanDate}
     */
    @OneToOne(fetch = FetchType.LAZY)
    private TechnologyLoanAmount loanAmount;

    /**
     * 可抵押物件类型
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = TechnologyLoanType.class)
    private List<TechnologyLoanType> technologyLoanTypes = Lists.newArrayList();

    /***
     * 申请条件对象集合
     * {@link TechnologyApplyCondition}
     */
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, targetEntity = TechnologyApplyCondition.class)
    @OrderBy("id asc ")
    private List<TechnologyApplyCondition> technologyApplyConditions = Lists.newArrayList();

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    /**
     * 发布时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date publicDate;


    /***
     * 办理流程对象集合
     * {@link TechnologyApplyCondition}
     */
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, targetEntity = TechnologyProcess.class)
    @OrderBy("id asc ")
    private List<TechnologyProcess> technologyProcesses = Lists.newArrayList();

    /**
     * 问题对象
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = TechnologyUsualProblem.class)
    private List<TechnologyUsualProblem> technologyUsualProblems = Lists.newArrayList();

    /**
     * 预约对象
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = TechnologyFinancialAppointment.class)
    private List<TechnologyFinancialAppointment> technologyFinancialAppointments;

//    /**
//     * 处理对象
//     */
//    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY,optional = true)
//    private ProcessObject processObjects;

//    /**
//     * 预约用户对象
//     */
//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = TechnologyFinancialUser.class)
//    private List<TechnologyFinancialUser> technologyFinancialUsers = Lists.newArrayList();

    /**
     * 关联的用户id
     */
    private Long userId;

    /**
     * 科技产品的状态具体类型看{@link TechnologyStatus#values()}
     */
    private Integer status = TechnologyStatus.DRAFT.value();

    /**
     * 创建者的所属银行{@link BankGroup#getName()}
     */
    private String bank;

    /**
     * 首页开启图片展示
     */
    private Boolean openImage = false;

    /**
     * 审核被拒绝的理由
     */
    private String refuseReason;

    /**
     * 拒绝时候是否阅读了.默认时false
     */
    private boolean refusedRead;


    public enum TechnologyType {
        /**
         * 信用贷
         */
        CREDIT_LOAN("信用贷"),
        /**
         * 抵押贷
         */
        MORTGAGES("抵押贷");

        private String type;

        TechnologyType(String type) {
            this.type = type;
        }

        public String value() {
            return this.type;
        }
    }

    public enum TechnologyStatus {
        /**
         * 草稿
         */
        DRAFT(0),
        /**
         * 审核中
         */
        APPLYING(1),
        /**
         * 审核拒绝
         */
        REFUSED(2),
        /**
         * 线上
         */
        ONLINE(3),
        /**
         * 下架
         */
        OFFLINE(4);

        private int status;

        TechnologyStatus(int type) {
            this.status = type;
        }

        public int value() {
            return this.status;
        }
    }


}
