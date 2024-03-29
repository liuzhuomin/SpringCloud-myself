package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import xinrui.cloud.domain.CarrierDefaultFile;
import xinrui.cloud.domain.IdEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @author Jihy
 * @since 2019-07-05 18:44
 */
@Data
@ApiModel("返回默认的6个文件配置")
public class CarrierDefaultFileDTO extends IdEntity {

  @ApiModelProperty(value = "文件标题", position = 1)
  private String title;

  @ApiModelProperty(value = "返回给前端，让前端做到数据绑定的数组", position = 2 )
  private List<CarrierDataBaseFilesDTO> carrierDataBaseFiles = new ArrayList<>();

  public static CarrierDefaultFileDTO copyForm(CarrierDefaultFile source){
    CarrierDefaultFileDTO dto = new CarrierDefaultFileDTO();
    BeanUtils.copyProperties(source, dto);
    return dto;
  }

  public static List<CarrierDefaultFileDTO> copyForm(List<CarrierDefaultFile> source){
    List<CarrierDefaultFileDTO> dtos = new ArrayList<> ();
    for (CarrierDefaultFile carrierDefaultFile : source) {
      dtos.add(CarrierDefaultFileDTO.copyForm(carrierDefaultFile));
    }
    return dtos;
  }
}
