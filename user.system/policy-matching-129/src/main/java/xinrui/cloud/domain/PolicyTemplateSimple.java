package xinrui.cloud.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * <B>Title:</B>PolicyTemplateSimple</br>
 * <B>Description:</B>用于记录填写过的题目（一旦填写完毕即便后台匹配规则改变，依然不影响） </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0 2019/4/10 9:55
 */
@Entity(name = "policy_template_simple")
public class PolicyTemplateSimple extends IdEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 最终生成的政策匹配模板对象
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	private MatchTemplate matchTemplate;

	/**
	 * 当前生成题目对应的政策对象
	 */
	@OneToOne
	private Policy policy;

	/**
	 * 所有的题目对象
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@Fetch(FetchMode.SUBSELECT)
	private List<ProblemSimple> problemSimples;

	public List<ProblemSimple> getProblemSimples() {
		return problemSimples;
	}

	public void setProblemSimples(List<ProblemSimple> problemSimples) {
		this.problemSimples = problemSimples;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public MatchTemplate getMatchTemplate() {
		return matchTemplate;
	}

	public void setMatchTemplate(MatchTemplate matchTemplate) {
		this.matchTemplate = matchTemplate;
	}
}
