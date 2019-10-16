package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.ProjectAudit;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("审核实体对象")
public class ProjectAuditDto {
    @ApiModelProperty(value = "办理完结的时间",position = 1)
    private Date handleTime;

    @ApiModelProperty(value = "载体ID",position = 2)
    private Long carrierId;

    @ApiModelProperty(value = "填写处理意见",position = 3)
    private String handleOpinion;

    @ApiModelProperty(value = "修改状态的标识:1未审核,2通过审核,3审核未通过",position = 4)
    private Integer projectState;

    @ApiModelProperty(value = "项目的信息",position = 5)
    private  ProjectInnerDto projectInnerDto;

    @ApiModelProperty(value = "上传的文件资料",position = 6)
    private List<ProjectFileDto> proFileDtoList;

    @ApiModelProperty(value = "审核人的姓名",position = 7)
    private String uName;

    @ApiModelProperty(value = "审核人的ID",position = 8)
    private Long uid;

    @ApiModelProperty(value = "项目审核状态的标识初始化状态为1未审核(审核中),2通过审核,3审核未通过",position = 9)
    private Integer proAuditFlag;

    //前端响应实体对象转Bean实体
    public static ProjectAudit toBean(ProjectAuditDto proAuditDto) {
        ProjectAudit ProjectAudit = new ProjectAudit();
        BeanUtils.copyProperties(proAuditDto,ProjectAudit);
        return ProjectAudit;
    }

    public static ProjectAuditDto copyFrom(ProjectAudit proAudit) {
        ProjectAuditDto proAuditDto = new ProjectAuditDto();
        if (proAudit==null){
            return null;
        }
        BeanUtils.copyProperties(proAudit, proAuditDto);
        return proAuditDto;
    }
    //Bean实体集合转前端响应实体对象
    public static List<ProjectAuditDto> copyFroms(List<ProjectAudit> proAudits) {
        List<ProjectAuditDto> ProAuditDtos = new ArrayList<>();
        for (ProjectAudit proAudit : proAudits) {
            ProjectAuditDto proAuditDto = ProjectAuditDto.copyFrom(proAudit);
            ProAuditDtos.add(proAuditDto);
        }
        return ProAuditDtos;
    }
}
