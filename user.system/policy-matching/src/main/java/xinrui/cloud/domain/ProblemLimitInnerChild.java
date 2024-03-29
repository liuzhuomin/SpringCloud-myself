package xinrui.cloud.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity(name = "policy_problemlimitinner_child")
public class ProblemLimitInnerChild extends ProblemLimitInner {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private ProblemTrigger trigger;

    @Override
    public ProblemTrigger getTrigger() {
        return trigger;
    }

    @Override
    public void setTrigger(ProblemTrigger trigger) {
        this.trigger = trigger;
    }

}
