package xinrui.cloud.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 科技金融预约对象
 *
 * @author liuliuliu
 */
@Entity
@Data
public class TechnologyFinancialAppointment extends IdEntity {

    /**
     * 预约的用户
     */
    @OneToOne(fetch = FetchType.LAZY)
    private TechnologyFinancialUser technologyFinancialUser;

    /**
     * 预约时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date appointmentDate = new Date();

    /**
     * 预约被处理的时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date processDate;
    /**
     * 科技金融对象
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private TechnologyFinancial technologyFinancial;

    /**
     * 预约用户id
     */
    private Long appointmentUserId;

}
