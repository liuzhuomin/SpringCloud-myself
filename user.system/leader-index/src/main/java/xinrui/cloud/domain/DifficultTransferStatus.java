package xinrui.cloud.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;


/**
 * 问题转办人以及回复列表状态表
 * @author jihy
 *
 */
@Entity
@Table(name = "qybf_difficult_transfer_status")
public class DifficultTransferStatus extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1193845337247426260L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "difficultTransfer_id")
	private DifficultTransfer difficultTransfer;
	
	/** 当前问题的处理结果-- 0 待办理 1 待处理  
	 * 2 无法办理 3 请示领导 4 转部门办理 5 正在办理 6 当前环节已完结 */
	private int status;

	/**  操作时间  */
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date operateAt;

	public DifficultTransfer getDifficultTransfer() {
		return difficultTransfer;
	}

	public void setDifficultTransfer(DifficultTransfer difficultTransfer) {
		this.difficultTransfer = difficultTransfer;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getOperateAt() {
		return operateAt;
	}

	public void setOperateAt(Date operateAt) {
		this.operateAt = operateAt;
	}
	
	
	

}
