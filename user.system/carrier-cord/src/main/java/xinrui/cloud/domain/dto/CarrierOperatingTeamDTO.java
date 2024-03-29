package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import xinrui.cloud.domain.CarrierOperatingTeam;
import xinrui.cloud.domain.IdEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * 运营管理团队人员转换实体类
 * @author Jihy
 * @since 2019-06-25 17:47
 */
@Data
@ApiModel("运营管理团队人员转换实体类")
public class CarrierOperatingTeamDTO extends IdEntity {

  @ApiModelProperty(value = "载体Id,新增操作勿传", position = 1)
  private Long carrierCoreDataId;

  @ApiModelProperty(value = "团队人员姓名", position = 2)
  private String name;

  @ApiModelProperty(value = "团队人员性别", position = 3)
  private String sex;

  @ApiModelProperty(value = "团队人员年龄", position = 4)
  private String age;

  @ApiModelProperty(value = "团队人员学历", position = 5)
  private String education;

  @ApiModelProperty(value = "团队人员职位", position = 6)
  private String position;

  @ApiModelProperty(value = "团队人员负责工作", position = 7)
  private String responsibleForWork;

  @ApiModelProperty(value = "团队人员主要履历", position = 8)
  private String theMainRecord;

  public CarrierOperatingTeam toCopyForm(){
    CarrierOperatingTeam team = new CarrierOperatingTeam();
    BeanUtils.copyProperties(this, team);
    return team;
  }

  public static CarrierOperatingTeamDTO copyForm(CarrierOperatingTeam source){
    CarrierOperatingTeamDTO dto = new CarrierOperatingTeamDTO();
    BeanUtils.copyProperties(source, dto);
    dto.setCarrierCoreDataId(source.getCarrierCoreData().getId());
    return dto;
  }

  public static List<CarrierOperatingTeamDTO> copyForm(List<CarrierOperatingTeam> source){
    List<CarrierOperatingTeamDTO> dtos = new ArrayList<>();
    for(CarrierOperatingTeam team : source){
      if(team != null){
        dtos.add(CarrierOperatingTeamDTO.copyForm(team));
      }
    }
    return dtos;
  }

}
