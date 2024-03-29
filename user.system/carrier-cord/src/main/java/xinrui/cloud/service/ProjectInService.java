package xinrui.cloud.service;

import xinrui.cloud.domain.ProjectInner;
import xinrui.cloud.domain.dto.ProjectFileDto;
import xinrui.cloud.domain.dto.ProjectFileReqDto;
import xinrui.cloud.domain.dto.ProjectInnerDto;
import xinrui.cloud.dto.PageDto;

import java.util.List;

/**
 * 项目入驻相关业务逻辑处理层
 */
public interface ProjectInService extends BaseService<ProjectInner> {

    /**
     * 添加或者修改项目的信息
     * @param proInnerDto
     * @return
     */
    ProjectInnerDto saveOrUpdatePorject(ProjectInnerDto proInnerDto);

    /**
     * 上传文件信息
     * @param projectFileReqDto
     * @return
     */
    void uploadData(ProjectFileReqDto projectFileReqDto);

    /**
     * 根据项目ID和类型查询项目列表政府端
     * @param findFlag
     * @param projectName
     * @param businessType
     * @param page
     * @param pageSize
     */
    PageDto<List<ProjectInnerDto>> findAllProByState(int findFlag, String projectName, String businessType, int page, int pageSize);

    /**
     * 查询项目列表企业端
     *      * @param userId
     * @param findFlag 查询状态的标识1是待审核(审核中),2 审核通过,3审核被驳回
     * @param page
     * @param pageSize
     */
    PageDto<List<ProjectInnerDto>> findProByState(Long userId, int findFlag, int page, int pageSize);

    /**
     * 通过项目ID获取项目详情并推荐载体(政府端)
     * @param projectId 项目的ID
     * @param page
     * @param pageSize
     * @return
     */
    PageDto<List<ProjectFileDto>> findProFileByProId(Long projectId, int page, int pageSize);

    /**
     *
     * @param projectId
     * @return
     */
    ProjectInnerDto getProjectDetail(Long projectId);

    /**
     * 通过项目ID删除项目信息(企业端)
     * @param projectId
     */
    void delProject(Long[] projectId);
}
