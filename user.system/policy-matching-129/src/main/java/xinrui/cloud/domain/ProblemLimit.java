package xinrui.cloud.domain;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * <B>Title:</B>ProblemLimit.java</br>
 * <B>Description:</B> 问题限制 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 * 问题限制的父节点的name代表是变量的id
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年4月1日
 */
@Entity(name = "policy_problemLimit")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProblemLimit extends TreeEntity<ProblemLimit> {

    private static final long serialVersionUID = 1L;

    /**
     * 左边的公式
     */
    @Column(columnDefinition = "TEXT")
    private String logicalFormLeft;

    /**
     * 左边的公式(id)
     */
    @Column(columnDefinition = "TEXT")
    private String logicalFormIdLeft;

    /**
     * 右边的公式
     */
    @Column(columnDefinition = "TEXT")
    private String logicalFormRight;

    /**
     * 左边的公式(id)
     */
    @Column(columnDefinition = "TEXT")
    private String logicalFormIdRight;

    /**
     * and 或者 or
     */
    private String logic;

    /**
     * 链接符号
     */
    private String connector;

    /**
     * 承载当前问题限制页面选中的变量
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "limit")
    private ProblemLimitInner variables;

    /**
     * 触发结构
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "limit")
    private ProblemTrigger trigger;

    /**
     * 问题对象
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Problem problem;

    public String getLogicalFormLeft() {
        return logicalFormLeft;
    }

    public void setLogicalFormLeft(String logicalFormLeft) {
        this.logicalFormLeft = logicalFormLeft;
    }

    public String getLogicalFormRight() {
        return logicalFormRight;
    }

    public void setLogicalFormRight(String logicalFormRight) {
        this.logicalFormRight = logicalFormRight;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public String getConnector() {
        return connector;
    }

    public void setConnector(String connector) {
        this.connector = connector;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public String getLogicalFormIdLeft() {
        return logicalFormIdLeft;
    }

    public void setLogicalFormIdLeft(String logicalFormIdLeft) {
        this.logicalFormIdLeft = logicalFormIdLeft;
    }

    public String getLogicalFormIdRight() {
        return logicalFormIdRight;
    }

    public void setLogicalFormIdRight(String logicalFormIdRight) {
        this.logicalFormIdRight = logicalFormIdRight;
    }

    public ProblemLimitInner getVariables() {
        return variables;
    }

    public void setVariables(ProblemLimitInner variables) {
        this.variables = variables;
    }

    public ProblemTrigger getTrigger() {
        return trigger;
    }

    public void setTrigger(ProblemTrigger trigger) {
        this.trigger = trigger;
    }

}
