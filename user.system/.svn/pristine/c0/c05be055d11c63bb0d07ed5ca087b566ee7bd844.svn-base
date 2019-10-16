package xinrui.cloud.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 历程批示
 * 
 * @author jihy
 *
 */
@Entity
@Table(name = "qybf_difficult_instructions")
public class DifficultInstructions extends DifficultInstructionsFather {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 企业困难及问题 */
	@OneToOne
	private DifficultCompany difficult;

	@ManyToOne
	@JoinColumn(name = "history_id")
	private DifficultHistory history;

	public DifficultCompany getDifficult() {
		return difficult;
	}

	public void setDifficult(DifficultCompany difficult) {
		this.difficult = difficult;
	}

	public DifficultHistory getHistory() {
		return history;
	}

	public void setHistory(DifficultHistory history) {
		this.history = history;
	}

}
