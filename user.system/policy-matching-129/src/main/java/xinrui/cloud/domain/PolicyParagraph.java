package xinrui.cloud.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * <B>Title:</B>PolicyParagraph</br>
 * <B>Description:</B> 政策描述对象</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0 2019/4/10 17:13
 */
@Entity(name = "zhjf_policy_paragraph")
public class PolicyParagraph extends IdEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 政策对象id
	 */
	@Column(name = "policy_id")
	private Long policyId;

	/**
	 * 政策描述
	 */
	@Column(columnDefinition = "TEXT")
	private String content;

	public Long getPolicyId() {
		return policyId;
	}

	public void setPolicyId(Long policyId) {
		this.policyId = policyId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
