package xinrui.cloud.domain;

import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.User;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 走访领导表
 *
 * @author jihy
 */
@Entity
@Table(name = "qybf_visitre_leaders")
public class VisitreLeaders extends IdEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 走访领导名称
     */
    private String name;

    /**
     * 走访领导对应的User
     */
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
