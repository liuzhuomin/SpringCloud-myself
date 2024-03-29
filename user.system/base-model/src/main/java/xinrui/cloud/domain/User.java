package xinrui.cloud.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import xinrui.cloud.domain.Interfaces.IRole;
import xinrui.cloud.domain.Interfaces.IUser;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

/**
 * <B>Title:</B>User</br>
 * <B>Description:</B>  </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/6 10:26
 */
@Entity
@Table(name = "tu_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@XmlRootElement
public class User extends NameEntity implements IUser {

    private static final long serialVersionUID = 1L;

    /**
     * 用户登录名 .
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * 登录密码.
     */
    @Column
    private String password;

    /**
     * 加密的登录密码.
     */
    private String shaPassword;

    /**
     * 用户姓名.
     */
    private String realname;

    /**
     * 简称.
     */
    private String nickname;

    /**
     * 直接分配的角色.
     */
    @ManyToMany
    @JoinTable(name = "tu_user_role", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    @Fetch(FetchMode.SUBSELECT)
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @OrderBy("id ASC")
    @JsonIgnore
    private List<Role> roles = Lists.newArrayList();


    /**
     * 首选组织机构
     */
    @OneToOne(fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @JoinColumn(name = "unique_group_id")
    @JsonIgnore
    private Group uniqueGroup;

    /**
     * 领导关注的企业
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fucosLeader", cascade = CascadeType.ALL)
    private List<OtherGroup> focusCompanies = Lists.newArrayList();

    /**
     * 所属组织机构.
     */
    @ManyToMany
    @JoinTable(name = "tu_user_group", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "group_id")})
    @Fetch(FetchMode.SUBSELECT)
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @OrderBy("id ASC")
    @JsonIgnore
    private List<Group> groups = Lists.newArrayList();

    /**
     * 联系人
     */
    private String concat;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 电话
     */
    private String phone;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 为了oauth2的验证将shapassword返回
     *
     * @return
     */
    public String getPassword() {
        return this.shaPassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getShaPassword() {
        return shaPassword;
    }

    public void setShaPassword(String shaPassword) {
        this.shaPassword = shaPassword;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<OtherGroup> getFocusCompanies() {
        return focusCompanies;
    }

    public void setFocusCompanies(List<OtherGroup> focusCompanies) {
        this.focusCompanies = focusCompanies;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public List<Role> getRoles() {
        return roles;
    }

    @Override
    public void setRoles(List<? extends IRole> roles) {
        this.roles = (List<Role>) roles;
    }

    public Group getUniqueGroup() {
        return uniqueGroup;
    }

    public void setUniqueGroup(Group uniqueGroup) {
        this.uniqueGroup = uniqueGroup;
    }


    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public String getConcat() {
        return concat;
    }

    public void setConcat(String concat) {
        this.concat = concat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
