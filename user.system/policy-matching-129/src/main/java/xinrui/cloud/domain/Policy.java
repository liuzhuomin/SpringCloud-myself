package xinrui.cloud.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;


/**
 * 政策列表
 *
 * @author liuliuliu
 */
@Entity(name = "zhjf_policy")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Policy extends IdEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 短标题
     */
    @Column(name = "short_title")
    private String shortTitle;

    /**
     * 长标题
     */
    @Column(name = "long_title")
    private String longTitle;

    /**
     * 状态 P 为上线 D为下线 R 为删除
     */
    @Column(name = "status")
    private String status;

    /**
     * 对应局
     */
    @Column(name = "is_administration")
    private String isAdministration;

    /**
     * 排序下标
     */
    @JsonIgnore
    @Column(name = "order_index")
    private Long orderIndex;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "policy_match_group_ship_id")
    private PolicyGroup policyGroup;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "policy", fetch = FetchType.LAZY, orphanRemoval = true)
    private PolicyTemplate policyTemplate;

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getLongTitle() {
        return longTitle;
    }

    public void setLongTitle(String longTitle) {
        this.longTitle = longTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsAdministration() {
        return isAdministration;
    }

    public void setIsAdministration(String isAdministration) {
        this.isAdministration = isAdministration;
    }

    public Long getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
    }

    public PolicyTemplate getPolicyTemplate() {
        return policyTemplate;
    }

    public void setPolicyTemplate(PolicyTemplate policyTemplate) {
        this.policyTemplate = policyTemplate;
    }

//    public Long getGroupId() {
//        return groupId;
//    }
//
//    public void setGroupId(Long groupId) {
//        this.groupId = groupId;
//    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public PolicyGroup getPolicyGroup() {
        return policyGroup;
    }

    public void setPolicyGroup(PolicyGroup policyGroup) {
        this.policyGroup = policyGroup;
    }
}
