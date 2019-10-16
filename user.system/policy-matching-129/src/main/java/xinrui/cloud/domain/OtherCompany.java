package xinrui.cloud.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <B>Title:</B>OtherCompany</br>
 * <B>Description:</B>用户政策匹配微信端简洁的添加企业用</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/7/12 17:47
 */
@Entity
@Table(name = "policy_other_company")
public class OtherCompany extends IdEntity {

    private String companyName;
    private String concatName;
    private String concatPhone;

    public OtherCompany() {
    }

    public OtherCompany(String companyName, String concatName, String concatPhone) {
        this.companyName = companyName;
        this.concatName = concatName;
        this.concatName = concatName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getConcatName() {
        return concatName;
    }

    public void setConcatName(String concatName) {
        this.concatName = concatName;
    }

    public String getConcatPhone() {
        return concatPhone;
    }

    public void setConcatPhone(String concatPhone) {
        this.concatPhone = concatPhone;
    }
}
