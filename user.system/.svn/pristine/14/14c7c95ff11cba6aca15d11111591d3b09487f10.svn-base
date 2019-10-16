package xinrui.cloud.domain;

import com.google.common.collect.Lists;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * 机构(政府部门).
 */
@Entity
@DiscriminatorValue(GroupType.ORGANIZATION)
@XmlRootElement
@Table(name = "tu_organization")
public class Organization extends Group {
    private static final long serialVersionUID = -2792481178746499900L;

    /**
     * 组织机构代码
     */
    @Column(nullable = true)
    private String code;

    public Organization() {
    }

    @Override
    public String getGroupType() {
        return GroupType.ORGANIZATION;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private String shouldManaged;

    public String getShouldManaged() {
        return shouldManaged;
    }

    public void setShouldManaged(String shouldManaged) {
        this.shouldManaged = shouldManaged;
    }

    /**
     * 标识各个局 1表示科技局  2 表示经促局
     */
    @Column(length = 200)
    private String isAdministration;


    public String getIsAdministration() {
        return isAdministration;
    }

    public void setIsAdministration(String isAdministration) {
        this.isAdministration = isAdministration;
    }

    /**
     * 分管领导
     */
    @ManyToMany
    @JoinTable(name = "tu_organization_user", joinColumns = {@JoinColumn(name = "organization_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id")})
    @Fetch(FetchMode.SUBSELECT)
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @OrderBy("id ASC")
    private List<User> managedBy = Lists.newArrayList();

    public List<User> getManagedBy() {
        return managedBy;
    }

    public void setManagedBy(List<User> managedBy) {
        this.managedBy = managedBy;
    }

}
