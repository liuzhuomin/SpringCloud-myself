package xinrui.cloud.controller;

import com.google.common.collect.Lists;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;
import xinrui.cloud.annotation.Group;
import xinrui.cloud.common.utils.DataUtil;
import xinrui.cloud.domain.GroupType;
import xinrui.cloud.domain.dto.AddressDto;
import xinrui.cloud.domain.dto.TechnologyFinancialAppointmentUserDto;
import xinrui.cloud.domain.dto.TechnologyFinancialAppointmentedDto;
import xinrui.cloud.domain.vto.ViewImagesVto;
import xinrui.cloud.dto.PageDto;
import xinrui.cloud.dto.ResponseDto;
import xinrui.cloud.dto.ResultDto;
import xinrui.cloud.service.TechnologyFinancialAppointmentService;
import xinrui.cloud.service.TechnologyFinancialService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * 科技金融接口(政府端)
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/8/9 14:57
 */
@Api("科技金融模块（企业端）")
@RestController
@RequestMapping("technology/company")
@Slf4j
@Group(GroupType.OTHER)
@SuppressWarnings("unused")
public class TechnologyFinancialCompanyController {

    @Resource
    TechnologyFinancialService technologyFinancialService;

    @Resource
    TechnologyFinancialAppointmentService technologyFinancialAppointmentService;

    @Resource
    CarrierController carrierController;


    @GetMapping("/list")
    @ApiOperation(position = 1, value = "根据筛选条件获取金融产品",
            notes = "根据筛选条件获取金融产品，(贷款金额、贷款期限、传递id值),其他的传递文本值即可",
            tags = "科技金融模块（企业端）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "amount", value = "传递贷款额度数据传输对象的id", paramType = "query", dataType = "INT"),
            @ApiImplicitParam(name = "limit", value = "传递贷款期限数据传输对象的id", paramType = "query", dataType = "INT"),
            @ApiImplicitParam(name = "category", value = "科技金融类别只可能为 信用贷/抵押贷", paramType = "query", dataType = "STRING"),
            @ApiImplicitParam(name = "name", value = "科技金融产品名称", paramType = "query", dataType = "STRING"),
            @ApiImplicitParam(name = "currentPage", value = "当前页码", paramType = "query", dataType = "INT"),
            @ApiImplicitParam(name = "pageSize", value = "一页大小", paramType = "query", dataType = "INT")
    })
    public ResultDto<PageDto<List<TechnologyFinancialAppointmentedDto>>> listAudit(@RequestParam(value = "amount", required = false) Long amount,
                                                                                   @RequestParam(value = "limit", required = false) Long limit,
                                                                                   @RequestParam(value = "category", required = false) String category,
                                                                                   @RequestParam(value = "name", required = false) String name,
                                                                                   @RequestParam(value = "currentPage", defaultValue = "1", required = false) int currentPage,
                                                                                   @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return ResponseDto.success(technologyFinancialService.onlines(amount, limit, category, name, currentPage, pageSize));
    }

    @GetMapping("/list/images")
    @ApiOperation(position = 2, value = "获取首页轮播展示图片的下载地址", notes = "获取首页轮播展示图片的下载地址",
            tags = "科技金融模块（企业端）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<List<ViewImagesVto>> imageUrls() {
        return ResponseDto.success(technologyFinancialService.listIndexImages());
    }

    @GetMapping("/list/appointment")
    @ApiOperation(position = 2, value = "获取所有已经预约的产品列表", notes = "获取所有已经预约的产品列表",
            tags = "科技金融模块（企业端）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<PageDto<List<TechnologyFinancialAppointmentedDto>>> listAppointment(@RequestParam(value = "currentPage", defaultValue = "1", required = false) int currentPage,
                                                                                         @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return ResponseDto.success(technologyFinancialAppointmentService.listAppointment(currentPage, pageSize));
    }

    @GetMapping("/address/{pid}")
    @ApiOperation(position = 3, value = "获取深圳行政区域及其所关联街道", notes = "获取深圳行政区域及其所关联街道",
            tags = "科技金融模块（企业端）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "pid", value = "父节点id可不传递，不传递代表获取的是深圳所有区，然后要获取街道则传递区的id即可",
            paramType = "path", dataType = "STRING")
    public ResultDto<List<AddressDto>> refused(@PathVariable(name = "pid", required = false) String pid,
                                               HttpServletRequest request) throws InvocationTargetException, IntrospectionException, InstantiationException, IllegalAccessException, ServletRequestBindingException {
        long parentId = 0b1000011100000101011L;
        if (!StringUtils.isEmpty(pid) && !"{pid}".equals(pid)) {
            parentId = Long.parseLong(pid);
        }
        Long pid1 = ServletRequestUtils.getLongParameter(request, "pid");
        log.info("ServletRequestUtils.getLongParameter={}", pid1);
        log.info("pid value : {}", pid);
        ResultDto<List<Map<String, String>>> address = carrierController.getAddress(parentId);
        List<Map<String, String>> data = address.getData();
        log.info("accept data :{}", data);
        List<AddressDto> result = Lists.newArrayList();
        for (Map m : data) {
            AddressDto addressDto = DataUtil.mapToBean(m, AddressDto.class);
            log.info("convert map to bean {}", addressDto);
            result.add(addressDto);
        }
        return ResponseDto.success(result);
    }

    @PostMapping("/appointment/id")
    @ApiOperation(position = 4, value = "科技金融产品预约", notes = "科技金融产品预约",
            tags = "科技金融模块（企业端）", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "科技金融对象的id", paramType = "path", dataType = "INT", required = true)
    })
    public ResultDto<?> offline(@PathVariable("id") @NotNull int id,
                                @RequestBody @ApiParam(value = "科技金融预约用户数据传输对象", required = true) TechnologyFinancialAppointmentUserDto user) {
        technologyFinancialService.appointment(id, user);
        return ResponseDto.success("恭喜您预约成功!");
    }

}
