package xinrui.cloud.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

/**
 * 载体入驻企业
 * @author Jihy
 * @since 2019-06-25 17:04
 */
@Data @Entity @Table(name = "zhjf_carrier_settled_enterprise")
public class CarrierSettledEnterprise extends IdEntity {


  @ManyToOne
  @JoinColumn(name = "carrier_id")
  private Carrier carrier;
  /** 项目类型 */
  private String projectType;
  /** 单位名称 */
  private String name;
  /** 机构代码 */
  private String orgCode;
  /** 行业领域 */
  private String industry;
  /** 创业类型 */
  private String entrepreneurshipType;
  /** 入驻时间 */
  private String inDate;
  /** 迁出时间 */
  private String moveOutDate;
  /** 注册时间 */
  private String registerDate;
  /** 注册地址 */
  private String registerAddress;
}
