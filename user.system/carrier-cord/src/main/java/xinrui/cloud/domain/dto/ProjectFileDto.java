package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.ProjectFile;
import xinrui.cloud.domain.ProjectFileInner;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("项目资料上传的实体")
public class ProjectFileDto extends IdEntity {
    @ApiModelProperty(value = "此次上传文件的标题", position = 1)
    private String title;

    @ApiModelProperty(value = "创建项目正文", position = 2)
    private String inputProText;

    @ApiModelProperty(value = "所关联的文件对象", position = 3)
    private List<ProjectFileInnerDto> projectFileInnerDtos;

    /**
     * 前端显示的实体属性映射
     * @param projectFileDto
     * @return
     */
    public static ProjectFile toBean(ProjectFileDto projectFileDto) {
        ProjectFile projectFile = new ProjectFile();
        BeanUtils.copyProperties(projectFileDto,projectFile,"projectFileInnerDtos");
        List<ProjectFileInnerDto> projectFileInnerDtos = projectFileDto.getProjectFileInnerDtos();
        projectFile.setProjectFileInerrList(ProjectFileInnerDto.toBean(projectFileDto.getProjectFileInnerDtos()));
        return projectFile;
    }

    //Bean实体集合转响应体集合
    public static List<ProjectFileDto> copyFroms(List<ProjectFile> projectFiles) {
        List<ProjectFileDto> proFileDtoList = new ArrayList<>();
        //获取文件信息
        for (ProjectFile projectFile : projectFiles) {
            //文件具体对象
            List<ProjectFileInnerDto> proFileInDtoList = new ArrayList<>();
            ProjectFileDto proFileDto = new ProjectFileDto();
            List<ProjectFileInner> proFileInList = projectFile.getProjectFileInerrList();
            //获取文件具体对象
            for (ProjectFileInner proFileIn : proFileInList) {
                //转前端实体对象
                ProjectFileInnerDto proFileInDto = ProjectFileInnerDto.copyFrom(proFileIn);
                proFileInDtoList.add(proFileInDto);
            }
            //转前端实体对象
            BeanUtils.copyProperties(projectFile,proFileDto);
            proFileDto.setProjectFileInnerDtos(proFileInDtoList);
            proFileDtoList.add(proFileDto);
        }
        return proFileDtoList;
    }

}
