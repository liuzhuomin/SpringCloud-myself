package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.List;
import xinrui.cloud.domain.CarrierCoreData;
import xinrui.cloud.domain.IdEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @author Jihy
 * @since 2019-06-26 15:49
 */
@Data
@ApiModel("科技载体核心数据传输DTO")
public class CarrierCoreDataDTO extends IdEntity {

  @ApiModelProperty(value = "载体名称", position = 1)
  private String brandName;

  @ApiModelProperty(value = "载体类型", position = 3)
  private Long carrierTypeId;

  @ApiModelProperty(value = "载体类型的Title", position = 4)
  private String carrierTypeTitle;

  @ApiModelProperty(value = "孵化基本地址", position = 5)
  private String incubationBaseAddress;

  @ApiModelProperty(value = "孵化面积", position = 6)
  private String incubationArea;

  @ApiModelProperty(value = "所属区域", position = 7)
  private String areasToWhichTheyBelong;

  @ApiModelProperty(value = "专业方向的Id", position = 8)
  private Long carrierProfessionalDirectionId;

  @ApiModelProperty(value = "专业方向的title", position = 9)
  private String carrierProfessionalDirectionTitle;

  @ApiModelProperty(value = "注册资金", position = 10)
  private String registeredFunds;

  @ApiModelProperty(value = "孵化资金", position = 11)
  private String incubationFunds;

  @ApiModelProperty(value = "入驻企业数量", position = 12)
  private String inEnterpriseNumber;

  @ApiModelProperty(value = "实际运作年限", position = 13)
  private String actualOperatingLife;

  @ApiModelProperty(value = "具有统计数据年限", position = 14)
  private String statisticalDataOfYears;

  @ApiModelProperty(value = "管理人员数量", position = 15)
  private String managersOfNumber;

  @ApiModelProperty(value = "大专以上人员数量", position = 16)
  private String jcAboveOfNumber;

  @ApiModelProperty(value = "专业背景人数", position = 17)
  private String majorNumberOfPeople;

  @ApiModelProperty(value = "载体统一社会信用代码", position = 18)
  private String xydm;

  @ApiModelProperty(value = "运营管理团队人员信息", position = 19)
  private List<CarrierOperatingTeamDTO> carrierOperatingTeams ;

  @ApiModelProperty(value = "专业服务合作机构和专家信息", position = 20)
  private List<CarrierMajorDTO> carrierMajors;

  @ApiModelProperty(value = "科技载体实例ID", position = 21)
  private Long carrierId;

  @ApiModelProperty(value = "经度", position = 22)
  private BigDecimal longitude;

  @ApiModelProperty(value = "纬度", position = 23)
  private BigDecimal latitude;

  @ApiModelProperty(value = "联系人姓名", position = 24)
  private String contactsName;

  @ApiModelProperty(value = "联系人电话", position = 25)
  private String contactsPhone;

  @ApiModelProperty(value = "孵化企业数量", position = 26)
  private String incubationBaseNumber;

  public static CarrierCoreDataDTO copyForm(CarrierCoreData source){
    CarrierCoreDataDTO dto = new CarrierCoreDataDTO();
    BeanUtils.copyProperties(source, dto, new String[]{"carrierOperatingTeams", "carrierMajors"});
    dto.setCarrierOperatingTeams(CarrierOperatingTeamDTO.copyForm(source.getCarrierOperatingTeams()));
    dto.setCarrierMajors(CarrierMajorDTO.copyForm(source.getCarrierMajors()));
    dto.setCarrierTypeId(source.getCarrierType().getId());
    dto.setCarrierTypeTitle(source.getCarrierType().getTitle());
    dto.setCarrierProfessionalDirectionId(source.getCarrierProfessionalDirection().getId());
    dto.setCarrierProfessionalDirectionTitle(source.getCarrierProfessionalDirection().getTitle());
    dto.setCarrierId(source.getCarrier().getId());
    return dto;
  }

  public  CarrierCoreData toCopyForm(){
    CarrierCoreData data = new CarrierCoreData();
    BeanUtils.copyProperties(this, data, new String[]{"carrierOperatingTeams", "carrierMajors"} );
    return data;
  }
}
