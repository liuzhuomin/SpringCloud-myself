package xinrui.cloud.controller;

import io.swagger.annotations.*;
import xinrui.cloud.domain.dto.PolicyActivityDto;
import xinrui.cloud.dto.ResponseDto;
import xinrui.cloud.dto.ResultDto;
import xinrui.cloud.service.ActivityService;
import xinrui.cloud.service.PolicyTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("模版环节相关接口接口")
@RestController
@RequestMapping("policy/activity")
public class ActivityController {

    @Autowired
    PolicyTemplateService policytemplateService;

    @Autowired
    ActivityService activityService;

    @ApiOperation(value = "根据模板id获取当前摸板对应的问题编辑项数据、触发器、公式等 (code 50006)", notes = "列出当前政策模版对象的所有环节实例(即所有问题)", tags = "模版环节相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParam(name = "tempId", value = "政策匹配模板id", paramType = "query", dataType = "Long", required = true)
    @PostMapping("list")
    public ResultDto<PolicyActivityDto> list(@RequestParam Long tempId) {
        return ResponseDto.success(activityService.findByTemp(tempId));
    }

    @ApiOperation(value = "根据环节id删除模板环节 (code 50007)", notes = "相当于删除某个模板下的主环节（所有的问题编辑，问题限制...等），但是不删除模板", tags = "模版环节相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParam(name = "activityId", value = "环节id", paramType = "query", dataType = "Long", required = true)
    @PostMapping("delete")
    public ResultDto<?> delete(@RequestParam Long activityId) {
        policytemplateService.init(activityService.removeActivityById(activityId));
        return ResponseDto.success();
    }


}
