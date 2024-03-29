package xinrui.cloud.controller;

import io.swagger.annotations.*;
import xinrui.cloud.dto.ResponseDto;
import xinrui.cloud.dto.ResultDto;
import xinrui.cloud.service.NoticeFileService;
import xinrui.cloud.service.NoticeInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("通知公告环节接口")
@RestController
@RequestMapping("notice")
public class NoticeInformationController {
    @Autowired
    NoticeInformationService noticeInformationService;
    @Autowired
    NoticeFileService noticeFileService;

    @ApiOperation(value = "政府端-通知列表 (code 60001)", notes = "通过标题查询通知列表", tags = "政府端", httpMethod = "GET", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "标题", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "order", value = "排序（1：发布时间（升序）；2：发布时间（降序）；3：结束时间（升序）；4：结束时间（降序）；5：阅读量（升序）；6：阅读量（降序））",
                    required = false, paramType = "query", dataType = "String")})
    @GetMapping("getGovListNotice")
    public ResultDto getGovListNotice(
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "2") String order) {
        return ResponseDto.success(noticeInformationService.getGovListNotice(title, order));
    }

    @ApiOperation(value = "政府端-获取草稿箱列表 (code 60002)", notes = "获取草稿箱列表", tags = "政府端", httpMethod = "GET", produces = "application/json")
    @GetMapping("getListNoticeDraft")
    public ResultDto getListNoticeDraft() {
        return ResponseDto.success(noticeInformationService.getListNoticeDraft());
    }


    @ApiOperation(value = "企业端-通知列表 (code 60003)", notes = "企业端-通知列表", tags = "企业端", httpMethod = "GET", produces = "application/json")
    @GetMapping("getCorpListNotice")
    public ResultDto getCorpListNotice() {
        return ResponseDto.success(noticeInformationService.getCorpListNotice());
    }

    @ApiOperation(value = "企业端-右侧通过标题名称通知列表 (code 60004)", notes = "企业端-右侧通过标题名称通知列表", tags = "企业端", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "标题", required = false, paramType = "query", dataType = "String")})
    @GetMapping("getCorpListNoticeByTitle")
    public ResultDto getCorpListNoticeByTitle(
            @RequestParam(defaultValue = "") String title) {
        return ResponseDto.success(noticeInformationService.getCorpListNoticeByTitle(title));
    }

    @ApiOperation(value = "通过通知Id获取通知详情和附件信息 (code 60005)", notes = "通过通知Id获取通知详情和附件信息", tags = "政府端", httpMethod = "GET", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "通知id", required = false, paramType = "query", dataType = "Long")})
    @GetMapping("getNoticeById")
    public ResultDto getNoticeById(@RequestParam Long id) {
        return ResponseDto.success(noticeInformationService.getNoticeInformationById(id));
    }

    @ApiOperation(value = "添加通知(code 60006)", notes = "添加通知", tags = "政府端", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "noticeInformation", value = "通知信息（title：标题；content：内容；userId：用户Id；beginTime：开始时间；beginTime：结束时间；status：状态（0：草稿箱，1：发布通知）；type：通知类型（1：纯文本）;imgPath：封面图片地址)", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "filesJson", value = "附件（附件名称：fileName，附件地址：filePath，附件后缀名：suffix）", required = false, defaultValue = "1", paramType = "query", dataType = "int")})
    @PostMapping("addNotice")
    public ResultDto addNotice(@RequestParam String noticeInformation, @RequestParam(defaultValue = "") String filesJson) {
        return ResponseDto.success(noticeInformationService.addNotice(noticeInformation, filesJson));
    }

    @ApiOperation(value = "更新通知(code 60007)", notes = "更新通知", tags = "政府端", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "noticeInformation", value = "通知信息（id:通知ID,title：标题；content：内容；userId：用户Id；beginTime：开始时间；beginTime：结束时间；status：状态（0：草稿箱，1：发布通知）；type：通知类型（1：纯文本）)", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "filesJson", value = "附件（附件名称：fileName，附件地址：filePath，附件后缀名：suffix）", required = false, defaultValue = "1", paramType = "query", dataType = "int")})
    @PostMapping("editNotice")
    public ResultDto editNotice(@RequestParam String noticeInformation, @RequestParam(defaultValue = "") String filesJson) {
        return ResponseDto.success(noticeInformationService.editNotice(noticeInformation, filesJson));
    }


    @ApiOperation(value = "删除通知(code 60008)", notes = "删除通知", tags = "政府端", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "通知id", required = false, paramType = "query", dataType = "Long")})
    @PostMapping("delNotice")
    public ResultDto delNotice(@RequestParam Long id) {
        noticeInformationService.deleteNotice(id);
        return ResponseDto.success();
    }

    @ApiOperation(value = "活动下架(code 60009)", notes = "活动下架", tags = "政府端", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParam(name = "id", value = "通知id", required = false, paramType = "query", dataType = "Long")
    @PostMapping("retNotice")
    public ResultDto retNotice(@RequestParam Long id) {
        noticeInformationService.retNotice(id);
        return  ResultDto.success();
    }

}
