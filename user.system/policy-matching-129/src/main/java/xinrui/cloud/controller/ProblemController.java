package xinrui.cloud.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import xinrui.cloud.BootException;
import xinrui.cloud.domain.dto.ProblemDto;
import xinrui.cloud.domain.dto.ProblemDto2;
import xinrui.cloud.domain.dto.ProblemModelDto;
import xinrui.cloud.dto.ResponseDto;
import xinrui.cloud.dto.ResultDto;
import xinrui.cloud.enums.ProblemStatus;
import xinrui.cloud.service.ActivityService;
import xinrui.cloud.service.PolicyTemplateService;
import xinrui.cloud.service.ProblemService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Api("问题编辑相关接口")
@RestController
@RequestMapping("problem")
public class ProblemController {

    @Autowired
    PolicyTemplateService policytemplateService;

    @Autowired
    ActivityService activityService;

    @Autowired
    ProblemService problemService;

    @ApiOperation(value = "问题编辑添加接口 (code 50008)", notes = "添加问题,适用于独选、天空和空三个类型", tags = "问题编辑相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tempId", value = "政策匹配规则模板id", paramType = "query", dataType = "Long", required = true),
            @ApiImplicitParam(name = "status", value = "状态值,标识是(独选(1),填空(2),空(3))中的哪一个类型", paramType = "query", dataType = "INT", required = true),
            @ApiImplicitParam(name = "title", value = "标题内容", paramType = "query", dataType = "String", required = false),
            @ApiImplicitParam(name = "value", value = "填入的值，可是表达式，变量名称等", paramType = "query", dataType = "String", required = false),
            @ApiImplicitParam(name = "max", value = "最高限制额度", paramType = "query", dataType = "INT", required = false),
            @ApiImplicitParam(name = "last", value = "是否仅仅针对上一个问题编辑项作用", paramType = "BOOLEAN", dataType = "boolean", required = false),
            @ApiImplicitParam(name = "isAmplitude", value = "是否是幅度上涨独选", paramType = "BOOLEAN", dataType = "boolean")})
    @PostMapping("add/other")
    public ResultDto<List<ProblemDto>> addProblem(@RequestParam Long tempId, @RequestParam Integer status,
                                                  @RequestParam(required = false) String title, @RequestParam(required = false) String value,
                                                  @RequestParam(required = false) Integer max, @RequestParam(required = false) Boolean last,
                                                  @RequestParam(required = false) Boolean isAmplitude) {
        checkStatus(status, title, value);
        activityService.addProblem(tempId, status, title, value, max, last, isAmplitude);
        return ResponseDto.success(activityService.findByTemp(tempId).getProblems());
    }

    @ApiOperation(value = "添加单选类型的问题编辑数据模板(code 50010)", notes = "添加问题模板，适用于单选,需要传递一级选项和二级选项的json数据，数据格式参见json接口</br>子选项只能是 单选(0)、填空(2)", tags = "问题编辑相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tempId", value = "政策匹配规则模板id", paramType = "query", dataType = "Long", required = true),
            @ApiImplicitParam(name = "json", value = "单选时需要提供json格式的数据", paramType = "query", dataType = "String", required = true)})
    @PostMapping("add/singleradio")
    public ResultDto<List<ProblemDto>> editSingleRadion(@RequestParam Long tempId, @RequestParam String json) {
        activityService.addProblem(tempId, 0, null, json, null, null, null);
        return ResponseDto.success(activityService.findByTemp(tempId).getProblems());
    }

    @ApiOperation(value = "编辑单选前需要的数据接口(50013)", notes = "编辑问题编辑数据版面需要之前需要的数据", tags = "问题编辑相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "problemId", value = "问题编辑数据的id", paramType = "query", dataType = "Long", required = true)})
    @PostMapping("edit/singleradio/before")
    public ResultDto<ProblemDto2> beforeEditSingleRadion(@RequestParam Long problemId) {
        return ResponseDto.success(activityService.beforeEditSingleRadion(problemId));
    }

    @ApiOperation(value = "修改单选类型的问题编辑数据模板(code 50014)", notes = "修改单选类型的问题编辑数据模板，适用于单选,需要传递一级选项和二级选项的json数据，数据格式参见json接口</br>子选项只能是 单选(0)、填空(2)", tags = "问题编辑相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "problemId", value = "政策匹配规则模板id", paramType = "query", dataType = "Long", required = true),
            @ApiImplicitParam(name = "json", value = "单选时需要提供json格式的数据", paramType = "query", dataType = "String", required = true)})
    @PostMapping("edit/singleradio")
    public ResultDto<ProblemDto> ediSingleradio(@RequestParam Long problemId, @RequestParam String json) {
        return ResponseDto.success(activityService.editProblem(problemId, 0, null, json, null, null, null));
    }

    @ApiOperation(value = "修改其他的编辑问题编辑模板数据 (code 50015)", notes = "编辑问题,适用于独选、填空和空三个类型，情怯重新返回当前问题编辑之后的编辑问题", tags = "问题编辑相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "problemId", value = "政策匹配规则模板id", paramType = "query", dataType = "Long", required = true),
            @ApiImplicitParam(name = "status", value = "状态值,标识是(独选(1),填空(2),空(3))中的哪一个类型", paramType = "query", dataType = "INT", required = true),
            @ApiImplicitParam(name = "title", value = "标题内容", paramType = "query", dataType = "String", required = false),
            @ApiImplicitParam(name = "value", value = "填入的值，可是表达式，变量名称等", paramType = "query", dataType = "String", required = false),
            @ApiImplicitParam(name = "max", value = "最高限制额度", paramType = "query", dataType = "INT", required = false),
            @ApiImplicitParam(name = "last", value = "是否仅仅针对上一个问题编辑项作用", paramType = "BOOLEAN", dataType = "String", required = false),
            @ApiImplicitParam(name = "isAmplitude", value = "是否是幅度上涨独选", paramType = "BOOLEAN", dataType = "boolean")})
    @PostMapping("edit/other")
    public ResultDto<ProblemDto> editProblem(@RequestParam Long problemId, @RequestParam Integer status,
                                             @RequestParam(required = false) String title, @RequestParam(required = false) String value,
                                             @RequestParam(required = false) Integer max, @RequestParam(required = false) Boolean last,
                                             @RequestParam(required = false) Boolean isAmplitude) {
        checkStatus(status, title, value);
        return ResponseDto.success(activityService.editProblem(problemId, status, title, value, max, last, isAmplitude));
    }

    @ApiOperation(value = "删除所有类型的问题编辑数据模板(code 50016)", notes = "删除单选类型的问题编辑数据模板", tags = "问题编辑相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "problemId", value = "政策匹配规则模板id", paramType = "query", dataType = "Long", required = true)})
    @PostMapping("delete/all")
    public ResultDto<?> deletProblem(@RequestParam Long problemId) {
        problemService.remove(problemId);
        return ResponseDto.success();
    }

    @ApiOperation(value = "问题编辑json数据示列 (code 50009)", notes = "测试activityjson", tags = "问题编辑相关接口", httpMethod = "POST", produces = "application/json")
    @PostMapping(value = "json")
    public ProblemDto2 json() throws UnsupportedEncodingException {
        ProblemDto2 problem = new ProblemDto2(ProblemStatus.SINGLE_RADIO, "TEST TITLE");
        problem.getFirst().add(new ProblemModelDto("A:AAA"));
        problem.getFirst().add(new ProblemModelDto("B:BBB"));
        problem.getFirst().add(new ProblemModelDto("C:CCC"));
        problem.getSecond().add(new ProblemModelDto("AA:AAA1"));
        problem.getSecond().add(new ProblemModelDto("BB:BBB1"));
        problem.getSecond().add(new ProblemModelDto("CC:CCC1"));
        return problem;
    }

    /**
     * 检查状态码是否合法
     *
     * @param status 需要传入的状态码
     * @param title  标题
     * @param value  填写的值
     */
    private void checkStatus(Integer status, String title, String value) {
        if (status == null || (status < 1 || status > 3)) {
            throw new BootException("参数不正确!");
        }
        if (status != 3 && StringUtils.isBlank(value) && StringUtils.isBlank(title)) {
            throw new BootException("参数不正确!");
        }
    }

}
