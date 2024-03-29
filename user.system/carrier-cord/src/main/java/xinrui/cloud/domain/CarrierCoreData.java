package xinrui.cloud.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

/**
 * 科技载体-核心数据
 * @author Jihy
 * @since 2019-06-26 15:43
 */
@Entity
@Data
@Table(name = "zhjf_carrier_core_data")
public class CarrierCoreData extends IdEntity {

  @OneToOne
  @JoinColumn(name = "carrier_id")
  private Carrier carrier;
  /** 载体名称 */
  private String brandName;
  /** 载体类型 */
  @OneToOne
  @JoinColumn(name = "carrierType_id")
  private CarrierType carrierType;
  /** 孵化基本地址 */
  private String incubationBaseAddress;
  /** 经度 */
  private BigDecimal longitude;
  /** 纬度 */
  private BigDecimal latitude;
  /** 孵化面积 */
  private String incubationArea;
  /** 所属区域 */
  private String areasToWhichTheyBelong;
  /** 专业方向 */
  @OneToOne
  @JoinColumn(name = "carrierProfessionalDirection_id")
  private CarrierProfessionalDirection carrierProfessionalDirection;
  /** 注册资金 */
  private String registeredFunds;
  /** 孵化资金 */
  private String incubationFunds;
  /** 入驻企业数量 */
  private String inEnterpriseNumber;
  /** 孵化企业数量 */
  private String incubationBaseNumber;
  /** 实际运作年限 */
  private String actualOperatingLife;
  /** 具有统计数据年限 */
  private String statisticalDataOfYears;
  /** 管理人员数量 */
  private String managersOfNumber;
  /** 大专以上人员数量 */
  private String jcAboveOfNumber;
  /** 专业背景人数 */
  private String majorNumberOfPeople;
  /** 统一社会信用代码 */
  private String xydm;
  /** 运营管理团队人员信息 */
  @OneToMany(mappedBy ="carrierCoreData", cascade = { CascadeType.ALL})
  private List<CarrierOperatingTeam> carrierOperatingTeams = new ArrayList<>();
  /** 专业服务合作机构和专家信息 */
  @OneToMany(mappedBy ="carrierCoreData", cascade = { CascadeType.ALL} )
  private List<CarrierMajor> carrierMajors = new ArrayList<>();
  /** 联系人姓名 */
  private String contactsName;
  /** 联系人电话 */
  private String contactsPhone;
}
