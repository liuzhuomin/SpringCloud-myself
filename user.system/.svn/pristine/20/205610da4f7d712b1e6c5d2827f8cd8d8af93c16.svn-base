package xinrui.cloud.domain;

import com.google.common.collect.Lists;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.List;

/**
 * <B>Title:</B>ProblemTriggerResult.java</br>
 * <B>Description:</B> 触发结果修饰 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年4月4日
 */
@Entity(name = "policy_problemtriggerresult")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProblemTriggerResult extends ProblemStatusFather implements ProblemTriggerFather {
    private static final long serialVersionUID = 1L;
    /**
     * 触发结果的最高限值的一级二级集合
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "triggerResult")
    private List<ProblemTriggerInner> child = Lists.newArrayList();

    @OneToOne(fetch = FetchType.LAZY)
    private ProblemTrigger trigger;

    private String logic;

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    @Override
    public List<ProblemTriggerInner> getChild() {
        return child;
    }

    public void setChild(List<ProblemTriggerInner> child) {
        this.child = child;
    }

    public ProblemTrigger getTrigger() {
        return trigger;
    }

    public void setTrigger(ProblemTrigger trigger) {
        this.trigger = trigger;
    }

}
