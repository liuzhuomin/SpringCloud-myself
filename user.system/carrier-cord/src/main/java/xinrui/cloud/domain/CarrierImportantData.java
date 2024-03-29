package xinrui.cloud.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

/**
 * 创新载体-重要数据
 * @author Jihy
 * @since 2019-06-25 16:34
 */
@Data @Entity @Table(name = "zhjf_carrier_important_data")
public class CarrierImportantData extends IdEntity {

  @OneToOne
  @JoinColumn(name = "carrier_id")
  private Carrier carrier;
  /** 最高楼层 */
  private String theTopFloor;
  /** 货梯载重 */
  private String transferLoad;
  /** 地面承重 */
  private String loadBearing;
  /** 已使用面积 */
  private String usedArea;
  /** 空置面积 */
  private String vacantArea;
  /** 地面层高 */
  private String groundLayerHeight;
  /** 用电负荷 */
  private String electricLoad;
  /** 租赁价格范围 */
  private String leasePriceRange;
  /** 载体交通情况介绍说明*/
  @Lob private String introductionToVehicle;
  /** 排污设备介绍说明 */
  @Lob private String introductionToSewageDisposal;
  /** 环评服务介绍说明 */
  @Lob private String introductionToELAServices;
  /** 住房介绍说明 */
  @Lob private String introductionToHousing;
  /** 信息化建设介绍说明 */
  @Lob private String introductionToConstruction;
  /** 公共技术平台介绍 */
  @Lob private String introductionToPublicTechnology;
  /** 意向区域需求 */
  @Lob private String intentionalRegionalDemand;




}
