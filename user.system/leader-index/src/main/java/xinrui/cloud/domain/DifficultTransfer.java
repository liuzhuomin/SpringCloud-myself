package xinrui.cloud.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;


/**
 * 问题转办人以及回复列表
 *
 * @author jihy
 */
@Entity
@Table(name = "qybf_difficult_transfer")
public class DifficultTransfer extends IdEntity {

    /**
     *
     */
    private static final long serialVersionUID = -3930600945563083667L;

    /**
     * 企业困难及问题
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "difficult_id")
    private DifficultCompany difficult;

    /**
     * 接收人的称呼
     */
    private String receiverName;

    /**
     * 当前处理问题的接收人--办理人
     * 注：因办理人会出现多个的情况 当企业第一次提交问题的时候此处是没有办理人和机构的 但是此条数据（第一条）是属于主办的
     */
    @OneToOne
    private User receiver;

//	/** 当前处理问题的接收机构.
//	 注：因办理人会出现多个的情况 当企业第一次提交问题的时候此处是没有办理人和机构的 但是此条数据（第一条）是属于主办的 */
//	@OneToOne
//	@JoinColumn(name = "group_id")
//	private Group receiverGroup;

    /**
     * 下个问题接受的DifficultTransfer
     */
    @OneToOne
    private DifficultTransfer receiverDifficulttransfer;

    /**
     * 上个问题发送的DifficultTransfer
     */
    @OneToOne
    private DifficultTransfer sendDifficulttransfer;

    /**
     * 问题发送人，以政府角度来说 第一位是企业管理员
     */
    @OneToOne
    private User sendUser;

//	/** 问题发送部门，以政府角度来说 第一位是企业  */
//	@OneToOne
//	private Group sendGroup;


    /**
     * 接受问题的时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date acceptDate;

    /**
     * 开始办理的时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    /**
     * 结束办理的时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    /**
     * 完成时限（预计时间）这个完成时限只针对于转办 并且是由上一个环节提供
     */
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date estimate;

    /**
     * 当前申请状态
     */
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "current_status")
    private DifficultTransferStatus currentStatus;

    /**
     * 申请状态详细列表
     */
    @OneToMany(mappedBy = "difficultTransfer", cascade = {CascadeType.ALL})
    private List<DifficultTransferStatus> difficultTransferStatus = new ArrayList<DifficultTransferStatus>();

    /**
     * 是否催办
     */
    private Boolean isUrge;

    /**
     * 办理意见
     */
    private String opinions;

    /**
     * 当前转办的排序
     */
    private Long orderIndex;

    /**
     * 是否是主办 true 表示主办， false 表示协办
     */
    private Boolean isHost = false;

    /**
     * 下一层是否有新的消息返回， 意思是下一层处理完毕后提醒上一层有新的消息
     */
    private Boolean isTopNotice = false;

    // 黄灯警告
    @Transient
    private boolean isRed;

    // 红灯警告
    @Transient
    private boolean isYellow;

    public DifficultCompany getDifficult() {
        return difficult;
    }

    public void setDifficult(DifficultCompany difficult) {
        this.difficult = difficult;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getSendUser() {
        return sendUser;
    }

    public void setSendUser(User sendUser) {
        this.sendUser = sendUser;
    }


    public Date getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(Date acceptDate) {
        this.acceptDate = acceptDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEstimate() {
        return estimate;
    }

    public void setEstimate(Date estimate) {
        this.estimate = estimate;
    }

    public DifficultTransferStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(DifficultTransferStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public List<DifficultTransferStatus> getDifficultTransferStatus() {
        return difficultTransferStatus;
    }

    public void setDifficultTransferStatus(
            List<DifficultTransferStatus> difficultTransferStatus) {
        this.difficultTransferStatus = difficultTransferStatus;
    }

    public Boolean getIsUrge() {
        return isUrge;
    }

    public void setIsUrge(Boolean isUrge) {
        this.isUrge = isUrge;
    }

    public String getOpinions() {
        return opinions;
    }

    public void setOpinions(String opinions) {
        this.opinions = opinions;
    }

    public Long getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Boolean getIsHost() {
        return isHost;
    }

    public void setIsHost(Boolean isHost) {
        this.isHost = isHost;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Boolean getIsTopNotice() {
        return isTopNotice;
    }

    public void setIsTopNotice(Boolean isTopNotice) {
        this.isTopNotice = isTopNotice;
    }


}
