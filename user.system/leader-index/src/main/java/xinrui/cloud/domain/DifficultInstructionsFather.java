package xinrui.cloud.domain;

import java.util.Date;

import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.User;


/**
 * 历程批示
 * 
 * @author jihy
 *
 */
@MappedSuperclass
public class DifficultInstructionsFather extends IdEntity {

	private static final long serialVersionUID = 1L;

	/** 批示的领导 */
	@OneToOne
	private User leader;

	/** 批示时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startDate;

	/** 批示内容 */
	@Lob
	private String context;

	/** 批示的领导职位 */
	private String leaderDuty;
	
	/** 批示的部门OA_ID */
	private String orId;
	
	/** 批示时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endDate;
	
	/** 是否主办单位 */
	private String isHostOrg;
	
	/** 部门名称 */
	private String receiverName;

	public String getLeaderDuty() {
		return leaderDuty;
	}

	public void setLeaderDuty(String leaderDuty) {
		this.leaderDuty = leaderDuty;
	}

	public User getLeader() {
		return leader;
	}

	public void setLeader(User leader) {
		this.leader = leader;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getOrId() {
		return orId;
	}

	public void setOrAd(String orId) {
		this.orId = orId;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getIsHostOrg() {
		return isHostOrg;
	}

	public void setIsHostOrg(String isHostOrg) {
		this.isHostOrg = isHostOrg;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public void setOrId(String orId) {
		this.orId = orId;
	}
}
