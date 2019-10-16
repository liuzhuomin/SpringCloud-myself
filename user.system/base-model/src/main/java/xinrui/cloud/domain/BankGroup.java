package xinrui.cloud.domain;

import com.google.common.collect.Lists;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * 银行组织
 */
@Entity
@DiscriminatorValue(GroupType.BANK)
@XmlRootElement
@Table(name = "tu_bank")
public class BankGroup extends Group {

    @ManyToMany
    @JoinTable(name = "tu_bank_user", joinColumns = {@JoinColumn(name = "tu_bank_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id")})
    @Fetch(FetchMode.SUBSELECT)
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @OrderBy("id ASC")
    private List<User> managedBy = Lists.newArrayList();

    @Override
    public String getGroupType() {
        return GroupType.BANK;
    }

    public List<User> getManagedBy() {
        return managedBy;
    }

    public void setManagedBy(List<User> managedBy) {
        this.managedBy = managedBy;
    }
}
