package xinrui.cloud.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.google.common.collect.Lists;

/**
 * <B>Title:</B>DifficultCompany</br>
 * <B>Description:</B> 企业诉求</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/5 14:59
 */
@Entity(name="qybf_difficult_company")
public class DifficultCompany extends IdEntity {

	private static final long serialVersionUID = 1L;

	@Column(name="company_id")
    private Long companyId;

    /** 问题类型 */
    @OneToOne
    @JoinColumn(name = "diffcultType_id")
    private DifficultType diffcultType;

    /**
     * 问题标题
     */
    private String diffcultTitle;

    /** 上报问题的时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date commitDate;


    /** 主要存在的问题 */
    private String mainProblems;

    /**
     * 表示是否跟踪此诉求
     */
    private Boolean flow=false;

    // 0 待办 1 在办(主办或协办) 2 办结
    private int status;

    /**
     * 办理时间限制
     */
    private Date processDate;

    /**
     * 表示是否批示
     */
    private Boolean pishi=false;

    /**
     * 领导批示的具体内容对象集合
     */
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Instructions> difficultInstructions= Lists.newArrayList();

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = DifficultCompanyRelation.class)
    @JoinColumn(name = "difficultCompanyRelation_id")
    private DifficultCompanyRelation difficultCompanyRelation;

    /** 历史记录 */
    @OneToOne
    private DifficultHistory difficultHistory;

    /**
     * 现场解决情况
     */
    @Lob
    private String xcjjqk;

    /**
     * 针对走访记录的状态 走访前 0 | 走访后 1 | 取消走访 2
     */
    private int visitreCordStatus;

    /**
     * 记录走访记录表反馈时间之前的最后一个部门处理意见
     */
    private String opinions;

    /**
     * 多条诉求对应一个走访
     */
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = VisitreCord.class)
    @JoinColumn(name = "visitreCord_id")
    private VisitreCord visitreCord;


    public Boolean getFlow() {
        return flow;
    }

    public void setFlow(Boolean flow) {
        this.flow = flow;
    }

    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    public List<Instructions> getDifficultInstructions() {
        return difficultInstructions;
    }

    public void setDifficultInstructions(List<Instructions> difficultInstructions) {
        this.difficultInstructions = difficultInstructions;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public DifficultType getDiffcultType() {
        return diffcultType;
    }

    public void setDiffcultType(DifficultType diffcultType) {
        this.diffcultType = diffcultType;
    }

    public String getDiffcultTitle() {
        return diffcultTitle;
    }

    public void setDiffcultTitle(String diffcultTitle) {
        this.diffcultTitle = diffcultTitle;
    }

    public Date getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(Date commitDate) {
        this.commitDate = commitDate;
    }

    public String getMainProblems() {
        return mainProblems;
    }

    public void setMainProblems(String mainProblems) {
        this.mainProblems = mainProblems;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DifficultHistory getDifficultHistory() {
        return difficultHistory;
    }

    public void setDifficultHistory(DifficultHistory difficultHistory) {
        this.difficultHistory = difficultHistory;
    }

    public DifficultCompanyRelation getDifficultCompanyRelation() {
        return difficultCompanyRelation;
    }

    public void setDifficultCompanyRelation(DifficultCompanyRelation difficultCompanyRelation) {
        this.difficultCompanyRelation = difficultCompanyRelation;
    }

    public Boolean getPishi() {
        return pishi;
    }

    public void setPishi(Boolean pishi) {
        this.pishi = pishi;
    }

    public String getXcjjqk() {
        return xcjjqk;
    }

    public void setXcjjqk(String xcjjqk) {
        this.xcjjqk = xcjjqk;
    }

    public int getVisitreCordStatus() {
        return visitreCordStatus;
    }

    public void setVisitreCordStatus(int visitreCordStatus) {
        this.visitreCordStatus = visitreCordStatus;
    }

    public String getOpinions() {
        return opinions;
    }

    public void setOpinions(String opinions) {
        this.opinions = opinions;
    }

    public VisitreCord getVisitreCord() {
        return visitreCord;
    }

    public void setVisitreCord(VisitreCord visitreCord) {
        this.visitreCord = visitreCord;
    }
}
