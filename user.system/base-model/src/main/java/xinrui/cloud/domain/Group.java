package xinrui.cloud.domain;

import com.google.common.collect.Lists;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;


/**
 * 组.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "groupType", discriminatorType = DiscriminatorType.STRING)
@Table(name = "tu_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@XmlRootElement
public abstract class Group extends TreeEntity<Group> {

    private static final long serialVersionUID = -2452388593856548731L;


    @Override
    public List<Group> getChildren() {
        return super.getChildren();
    }

    /**
     * 标识类别
     */
    private String groupType;

    /**
     * Logo
     */
    private String logo;

    @Column(name = "creator_id", insertable = false, updatable = false)
    private Long creatorId;

    /**
     * 创建者 主账号
     */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @ForeignKey(name = "null")
//    @JoinColumn(name = "creator_id",insertable=false,updatable=false)
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "creator_id");
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "creator_id", insertable = false, updatable = false)
//    private User creator;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /**
     * 分组直属用户
     */
    @ManyToMany(mappedBy = "groups")
    @Fetch(FetchMode.SUBSELECT)
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @OrderBy("id ASC")
    private List<User> users = Lists.newArrayList();


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
//
//    public User getCreator() {
//        return creator;
//    }
//
//    public void setCreator(User creator) {
//        this.creator = creator;
//    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }
}
