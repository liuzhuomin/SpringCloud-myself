package xinrui.cloud.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * <B>Title:</B>MatchTemplate</br>
 * <B>Description:</B> 政策匹配题目模板，用于保存于政府端显示</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0 2019/4/9 10:36
 */
@Entity(name = "policy_match_template")
public class MatchTemplate extends IdEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 代表政策组的id(多个使用英文下的逗号隔开)
	 */
	private String groupIds;

	/**
	 * 代表单位主体
	 */
	private String uniSubject;

	/**
	 * 最终生成的包含多个展示给前端的政策对应的问题模板对象
	 */
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@Fetch(FetchMode.SUBSELECT)
	private List<PolicyTemplateSimple> policyTemplateSimple;

	/**
	 * 企业的id
	 */
	private Long companyId;

	public String getUniSubject() {
		return uniSubject;
	}

	public void setUniSubject(String uniSubject) {
		this.uniSubject = uniSubject;
	}

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}

	public List<PolicyTemplateSimple> getPolicyTemplateSimple() {
		return policyTemplateSimple;
	}

	public void setPolicyTemplateSimple(List<PolicyTemplateSimple> policyTemplateSimple) {
		this.policyTemplateSimple = policyTemplateSimple;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
}
