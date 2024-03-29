package xinrui.cloud.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * 科技金融预约用户数据传输对象
 *
 * @author liuliuliu
 * @version 1.0
 *  2019/8/9 17:17
 */
@Entity
@Data
public class TechnologyFinancialUser extends IdEntity {
    /**
     * 用户名称
     */
    private String username;
    /**
     * 用户性别
     */
    private String gender;
    /**
     * 用户所在区域
     */
    private String region;
    /**
     * 用户所在街道
     */
    private String street;
    /**
     * 用户手机
     */
    private String phone;
    /**
     * 用户邮箱
     */
    private String email;




}
