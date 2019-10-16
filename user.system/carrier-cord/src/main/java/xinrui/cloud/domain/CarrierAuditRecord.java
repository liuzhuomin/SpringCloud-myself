package xinrui.cloud.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 载体审核记录表
 * @author Jihy
 * @since 2019-06-25 18:02
 */
@Data @Entity @Table(name = "zhjf_carrier_audit_record") public class CarrierAuditRecord extends IdEntity {

  @OneToOne
  @JoinColumn(name = "carrier_id")
  private Carrier carrier;
  /** 审核时间 */
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date auditAt;
  /** 审核人的ID */
  private Long uid;
  /** 审核人的姓名 */
  private String uName;
  /** 审核状态(通过或拒绝) */
  private int status;
  /** 拒绝理由 */
  private String content;
}
