package xinrui.cloud.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import xinrui.cloud.domain.dto.ProblemTriggerInnerDto;
import xinrui.cloud.domain.dto.ProblemTriggerResultDto;
import xinrui.cloud.dto.ResponseDto;
import xinrui.cloud.dto.ResultDto;
import xinrui.cloud.service.ProblemTriggerResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("触发结果修饰的相关接口")
@RestController
@RequestMapping("problem/trigger/result")
public class ProblemTriggerResultController {

    @Autowired
    ProblemTriggerResultService triggerResultService;

    @ApiOperation(value = "添加触发结果修饰(code 50025)", notes = "添加触发结果修饰对象到模板中", tags = "触发结果修饰的相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "triggerId", value = "触发结果对象的id", paramType = "query", dataType = "Long", required = true),
            @ApiImplicitParam(name = "json", value = "触发结果修饰对象的json形式", paramType = "query", dataType = "String", required = true)})
    @PostMapping("add")
    public ResultDto<ProblemTriggerResultDto> add(@RequestParam Long triggerId, @RequestParam String json) {
        ProblemTriggerResultDto parseObject = JSON.parseObject(json, ProblemTriggerResultDto.class);
        Assert.notNull(parseObject, "json字符串解析错误！");
        return ResponseDto.success(triggerResultService.add(triggerId, parseObject));
    }

    @ApiOperation(value = "删除触发结果(code 50026)", notes = "删除触发结果", tags = "触发结果修饰的相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "triggerResultId", value = "触发结果修饰对象的id", paramType = "query", dataType = "Long", required = true)})
    @PostMapping("delete")
    public ResultDto<?> delete(@RequestParam Long triggerResultId) {
        triggerResultService.remove(triggerResultId);
        return ResponseDto.success();
    }

    @ApiOperation(value = "修改触发结果修饰(code 50027)", notes = "修改触发结果修饰", tags = "触发结果修饰的相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "triggerResultId", value = "触发结果修饰的id", paramType = "query", dataType = "Long", required = true),
            @ApiImplicitParam(name = "json", value = "触发结果修饰对象的json形式,详情见json接口", paramType = "query", dataType = "String", required = true)})
    @PostMapping("edit")
    public ResultDto<ProblemTriggerResultDto> edit(@RequestParam Long triggerResultId, @RequestParam String json) {
        ProblemTriggerResultDto parseObject = JSON.parseObject(json, ProblemTriggerResultDto.class);
        Assert.notNull(parseObject, "json字符串解析错误！");
        return ResponseDto.success(triggerResultService.edit(triggerResultId, parseObject));
    }

    @ApiOperation(value = "触发结果的json数据示例(code 50028)", notes = "触发结果修饰的json数据示例", tags = "触发结果修饰的相关接口", httpMethod = "POST", produces = "application/json")
    @PostMapping("json")
    public ProblemTriggerResultDto json() {
        ProblemTriggerResultDto problemTriggerDto = new ProblemTriggerResultDto();
        List<ProblemTriggerInnerDto> result = Lists.newArrayList();
        for (int i = 0; i < 3; i++) {
            ProblemTriggerInnerDto dto = new ProblemTriggerInnerDto();
            dto.setLogicOne("你" + i);
            dto.setLogicTwo("你" + i);
            dto.setId(666L);
            result.add(dto);
        }
        problemTriggerDto.setChild(result);
        return problemTriggerDto;
    }
}
