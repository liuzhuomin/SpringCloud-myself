package xinrui.cloud.domain;

/**
 * <B>Title:</B>Role</br>
 * <B>Description:</B> 角色实体类，对应用户相应的角色 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/26 9:31
 */

import com.google.common.collect.Lists;
import xinrui.cloud.domain.Interfaces.IRole;
import xinrui.cloud.domain.Interfaces.IUser;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * 角色.
 */
@Entity
@Table(name = "tu_role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@XmlRootElement
public class Role extends TreeEntity<Role> implements IRole {

    /**
     * 直属用户
     */
    @ManyToMany(mappedBy = "roles")
    @Fetch(FetchMode.SUBSELECT)
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @OrderBy(clause = "id ASC")
    private List<User> users = Lists.newArrayList();

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public void setUsers(List<? extends IUser> users) {
        this.users = (List<User>) users;
    }

}