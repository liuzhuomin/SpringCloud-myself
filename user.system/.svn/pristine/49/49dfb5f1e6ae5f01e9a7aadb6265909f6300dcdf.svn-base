package xinrui.cloud.domain;

import com.google.common.base.MoreObjects.ToStringHelper;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

/**
 * 政策模版对象
 *
 * @author liuliuliu
 */
@Entity(name = "policy_template")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PolicyTemplate extends IdEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 政策对象
     */
    @OneToOne(fetch = FetchType.EAGER, optional = true)
    private Policy policy;

    /**
     * 政策主体名称
     */
    private String mainName;

    /**
     * 单位主体(多个使用逗号隔开)
     */
    private String unitSubject;

    /**
     * 政策描述
     */
    private String description;

    /**
     * 模版创建的环节对象
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "template")
    private PolicyActivity activity;

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public String getUnitSubject() {
        return unitSubject;
    }

    public void setUnitSubject(String unitSubject) {
        this.unitSubject = unitSubject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PolicyActivity getActivity() {
        return activity;
    }

    public void setActivity(PolicyActivity activity) {
        this.activity = activity;
    }

    public String getMainName() {
        return mainName;
    }

    public void setMainName(String mainName) {
        this.mainName = mainName;
    }

    @Override
    public ToStringHelper toStringHelper() {
        return super.toStringHelper().add("activity", activity);
    }

}
