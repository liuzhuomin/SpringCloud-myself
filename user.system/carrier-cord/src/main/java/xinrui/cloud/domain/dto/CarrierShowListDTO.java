package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import xinrui.cloud.domain.Carrier;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.dto.OtherGroupDto;
import xinrui.cloud.service.feign.CompanyServiceFeign;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 科技载体列表整合DTO
 * @author Jihy
 * @since 2019-07-02 20:58
 */
@Data
@ApiModel("科技载体列表数据传输DTO")
public class CarrierShowListDTO extends IdEntity {

  @ApiModelProperty(value ="品牌名", position = 1)
  private String brandName;

  @ApiModelProperty(value ="孵化基本地址", position = 2)
  private String incubationBaseAddress;

  @ApiModelProperty(value ="孵化器专业方向", position = 3)
  private String carrierProfessionalDirectionTitle;

  @ApiModelProperty(value ="注册资金", position = 4)
  private String registeredFunds;

  @ApiModelProperty(value ="已使用面积", position = 5)
  private String usedArea;

  @ApiModelProperty(value ="空置面积 ", position = 6)
  private String vacantArea;

  @ApiModelProperty(value ="入驻企业数量 ", position = 7)
  private String inEnterpriseNumber;

  @ApiModelProperty(value ="孵化企业数量 ", position = 7)
  private String incubationBaseNumber;

  @ApiModelProperty(value = "企业名称", position = 8)
  private String companyName;

  @ApiModelProperty(value = "载体统一社会信用代码", position = 9)
  private String xydm;

  @ApiModelProperty(value = "载体类型", position = 10)
  private String carrierType;

  @ApiModelProperty(value = "载体状态( 1 审核中 2 审核通过 3审核被驳回)", position = 11)
  public int status;

  @ApiModelProperty(value = "创建时间(时间戳)", position = 12)
  private Long createAr;

  @ApiModelProperty(value = "租赁价格范围", position = 13)
  private String leasePriceRange;

  @ApiModelProperty(value = "所属区域", position = 14)
  private String areasToWhichTheyBelong;

  @ApiModelProperty(value = "载体交通情况介绍说明", position = 15)
  private String introductionToVehicle;

  @ApiModelProperty(value = " 排污设备介绍说明", position = 16)
  private String introductionToSewageDisposal;

  @ApiModelProperty(value = "环评服务介绍说明", position = 17)
  private String introductionToELAServices;

  @ApiModelProperty(value = "住房介绍说明", position = 18)
  private String introductionToHousing;

  @ApiModelProperty(value = "信息化建设介绍说明 ", position =19)
  private String introductionToConstruction;

  @ApiModelProperty(value = "公共技术平台介绍", position = 20)
  private String introductionToPublicTechnology;

  @ApiModelProperty(value = "联系人姓名", position = 21)
  private String contactsName;

  @ApiModelProperty(value = "联系人电话", position = 22)
  private String contactsPhone;


  public static List<CarrierShowListDTO> copyFormByCarrier(List<Carrier> carriers, CompanyServiceFeign companyServiceFeign){
    List<CarrierShowListDTO> d = new ArrayList<>();
    for (Carrier carrier : carriers) {
      CarrierShowListDTO dto = new CarrierShowListDTO();
      dto.id = carrier.getId();
      if(carrier.getCarrierCoreData() != null){
        dto.brandName = carrier.getCarrierCoreData().getBrandName();
        dto.incubationBaseAddress = carrier.getCarrierCoreData().getIncubationBaseAddress();
        dto.carrierProfessionalDirectionTitle = carrier.getCarrierCoreData().getCarrierProfessionalDirection().getTitle();
        dto.registeredFunds = carrier.getCarrierCoreData().getRegisteredFunds();
        dto.inEnterpriseNumber = carrier.getCarrierCoreData().getInEnterpriseNumber();
        dto.incubationBaseNumber = carrier.getCarrierCoreData().getIncubationBaseNumber();
        dto.xydm = carrier.getCarrierCoreData().getXydm();
        dto.carrierType = carrier.getCarrierCoreData().getCarrierType().getTitle();
        dto.contactsName = carrier.getCarrierCoreData().getContactsName();
        dto.contactsPhone = carrier.getCarrierCoreData().getContactsPhone();
        dto.areasToWhichTheyBelong = carrier.getCarrierCoreData().getAreasToWhichTheyBelong();
      }
      if(carrier.getCarrierImportantData() != null){
        dto.usedArea = carrier.getCarrierImportantData().getUsedArea();
        dto.vacantArea = carrier.getCarrierImportantData().getVacantArea();
        dto.leasePriceRange = carrier.getCarrierImportantData().getLeasePriceRange();
        dto.introductionToVehicle = carrier.getCarrierImportantData().getIntroductionToVehicle();
        dto.introductionToSewageDisposal = carrier.getCarrierImportantData().getIntroductionToSewageDisposal();
        dto.introductionToELAServices = carrier.getCarrierImportantData().getIntroductionToELAServices();
        dto.introductionToHousing = carrier.getCarrierImportantData().getIntroductionToHousing();
        dto.introductionToConstruction = carrier.getCarrierImportantData().getIntroductionToConstruction();
        dto.introductionToPublicTechnology = carrier.getCarrierImportantData().getIntroductionToPublicTechnology();
      }
      dto.status = carrier.getStatus();
      dto.createAr = carrier.getCreateAt().getTime();
      Long groupId = carrier.getCreatorGroupId();

      OtherGroupDto otherGroupDto = companyServiceFeign.findCompanyDtoById(groupId);
      if(otherGroupDto != null){
        dto.companyName = otherGroupDto.getName();
        Map<String, Object> m = companyServiceFeign.findCompanyMapById(groupId);
        if(m != null ){
          dto.xydm = String.valueOf(m.get("org_code") != null ? m.get("org_code") : "");
        }
      }
      d.add(dto);
    }
    return d;
  }

}
