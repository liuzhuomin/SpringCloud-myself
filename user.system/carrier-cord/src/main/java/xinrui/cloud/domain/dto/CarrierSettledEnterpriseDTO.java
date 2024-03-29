package xinrui.cloud.domain.dto;
import	java.util.ArrayList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import xinrui.cloud.domain.CarrierSettledEnterprise;
import xinrui.cloud.domain.IdEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @author Jihy
 * @since 2019-06-25 17:54
 */
@Data
@ApiModel("入驻企业")
public class CarrierSettledEnterpriseDTO extends IdEntity {

  @ApiModelProperty(value = "载体Id,新增操作勿传", position = 1)
  private Long carrierId;

  @ApiModelProperty(value = "项目类型", position = 2)
  private String projectType;

  @ApiModelProperty(value = "单位名称", position = 3)
  private String name;

  @ApiModelProperty(value = "机构代码", position = 4)
  private String orgCode;

  @ApiModelProperty(value = "行业领域", position = 5)
  private String industry;

  @ApiModelProperty(value = "创业类型", position = 6)
  private String entrepreneurshipType;

  @ApiModelProperty(value = "入驻时间", position = 7)
  private String inDate;

  @ApiModelProperty(value = "迁出时间", position = 8)
  private String moveOutDate;

  @ApiModelProperty(value = "注册时间", position = 9)
  private String registerDate;

  @ApiModelProperty(value = "注册地址", position = 10)
  private String registerAddress;

  public CarrierSettledEnterprise toCopyForm(){
    CarrierSettledEnterprise settledEnterprise = new CarrierSettledEnterprise();
    BeanUtils.copyProperties(this, settledEnterprise);
    return settledEnterprise;
  }

  public static CarrierSettledEnterpriseDTO copyForm(CarrierSettledEnterprise source){
    CarrierSettledEnterpriseDTO dto = new CarrierSettledEnterpriseDTO();
    BeanUtils.copyProperties(source, dto);
    dto.setCarrierId(source.getId());
    return dto;
  }

  public static List<CarrierSettledEnterpriseDTO> copyForm(List<CarrierSettledEnterprise> source){
    List<CarrierSettledEnterpriseDTO> dtos = new ArrayList<> ();
    for (CarrierSettledEnterprise entity : source) {
      dtos.add(CarrierSettledEnterpriseDTO.copyForm(entity));
    }
    return dtos;
  }
}
