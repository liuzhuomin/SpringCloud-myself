package xinrui.cloud.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * <B>Title:</B>ProblemModelSimple</br>
 * <B>Description:</B> 问题编辑的详情展示 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0 2019/4/10 16:02
 */
@Entity(name = "policy_problem_model_simple")
public class ProblemModelSimple extends TreeEntity<ProblemModelSimple> {

	private static final long serialVersionUID = 1L;

	/**
	 * 等待填入的值
	 */
	private String value;

	@ManyToOne(fetch = FetchType.LAZY)
	private ProblemSimple problemSimple;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ProblemSimple getProblemSimple() {
		return problemSimple;
	}

	public void setProblemSimple(ProblemSimple problemSimple) {
		this.problemSimple = problemSimple;
	}
}
