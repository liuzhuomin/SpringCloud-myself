package xinrui.cloud.domain;

import com.google.common.base.MoreObjects;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.List;

/**
 * <B>Title:</B>ProblemLimitInner.java</br>
 * <B>Description:</B> 用于存储问题限制触发器等需要存储变量id的对象 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年4月3日
 */
@Entity(name = "policy_problemlimitinner")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProblemLimitInner extends TreeEntity<ProblemLimitInner> {
    private static final long serialVersionUID = 1L;
    /**
     * 变量id
     */
    private Long variableId;

    /**
     * 当前变量对应的值
     */
    private String value;

    /**
     * 问题限制对象
     */
    @OneToOne
    private ProblemLimit limit;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProblemTrigger trigger;

    public ProblemLimitInner() {
    }

    public ProblemLimitInner(Long variableId, String name) {
        super(name);
        this.variableId = variableId;
    }

    public ProblemLimitInner(List<ProblemLimitInner> variables) {
        this.children = variables;
    }

    public Long getVariableId() {
        return variableId;
    }

    public void setVariableId(Long variableId) {
        this.variableId = variableId;
    }

    public ProblemLimit getLimit() {
        return limit;
    }

    public void setLimit(ProblemLimit limit) {
        this.limit = limit;
    }

    public ProblemTrigger getTrigger() {
        return trigger;
    }

    public void setTrigger(ProblemTrigger trigger) {
        this.trigger = trigger;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public MoreObjects.ToStringHelper toStringHelper() {
        return MoreObjects.toStringHelper(this).add("name", name).add("variableId", variableId);
    }
}