package xinrui.cloud.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;


/**
 * 历史回复--涉及部门
 * @author jihy
 *
 */
@Entity
@Table(name = "qybf_difficult_history_involve")
public class DifficultHistoryToInvolve extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6255121195558043145L;
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "histor_id")
	private DifficultHistory histor;
	
	
//	/** 涉及部门 */
//	@OneToOne
//	private Organization group;
	
	/** 联系人 */
	@OneToOne
	private User user;
	
	private String telePhone;
	
	/**
	 * 首次阅读时间(如果为null代表未曾阅读)
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date readDate;
//
//	public Group getGroup() {
//		return group;
//	}
//
//	public void setGroup(Organization group) {
//		this.group = group;
//	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTelePhone() {
		return telePhone;
	}

	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}

	public DifficultHistory getHistor() {
		return histor;
	}

	public void setHistor(DifficultHistory histor) {
		this.histor = histor;
	}

	public Date getReadDate() {
		return readDate;
	}

	public void setReadDate(Date readDate) {
		this.readDate = readDate;
	}
	
}
