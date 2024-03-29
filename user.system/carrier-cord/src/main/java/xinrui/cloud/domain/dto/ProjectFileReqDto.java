package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("文件上传的实体类")
public class ProjectFileReqDto {
    @ApiModelProperty(value = "所关联项目的id", position = 1)
    private Long projectInnerId;
    @ApiModelProperty(value = "文件上传的相关信息", position = 2)
    private List<ProjectFileDto> projectFileDto;
}
