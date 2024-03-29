package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import xinrui.cloud.domain.CarrierMajor;
import xinrui.cloud.domain.IdEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * 专业服务合作机构和专家信息实体转换类
 * @author Jihy
 * @since 2019-06-25 17:48
 */
@Data
@ApiModel("专业服务合作机构和专家信息")
public class CarrierMajorDTO extends IdEntity {

  @ApiModelProperty(value = "载体Id,新增操作勿传", position = 1)
  private Long carrierCoreDataId;

  @ApiModelProperty(value = "机构名称", position = 2)
  private String name;

  @ApiModelProperty(value = "负责人", position = 3)
  private String headPeople;

  @ApiModelProperty(value = "机构代码", position = 4)
  private String organizationCode;

  @ApiModelProperty(value = "合作期限", position = 5)
  private String cooperationTerm;

  @ApiModelProperty(value = "合作内容", position = 6)
  private String cooperationContent;

  @ApiModelProperty(value = "机构地址", position = 7)
  private String organizationAddress;

  public CarrierMajor toCopyForm(){
    CarrierMajor major = new CarrierMajor();
    BeanUtils.copyProperties(this, major);
    return major;
  }

  public static CarrierMajorDTO copyForm(CarrierMajor source){
    CarrierMajorDTO dto = new CarrierMajorDTO();
    BeanUtils.copyProperties(source, dto);
    dto.setCarrierCoreDataId(source.getCarrierCoreData().getId());
    return dto;
  }

  public static List<CarrierMajorDTO> copyForm(List<CarrierMajor> source){
    List<CarrierMajorDTO> dtos = new ArrayList<>();
    for(CarrierMajor major : source){
      dtos.add(CarrierMajorDTO.copyForm(major));
    }
    return dtos;
  }
}
