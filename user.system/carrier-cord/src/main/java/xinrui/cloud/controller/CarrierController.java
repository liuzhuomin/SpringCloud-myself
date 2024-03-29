package xinrui.cloud.controller;

import com.google.common.base.Preconditions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

import xinrui.cloud.BootException;
import xinrui.cloud.base.BaseController;
import xinrui.cloud.common.enums.ResultCode;
import xinrui.cloud.domain.Carrier;
import xinrui.cloud.domain.CarrierAuditRecord;
import xinrui.cloud.domain.CarrierCoreData;
import xinrui.cloud.domain.CarrierDataBase;
import xinrui.cloud.domain.CarrierDataBaseFiles;
import xinrui.cloud.domain.CarrierImportantData;
import xinrui.cloud.domain.CarrierMajor;
import xinrui.cloud.domain.CarrierOperatingTeam;
import xinrui.cloud.domain.CarrierProfessionalDirection;
import xinrui.cloud.domain.CarrierSettledEnterprise;
import xinrui.cloud.domain.CarrierType;
import xinrui.cloud.domain.dto.CarrierCoreDataDTO;
import xinrui.cloud.domain.dto.CarrierDTO;
import xinrui.cloud.domain.dto.CarrierDataBaseDTO;
import xinrui.cloud.domain.dto.CarrierDataBaseFilesDTO;
import xinrui.cloud.domain.dto.CarrierDefaultFileDTO;
import xinrui.cloud.domain.dto.CarrierImportantDataDTO;
import xinrui.cloud.domain.dto.CarrierMajorDTO;
import xinrui.cloud.domain.dto.CarrierOperatingTeamDTO;
import xinrui.cloud.domain.dto.CarrierSettledEnterpriseDTO;
import xinrui.cloud.domain.dto.CarrierShowListDTO;
import xinrui.cloud.domain.GroupType;
import xinrui.cloud.dto.PageDto;
import xinrui.cloud.dto.ResponseDto;
import xinrui.cloud.dto.ResultDto;
import xinrui.cloud.dto.UserDto;
import xinrui.cloud.service.CarrierAuditRecordService;
import xinrui.cloud.service.CarrierCoreDataService;
import xinrui.cloud.service.CarrierDataBaseFilesService;
import xinrui.cloud.service.CarrierDataBaseService;
import xinrui.cloud.service.CarrierImportantDataService;
import xinrui.cloud.service.CarrierMajorService;
import xinrui.cloud.service.CarrierOperatingTeamService;
import xinrui.cloud.service.CarrierProfessionalDirectionService;
import xinrui.cloud.service.CarrierService;
import xinrui.cloud.service.CarrierSettledEnterpriseService;
import xinrui.cloud.service.CarrierTypeService;
import xinrui.cloud.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创新载体模块
 * @author Jihy
 * @since 2019-06-25 10:07
 */
@RestController
@RequestMapping("carrier")
@Api("创新载体模块")
@Slf4j
public class CarrierController extends BaseController {

  @Autowired private CompanyService companyService;

  @Autowired private CarrierService carrierService;

  @Autowired private CarrierOperatingTeamService carrierOperatingTeamService;

  @Autowired private CarrierMajorService carrierMajorService;

  @Autowired private CarrierImportantDataService carrierImportantDataService;

  @Autowired private CarrierSettledEnterpriseService carrierSettledEnterpriseService;

  @Autowired private CarrierDataBaseService carrierDataBaseService;

  @Autowired private CarrierCoreDataService carrierCoreDataService;

  @Autowired private CarrierTypeService carrierTypeService;

  @Autowired private CarrierProfessionalDirectionService carrierProfessionalDirectionService;

  @Autowired private CarrierAuditRecordService carrierAuditRecordService;

  @Autowired private CarrierDataBaseFilesService carrierDataBaseFilesService;

  @GetMapping("getCompanyByName")
  @ApiOperation( position = 1, value = "通过企业名称获取企业信息", notes = "通过企业名称获取企业信息", tags = "创新载体模块", httpMethod = "GET", produces = "application/json")
  @ApiImplicitParam(name = "name", value = "企业名称", paramType = "query", dataType = "String", required = true, readOnly = true)
  public ResultDto getCompanyByName(@RequestParam String name){

    return ResponseDto.success(companyService.getCompanyByName(name));
  }

  @GetMapping("getCarrierType")
  @ApiOperation( position = 2, value = "获取载体类型", notes = "获取载体类型", tags = "创新载体模块", httpMethod = "GET", produces = "application/json")
  public ResultDto<List<CarrierType>> getCarrierType(){
    return ResponseDto.success(carrierService.getCarrierTypes());
  }

  @GetMapping("getCarrierProfessionalDirection")
  @ApiOperation(position = 3 , value= "获取专业方向", notes = "获取专业方向", tags = "创新载体模块", httpMethod ="GET", produces = "application/json")
  public ResultDto<List<CarrierProfessionalDirection>> getCarrierProfessionalDirection(){
    return ResponseDto.success(carrierService.getCarrierProfessionalDirection());
  }

  @GetMapping("getCarrierDefaultFile")
  @ApiOperation(position = 4, value = "获取默认的文件配置", notes = "获取默认的文件配置", tags = "创新载体模块", httpMethod ="GET", produces = "application/json")
  public ResultDto<List<CarrierDefaultFileDTO>> getCarrierDefaultFile(){
    return ResponseDto.success(carrierService.getCarrierDefaultFiles());
  }

  @GetMapping("getAllCarrier")
  @ApiOperation(position = 5, value = "获取所有的科技载体", notes = "获取所有的科技载体", tags = "创新载体模块", httpMethod ="GET", produces = "application/json")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "page", value = "当前页码", required = false, defaultValue = "1", paramType = "query", dataType = "int"),
      @ApiImplicitParam(name = "pageSize", value = "一页大小", required = false, defaultValue = "10", paramType = "query", dataType = "int"),
      @ApiImplicitParam(name = "name", value = "品牌名称", required = false, defaultValue = "", paramType = "query", dataType = "String"),
      @ApiImplicitParam(name = "carrierTypeId", value = "载体类型ID", required = false, defaultValue = "-1", paramType = "query", dataType = "Long"),
      @ApiImplicitParam(name = "street", value = "街道名称", required = false, defaultValue = "all", paramType = "query", dataType = "String"),
      @ApiImplicitParam(name = "carrierProfessionalDirectionId", value = "专业ID", required = false, defaultValue = "-1", paramType = "query", dataType = "Long"),
      @ApiImplicitParam(name = "orderByName", value = "排序条件(默认 brandName, 已使用面积 usedArea, 空置面积 vacantArea, 入驻企业数量 inEnterpriseNumber, 孵化企业数量 incubationBaseNumber)", required = false, defaultValue = "brandName", paramType = "query", dataType = "String"),
      @ApiImplicitParam(name = "ascending", value = "是否是升序(true 升序 false 降序)", required = false, defaultValue = "true", paramType = "query", dataType = "Boolean")
  })
  public ResultDto<PageDto<List<CarrierShowListDTO>>> getAllCarrier(@RequestParam(required = false, defaultValue = "1") int page,
                                                                    @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                                    @RequestParam(required = false, defaultValue = "") String name,
                                                                    @RequestParam(required = false, defaultValue = "-1") Long carrierTypeId,
                                                                    @RequestParam(required = false, defaultValue = "all") String street,
                                                                    @RequestParam(required = false, defaultValue = "-1") Long carrierProfessionalDirectionId,
                                                                    @RequestParam(required = false, defaultValue = "brandName") String orderByName,
                                                                    @RequestParam(required = false, defaultValue = "true") Boolean ascending
  ){
    UserDto user = getCurrentUser();
    return ResponseDto.success(carrierService.getAllCarrier(page, pageSize, user, name, carrierTypeId, street, carrierProfessionalDirectionId, orderByName, ascending));
  }

  @PostMapping("saveCarrier")
  @ApiOperation(position = 6 , value= "新增一条科技载体", notes = "新增一条科技载体", tags = "创新载体模块", httpMethod ="POST", produces = "application/json")
  public ResultDto<?> saveCarrier(@RequestBody CarrierDTO carrierDTO){
    UserDto user = getCurrentUser();
    Carrier carrier = carrierDTO.toCopyForm();
    carrier.setCreateAt(new Date());
    log.info("用户的公司{}", user.getUniqueGroup());
    log.info("用户的公司GroupType{}", user.getUniqueGroup().getGroupType());
    if(StringUtils.equals(user.getUniqueGroup().getGroupType(), GroupType.OTHER)){
      carrier.setStatus(1);
      carrier.setCreateType('B');
    }else{
      carrier.setStatus(2);
      carrier.setCreateType('A');
    }
    carrier.setCreatorId(user.getId());
    carrier.setCreatorGroupId(user.getUniqueGroup().getId());
    carrier =  carrierService.persistAndGet(carrier);
    //处理载体核心数据
    CarrierCoreDataDTO carrierCoreDataDTO = carrierDTO.getCarrierCoreDataDTO();
    CarrierCoreData coreData = carrierCoreDataDTO.toCopyForm();
    coreData.setCarrier(carrier);
    coreData = carrierCoreDataService.persistAndGet(coreData);
    carrier.setCarrierCoreData(coreData);

    //查询载体类型
    CarrierType carrierType = carrierTypeService.findById(carrierCoreDataDTO.getCarrierTypeId());
    coreData.setCarrierType(carrierType);
    //查询专业方向
    if(carrierCoreDataDTO.getCarrierProfessionalDirectionId() != null){
      CarrierProfessionalDirection carrierProfessionalDirection = carrierProfessionalDirectionService
          .findById(carrierCoreDataDTO.getCarrierProfessionalDirectionId());
      coreData.setCarrierProfessionalDirection(carrierProfessionalDirection);
    }
    List<CarrierOperatingTeamDTO> carrierOperatingTeams = carrierCoreDataDTO.getCarrierOperatingTeams();
    List<CarrierMajorDTO> carrierMajors = carrierCoreDataDTO.getCarrierMajors();
    for(CarrierOperatingTeamDTO dto : carrierOperatingTeams){
      CarrierOperatingTeam t = dto.toCopyForm();
      t.setCarrierCoreData(coreData);
      carrierOperatingTeamService.persist(t);
    }
    for(CarrierMajorDTO dto : carrierMajors){
        CarrierMajor major = dto.toCopyForm();
        major.setCarrierCoreData(coreData);
        carrierMajorService.persist(major);
    }
    carrierCoreDataService.merge(coreData);
    //处理载体重要数据
    CarrierImportantDataDTO dataDTO = carrierDTO.getCarrierImportantData();
    if(dataDTO != null){
      CarrierImportantData importantData = dataDTO.toCopyForm();
      importantData.setCarrier(carrier);
      carrierImportantDataService.persist(importantData);
      carrier.setCarrierImportantData(importantData);
    }

    //处理入驻企业
    List<CarrierSettledEnterpriseDTO> carrierSettledEnterpriseDTOS = carrierDTO.getCarrierSettledEnterprises();
    for(CarrierSettledEnterpriseDTO dto : carrierSettledEnterpriseDTOS){
      CarrierSettledEnterprise entity = dto.toCopyForm();
      entity.setCarrier(carrier);
      carrierSettledEnterpriseService.persist(entity);
    }
    //处理文件库
    List<CarrierDataBaseDTO> carrierDataBaseDTOS = carrierDTO.getCarrierDataBases();
    for(CarrierDataBaseDTO dto : carrierDataBaseDTOS){
      CarrierDataBase dataBase = dto.toCopyForm();
      dataBase.setCreateAt(new Date());
      dataBase.setCarrier(carrier);
      dataBase = carrierDataBaseService.persistAndGet(dataBase);
      //处理文件载体库具体文件
      List<CarrierDataBaseFilesDTO> filesDTOS = dto.getCarrierDataBaseFiles();
      for (CarrierDataBaseFilesDTO files : filesDTOS) {
        CarrierDataBaseFiles file = files.toCopyForm();
        file.setCreateAt(new Date());
        file.setUid(user.getId());
        file.setCarrierDataBase(dataBase);
        carrierDataBaseFilesService.persist(file);
      }
    }
    carrierService.merge(carrier);
    return ResponseDto.success();
  }

  @GetMapping("getCarrierById")
  @ApiOperation(position = 7, value = "获取科技载体详情", notes = "获取科技载体详情", tags = "创新载体模块", httpMethod = "GET", produces = "application/json")
  @ApiImplicitParam(name = "id", value = "科技载体Id", paramType = "query", dataType = "Integer", required = true, readOnly = true)
  public ResultDto<CarrierDTO> getCarrierById(@RequestParam(name = "id") Long id){
      return ResponseDto.success(carrierService.findCarrierById(id));
  }

  @GetMapping("getCarrierSettledEnterpriseInfoById")
  @ApiOperation(position = 8, value = "通过入驻企业信息ID获取入驻企业信息详情", notes = "通过入驻企业信息ID获取入驻企业信息详情",
      tags = "创新载体模块", httpMethod = "GET", produces = "application/json")
  @ApiImplicitParam(name = "enterpriseId", value = "入驻的企业信息ID", paramType = "query", dataType = "Long", required = true, readOnly = true)
  public CarrierSettledEnterpriseDTO getCarrierSettledEnterpriseInfoById(@RequestParam Long enterpriseId){
    CarrierSettledEnterprise enterprise = carrierSettledEnterpriseService.findById(enterpriseId);
    if(enterprise == null){
      throw new BootException("载体入驻企业不存在！");
    }
    return CarrierSettledEnterpriseDTO.copyForm(enterprise);
  }

  @PostMapping("delCarrierSettledEnterpriseInfoById")
  @ApiOperation(position = 9, value = "通过入驻企业信息ID删除入驻企业信息", notes = "通过入驻企业信息ID删除入驻企业信息",
      tags = "创新载体模块", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParam(name = "enterPriseId", value = "入驻的企业信息ID", paramType = "query", dataType = "Long", required = true, readOnly = true)
  public ResultDto delCarrierSettledEnterpriseInfoById(@RequestParam Long enterPriseId){
      CarrierSettledEnterprise enterprise = carrierSettledEnterpriseService.findById(enterPriseId);
      if(enterprise == null){
        throw new BootException(501, "入驻的企业信息不存在！");
      }
      carrierSettledEnterpriseService.remove(enterPriseId);
    return ResponseDto.success();
  }

  @GetMapping("isCompanyInEnter")
  @ApiOperation(position = 10, value = "通过机构代码判断该机构是否入驻过其他载体", notes = "通过机构代码判断该机构是否入驻过其他载体",
      tags = "创新载体模块", httpMethod = "GET", produces = "application/json")
  @ApiImplicitParam(name = "orgCode", value = "机构代码", paramType = "query", dataType = "String", required = true, readOnly = true)
  @ApiResponse(code = 200, message = "data true 表示已经入驻过其他载体，false 表示尚未入驻载体")
  public ResultDto<Boolean> isCompanyInEnter(@RequestParam String orgCode){
      int count =  carrierSettledEnterpriseService.getCarrierSettledEnterPriseByOrgCode(orgCode);
      return ResponseDto.success(count == 0 ? false : true);
  }

  @PostMapping("updateOrSaveCarrierSettledEnterpriseInfo")
  @ApiOperation(position = 11, value = "修改或者新增入驻企业信息", notes = "修改或者新增入驻企业信息(CarrierSettledEnterpriseDTO中不存在ID即是新增，存在ID即是修改)", tags = "创新载体模块", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParam(name = "carrierId", value = "载体实体Id", paramType = "query", dataType = "Long", required = true, readOnly = true)
  public ResultDto<?> updateOrSaveCarrierSettledEnterpriseInfo(@RequestParam Long carrierId,
      @RequestBody CarrierSettledEnterpriseDTO carrierSettledEnterpriseDTO) {
    Carrier carrier = carrierService.findById(carrierId);
    if (carrier == null) {
      throw new BootException(501, "载体信息不存在！");
    }

    CarrierSettledEnterprise enterprise = carrierSettledEnterpriseDTO.toCopyForm();
    //remove掉原有企业的载体信息
    CarrierSettledEnterprise removeEnterPrise =
        carrierSettledEnterpriseService.getCarrierSettledEnterPriseByOrgCodeToEntity(
            enterprise.getOrgCode());
    if (removeEnterPrise != null) {
      log.info("旧数据ID{}, 旧数据orgcode{}", removeEnterPrise.getId(), removeEnterPrise.getOrgCode());
      carrierSettledEnterpriseService.remove(removeEnterPrise.getId());
      System.out.println("新增企业的时候重载旧数据，旧数据ID：" + removeEnterPrise.getId());
    }
    log.info("orgCode{}", enterprise.getOrgCode());
    if (enterprise.getId() == null) {
      log.info("enterprise.getID == null");
      //新增
      enterprise.setCarrier(carrier);
      carrierSettledEnterpriseService.persist(enterprise);
    } else {
      log.info("enterprise.getID != null");
      //修改
      enterprise.setCarrier(carrier);
      carrierSettledEnterpriseService.merge(enterprise);
    }

    return ResponseDto.success();
  }

  @GetMapping("getCarrierCoreDataById")
  @ApiOperation(position = 12, value = "通过科技载体ID获取载体核心数据", notes = "过科技载体ID获取载体核心数据",
      tags = "创新载体模块", httpMethod = "GET", produces = "application/json")
  public ResultDto<CarrierCoreDataDTO> getCarrierCoreDataById(@RequestParam Long carrierId){
    return ResponseDto.success(carrierCoreDataService.getCarrierCoreDataByCarrierId(carrierId));
  }

  @PostMapping("updateCarrierCoreDataInfo")
  @ApiOperation(position = 13, value = "修改核心数据-基本信息(此处只修改基本信息，运营管理团队和专业服务合作机构有单独接口修改！)", notes = "修改核心数据-基本信息",
      tags = "创新载体模块", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParam(name = "carrierCoreDataId", value = "载体核心数据实际Id", paramType = "query", dataType = "Long", required = true, readOnly = true)
  public ResultDto<?> updateCarrierCoreDataInfo(@RequestBody CarrierCoreDataDTO carrierCoreDataDTO){
    CarrierCoreData coreData = carrierCoreDataDTO.toCopyForm();
    //处理Carrier科技载体实类
    Long carrierId = carrierCoreDataDTO.getCarrierId();
    Carrier carrier = carrierService.findById(carrierId);
    Preconditions.checkArgument(carrier != null, ResultCode.EN99990101);
    coreData.setCarrier(carrier);
    //处理专业方向
    Long carrierProfessionalDirectionId = carrierCoreDataDTO.getCarrierProfessionalDirectionId();
    CarrierProfessionalDirection direction = carrierProfessionalDirectionService.findById(carrierProfessionalDirectionId);
    Preconditions.checkArgument(direction != null,  ResultCode.EN99990102);
    coreData.setCarrierProfessionalDirection(direction);
    //处理载体类型
    Long carrierTypeId = carrierCoreDataDTO.getCarrierTypeId();
    CarrierType type = carrierTypeService.findById(carrierTypeId);
    Preconditions.checkArgument(type != null,  ResultCode.EN99990103.msg());
    coreData.setCarrierType(type);
    carrierCoreDataService.merge(coreData);
    return ResponseDto.success();
  }

  @GetMapping("getCarrierOperatingTeamById")
  @ApiOperation(position = 14, value = "通过运营管理团队人员id获取运营团队管理人员信息", notes = "通过运营管理团队人员id获取运营团队管理人员信息",
      tags = "创新载体模块", httpMethod = "GET", produces = "application/json")
  @ApiImplicitParam(name = "teamId", value = "运营管理团队人员id", paramType = "query", dataType = "Long", required = true, readOnly = true)
  public ResultDto<CarrierOperatingTeamDTO> getCarrierOperatingTeamById(Long teamId){
    CarrierOperatingTeam carrierOperatingTeam = carrierOperatingTeamService.findById(teamId);
    if(carrierOperatingTeam == null){
      throw new BootException(501, "运营管理团队人员不存在！");
    }
    return ResponseDto.success(CarrierOperatingTeamDTO.copyForm(carrierOperatingTeam));
  }

  @PostMapping("updateOrSaveCarrierOperatingTeam")
  @ApiOperation(position = 15, value = "修改或者新增运营管理团队", notes = "修改或者新增运营管理团队(CarrierOperatingTeamDTO中不存在ID即是新增，存在ID即是修改)",
      tags = "创新载体模块", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParam(name = "coreDataId", value = "科技载体核心数据ID（非科技载体ID）", paramType = "query", dataType = "Long", required = true, readOnly = true)
  public ResultDto<?> updateOrSaveCarrierOperatingTeam(@RequestParam Long coreDataId, @RequestBody CarrierOperatingTeamDTO carrierOperatingTeamDTO){
    CarrierCoreData coreData = carrierCoreDataService.findById(coreDataId);
    if(coreData == null){
      throw new BootException(501, "科技载体核心数据不存在！");
    }
    CarrierOperatingTeam carrierOperatingTeam = carrierOperatingTeamDTO.toCopyForm();
    if(carrierOperatingTeamDTO.getId() == null){
      carrierOperatingTeam.setCarrierCoreData(coreData);
      carrierOperatingTeamService.persist(carrierOperatingTeam);
    }else{
      carrierOperatingTeam.setCarrierCoreData(coreData);
      carrierOperatingTeamService.merge(carrierOperatingTeam);
    }
    return ResponseDto.success();
  }


  @PostMapping("delCarrierOperatingTeamById")
  @ApiOperation(position = 16, value = "删除运营团队管理人员", notes = "删除运营团队管理人员",
      tags = "创新载体模块", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParam(name = "teamId", value = "运营管理团队人员id", paramType = "query", dataType = "Long", required = true, readOnly = true)
  public ResultDto delCarrierOperatingTeamById(@RequestParam Long teamId){
    CarrierOperatingTeam carrierOperatingTeam = carrierOperatingTeamService.findById(teamId);
    if(carrierOperatingTeam == null){
      throw new BootException(501, "运营团队管理人员不存在！");
    }
    carrierOperatingTeamService.remove(teamId);
    return ResponseDto.success();
  }

  @GetMapping("getCarrierMajorById")
  @ApiOperation(position = 17, value = "通过专业服务机构或专家信息ID获取详情", notes = "通过专业服务结构或专家信息ID获取详情",
      tags = "创新载体模块", httpMethod = "GET", produces = "application/json")
  @ApiImplicitParam(name = "marjorId", value = "专业服务机构或专家信息ID", paramType = "query", dataType = "Long", required = true, readOnly = true)
  public ResultDto<CarrierMajorDTO> getCarrierMajorById(@RequestParam Long marjorId){
    CarrierMajor major = carrierMajorService.findById(marjorId);
    if(major == null){
      throw new BootException(501, "专业服务机构或专家信息不存在！");
    }
    return ResponseDto.success(CarrierMajorDTO.copyForm(major));
  }

  @PostMapping("updateOrSaveCarrierMajor")
  @ApiOperation(position = 18, value = "修改或者新增专业服务机构和专家信息",
      notes = "修改或者新增专业服务机构和专家信息(CarrierOperatingTeamDTO中不存在ID即是新增，存在ID即是修改)",
      tags = "创新载体模块", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParam(name = "coreDataId", value = "科技载体核心数据ID（非科技载体ID）", paramType = "query", dataType = "Long", required = true, readOnly = true)
  public ResultDto<?> updateOrSaveCarrierMajor(@RequestParam Long coreDataId, @RequestBody CarrierMajorDTO carrierMajorDTO){
    CarrierCoreData coreData = carrierCoreDataService.findById(coreDataId);
    if(coreData == null){
      throw new BootException(501, "科技载体核心数据不存在！");
    }
    CarrierMajor carrierMajor = carrierMajorDTO.toCopyForm();
    if(carrierMajor.getId() != null){
      carrierMajor.setCarrierCoreData(coreData);
      carrierMajorService.merge(carrierMajor);
    }else{
      carrierMajor.setCarrierCoreData(coreData);
      carrierMajorService.persist(carrierMajor);
    }
    return ResponseDto.success();
  }

  @GetMapping("getCarrierImportantDataById")
  @ApiOperation(position = 19, value = "通过载体CarrierId获取到重要数据", notes = "通过载体CarrierId获取到重要数据",
      tags = "创新载体模块", httpMethod = "GET", produces = "application/json")
  @ApiImplicitParam(name = "carrierId", value = "载体Id", paramType = "query", dataType = "Long", required = true, readOnly = true)
  public ResultDto<CarrierImportantDataDTO> getCarrierImportantDataById(@RequestParam Long carrierId){
    return ResponseDto.success(carrierImportantDataService.getCarrierImportantDataByCarrierId(carrierId));
  }

  @PostMapping("updateCarrierImportantData")
  @ApiOperation(position = 20, value = "通过载体Id和CarrierImportantDataDTO修改载体重要数据", notes = "通过载体Id和CarrierImportantDataDTO修改载体重要数据",
      tags = "创新载体模块", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParam(name = "carrierId", value = "载体Id", paramType = "query", dataType = "Long", required = true, readOnly = true)
  public ResultDto<?> updateCarrierImportantData(@RequestParam Long carrierId, @RequestBody CarrierImportantDataDTO carrierImportantDataDTO){
    Carrier carrier = carrierService.findById(carrierId);
    if(carrier == null){
      throw new BootException(501, "载体信息不存在！");
    }
    CarrierImportantData importantData = carrierImportantDataDTO.toCopyForm();
    importantData.setCarrier(carrier);
    carrierImportantDataService.merge(importantData);
    return  ResponseDto.success();
  }

  @GetMapping("getCarrierDataBaseById")
  @ApiOperation(position = 21, value = "通过科技载体ID获取载体文件资料库", notes = "通过载体Id和CarrierImportantDataDTO修改载体重要数据",
      tags = "创新载体模块", httpMethod = "GET", produces = "application/json")
  @ApiImplicitParam(name = "carrierId", value = "载体Id", paramType = "query", dataType = "Long", required = true, readOnly = true)
  public ResultDto<List<CarrierDataBaseDTO>> getCarrierDataBaseById(@RequestParam Long carrierId){
    return ResponseDto.success(carrierDataBaseService.getCarrierDataBaseListByCarrierId(carrierId));
  }

  @PostMapping("delAllCarrierDataBaseById")
  @ApiOperation(position = 22, value = "通过载体资料库ID删除具体实例(此接口是删除整个文件载体资料库，包括文件列表)", notes = "通过载体资料库ID删除具体实例",
      tags = "创新载体模块", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParam(name = "dataBaseId", value = "载体资料库ID", paramType = "query", dataType = "Long", required = true, readOnly = true)
  public ResultDto<?> delAllCarrierDataBaseById(@RequestParam Long dataBaseId){
    CarrierDataBase dataBase = carrierDataBaseService.findById(dataBaseId);
    if(dataBase == null){
      throw new BootException(501, "载体资料库数据不存在！");
    }
    carrierDataBaseService.remove(dataBase.getId());
    return ResponseDto.success();
  }

  @PostMapping("delCarrierDataBaseById")
  @ApiOperation(position = 23, value = "删除默认的文件配置里面具体的单个文件", notes = "删除默认的文件配置里面具体的单个文件",
      tags = "创新载体模块", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParams({@ApiImplicitParam(name = "dataBaseId", value = "文件载体资料库ID", paramType = "query", dataType = "Long", required = true, readOnly = true),
      @ApiImplicitParam(name = "fileId", value = "具体的文件ID", paramType = "query", dataType = "Long", required = true, readOnly = true)})
  public ResultDto<?> delDefaultCarrierDataBaseById(@RequestParam Long dataBaseId, @RequestParam Long fileId){
    CarrierDataBase dataBase = carrierDataBaseService.findById(dataBaseId);
    Preconditions.checkNotNull(dataBase, ResultCode.EN99990104);
    CarrierDataBaseFiles files = carrierDataBaseFilesService.getCarrierDataBaseFilesByDataId(dataBase.getId(), fileId);
    if(!dataBase.getIsDefaultFile()){
      throw new BootException(503, "非默认文件配置，无法删除单个文件。");
    }
    carrierDataBaseFilesService.remove(files.getId());
    return ResponseDto.success();
  }

  @PostMapping("saveCarrierDataBase")
  @ApiOperation(position = 24, value = "新增一条载体资料库到科技载体", notes = "新增一条载体资料库到科技载体",
      tags = "创新载体模块", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParam(name = "carrierId", value = "科技载体实例Id", paramType = "query", dataType = "Long", required = true, readOnly = true)
  public ResultDto<?> saveCarrierDataBase(@RequestParam Long carrierId, @RequestBody CarrierDataBaseDTO carrierDataBaseDTO){
    UserDto user = getCurrentUser();
    Carrier carrier = carrierService.findById(carrierId);
    if(carrier == null){
      throw new BootException(501, "载体信息不存在！");
    }
    CarrierDataBase dataBase = carrierDataBaseDTO.toCopyForm();
    dataBase.setCarrier(carrier);
    dataBase.setCreateAt(new Date());
    carrierDataBaseService.persist(dataBase);
    List<CarrierDataBaseFilesDTO> filesDTOS = carrierDataBaseDTO.getCarrierDataBaseFiles();
    for (CarrierDataBaseFilesDTO files : filesDTOS) {
      CarrierDataBaseFiles file = files.toCopyForm();
      file.setCreateAt(new Date());
      file.setCarrierDataBase(dataBase);
      file.setUid(user.getId());
      carrierDataBaseFilesService.persist(file);
    }
    return ResponseDto.success();
  }

  @PostMapping("updateCarrierDataBaseById")
  @ApiOperation(position = 25, value = "修改一条载体资料库到科技载体(只修改文件库资料，不修改文件)", notes = "修改一条载体资料库到科技载体",
      tags = "创新载体模块", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParam(name = "dataBaseId", value = "载体资料库实例Id", paramType = "query", dataType = "Long", required = true, readOnly = true)
  public ResultDto<?> updateCarrierDataBaseById(@RequestParam Long dataBaseId, @RequestBody CarrierDataBaseDTO carrierDataBaseDTO){
    CarrierDataBase oldDataBase = carrierDataBaseService.findById(dataBaseId);
    CarrierDataBase dataBase = carrierDataBaseDTO.toCopyForm();
    dataBase.setCarrier(oldDataBase.getCarrier());
    carrierDataBaseService.merge(dataBase);
    return ResponseDto.success();
  }

  @PostMapping("addCarrierDataBaseFilesToCarrierDataBase")
  @ApiOperation(position = 26, value = "新增文件到科技载体文件库", notes = "新增文件到科技载体文件库",
      tags = "创新载体模块", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParam(name = "dataBaseId", value = "载体资料库实例Id", paramType = "query", dataType = "Long", required = true, readOnly = true)
  public ResultDto<?> addCarrierDataBaseFilesToCarrierDataBase(@RequestParam Long dataBaseId, @RequestBody CarrierDataBaseFilesDTO filesDTO){
    UserDto user = getCurrentUser();
    CarrierDataBase dataBase = carrierDataBaseService.findById(dataBaseId);
    //checkNull(dataBase, "载体文件库不存在！");
    CarrierDataBaseFiles file = filesDTO.toCopyForm();
    file.setCarrierDataBase(dataBase);
    file.setCreateAt(new Date());
    file.setUid(user.getId());
    return ResponseDto.success();
  }

  @GetMapping("getToAuditCarrier")
  @ApiOperation(position = 27, value = "[政府端]获取所有待审核的科技载体申请", notes = "获取所有待审核的科技载体申请",
      tags = "创新载体模块", httpMethod = "GET", produces = "application/json")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "page", value = "当前页码", required = false, defaultValue = "1", paramType = "query", dataType = "int"),
      @ApiImplicitParam(name = "pageSize", value = "一页大小", required = false, defaultValue = "10", paramType = "query", dataType = "int")})
  public ResultDto<PageDto<List<CarrierShowListDTO>>> getToAuditCarrier(
      @RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "20") int pageSize){
      return ResponseDto.success(carrierService.getAllToAuditCarrier(page, pageSize));
  }

  @PostMapping("auditCarrier")
  @ApiOperation(position = 28, value = "审核科技载体", notes = "审核科技载体",
      tags = "创新载体模块", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "carrierId", value = "科技载体ID", required = false, defaultValue = "1", paramType = "query", dataType = "int"),
      @ApiImplicitParam(name = "status", value = "审核状态， 通过是2 驳回是3", required = false, defaultValue = "10", paramType = "query", dataType = "int")})
  public ResultDto<?> auditCarrier(@RequestParam Long carrierId, @RequestParam Integer status,
      @RequestParam(required = false) String text){
    UserDto user = ensureGovernment();
    Carrier carrier = carrierService.findById(carrierId);
    if(carrier == null){
      throw new BootException(501, "科技载体数据不存在！");
    }
    CarrierAuditRecord auditRecord = new CarrierAuditRecord();
    auditRecord.setAuditAt(new Date());
    auditRecord.setCarrier(carrier);
    if(status == 2){
      //审核通过
      if(user != null){
        auditRecord.setUid(user.getId());
        auditRecord.setUName(user.getRealName());
      }
      carrier.setAuditText(null);
      auditRecord.setStatus(2);
      carrier.setStatus(2);
      carrierService.merge(carrier);
    }else if(status == 3){
      //审核拒绝
      if(user != null){
        auditRecord.setUid(user.getId());
        auditRecord.setUName(user.getRealName());
      }
      auditRecord.setStatus(3);
      auditRecord.setContent(text);
      carrier.setStatus(3);
      carrier.setAuditText(text);
      carrierService.merge(carrier);
    }
    carrierAuditRecordService.persist(auditRecord);
    return ResponseDto.success();
  }


  @GetMapping("getCompanyAllAudit")
  @ApiOperation(position = 29, value = "获取当前企业提交的所有待审核的科技载体", notes = "获取当前企业提交的所有待审核的科技载体",
      tags = "创新载体模块", httpMethod = "GET", produces = "application/json")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "page", value = "当前页码", required = false, defaultValue = "1", paramType = "query", dataType = "int"),
      @ApiImplicitParam(name = "pageSize", value = "一页大小", required = false, defaultValue = "10", paramType = "query", dataType = "int")})
  public ResultDto<PageDto<List<CarrierShowListDTO>>> getCompanyAllAudit(
      @RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "20") int pageSize){
    UserDto userTO = ensureCompany();
    return ResultDto.success(carrierService.getCompanyAllAudit(page, pageSize, userTO.getUniqueGroup().getId()));
  }

  @PostMapping("updateCompanyCarrierStatus")
  @ApiOperation(position = 30, value = "企业重新提交科技载体审核", notes = "企业重新提交科技载体审核",
      tags = "创新载体模块", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParam(name = "carrierId", value = "科技载体实例ID", required = true, paramType = "query", dataType = "Long")
  public ResultDto<?> updateCompanyCarrierStatus(@RequestParam Long carrierId){
      UserDto userTo = ensureCompany();
      Carrier carrier = carrierService.findById(carrierId);
      //checkNull(carrier, "科技载体数据不存在！");
      if(carrier.getStatus() != 3){
        throw new BootException(502, "该科技载体审核状态非驳回状态！");
      }
      if(userTo.getUniqueGroup().getId().longValue() != carrier.getCarrierSubordinateId().longValue()){
        throw new BootException(503, "无权限操作！");
      }
      carrier.setStatus(1);
      carrierService.merge(carrier);
      return ResponseDto.success();
  }


  @GetMapping("getAddress")
  @ApiOperation(position = 31, value = "获取国家省市C区三级联动", notes = "获取国家省市区三级联动",
      tags = "创新载体模块", httpMethod = "GET", produces = "application/json")
  @ApiImplicitParam(name = "addressId", value = "地址ID， 如果获取省的话就不传，市和区传返回数据的parentId", paramType = "query", dataType = "Long", required = false, readOnly = true)
  public ResultDto<List<Map<String, String>> > getAddress(@RequestParam(required =  false, defaultValue = "-1") Long addressId){
      return ResponseDto.success(carrierService.getAddress(addressId));
  }

  @GetMapping("getAllCarrierByCompanyId")
  @ApiOperation(position = 32, value = "通过企业id获取该企业所有已经通过审核的科技载体", notes = "通过企业id获取该企业所有已经通过审核的科技载体",
      tags = "创新载体模块", httpMethod = "GET", produces = "application/json")

  @ApiImplicitParams({
      @ApiImplicitParam(name = "companyId", value = "企业ID", paramType = "query", dataType = "Long", required = true, readOnly = true),
      @ApiImplicitParam(name = "carrierId", value = "需要过滤当前详情的载体ID", defaultValue = "-1", required = false, paramType = "query", dataType = "Long")})
  public ResultDto<List<CarrierShowListDTO>> getAllCarrierByCompanyId(@RequestParam Long companyId, @RequestParam(required = false, defaultValue = "-1") Long carrierId){
    return ResponseDto.success(carrierService.getAllCarrierByCompanyId(companyId, carrierId));
  }

  @PostMapping("delCarrierCarrierMajorById")
  @ApiOperation(position = 33, value = "删除专业服务合作机构和专家信息", notes = "删除专业服务合作机构和专家信息",
      tags = "创新载体模块", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParam(name = "majorId", value = "运营管理团队人员id", paramType = "query", dataType = "Long", required = true, readOnly = true)
  public ResultDto<?> delCarrierCarrierMajorById(@RequestParam Long majorId){
    CarrierMajor major = carrierMajorService.findById(majorId);
    Preconditions.checkNotNull(major, ResultCode.EN99990105.msg());
    carrierMajorService.remove(major.getId());
    return ResponseDto.success();
  }
  @GetMapping("getAllRecommendCarrier")
  @ApiOperation(position =34, value = "获取所有的科技载体作为项目的推荐载体", notes = "获取所有的科技载体作为项目的推荐载体", tags = "创新载体模块", httpMethod ="GET", produces = "application/json")
  public ResultDto<List<CarrierShowListDTO>> getAllRecommendCarrier(){
    return ResponseDto.success(carrierService.getAllRecommendCarrier());
  }

  @GetMapping("getCarrierByPage")
  @ApiOperation(position =35, value = "获取分页已入库的科技载体", notes = "获取分页已入库的科技载体", tags = "创新载体模块", httpMethod ="GET", produces = "application/json")
  @ApiImplicitParams({
          @ApiImplicitParam(name = "page", value = "当前页码", required = false, defaultValue = "1", paramType = "query", dataType = "int"),
          @ApiImplicitParam(name = "pageSize", value = "一页大小", required = false, defaultValue = "10", paramType = "query", dataType = "int")})
  public ResultDto<PageDto<List<CarrierShowListDTO>>> getCarrierByPage(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "10") int pageSize){
    return ResponseDto.success(carrierService.getCarrierByPage(page,pageSize));
  }

}
