package xinrui.cloud.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import xinrui.cloud.domain.dto.*;
import xinrui.cloud.dto.PageDto;
import xinrui.cloud.dto.ResponseDto;
import xinrui.cloud.dto.ResultDto;
import xinrui.cloud.service.PolicyGroupServcice;
import xinrui.cloud.service.PolicyService;
import xinrui.cloud.service.PolicyTemplateService;
import xinrui.cloud.util.AppUtils;
import xinrui.cloud.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "政策相关接口")
@RestController
@RequestMapping("policy")
public class PolicyController {

    @Autowired
    PolicyService policyservice;

    @Autowired
    PolicyTemplateService templateService;

    @Autowired
    PolicyGroupServcice policyGroupServcice;


    @ApiOperation(value = "获取未创建模板的政策列表 (code 50001) ", notes = "1.查询所有已经上线的并且未曾创建政策匹配模板并且是科创的政策;</br>2.如果传递了name参数，按照条件1查询并且根据name模糊搜索政策的短标题</br>3如果传递了policyId參數，则查询所有已经上线的并且未曾创建政策匹配模板并且是科创的政策，或者id为policyid的政策;", tags = "政策相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "政策简写名称", paramType = "query", dataType = "String", required = false, readOnly = true),
            @ApiImplicitParam(name = "policyId", value = "需要排除筛选的政策id", paramType = "query", dataType = "Long", required = false, readOnly = true)})
    @PostMapping("list/notCreate")
    public ResultDto<List<PolicyDto>> list(@RequestParam(required = false) String name,
                                           @RequestParam(required = false) Long policyId) {
        return ResponseDto.success(templateService.listCreatedPolicyId(name, policyId),
                AppUtils.getRealUrl("policyTemplate/save"));
    }

    @ApiOperation(value = "获取所有的政策组名称 (code 50029) ", notes = "获取所有的政策组名称", tags = "政策相关接口", httpMethod = "POST", produces = "application/json")
    @PostMapping("list/group/all")
    public ResultDto<List<PolicyGroupDto>> listAllGroup() {
        return ResponseDto.success(policyservice.listAllGroup(), AppUtils.getRealUrl("list/group/all"));
    }

    @ApiOperation(value = "根据政策组id分页获取政策列表(code 50030) ", notes = "通过政策组id分页获取指定政策组的所有政策列表,如果不传递groupId，查询全部的", tags = "政策相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "政策组id", required = false, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "currentPage", value = "当前页码", required = false, defaultValue = "1", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "一页大小", required = false, defaultValue = "10", paramType = "query", dataType = "int")})
    @PostMapping("page/byGroup")
    public ResultDto<PageDto<List<PolicyDto>>> listPolicyByGroupId(@RequestParam(required = false) Long groupId, @RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseDto.success(policyservice.listPolicyByGroupId(groupId, currentPage, pageSize));
    }


    @ApiOperation(value = "获取创建了模板的政策组列表 (code 50031) ", notes = "列出政策组列表，如果没有单位属性可不传递，" +
            "便查询所有创建了模板的政策组，否则查询按照单位属性创建了模板的所有所属政策的所属政策组", tags = "政策相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParam(name = "uniSubject", value = "单位主体", paramType = "query", dataType = "String", required = true, readOnly = true)
    @PostMapping("list/group/created")
    public ResultDto<List<PolicyGroupDto>> listGroup(@RequestParam(required = false) String uniSubject) {
        return ResponseDto.success(policyservice.listGroup(uniSubject));
    }

    @ApiOperation(value = "获取题目(code 50032) ", notes = "获取题目模板,根据之前填写的值和当前用户选择的政策组生成匹配模板", tags = "政策相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupIds", value = "政策组id，使用英文状态下的逗号隔开", paramType = "query", dataType = "String", required = true, readOnly = true),
            @ApiImplicitParam(name = "uniSubject", value = "单位主体", paramType = "query", dataType = "String", required = true, readOnly = true)})
    @PostMapping("create/template")
    public ResultDto<MatchtemplateDto> createTemplate(@RequestParam String groupIds, @RequestParam String uniSubject) {
        List<String> groupIdList = DataUtil.split(groupIds);
        Assert.notEmpty(groupIdList, "政策组不能为空!");
        List<Long> groupLongList = Lists.newArrayList();
        for (String str : groupIdList) {
            groupLongList.add(Long.parseLong(str));
        }
        return ResponseDto.success(policyservice.createTemplate(groupLongList, uniSubject), AppUtils.getRealUrl("/policy/match"));
    }

    @ApiOperation(value = "政策匹配 (code 50033) ", notes = "将json数据传递过来，进行政策匹配", tags = "政策相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "json", value = "数据格式见create/template接口返回的t", paramType = "query", dataType = "String", required = true, readOnly = true),
            @ApiImplicitParam(name = "companyId", value = "企业id", paramType = "query", dataType = "Long", required = true, readOnly = true)})
    @PostMapping("match")
    public ResultDto<MatchResultDto> match(@RequestParam String json, @RequestParam Long companyId) {
        return ResponseDto.success(policyservice.match(new MatchtemplateDto(JSON.parseArray(json, PolicyGroupDto.class)), companyId));
    }

    @ApiOperation(position = 18, value = "修改政策组(50051) ", notes = "修改政策组", tags = "政策相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "政策组和政策的json字符串groupJson", value = "政策组和政策的json字符串", paramType = "query", dataType = "INT", required = true, readOnly = true)})
    @PostMapping("editGroup")
    public ResultDto<List<PolicyGroupDto2>> editGroup(@RequestParam String groupJson) {
        return ResponseDto.success(policyGroupServcice.editGroup(groupJson));
    }

    @ApiOperation(position = 18, value = "列出政策组(50052) ", notes = "列出政策组,返回的是一个数组对象，这个集合对象不可能为空，第一个下标是没有分配政策组的政策", tags = "政策相关接口", httpMethod = "POST", produces = "application/json")
    @PostMapping("listGroup")
    public ResultDto<List<PolicyGroupDto2>> listGroup() {
        return ResponseDto.success(policyGroupServcice.listGroup());
    }


    @ApiOperation(position = 19, value = "根据名称搜索企业名称(50053) ", notes = "根据企业名称模糊搜索企业名称和企业id", tags = "政策相关接口", httpMethod = "POST", produces = "application/json")
    @PostMapping("findCompanyInfo/byName")
    public ResultDto<List<CompanyInfoDto>> findInfoByName(@RequestParam String name) {
        return ResponseDto.success(policyservice.findInfoByName(name));
    }


    @ApiOperation(position = 20, value = "添加新的企业(50054) ", notes = "仅仅政策匹配小程序和H5用得上，添加简略的企业", tags = "政策相关接口", httpMethod = "POST", produces = "application/json")
    @PostMapping("addCompany")
    public ResultDto<CompanyInfoDto> addCompany(@RequestParam String companyName, @RequestParam String concatName, @RequestParam String concatPhone) {
        return ResponseDto.success( policyservice.addCompany(companyName, concatName, concatPhone));
    }
}
