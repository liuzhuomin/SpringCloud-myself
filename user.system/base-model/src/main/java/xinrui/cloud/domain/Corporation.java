package xinrui.cloud.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 企业基本信息
 */
@Entity
@Table(name = "t_corporation")
@XmlRootElement
public class Corporation extends  IdEntity{

    private String xydm;

    public String getXydm() {
        return xydm;
    }

    public void setXydm(String xydm) {
        this.xydm = xydm;
    }
}
