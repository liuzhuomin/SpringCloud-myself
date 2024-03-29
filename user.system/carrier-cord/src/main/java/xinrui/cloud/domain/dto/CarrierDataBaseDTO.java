package xinrui.cloud.domain.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import xinrui.cloud.domain.CarrierDataBase;
import xinrui.cloud.domain.IdEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @author Jihy
 * @since 2019-06-25 17:55
 */
@Data
@ApiModel("载体文件资料库")
public class CarrierDataBaseDTO extends IdEntity {

  @ApiModelProperty(value = "载体Id,新增操作勿传", position = 1)
  private Long carrierId;

  /** 创建人的用户ID */
  @ApiModelProperty(value = "创建人的用户ID(如果是默认的文件配置，取创建者)", position = 2)
  private Long createUId;

  @ApiModelProperty(value = "创建的时间", position = 3)
  private Long createAt;

  @ApiModelProperty(value = "名称", position = 4)
  private String name;

  @ApiModelProperty(value = "文件描述", position = 5)
  private String fileDescribe;

  @ApiModelProperty(value = "是否是配置的默认文件", position = 6)
  private Boolean isDefaultFile;

  @ApiModelProperty(value = "载体文件库具体文件列表", position = 7)
  private List<CarrierDataBaseFilesDTO> carrierDataBaseFiles;

  public CarrierDataBase toCopyForm(){
    CarrierDataBase dataBase = new CarrierDataBase();
    BeanUtils.copyProperties(this, dataBase, new String []{"carrierDataBaseFiles"});
    return dataBase;
  }

  public static CarrierDataBaseDTO copyForm(CarrierDataBase source){
    CarrierDataBaseDTO dto = new CarrierDataBaseDTO();
    BeanUtils.copyProperties(source, dto, new String[]{"carrierDataBaseFiles"});
    dto.setCarrierId(source.getCarrier().getId());
    //处理文件资料库
    dto.setCarrierDataBaseFiles(CarrierDataBaseFilesDTO.copyForm(source.getCarrierDataBaseFiles()));
    return dto;
  }

  public static List<CarrierDataBaseDTO> copyForm(List<CarrierDataBase> source){
    List<CarrierDataBaseDTO> dtos = new ArrayList<>();
    for (CarrierDataBase dataBase : source) {
      if(dataBase != null){
        dtos.add(CarrierDataBaseDTO.copyForm(dataBase));
      }
    }
    return dtos;
  }
}
