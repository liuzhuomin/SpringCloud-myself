package xinrui.cloud.domain;

import com.google.common.collect.Lists;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.List;

/**
 * 政策组(政策匹配单独的使用的政策组)
 *
 * @author liuliuliu
 */
@Entity(name = "policy_match_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PolicyGroup extends IdEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 政策組名稱
     */
    private String title;

    /**
     * 图片地址
     */
    @Column(name = "small_pic")
    private String imageUrl;

    /**
     * 政策描述
     */
    @Column(name = "content")
    private String content;

    /**
     * 对应局
     */
    @Column(name = "is_administration")
    private String isAdministration;


    /**
     * 政策对象集合
     */
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private List<Policy> policies = Lists.newArrayList();

    public PolicyGroup() {
    }

    public PolicyGroup(Long id, String title) {
        this.id=id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsAdministration() {
        return isAdministration;
    }

    public void setIsAdministration(String isAdministration) {
        this.isAdministration = isAdministration;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(List<Policy> policies) {
        this.policies = policies;
    }


    @Override
    public String toString() {
        return "PolicyGroup{" +
                "title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", content='" + content + '\'' +
                ", isAdministration='" + isAdministration + '\'' +
                '}';
    }
}
