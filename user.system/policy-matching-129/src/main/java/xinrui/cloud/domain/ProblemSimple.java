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
 * <B>Title:</B>ProblemSimple</br>
 * <B>Description:</B>问题基础对象，用于前端填写问题</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0 2019/4/10 10:41
 */
@Entity(name = "policy_problem_simple")
public class ProblemSimple extends ProblemStatusFather {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 对应的问题对象(里面有对应的应该处理的逻辑)
	 */
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Problem problem;

	/**
	 * 问题基础模板对象
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	private PolicyTemplateSimple policyTemplateSimple;

	/**
	 * 问题真正的描述项
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@Fetch(FetchMode.SUBSELECT)
	private List<ProblemModelSimple> child;

	/**
	 * 问题的标题
	 */
	private String title;

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public PolicyTemplateSimple getPolicyTemplateSimple() {
		return policyTemplateSimple;
	}

	public void setPolicyTemplateSimple(PolicyTemplateSimple policyTemplateSimple) {
		this.policyTemplateSimple = policyTemplateSimple;
	}

	public List<ProblemModelSimple> getChild() {
		return child;
	}

	public void setChild(List<ProblemModelSimple> child) {
		this.child = child;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
