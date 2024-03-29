package xinrui.cloud.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;


/**
 * 走访记录
 * @author jihy
 *
 */
@Entity 
@Table(name = "qybf_visitre_cord") 
public class VisitreCord  extends IdEntity {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	/**
//	 * 创建走访记录用户
//	 */
//	@OneToOne
//	private User creator;
//
//	/** * 走访的企业 */
//	@OneToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "company_id")
	@Column(name="company_id")
	private Long companyId;
	
	/** 上报走访的时间 */
	@Temporal(TemporalType.TIMESTAMP) 
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date commitDate;
	
	/*** 挂点领导 */
	private String gdld;
	
	/**
	 * 挂点领导id
	 */
	private Long gdldId;
	
	/** * 主营业务  */
	private String zyyw;
	
	/*** 关联公司情况  */
	private String glgsqk;
	
	/*** 行业地位 */
	private String hydw;
	
	/**
	 * 在光明区项目情况
	 */
	private String inGmProjectState;
	
	/**
	 * 牵头单位
	 */
	private String qtdw;
	
	/**
	 * 所属街道
	 */
	private String ssjd;
	
	/**
	 * 上年度产值
	 */
	private String sndcz;
	
	/**
	 * 截止目前产值
	 */
	private String jzmqcz;
	
	/**
	 * 上年度税收
	 */
	private String sndss;
	
	/**
	 * 截止目前税收
	 */
	private String jzmqss;
	
	/**
	 * 知识产权情况
	 */
	private String zscqqk;
	
	/*** 主要负责人简介  */
	private String zyfzrjj;
	
	/*** 在其他区物业情况 */
	private String zqtqwyqk;
	
	/** 走访领导  */
	@OneToMany(mappedBy = "visitreCord", cascade = { CascadeType.ALL })
 	private List<VisitreLeaderShip> leaderShip = new ArrayList<VisitreLeaderShip>();
	
	/**
	 * 默认第一位走访领导id
	 */
	private Long oneLeaderId;
	
	/**
	 * 默认第一位走访领导姓名
	 */
	private String oneLeaderName;
	
	/** 走访类型  */
	@OneToOne
	@JoinColumn(name="visitreCordType_id")
	private VisitreCordType visitreCordType;
	
	/** 走访时间 */
	@Temporal(TemporalType.DATE) 
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date visitreDate;
	
	/** 调研次数  */
	private int researchCount;
	
	/** 对应的诉求 */
	@OneToMany(mappedBy = "visitreCord", cascade = { CascadeType.ALL }, targetEntity = DifficultCompany.class)
	private List<DifficultCompany> difficultCompany = new ArrayList<DifficultCompany>();
	
	/** 走访前的问题 */
	private String preVisitIssues;
	
	/**
	 * 走访状态 分为走访前 0 | 走访后 1 | 取消走访 2 
	 */
	private int status;
	
	/**
	 * 走访后计划完成时间
	 */
	@Temporal(TemporalType.DATE) 
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date afterPlanDate;

	/**
	 * 下次更新时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date lastUpdate;
	
	
	
	public Long getOneLeaderId() {
		return oneLeaderId;
	}

	public void setOneLeaderId(Long oneLeaderId) {
		this.oneLeaderId = oneLeaderId;
	}

	public String getOneLeaderName() {
		return oneLeaderName;
	}

	public void setOneLeaderName(String oneLeaderName) {
		this.oneLeaderName = oneLeaderName;
	}

	public Date getAfterPlanDate() {
		return afterPlanDate;
	}

	public void setAfterPlanDate(Date afterPlanDate) {
		this.afterPlanDate = afterPlanDate;
	}


	public Date getCommitDate() {
		return commitDate;
	}

	public void setCommitDate(Date commitDate) {
		this.commitDate = commitDate;
	}

	public String getGdld() {
		return gdld;
	}

	public void setGdld(String gdld) {
		this.gdld = gdld;
	}

	public String getZyyw() {
		return zyyw;
	}

	public void setZyyw(String zyyw) {
		this.zyyw = zyyw;
	}

	public String getGlgsqk() {
		return glgsqk;
	}

	public void setGlgsqk(String glgsqk) {
		this.glgsqk = glgsqk;
	}

	public String getHydw() {
		return hydw;
	}

	public void setHydw(String hydw) {
		this.hydw = hydw;
	}

	public String getInGmProjectState() {
		return inGmProjectState;
	}

	public void setInGmProjectState(String inGmProjectState) {
		this.inGmProjectState = inGmProjectState;
	}

	public String getQtdw() {
		return qtdw;
	}

	public void setQtdw(String qtdw) {
		this.qtdw = qtdw;
	}

	public String getSsjd() {
		return ssjd;
	}

	public void setSsjd(String ssjd) {
		this.ssjd = ssjd;
	}

	public String getSndcz() {
		return sndcz;
	}

	public void setSndcz(String sndcz) {
		this.sndcz = sndcz;
	}

	public String getJzmqcz() {
		return jzmqcz;
	}

	public void setJzmqcz(String jzmqcz) {
		this.jzmqcz = jzmqcz;
	}

	public String getSndss() {
		return sndss;
	}

	public void setSndss(String sndss) {
		this.sndss = sndss;
	}

	public String getJzmqss() {
		return jzmqss;
	}

	public void setJzmqss(String jzmqss) {
		this.jzmqss = jzmqss;
	}

	public String getZscqqk() {
		return zscqqk;
	}

	public void setZscqqk(String zscqqk) {
		this.zscqqk = zscqqk;
	}

	public String getZyfzrjj() {
		return zyfzrjj;
	}

	public void setZyfzrjj(String zyfzrjj) {
		this.zyfzrjj = zyfzrjj;
	}

	public String getZqtqwyqk() {
		return zqtqwyqk;
	}

	public void setZqtqwyqk(String zqtqwyqk) {
		this.zqtqwyqk = zqtqwyqk;
	}

	public List<VisitreLeaderShip> getLeaderShip() {
		return leaderShip;
	}

	public void setLeaderShip(List<VisitreLeaderShip> leaderShip) {
		this.leaderShip = leaderShip;
	}

	public VisitreCordType getVisitreCordType() {
		return visitreCordType;
	}

	public void setVisitreCordType(VisitreCordType visitreCordType) {
		this.visitreCordType = visitreCordType;
	}

	public Date getVisitreDate() {
		return visitreDate;
	}

	public void setVisitreDate(Date visitreDate) {
		this.visitreDate = visitreDate;
	}

	public int getResearchCount() {
		return researchCount;
	}

	public void setResearchCount(int researchCount) {
		this.researchCount = researchCount;
	}

	public List<DifficultCompany> getDifficultCompany() {
		return difficultCompany;
	}

	public void setDifficultCompany(List<DifficultCompany> difficultCompany) {
		this.difficultCompany = difficultCompany;
	}

	public String getPreVisitIssues() {
		return preVisitIssues;
	}

	public void setPreVisitIssues(String preVisitIssues) {
		this.preVisitIssues = preVisitIssues;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getGdldId() {
		return gdldId;
	}

	public void setGdldId(Long gdldId) {
		this.gdldId = gdldId;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
