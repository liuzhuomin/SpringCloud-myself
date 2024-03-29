package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.CarrierImportantData;
import xinrui.cloud.domain.IdEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * 创新载体-重要数据实体转换类
 * @author Jihy
 * @since 2019-06-25 17:49
 */
@Data
@ApiModel("载体重要数据")
public class CarrierImportantDataDTO extends IdEntity {

  @ApiModelProperty(value = "载体Id,新增操作勿传", position = 1)
  private Long carrierId;

  @ApiModelProperty(value = "最高楼层", position = 2)
  private String theTopFloor;

  @ApiModelProperty(value = "货梯载重", position = 3)
  private String transferLoad;

  @ApiModelProperty(value = "地面承重", position = 4)
  private String loadBearing;

  @ApiModelProperty(value = "已使用面积", position = 5)
  private String usedArea;

  @ApiModelProperty(value = "空置面积", position = 6)
  private String vacantArea;

  @ApiModelProperty(value = "地面层高", position = 7)
  private String groundLayerHeight;

  @ApiModelProperty(value = "用电负荷", position = 8)
  private String electricLoad;

  @ApiModelProperty(value = "租赁价格范围", position = 9)
  private String leasePriceRange;

  @ApiModelProperty(value = "载体交通情况介绍说明", position = 10)
  private String introductionToVehicle;

  @ApiModelProperty(value = "排污设备介绍说明", position = 11)
  private String introductionToSewageDisposal;

  @ApiModelProperty(value = "环评服务介绍说明", position = 12)
 private String introductionToELAServices;

  @ApiModelProperty(value = "住房介绍说明", position = 13)
  private String introductionToHousing;

  @ApiModelProperty(value = "信息化建设介绍说明", position = 14)
  private String introductionToConstruction;

  @ApiModelProperty(value = "公共技术平台介绍", position = 15)
  private String introductionToPublicTechnology;

  @ApiModelProperty(value = "意向区域需求", position = 16)
  private String intentionalRegionalDemand;

  public CarrierImportantData toCopyForm(){
    CarrierImportantData data = new CarrierImportantData();
    BeanUtils.copyProperties(this, data);
    return data;
  }

  public static CarrierImportantDataDTO copyForm(CarrierImportantData source){
    CarrierImportantDataDTO dto = new CarrierImportantDataDTO();
    BeanUtils.copyProperties(source, dto);
    dto.setCarrierId(source.getCarrier().getId());
    return dto;
  }
}
