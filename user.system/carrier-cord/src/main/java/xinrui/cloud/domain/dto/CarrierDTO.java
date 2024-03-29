package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import xinrui.cloud.domain.*;
import xinrui.cloud.dto.OtherGroupDto;
import xinrui.cloud.service.feign.CompanyServiceFeign;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 载体转换实体对象
 * @author Jihy
 * @since 2019-06-25 17:45
 */
@Data
@ApiModel("科技载体数据传输DTO")
public class CarrierDTO extends IdEntity {

  @ApiModelProperty(value ="载体核心数据", position = 1)
  private CarrierCoreDataDTO carrierCoreDataDTO;

  @ApiModelProperty(value = "载体重要数据", position = 2)
  private CarrierImportantDataDTO carrierImportantData;

  @ApiModelProperty(value = "入驻企业", position = 3)
   private List<CarrierSettledEnterpriseDTO> carrierSettledEnterprises;

  @ApiModelProperty(value = "载体文件资料库", position = 4)
  private List<CarrierDataBaseDTO> carrierDataBases;

  @ApiModelProperty(value = "审核状态", position = 5)
  private Integer status;

  @ApiModelProperty(value = "载体创建类型 A 是政府代创建 B是企业自建", position = 6)
  private Character createType;

  @ApiModelProperty(value = "创建者的UserId", position = 7)
  private Long creatorId;

  @ApiModelProperty(value = "创建者的组ID", position = 8)
  private Long creatorGroupId;

  @ApiModelProperty(value = "创建的时间", position = 9)
  private Long createAt;

  @ApiModelProperty(value = "审核被拒绝后保存的拒绝内容", position = 10)
  private String auditText;

  @ApiModelProperty(value = "载体所属的企业名称", position = 11)
  private String companyName;

  @ApiModelProperty(value = "载体所属的企业ID", position = 12)
  private Long carrierSubordinateId;

  @ApiModelProperty(value = "载体所属企业的信用代码", position = 13)
  private String companyXydm;

  public Carrier toCopyForm(){
    Carrier carrier = new Carrier();
    BeanUtils.copyProperties(this, carrier, new String[]{"carrierOperatingTeams", "carrierMajors", "carrierSettledEnterprises", "carrierDataBases"});
    return carrier;
  }

  public static CarrierDTO copyForm(Carrier source, CompanyServiceFeign companyServiceFeign){
    if(source == null){
      return null;
    }
    CarrierDTO dto = new CarrierDTO();
    BeanUtils.copyProperties(source, dto, new String[]{"carrierOperatingTeams", "carrierMajors", "carrierSettledEnterprises", "carrierDataBases"});
    OtherGroupDto otherGroupDto = companyServiceFeign.findCompanyDtoById(source.getCarrierSubordinateId());
    if(otherGroupDto != null){
      dto.setCompanyName(otherGroupDto.getName());
      Map<String, Object> m = companyServiceFeign.findCompanyMapById(source.getCarrierSubordinateId());
      dto.setCompanyXydm(String.valueOf(m.get("org_code")));
    }
    dto.setCreateAt(source.getCreateAt().getTime());
    //处理载体核心数据
    CarrierCoreData carrierCoreData = source.getCarrierCoreData();
    dto.setCarrierCoreDataDTO(CarrierCoreDataDTO.copyForm(carrierCoreData));
    //载体重要数据
    CarrierImportantData data = source.getCarrierImportantData();
    if(data != null){
      dto.setCarrierImportantData(CarrierImportantDataDTO.copyForm(data));
    }
    //入驻企业
    List<CarrierSettledEnterprise> carrierSettledEnterprise = source.getCarrierSettledEnterprises();
    dto.setCarrierSettledEnterprises(CarrierSettledEnterpriseDTO.copyForm(carrierSettledEnterprise));
    //载体资料文件库
    List<CarrierDataBase> carrierDataBases = source.getCarrierDataBases();
    dto.setCarrierDataBases(CarrierDataBaseDTO.copyForm(carrierDataBases));
    return dto;
  }

  public static List<CarrierDTO> copyFormByCarrier(List<Carrier> carrier, CompanyServiceFeign companyServiceFeign){
    List<CarrierDTO> carriers = new ArrayList<> ();
    for(Carrier c : carrier){
        if(c != null) {
          carriers.add(CarrierDTO.copyForm(c, companyServiceFeign));
        }
    }
    return carriers;
  }

}
