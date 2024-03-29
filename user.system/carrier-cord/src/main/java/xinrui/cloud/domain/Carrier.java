package xinrui.cloud.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 创新载体 主体以及核心数据
 * @author Jihy
 * @since 2019-06-25 14:18
 */
@Data
@Entity
@Table(name = "zhjf_carrier")
public class Carrier extends IdEntity {

  /** 载体创建类型 A 是政府代创建 B是企业自建 */
  private char createType;
  /** 创建者的uid */
  private Long creatorId;
  /** 创建人的组织ID（如果是企业就是企业ID 如果是政府就是政府ID） */
  private Long creatorGroupId;
  /** 创建创新载体的时间 */
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createAt;
  /** 载体核心数据 */
  @OneToOne
  @JoinColumn(name = "carrierCoreData_id")
  private CarrierCoreData carrierCoreData;
  /** 载体重要数据 */
  @OneToOne
  @JoinColumn(name = "carrierImportantData_id")
  private CarrierImportantData carrierImportantData;
  /** 入驻企业 */
  @OneToMany(mappedBy ="carrier", cascade = { CascadeType.ALL})
  private List<CarrierSettledEnterprise> carrierSettledEnterprises = new ArrayList<>();
  /** 载体文件资料库 */
  @OneToMany(mappedBy ="carrier", cascade = {CascadeType.ALL} )
  private List<CarrierDataBase> carrierDataBases = new ArrayList<>();
  /** 审核状态  1 审核中 2 审核通过 3审核被驳回  */
  private int status;
  /** 审核被拒绝后保存的拒绝内容 */
  @Lob private String auditText;
  /** 载体的所属者企业ID(只能是企业) */
  private Long carrierSubordinateId;
}
