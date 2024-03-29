package xinrui.cloud.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import xinrui.cloud.BootException;
import xinrui.cloud.compoment.UserContext;
import xinrui.cloud.domain.GroupType;
import xinrui.cloud.domain.dto.ProjectAuditDto;
import xinrui.cloud.domain.dto.ProjectFileDto;
import xinrui.cloud.domain.dto.ProjectFileReqDto;
import xinrui.cloud.domain.dto.ProjectInnerDto;
import xinrui.cloud.dto.PageDto;
import xinrui.cloud.dto.ResponseDto;
import xinrui.cloud.dto.ResultDto;
import xinrui.cloud.dto.UserDto;
import xinrui.cloud.service.ProjectAuditService;
import xinrui.cloud.service.ProjectInService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <B>Title:</B>LeaderController</br>
 * <B>Description:</B>项目管理controller  </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author lpan
 * @version 1.0
 * 2019/6/25 10:59
 */
@Api("项目入驻相关接口")
@RestController
@RequestMapping("projectIn")
public class ProjectInController {

    private final static Logger LOGGER= LoggerFactory.getLogger(ProjectInController.class);

    @Autowired
    private ProjectInService projectInService;

    @Autowired
    private ProjectAuditService projectAuditService;

    @Autowired
    UserContext userContext;

    @ApiOperation(position = 1,value = "添加或者修改项目的信息(60033) ", notes = "添加或者修改项目的信息(政府端,企业端)", tags = "项目入驻相关接口", httpMethod = "POST", produces = "application/json")
    @PostMapping("saveOrUpdatePorject")
    public ResultDto<ProjectInnerDto> saveOrUpdatePorject(@RequestBody ProjectInnerDto projectInnerDto) {
        if (projectInnerDto==null){
            throw new BootException(501,"请填写项目申请信息");
        }
        //获取当前角色的信息,并且设置
        UserDto currentUser = userContext.getCurrentUser();
//        A 是政府代创建 B是企业自建,默认值是个人是为空
            if(StringUtils.equals(currentUser.getUniqueGroup().getGroupType(), GroupType.OTHER)){
                //企业设置用户信息
                projectInnerDto.setCreateType('B');
                projectInnerDto.setCreatorId(currentUser.getId());
                //创建人的组织ID（如果是企业就是企业ID 如果是政府就是政府ID）
                projectInnerDto.setCreatorGroupId(currentUser.getUniqueGroup().getId());
            }else if (StringUtils.equals(currentUser.getUniqueGroup().getGroupType(), GroupType.ORGANIZATION)){
                //政府设置用户信息
                projectInnerDto.setCreateType('A');
                projectInnerDto.setCreatorId(currentUser.getId());
                projectInnerDto.setCreatorGroupId(currentUser.getUniqueGroup().getId());
            } else {
            //默认是个人,组织是空值
             projectInnerDto.setCreatorId(currentUser.getId());
            }
            return ResponseDto.success(projectInService.saveOrUpdatePorject(projectInnerDto));
    }

    @ApiOperation(position = 2,value = "上传项目资料(60034) ", notes = "上传项目资料(政府端,企业端)", tags = "项目入驻相关接口", httpMethod = "POST", produces = "application/json")
    @PostMapping("uploadData")
    public ResultDto<?> uploadData(@RequestBody ProjectFileReqDto projectFileReqDto) {
        if (projectFileReqDto.getProjectFileDto()==null && projectFileReqDto.getProjectFileDto().size()<0){
            return  ResultDto.error("您好,请选择上传文件");
        }else {
            projectInService.uploadData(projectFileReqDto);
            return ResponseDto.success();
        }
    }

    @ApiOperation(position = 3,value = "查询项目列表企业端(60035) ", notes = "查询办理项目(企业端)", tags = "项目入驻相关接口", httpMethod = "GET", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="findFlag",value = "查询的状态码:1是待审核,2 审核中,3 审核通过,4审核被驳回",paramType = "query" ,dataType = "int",required = true,readOnly = true),
            @ApiImplicitParam(name ="page", value = "当前页码", required = false, defaultValue = "1", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name ="pageSize", value = "一页大小", required = false, defaultValue = "10", paramType = "query", dataType = "int")
    })
    @GetMapping("findProjectByState")
    public ResultDto<PageDto<List<ProjectInnerDto>>> findProByState(@RequestParam int findFlag,
                                                                    @RequestParam(required = false, defaultValue = "1") int page,
                                                                    @RequestParam(required = false, defaultValue = "10") int pageSize) {
        //获取当前用户的角色
        UserDto currentUser = userContext.getCurrentUser();
        return ResponseDto.success(projectInService.findProByState(currentUser.getId(),findFlag,page,pageSize));
    }

    @ApiOperation(position = 4,value = "企业端项目审核处理申请(60036) ", notes = "企业端项目审核处理申请", tags = "项目入驻相关接口", httpMethod = "POST", produces = "application/json")
    @PostMapping("projectAudit")
    public ResultDto<?> projectAudit(@RequestBody ProjectAuditDto proAuditDto) {
        //获取当前用户的ID权限鉴定
        projectAuditService.projectAudit(proAuditDto);
        return ResponseDto.success();
    }


    @ApiOperation(position = 5,value = "根据项目名和项目类型查询项目列表政府端(60037) ", notes = "根据项目名和项目类型查询项目(政府端)", tags = "项目入驻相关接口", httpMethod = "GET", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="findFlag",value = "查询的状态码:1是待审核,2 审核中,3 审核通过,4审核被驳回",paramType = "query" ,dataType = "int",required = true,readOnly = true),
            @ApiImplicitParam(name ="findName",value = "企业名项目名",paramType = "query" ,dataType = "String"),
            @ApiImplicitParam(name ="businessType",value = "创业类型",paramType = "query" ,dataType = "String"),
            @ApiImplicitParam(name = "page", value = "当前页码", required = false, defaultValue = "1", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "一页大小", required = false, defaultValue = "10", paramType = "query", dataType = "int")
    })
    @GetMapping("findAllProByState")
    public ResultDto<PageDto<List<ProjectInnerDto>>> findAllProByState( @RequestParam  int findFlag,
                                                                        @RequestParam  String findName,
                                                                        @RequestParam  String businessType,
                                                                        @RequestParam(required = false, defaultValue = "1") int page,
                                                                        @RequestParam(required = false, defaultValue = "10") int pageSize) {
        //获取当前用户
        UserDto currentUser = userContext.getCurrentUser();
        if (StringUtils.equals(currentUser.getUniqueGroup().getGroupType(),GroupType.ORGANIZATION)){
            return ResponseDto.success(projectInService.findAllProByState(findFlag,findName,businessType,page, pageSize));
        }else {
              return  ResultDto.error("您好,您的权限不足!");
        }
    }

    @ApiOperation(position = 6,value = "通过项目ID查询项目文件资料列表(60038) ", notes = "通过项目ID查询项目文件资料列表(政府端,企业端)", tags = "项目入驻相关接口", httpMethod = "GET", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目的ID",paramType = "query", dataType = "Long",required = true,readOnly = true),
            @ApiImplicitParam(name = "page", value = "当前页码", required = false, defaultValue = "1", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "一页大小", required = false, defaultValue = "10", paramType = "query", dataType = "int")
    })
    @GetMapping("findProFileByProId")
    public ResultDto<PageDto<List<ProjectFileDto>>> findProFileByProId(@RequestParam Long projectId,
                                                                       @RequestParam(required = false, defaultValue = "1") int page,
                                                                       @RequestParam(required = false, defaultValue = "20") int pageSize) {
        return ResponseDto.success(projectInService.findProFileByProId(projectId,page,pageSize));
    }

    @ApiOperation(position = 7,value = "获取项目详情并推荐载体(60039) ", notes = "获取项目详情并推荐载体()", tags = "项目入驻相关接口", httpMethod = "GET", produces = "application/json")
    @ApiImplicitParam(name = "projectId", value = "项目的ID",paramType = "query", dataType = "Long",required = true,readOnly = true)
    @GetMapping("getProjectDetail")
    public ResultDto<ProjectInnerDto> getProjectDetail(@RequestParam Long projectId ) {
        return ResponseDto.success(projectInService.getProjectDetail(projectId));
    }

    @ApiOperation(position = 8,value = "通过项目ID删除项目信息(60040) ", notes = "通过项目ID删除项目信息(企业端)", tags = "项目入驻相关接口", httpMethod = "DELETE", produces = "application/json")
    @RequestMapping(value = "delProject",method =RequestMethod.DELETE)
    public ResultDto<?> delProject(@RequestParam Long[] ProjectId ) {
        //获取当前用户的ID权限鉴定
        UserDto currentUser = userContext.getCurrentUser();
        if (StringUtils.equals(currentUser.getUniqueGroup().getGroupType(),GroupType.ORGANIZATION)){
            projectInService.delProject(ProjectId);
            return ResponseDto.success();
        }else {
            return  ResultDto.error("您好,您的权限不足!");
        }

    }

}
