package xinrui.cloud.domain;

import com.google.common.base.MoreObjects.ToStringHelper;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * <B>Title:</B>ProblemTriggerInner.java</br>
 * <B>Description:</B> 触发结构内部对象 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年4月4日
 */
@Entity(name = "policy_problemtriggerinner")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProblemTriggerInner extends TreeEntity<ProblemTriggerInner> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "id公式", position = 3)
    private String logicOne;

    @ApiModelProperty(name = "name公式", position = 4)
    private String logicTwo;

    @ApiModelProperty(name = "最高限制的值", position = 5)
    private Integer value;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProblemTrigger problemTrigger;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProblemTriggerResult triggerResult;

    public String getLogicOne() {
        return logicOne;
    }

    public void setLogicOne(String logicOne) {
        this.logicOne = logicOne;
    }

    public String getLogicTwo() {
        return logicTwo;
    }

    public void setLogicTwo(String logicTwo) {
        this.logicTwo = logicTwo;
    }

    public ProblemTrigger getProblemTrigger() {
        return problemTrigger;
    }

    public void setProblemTrigger(ProblemTrigger problemTrigger) {
        this.problemTrigger = problemTrigger;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public ProblemTriggerResult getTriggerResult() {
        return triggerResult;
    }

    public void setTriggerResult(ProblemTriggerResult triggerResult) {
        this.triggerResult = triggerResult;
    }

    @Override
    public ToStringHelper toStringHelper() {
        return super.toStringHelper().add("name", name).add("problemTrigger", problemTrigger).add("triggerResult",
                triggerResult);
    }
}
