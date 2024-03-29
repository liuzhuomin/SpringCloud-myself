package xinrui.cloud.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import xinrui.cloud.BootException;
import xinrui.cloud.domain.dto.AllCountInfoDto;
import xinrui.cloud.domain.dto.ApplyDto;
import xinrui.cloud.domain.dto.CompanyDto;
import xinrui.cloud.domain.dto.CompanyInfoDto;
import xinrui.cloud.domain.dto.CompanyMessageDto;
import xinrui.cloud.domain.dto.DifficultCompanyDto;
import xinrui.cloud.domain.dto.DifficultCourseTO;
import xinrui.cloud.domain.dto.EchartDto;
import xinrui.cloud.domain.dto.MessageCountDto;
import xinrui.cloud.domain.dto.VisitreCordDto;
import xinrui.cloud.dto.PageDto;
import xinrui.cloud.dto.ResponseDto;
import xinrui.cloud.dto.ResultDto;
import xinrui.cloud.service.*;

/**
 * <B>Title:</B>LeaderController</br>
 * <B>Description:</B>挂点领导相关接口的controller  </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/5 10:59
 */
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("leader")
@Api("挂点领导相关的接口")
public class LeaderController {

    @Autowired
    private LeaderService leaderService;

    @Autowired
    private DifficultService difficultService;

    @Autowired
    private VisitreCordService visitreCordService;

    @Autowired
    private UserService userService;

    @Autowired
    private OtherGroupService otherGroupService;

    @ApiOperation( position = 1,value = "获取当前挂点领导的所有消息(code 50033) ", notes = "获取当前挂点领导关联的所有消息", tags = "挂点领导相关的接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "leaderId", value = "当前挂点领导的用户id", paramType = "query", dataType = "INT", required = true, readOnly = true),
            @ApiImplicitParam(name = "end", value = "是否查询已经完结的，查询完结的传递true，否则传递false，查询所有的可不传递", paramType = "query", dataType = "BOOLEAN", required = false, readOnly = true),
            @ApiImplicitParam(name = "currentPage", value = "当前页数", paramType = "query", dataType = "INT", required = false, defaultValue = "1", readOnly = true),
            @ApiImplicitParam(name = "pageSize", value = "一页大小", paramType = "query", dataType = "INT", required = false, defaultValue = "10", readOnly = true),
            @ApiImplicitParam(name = "type", value = "需要查询消息的类型，A、B、C分别代表诉求，走访，资金申报", paramType = "query", dataType = "STRING", readOnly = true)
    })
    @PostMapping("listMessage")
    public ResultDto<PageDto<List<CompanyMessageDto>>> listMessage(@RequestParam Long leaderId, @RequestParam(required = false) Boolean end,
                                                                   @RequestParam(required = false) Character type,
                                                                   @RequestParam(required = false, defaultValue = "1") int currentPage, @RequestParam(required = false, defaultValue = "10") int pageSize) {
        return ResponseDto.success(leaderService.listMessageByLeaderId(leaderId, end, currentPage, pageSize, type));
    }

    @ApiOperation(position = 2,value = "根据诉求id获取诉求的相关详细信息(code 50034) ", notes = "根据诉求id获取诉求的相关详细信息", tags = "挂点领导相关的接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParam(name = "difficultId", value = "当前消息通知关联的id", paramType = "query", dataType = "INT", required = true, readOnly = true)
    @PostMapping("difficultInfo")
    public ResultDto<DifficultCompanyDto> getDifficult(@RequestParam Long difficultId) {
        return ResponseDto.success(difficultService.getDifficultByDifficultId(difficultId));
    }

   
	@ApiOperation(position = 3,value = "跟踪或者取消跟踪当前诉求件(code 50035) ", notes = "跟踪或者取消跟踪当前诉求件，如果type为0代表跟踪，1代表取消跟踪", tags = "挂点领导相关的接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "difficultId", value = "诉求id", paramType = "query", dataType = "INT", required = true, readOnly = true),
            @ApiImplicitParam(name = "type", value = "操作的类型，0代表跟踪当前诉求件，1代表取消跟踪当前诉求件", paramType = "query", dataType = "INT", readOnly = true)
    })
    @PostMapping("flowDifficult")
    public ResultDto flowDifficult(@RequestParam Long difficultId, @RequestParam(defaultValue = "0") int type) {
        difficultService.flowDifficult(difficultId, type);
        return ResponseDto.success();
    }

    @ApiOperation(position = 4,value = "批示当前诉求件并对当前诉求件设置办理时间(code 50036) ", notes = "批示当前诉求件，或者对当前诉求件设置办理时间，如果不传递限制时间，则不设置办理时限", tags = "挂点领导相关的接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "leaderId", value = "批示的领导的id", paramType = "query", dataType = "String", required = true, readOnly = true),
            @ApiImplicitParam(name = "difficultId", value = "诉求id", paramType = "query", dataType = "INT", required = true, readOnly = true),
            @ApiImplicitParam(name = "context", value = "批示的内容", paramType = "query", dataType = "String", required = true, readOnly = true),
            @ApiImplicitParam(name = "time", value = "办理时限的时间毫秒值", paramType = "query", dataType = "INT", required = true, readOnly = true)
    })
    @PostMapping("processDifficult")
    public ResultDto processDifficult(@RequestParam Long leaderId, @RequestParam Long difficultId, @RequestParam String context, @RequestParam(required = false, defaultValue = "0") long time) {
        difficultService.processDifficult(leaderId, difficultId, context, time);
        return ResponseDto.success();
    }


    @ApiOperation(position = 5,value = "阅读当前消息通知code 50037) ", notes = "传递当前消息通知对象的id，如果返回正常即完成阅读", tags = "挂点领导相关的接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "messageId", value = "消息的id", paramType = "query", dataType = "INT", required = true, readOnly = true)})
    @PostMapping("readMessage")
    public ResultDto readMessage(@RequestParam Long messageId) {
        leaderService.read(messageId);
        return ResponseDto.success();
    }

    @ApiOperation(position = 6,value = "获取当前诉求件的办理历程(code 50038) ", notes = "根据诉求的id获取当前诉求件的办理历程", tags = "挂点领导相关的接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "difficultId", value = "诉求件的id", paramType = "query", dataType = "INT", required = true, readOnly = true)})
    @PostMapping("listHistory")
    public ResultDto<List<DifficultCourseTO>> listHistory(@RequestParam Long difficultId) {
        return ResponseDto.success(difficultService.listHistory(difficultId));
    }

    @ApiOperation(position = 7,value = "获取指定领导挂点的所有企业对象(code 50039) ", notes = "获取指定领导挂点的所有企业对象", tags = "挂点领导相关的接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "leaderId", value = "挂点领导的id", paramType = "query", dataType = "INT", required = true, readOnly = true)})
    @PostMapping("listCompany")
    public ResultDto<List<CompanyDto>> listCompany(@RequestParam Long leaderId) {
        return ResponseDto.success(otherGroupService.listCompany(leaderId));
    }

    @ApiOperation(position = 8,value = "获取走访通知卡片数据(code 50040) ", notes = "获取走访通知卡片数据，消息通知对象的type为b请求", tags = "挂点领导相关的接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "visitreId", value = "挂点领导的id", paramType = "query", dataType = "INT", required = true, readOnly = true),
            @ApiImplicitParam(name = "messageId", value = "消息通知的id", paramType = "query", dataType = "INT", required = true, readOnly = true)})
    @PostMapping("getVisitreCord")
    public ResultDto<VisitreCordDto> getVisitreCord(@RequestParam Long visitreId,@RequestParam Long messageId) {
        return ResponseDto.success(visitreCordService.getVisitreCordSimpleInfo(visitreId,messageId));
    }

    @ApiOperation(position = 9,value = "领导用户添加关注企业(code 50041) ", notes = "领导用户添加关注企业", tags = "挂点领导相关的接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "leaderId", value = "挂点领导的id", paramType = "query", dataType = "INT", required = true, readOnly = true),
            @ApiImplicitParam(name = "companyId", value = "企业id如果是多个采用英文状态下的逗号隔开", paramType = "query", dataType = "String", required = true, readOnly = true)})
    @PostMapping("focusCompany")
    public ResultDto<?> focusCompany(@RequestParam Long leaderId, @RequestParam String companyId) {
        if(StringUtils.isBlank(companyId)){
            throw new BootException("企业找不到!");
        }
        if (companyId.contains(",")) {
            String[] split = companyId.split(",");
            for (String str : split) {
                if (StringUtils.isNotBlank(str)) {
                    userService.focusCompany(leaderId, Long.parseLong(str));
                }
            }
        } else {
            userService.focusCompany(leaderId, Long.parseLong(companyId));
        }
        return ResponseDto.success();
    }

    @ApiOperation(position = 10,value = "获取诉求统计图(50042) ", notes = "获取诉求统计图", tags = "挂点领导相关的接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "leaderId", value = "挂点领导id", paramType = "query", dataType = "INT", required = true, readOnly = true),
            @ApiImplicitParam(name = "timestamp", value = "查询时间段的毫秒值", paramType = "query", dataType = "INT", required = true, readOnly = true)})
    @PostMapping("difficultEcharts")
    public ResultDto<List<EchartDto>> difficultEcharts(@RequestParam Long leaderId, @RequestParam Long timestamp) {
        checkTime(timestamp);
        return ResponseDto.success(difficultService.difficultEcharts(leaderId, timestamp));
    }

    private void checkTime(@RequestParam Long timestamp) {
        if(timestamp==null|| timestamp<0) {
            throw new BootException("时间不能为空!");
        }
    }

    @ApiOperation(position = 11,value = "按照企业名称模糊搜索企业列表(50043) ", notes = "按照企业名称模糊搜索企业列表", tags = "挂点领导相关的接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "企业名称", paramType = "query", dataType = "INT", required = true, readOnly = true),
            @ApiImplicitParam(name = "leaderId", value = "挂点领导id", paramType = "query", dataType = "INT", required = true, readOnly = true)})
    @PostMapping("searchNames")
    public ResultDto<List<CompanyDto>> searchNames(@RequestParam String name,@RequestParam Long leaderId) {
        return ResponseDto.success(otherGroupService.searchNames(name,leaderId));
    }

    @ApiOperation(position = 12,value = "删除关注企业(50044) ", notes = "删除关注企业", tags = "挂点领导相关的接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "企业id", paramType = "query", dataType = "INT", required = true, readOnly = true),
            @ApiImplicitParam(name = "leaderId", value = "领导id", paramType = "query", dataType = "INT", required = true, readOnly = true)})
    @PostMapping("deleteFocusCompany")
    public ResultDto<?> deleteFocusCompany(@RequestParam Long companyId, @RequestParam Long leaderId) {
        otherGroupService.deleteFocusCompany(leaderId, companyId);
        return ResponseDto.success();
    }

    @ApiOperation(position = 13,value = "获取走访数据echarts图(50045) ", notes = "获取走访数据echarts图", tags = "挂点领导相关的接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "leaderId", value = "挂点领导id", paramType = "query", dataType = "INT", required = true, readOnly = true),
            @ApiImplicitParam(name = "timestamp", value = "查询时间段的毫秒值", paramType = "query", dataType = "INT", required = true, readOnly = true)})
    @PostMapping("visitreEchart")
    public ResultDto<List<EchartDto>> visitreEchart(@RequestParam Long leaderId, @RequestParam Long timestamp) {
        checkTime(timestamp);
        return ResponseDto.success(difficultService.visitreEchart(leaderId, timestamp));
    }

    @ApiOperation(position = 14,value = "获取申报数据echarts图(50046) ", notes = "获取申报数据echart图", tags = "挂点领导相关的接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "leaderId", value = "挂点领导id", paramType = "query", dataType = "INT", required = true, readOnly = true),
            @ApiImplicitParam(name = "timestamp", value = "查询时间段的毫秒值", paramType = "query", dataType = "INT", required = true, readOnly = true)})
    @PostMapping("applyEchart")
    public ResultDto<List<EchartDto>> applyEchart(@RequestParam Long leaderId, @RequestParam Long timestamp) {
        checkTime(timestamp);
        return ResponseDto.success(difficultService.applyEchart(leaderId, timestamp));
    }

    @ApiOperation(position = 15,value = "获取诉求和走访统计数据(50047) ", notes = "获取诉求和走访统计数据", tags = "挂点领导相关的接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "企业id", paramType = "query", dataType = "INT", required = true, readOnly = true),
            @ApiImplicitParam(name = "timestamp", value = "查询时间段的毫秒值", paramType = "query", dataType = "INT", required = true, readOnly = true)})
    @PostMapping("difficultAndVisitreCount")
    public ResultDto<AllCountInfoDto> difficultAndVisitreCount(@RequestParam Long companyId, @RequestParam Long timestamp) {
        return ResponseDto.success(difficultService.simpleCompanyInfo(companyId, timestamp));
    }

    @ApiOperation(position = 16,value = "按照年份获取企业简略信息(50048) ", notes = "按照年份获取企业简略信息", tags = "挂点领导相关的接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "企业id", paramType = "query", dataType = "INT", required = true, readOnly = true),
            @ApiImplicitParam(name = "year", value = "年份xxxx", paramType = "query", dataType = "STRING", required = true, readOnly = true)})
    @PostMapping("companyInfo")
    public ResultDto<CompanyInfoDto> companyInfo(@RequestParam Long companyId, @RequestParam String year) {
        return ResponseDto.success(difficultService.companyInfo(companyId, Long.parseLong(year)));
    }


    @ApiOperation(position = 17,value = "获取消息通知数量(50049) ", notes = "获取消息通知数量,如果要获取指定类型的消息通知，即传递type字段，获取全部则不传递type字段", tags = "挂点领导相关的接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "leaderId", value = "挂点领导id", paramType = "query", dataType = "INT", required = true, readOnly = true),
            @ApiImplicitParam(name = "type", value = "需要查询消息的类型，A、B、C分别代表诉求，走访，资金申报", paramType = "query", dataType = "STRING", readOnly = true)})
    @PostMapping("messageCount")
    public ResultDto<MessageCountDto> messageCount(@RequestParam Long leaderId,@RequestParam(required = false) Character type) {
        return ResponseDto.success(leaderService.messageCount(leaderId,type));
    }

    @ApiOperation(position = 18,value = "根据申请id获取申请相关数据(50050) ", notes = "根据申请id获取申请相关数据", tags = "挂点领导相关的接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyId", value = "挂点领导id", paramType = "query", dataType = "INT", required = true, readOnly = true)})
    @PostMapping("applyInfo")
    public ResultDto<ApplyDto> applyInfo(@RequestParam Long applyId) {
        return ResponseDto.success(leaderService.applyInfo(applyId));
    }


}
