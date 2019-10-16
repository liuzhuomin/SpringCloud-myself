package xinrui.cloud.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;


/**
 * 历史回复
 * @author jihy
 *
 */
@Entity
@Table(name = "qybf_difficult_history")
public class DifficultHistory extends IdEntity {

	private static final long serialVersionUID = -6907153654752985984L;
	
	/** 企业困难及问题 */
	@OneToOne
	private DifficultCompany difficult;
	
	@ManyToOne
	private DifficultTransfer difficultTransfer;
	
	/** 办理人 */
	@OneToOne
	private User receiver;
	
	/**  状态 */
	private String status ;
	
	/** 办理时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startDate;
	
	/** 完成时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endDate;
	
	/**  接受问题的时间  */
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date acceptDate;

	
	/** 办理意见 */
	private String opinions;
	
	/** 申请状态详细列表 */
	@OneToMany(mappedBy = "histor", cascade = { CascadeType.ALL })
	private List<DifficultHistoryToInvolve> difficultHistoryToInvolve = new ArrayList<DifficultHistoryToInvolve>();
	
	/** 完成时限 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date estimate; 

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getOpinions() {
		return opinions;
	}

	public void setOpinions(String opinions) {
		this.opinions = opinions;
	}

	public List<DifficultHistoryToInvolve> getDifficultHistoryToInvolve() {
		return difficultHistoryToInvolve;
	}

	public void setDifficultHistoryToInvolve(
			List<DifficultHistoryToInvolve> difficultHistoryToInvolve) {
		this.difficultHistoryToInvolve = difficultHistoryToInvolve;
	}

	public DifficultTransfer getDifficultTransfer() {
		return difficultTransfer;
	}

	public void setDifficultTransfer(DifficultTransfer difficultTransfer) {
		this.difficultTransfer = difficultTransfer;
	}

	public Date getEstimate() {
		return estimate;
	}

	public void setEstimate(Date estimate) {
		this.estimate = estimate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getAcceptDate() {
		return acceptDate;
	}

	public void setAcceptDate(Date acceptDate) {
		this.acceptDate = acceptDate;
	}
	
	
	
	
}
