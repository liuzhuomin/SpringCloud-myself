package xinrui.cloud.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

/**
 * 专业服务合作机构和专家信息
 * @author Jihy
 * @since 2019-06-25 14:43
 */
@Data @Entity @Table(name = "zhjf_carrier_major") public class CarrierMajor extends IdEntity {

  @ManyToOne
  @JoinColumn(name = "carrierCoreData_id")
  private CarrierCoreData carrierCoreData;
  /** 机构名称*/
  private String name;
  /** 负责人 */
  private String headPeople;
  /** 机构代码 */
  private String organizationCode;
  /** 合作期限 */
  private String cooperationTerm;
  /** 合作内容 */
  private String cooperationContent;
  /** 机构地址 */
  private String organizationAddress;

}
