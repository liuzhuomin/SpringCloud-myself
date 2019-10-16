package xinrui.cloud.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;


/**
 * 诉求的督办和批示
 * @author jihy
 *
 */
@Entity
@Table(name = "qybf_difficult_supervise")
public class DifficultSupervise extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 企业困难及问题 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "difficult_id")
	private DifficultCompany difficult;
	
	/** 督办时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date superviseDate;
	
	/** 督办人 */
	@OneToOne
	@JoinColumn(name = "creator_id")
	private User creator;
	
	public DifficultCompany getDifficult() {
		return difficult;
	}

	public void setDifficult(DifficultCompany difficult) {
		this.difficult = difficult;
	}

	public Date getSuperviseDate() {
		return superviseDate;
	}

	public void setSuperviseDate(Date superviseDate) {
		this.superviseDate = superviseDate;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

}
