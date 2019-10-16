package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.ProjectInner;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * API的字段说明,对外提供的实体类
 */
@Data
@ApiModel("项目入驻实体对象")
public class ProjectInnerDto extends IdEntity {
    @ApiModelProperty(value = "申请人联系电话", position = 1)
    private String applicantContactInfo;
    @ApiModelProperty(value = "项目申请人名称", position = 2)
    private String applicantName;
    @ApiModelProperty(value = "场地层高", position = 3)
    private String areaHeight;
    @ApiModelProperty(value = "企业团队名称", position = 4)
    private String companyGroupName;
    @ApiModelProperty(value = "研发场地面积", position = 5)
    private String devArea;
    @ApiModelProperty(value = "用电需求", position = 6)
    private String electricDemand;
    @ApiModelProperty(value = "货梯需求", position = 7)
    private String elevatorDemand;
    @ApiModelProperty(value = "地面承重", position = 8)
    private String groundBear;
    @ApiModelProperty(value = "意向区域需求", position = 9)
    private String areasToWhichTheyBelong;
    @ApiModelProperty(value = "意向载体类型", position = 10)
    private String carrierType;
    @ApiModelProperty(value = "租赁价格范围", position = 11)
    private String leasePriceRange;
    @ApiModelProperty(value = "项目的办理状态", position = 12)
    private Integer projectState;
    @ApiModelProperty(value = "办公场地面积", position = 13)
    private String officeArea;
    @ApiModelProperty(value = "企业项目名称", position = 14)
    private String projectName;
    @ApiModelProperty(value = "项目类型", position = 15)
    private String projectType;
    @ApiModelProperty(value = "统一社会信用代码", position = 16)
    private String socialCreditCode;
    @ApiModelProperty(value = "楼层", position = 17)
    private String storey;
    @ApiModelProperty(value = "合计总面积", position = 18)
    private String vacantArea;
    @ApiModelProperty(value = "地理位置要求文本", position = 19)
    private String placeText;
    @ApiModelProperty(value = "交通需求文本", position = 20)
    private String trafficDt;
    @ApiModelProperty(value = "排污设施需求文本", position = 21)
    private String introductionToSewageDisposal;
    @ApiModelProperty(value = "环评需求文本", position = 22)
    private String introductionToELAServices;
    @ApiModelProperty(value = "住房需求文本", position = 23)
    private String houseDt;
    @ApiModelProperty(value = "信息化建设需求文本", position = 24)
    private String introductionToConstruction;
    @ApiModelProperty(value = "公共技术平台开放需求文本", position = 25)
    private String introductionToPublicTechnology;
    @ApiModelProperty(value = "备注文本", position = 26)
    private String remarkText;

    @ApiModelProperty(value = "上传资料信息集合", position = 27)
    private List<ProjectFileDto> proFileDtoList;

    @ApiModelProperty(value = "项目关联的载体信息", position = 28)
    private List<ProAndCarrierDto> proAndCarDtoList;

    @ApiModelProperty(value = "项目创建类型A是政府代创建,B是企业自建", position = 29)
    private Character createType;

    @ApiModelProperty(value = "创建者的uid", position = 30)
    private Long creatorId;

    @ApiModelProperty(value = "创建人的组织ID（如果是企业就是企业ID 如果是政府就是政府ID）", position = 31)
    private Long creatorGroupId;

    @ApiModelProperty(value = "创建新项目的时间", position = 32)
    private Long createAt;

    // 孵化面积
    @ApiModelProperty(value = "孵化面积", position = 35)
    private String hatchArea;
    @ApiModelProperty(value = "经营范围", position = 36)
    private String manageScope;
    @ApiModelProperty(value = "项目行业领域", position = 37)
    private String industryField;
    @ApiModelProperty(value = "项目创业类型", position = 38)
    private String businessType;

    /**
     * 接受前端的信息,属性映射
     * @param projectInnerDto
     * @return
     */
    public static ProjectInner toBean(ProjectInnerDto projectInnerDto) {
        ProjectInner projectInner = new ProjectInner();
        //拷贝文件
        BeanUtils.copyProperties(projectInnerDto,projectInner,new String[]{"createType", "createAt"});
        return projectInner;
    }
    //Bean转响应体
    public static ProjectInnerDto copyFrom(ProjectInner source) {
        ProjectInnerDto proDto = new ProjectInnerDto();
        //封装项目的基本信息
        BeanUtils.copyProperties(source,proDto);
        //封装项目的创建时间
        if (source.getCreateAt()!=null){
            proDto.setCreateAt(source.getCreateAt().getTime());
        }
        //封装项目的上传资料信息
        if (source.getProjectFiles()==null){
            return proDto;
        }
        proDto.setProFileDtoList(ProjectFileDto.copyFroms(source.getProjectFiles()));
        //封装项目的审核信息
        return proDto;
    }
    //Bean集合转响应体集合
    public static List<ProjectInnerDto> copyFromByProIn(List<ProjectInner> proIns) {
        List<ProjectInnerDto> proInList = new ArrayList<>();
        for (ProjectInner proIn : proIns) {
            if (proIn!=null){
                proInList.add(ProjectInnerDto.copyFrom(proIn));
            }
        }
        return proInList;
    }
}
