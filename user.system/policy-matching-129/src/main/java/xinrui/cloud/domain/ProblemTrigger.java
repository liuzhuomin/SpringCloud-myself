package xinrui.cloud.domain;

import com.google.common.collect.Lists;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.List;

/**
 * <B>Title:</B>ProblemTrigger.java</br>
 * <B>Description:</B> 触发结构外部对象</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年4月4日
 */
@Entity(name = "policy_problemtrigger")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProblemTrigger extends ProblemStatusFather implements ProblemTriggerFather {
    private static final long serialVersionUID = 1L;

    /**
     * 所有的选项
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ProblemTriggerInner> child = Lists.newArrayList();

    /**
     * 问题限制对象
     */
    @OneToOne(fetch = FetchType.LAZY)
    private ProblemLimit limit;

    /**
     * 对应的触发结构对象
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "trigger", optional = true, orphanRemoval = true)
    private ProblemTriggerResult triggerResult;

    /**
     * 存储name和id的键值对
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ProblemLimitInner> variables = Lists.newArrayList();

    /**
     * 最后的公式
     */
    @Column(columnDefinition = "TEXT")
    private String finalLogic;

    @Column(columnDefinition = "TEXT")
    private String logic;

    @Override
    public List<ProblemTriggerInner> getChild() {
        return child;
    }

    public void setChild(List<ProblemTriggerInner> child) {
        this.child = child;
    }


    public ProblemLimit getLimit() {
        return limit;
    }

    public void setLimit(ProblemLimit limit) {
        this.limit = limit;
    }

    public ProblemTriggerResult getTriggerResult() {
        return triggerResult;
    }

    public void setTriggerResult(ProblemTriggerResult triggerResult) {
        this.triggerResult = triggerResult;
    }

    public List<ProblemLimitInner> getVariables() {
        return variables;
    }

    public void setVariables(List<ProblemLimitInner> variables) {
        this.variables = variables;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getFinalLogic() {
        return finalLogic;
    }

    public void setFinalLogic(String finalLogic) {
        this.finalLogic = finalLogic;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }
}
