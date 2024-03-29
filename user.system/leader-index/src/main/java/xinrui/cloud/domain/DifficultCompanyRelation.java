package xinrui.cloud.domain;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * 企业诉求关联表
 * @author jihy
 *
 */
@Entity @Table(name = "qybf_difficult_company_relation")
public class DifficultCompanyRelation extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 关联的人
	 */
	@OneToOne
	private User creator;
	
	/** * 关联的企业 */
	@OneToOne
	@JoinColumn(name = "company_id") 
	private OtherGroup company;
	
	/**
	 * 关联原因
	 */
	private String text;
	
	/**
	 * 关联的诉求
	 */
	@OneToMany(mappedBy = "difficultCompanyRelation", cascade = { CascadeType.ALL }, targetEntity = DifficultCompany.class)
	private List<DifficultCompany> difficultCompany = new ArrayList<DifficultCompany>();

	public OtherGroup getCompany() {
		return company;
	}

	public void setCompany(OtherGroup company) {
		this.company = company;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<DifficultCompany> getDifficultCompany() {
		return difficultCompany;
	}

	public void setDifficultCompany(List<DifficultCompany> difficultCompany) {
		this.difficultCompany = difficultCompany;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	
	
}
