package xinrui.cloud.domain.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.ProjectFileInner;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
@ApiModel("项目资料细节实体对象")
public class ProjectFileInnerDto extends IdEntity {

    /**
     * 文件的名
     */
    @ApiModelProperty(value = "文件名", position = 1)
    private String fileName;

    /**
     * 文件的路径
     */
    @ApiModelProperty(value = "文件的路径", position = 2)
    private String url;

    public static List<ProjectFileInner> toBean(List<ProjectFileInnerDto> projectFileInnerDtos) {
        List<ProjectFileInner> fields = Lists.newArrayList();
        for (ProjectFileInnerDto dto : projectFileInnerDtos) {
            fields.add(toBean(dto));
        }
        return fields;
    }

    public static ProjectFileInner toBean(ProjectFileInnerDto dto){
        ProjectFileInner projectFileInner = new ProjectFileInner();
        BeanUtils.copyProperties(dto,projectFileInner);
        return projectFileInner;
    }

    public static ProjectFileInnerDto copyFrom(ProjectFileInner proFileIn) {
        ProjectFileInnerDto proFileInDto = new ProjectFileInnerDto();
        BeanUtils.copyProperties(proFileIn,proFileInDto);
        return proFileInDto;
    }
}
