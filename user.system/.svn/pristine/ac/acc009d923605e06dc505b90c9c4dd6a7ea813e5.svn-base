package xinrui.cloud.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * <B>Title:</B>ProblemModel.java</br>
 * <B>Description:</B>问题编辑最基础的模板,其二级选项是children</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年4月1日
 */
@Entity(name = "policy_problem_model")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProblemModel extends TreeEntity<ProblemModel> {

    private static final long serialVersionUID = 1L;

    public ProblemModel() {
        super();
    }

    public ProblemModel(String name, String description) {
        super(name, description);
    }

    public ProblemModel(String name) {
        super(name);
    }

    public ProblemModel(Long id, String name) {
        super(id, name);
    }

    /**
     * 问题编辑的模板对象
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Problem problem;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }


}
