package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import xinrui.cloud.domain.CarrierDataBaseFiles;
import xinrui.cloud.domain.IdEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * 载体文件资料库-具体文件
 * @author Jihy
 * @since 2019-07-04 14:49
 */
@Data @ApiModel("载体文件资料库-具体文件")
public class CarrierDataBaseFilesDTO extends IdEntity {

  @ApiModelProperty(value = "文件资料库Id", position = 1)
  private Long carrierDataBaseId;

  @ApiModelProperty(value = "文件名", position = 2)
  private String fileName;

  @ApiModelProperty(value = "文件路径", position = 3)
  private String filePath;

  @ApiModelProperty(value = "上传的用户ID", position = 4)
  private Long uid;

  @ApiModelProperty(value = "上传的时间(时间戳)", position = 5)
  private Long createAt;

  public CarrierDataBaseFiles toCopyForm(){
    CarrierDataBaseFiles files = new CarrierDataBaseFiles();
    BeanUtils.copyProperties(this, files);
    return files;
  }

  public static List<CarrierDataBaseFilesDTO> copyForm(List<CarrierDataBaseFiles> source){
    List<CarrierDataBaseFilesDTO> files = new ArrayList<>();
    for (CarrierDataBaseFiles carrierDataBaseFiles : source) {
      files.add(CarrierDataBaseFilesDTO.copyForm(carrierDataBaseFiles));
    }
    return files;
  }

  public static CarrierDataBaseFilesDTO copyForm(CarrierDataBaseFiles source){
    CarrierDataBaseFilesDTO dto = new CarrierDataBaseFilesDTO();
    BeanUtils.copyProperties(source, dto);
    dto.setCarrierDataBaseId(source.getCarrierDataBase().getId());
    dto.setCreateAt(source.getCreateAt().getTime());
    return dto;
  }
}
