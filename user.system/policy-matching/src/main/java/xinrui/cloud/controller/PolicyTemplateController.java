package xinrui.cloud.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import xinrui.cloud.BootException;
import xinrui.cloud.domain.Policy;
import xinrui.cloud.domain.PolicyTemplate;
import xinrui.cloud.domain.dto.PolicyTemplateDto;
import xinrui.cloud.dto.ResponseDto;
import xinrui.cloud.dto.ResultDto;
import xinrui.cloud.service.PolicyService;
import xinrui.cloud.service.PolicyTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("政策模版相关接口")
@RestController
@RequestMapping("policy/template")
public class PolicyTemplateController {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(PolicyTemplateController.class);

    @Autowired
    PolicyTemplateService policytemplateService;

    @Autowired
    PolicyService policyService;

    @ApiOperation(value = "添加政策模板主体 (code 50002)", notes = "添加政策主体模版,多个单位主体逗号隔开", tags = "政策模版相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "policyId", value = "政策id", paramType = "query", dataType = "Long", required = true),
            @ApiImplicitParam(name = "unitSubject", value = "单位主体(多个使用,隔开)", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "description", value = "政策描述", paramType = "query", dataType = "String", required = true)})
    @PostMapping("add")
    public ResultDto<PolicyTemplateDto> add(@RequestParam Long policyId, @RequestParam String unitSubject,
                                            @RequestParam String description) {
        Policy policy = policyService.findById(policyId);
        if (policy == null) {
            throw new BootException("政策不存在!");
        }
        if (!policytemplateService.checkNotCreate(policyId)) {
            throw new BootException("当前政策已经创建模版!");
        }
        return ResponseDto.success(policytemplateService.createTemplate(unitSubject, description,policyId));
    }

    @ApiOperation(value = "删除政策模板主体 (code 50004)", notes = "删除成功为返回的消息为success,否则为error", tags = "政策模版相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tempId", value = "模版id", paramType = "query", dataType = "Long", required = true)})
    @PostMapping("delete")
    public ResultDto<?> delete(@RequestParam Long tempId) {
        policytemplateService.removeAll(tempId);
        return ResponseDto.success();
    }

    @ApiOperation(value = "修改政策模板主体 (code 50003)", notes = "修改政策模版主体,多个单位主体逗号隔开,仅可修改单位主体和模版描述", tags = "政策模版相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tempId", value = "模版id", paramType = "query", dataType = "Long", required = true),
            @ApiImplicitParam(name = "unitSubject", value = "单位主体(多个使用,隔开)", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "description", value = "政策描述", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "policyId", value = "政策id", paramType = "query", dataType = "Long", required = true)})
    @PostMapping("edit")
    public ResultDto<PolicyTemplateDto> edit(@RequestParam Long tempId, @RequestParam String unitSubject,
                                             @RequestParam String description, @RequestParam Long policyId) {
        PolicyTemplate temp = policytemplateService.findById(tempId);
        if (temp == null) {
            throw new BootException("未曾查找到模版对象");
        }
        Policy policy = policyService.findById(policyId);
        if (policy == null) {
            throw new BootException("政策不存在!");
        }
        temp.setUnitSubject(unitSubject);
        temp.setDescription(description);
        return ResponseDto.success(policytemplateService.editTemplate(temp, policyId));
    }

    @ApiOperation(value = "获取政策模版列表 (code 50005)", notes = "添加问题", tags = "政策模版相关接口", httpMethod = "POST", produces = "application/json")
    @PostMapping("list")
    public ResultDto<List<PolicyTemplateDto>> list() {
        return ResponseDto.success(PolicyTemplateDto.copyList(policytemplateService.findAll()));
    }

}
