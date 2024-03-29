package xinrui.cloud.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import xinrui.cloud.BootException;
import xinrui.cloud.controller.CarrierController;
import xinrui.cloud.controller.ProjectInController;
import xinrui.cloud.domain.ProjectFile;
import xinrui.cloud.domain.ProjectFileInner;
import xinrui.cloud.domain.ProjectInner;
import xinrui.cloud.domain.dto.*;
import xinrui.cloud.dto.PageDto;
import xinrui.cloud.dto.ResultDto;
import xinrui.cloud.service.ProjectInService;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * <B>Title:</B>LeaderServiceImpl</br>
 * <B>Description:</B>  </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 * 项目入驻的相关业务处理层
 *
 * @author lpan
 * @version 1.0
 * 2019/6/25 11:31
 */
@Service
@Transactional
public class ProjectInServiceImpl extends BaseServiceImpl<ProjectInner> implements ProjectInService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProjectInController.class);

    @Autowired
    private CarrierController carrierClientService;

    @Override
    public ProjectInnerDto saveOrUpdatePorject(ProjectInnerDto proInDto) {
            ProjectInnerDto projectInnerDto = saveOrUpdatePro(proInDto);
            //根据项目的信息返回有推荐的载体项目信息
            ProjectInnerDto proAndCarri = getMatchCarrier(projectInnerDto);
            return proAndCarri;
    }

    /**
     * 保存或者修改项目信息
     *
     * @param proInDto
     */
    private ProjectInnerDto saveOrUpdatePro(ProjectInnerDto proInDto) {
        //判断保存还是修改项目
        if (proInDto.getId()!=null){
          //找到文件清空文件关联对象
            DetachedCriteria d = DetachedCriteria.forClass(ProjectFile.class);
            d.createAlias("projectInner", "p").add(Restrictions.eq("p.id", proInDto.getId()));
            List<ProjectFile> projectFiles = dao.listBydCriteria(d);
            for (ProjectFile projectFile : projectFiles) {
               //删除文件关联
                DetachedCriteria d1 = DetachedCriteria.forClass(ProjectFileInner.class);
                d1.createAlias("projectFile", "pf").add(Restrictions.eq("pf.id", projectFile.getId()));
                List<ProjectFileInner> projectFileIns = dao.listBydCriteria(d1);
                for (ProjectFileInner projectFileIn : projectFileIns) {
                    dao.remove(ProjectFileInner.class,projectFileIn.getId());
                }
                dao.remove(ProjectFile.class,projectFile.getId());
            }
        }
        //初始化状态1是待审核,2 审核中,3 审核通过,4审核被驳回
        //判断上传文件是否为空
        if (proInDto.getProFileDtoList() != null && proInDto.getProFileDtoList().size() > 0) {
            List<ProjectFileDto> proFileDtos = proInDto.getProFileDtoList();
            List<ProjectFileDto> proFileDtoLsit = new ArrayList<>(proFileDtos);
            //清空关联关系
            proInDto.getProFileDtoList().clear();
            //保存项目主对象
            ProjectInner proIn = merge(ProjectInnerDto.toBean(proInDto));
            //创建时间
            proIn.setCreateAt(new Date());
            proIn = saveFile(proFileDtoLsit, proIn);
            //保存项目
            ProjectInner merge = merge(proIn);
            ProjectInnerDto projectInnerDto = ProjectInnerDto.copyFrom(merge);
            return projectInnerDto;
        } else {
            //保存项目主对象
            ProjectInner proIn = merge(ProjectInnerDto.toBean(proInDto));
            ProjectInnerDto projectInnerDto = ProjectInnerDto.copyFrom(proIn);
            return projectInnerDto;
        }


    }

    /**
     * 保存文件
     *
     * @param proFileDtoLsit
     * @param proIn
     */
    private ProjectInner saveFile(List<ProjectFileDto> proFileDtoLsit, ProjectInner proIn) {
        //保存文件
        for (ProjectFileDto projectFileDto : proFileDtoLsit) {
            ProjectFile projectFile = ProjectFileDto.toBean(projectFileDto);
            List<ProjectFileInner> projectFileInerrList = projectFile.getProjectFileInerrList();
            List<ProjectFileInner> projectFileInners = new ArrayList<>(projectFileInerrList);
            //清空关联关系
            projectFileInerrList.clear();
            ProjectFile proFile = dao.merge(ProjectFile.class, projectFile);//具体对象
            //保存子文件信息
            for (ProjectFileInner inner : projectFileInners) {
                inner.setProjectFile(proFile);//保存的是持久态数据
                ProjectFileInner innerMerge = dao.merge(ProjectFileInner.class, inner);
                proFile.getProjectFileInerrList().add(innerMerge);
            }
            proFile.setProjectInner(proIn);
            proFile = dao.merge(ProjectFile.class, proFile);//更新文件信息
            proIn.getProjectFiles().add(proFile);
        }
        return proIn;
    }

    @Override
    public void uploadData(ProjectFileReqDto projectFileReqDto) {
        //获取项目id,判断项目是否存在
        Long projectInnerId = projectFileReqDto.getProjectInnerId();
        ProjectInner projectInner = findById(projectInnerId);
        Assert.notNull(projectInner, "项目入驻详情对象找不到!");
        //获取多个资料上传的信息
        List<ProjectFileDto> projectFileDtoList = projectFileReqDto.getProjectFileDto();
        projectInner = saveFile(projectFileDtoList, projectInner);
        merge(projectInner);//更新项目信息
    }

    @Override
    public PageDto<List<ProjectInnerDto>> findAllProByState(int findFlag, String findName, String businessType, int page, int pageSize) {
        if (1 == findFlag) {
            //根据状态分页查询项目,查询的项目
            DetachedCriteria d = DetachedCriteria.forClass(ProjectInner.class);
            if (findName != null && findName != "") {
                //添加企业项目名查询
                d.add(Restrictions.like("projectName", findName, MatchMode.ANYWHERE));
            }
            if (businessType != null && businessType != "") {
                //添加创业类型查询
                d.add(Restrictions.like("businessType", businessType, MatchMode.ANYWHERE));
            }
            d.add(Restrictions.eq("projectState", findFlag));
            d.addOrder(Order.desc("createAt"));
            PageDto<List<ProjectInner>> pg1 = new PageDto<>(--page * pageSize, pageSize, d);
            dao.pageByCriteria(pg1);
            if (CollectionUtils.isEmpty(pg1.getT())) {
                List<ProjectInnerDto> proInDtoList = new ArrayList<ProjectInnerDto>();
                return new PageDto<List<ProjectInnerDto>>(pg1.getTotalPage(), proInDtoList);
            }
            List<ProjectInner> projectInners = pg1.getT();
            //项目bean转响应实体
            List<ProjectInnerDto> proInDtoList = ProjectInnerDto.copyFromByProIn(projectInners);

            return new PageDto<List<ProjectInnerDto>>(pg1.getTotalPage(), proInDtoList);
        } else {
            throw new BootException(501, "请校对请求参数后重新输入!");
        }
    }

    @Override
    public PageDto<List<ProjectInnerDto>> findProByState(Long userId, int findFlag, int page, int pageSize) {
        //查询企业未审核的,标识1是待审核(审核中),2 审核通过,3审核被驳回
        if (1 == findFlag) {
            return getListWaitAudit(userId, findFlag, page, pageSize);
        } else {
            throw new BootException(501, "请校对请求参数后重新输入!");
        }

    }

    /**
     * 企业未审核状态, 查询审核中状态得到分页的项目集合
     *
     * @param userId
     * @param findFlag
     * @param page
     * @param pageSize
     * @return
     */
    private PageDto<List<ProjectInnerDto>> getListWaitAudit(Long userId, int findFlag, int page, int pageSize) {
        //根据状态分页查询项目,根据时间倒序
        DetachedCriteria d = DetachedCriteria.forClass(ProjectInner.class);
        d.add(Restrictions.eq("projectState", findFlag))
                .add(Restrictions.eq("creatorId", userId))
                .addOrder(Order.desc("createAt"));
        PageDto<List<ProjectInner>> pg1 = new PageDto<>(--page * pageSize, pageSize, d);
        dao.pageByCriteria(pg1);
        if (CollectionUtils.isEmpty(pg1.getT())) {
            List<ProjectInnerDto> proInnerDtos = new ArrayList<>();
            return new PageDto<List<ProjectInnerDto>>(pg1.getTotalPage(), proInnerDtos);
        }
        List<ProjectInner> proIns = pg1.getT();
        //项目bean转响应实体
        List<ProjectInnerDto> proInList = ProjectInnerDto.copyFromByProIn(proIns);

        List<ProjectInnerDto> proInnerDtos = new ArrayList<>();
        LOGGER.info("开始调用载体微服务,获取全部载体");
        //封装推荐的载体
        for (ProjectInnerDto proInDto : proInList) {
            ProjectInnerDto ProInnerDto = getMatchCarrier(proInDto);
            proInnerDtos.add(ProInnerDto);
        }

        return new PageDto<List<ProjectInnerDto>>(pg1.getTotalPage(), proInnerDtos);
    }

    @Override
    public PageDto<List<ProjectFileDto>> findProFileByProId(Long projectId, int page, int pageSize) {
        //根据项目ID分页查询项目文件资料
        DetachedCriteria d = DetachedCriteria.forClass(ProjectFile.class);
        d.createAlias("projectInner", "p").add(Restrictions.eq("p.id", projectId));//实体类具体属性用别名配置
        PageDto<List<ProjectFile>> pg1 = new PageDto<>(--page * pageSize, pageSize, d);
        dao.pageByCriteria(pg1);
        //项目bean转响应实体
        return new PageDto<List<ProjectFileDto>>(pg1.getTotalPage(), ProjectFileDto.copyFroms(pg1.getT()));
    }

    @Override
    public ProjectInnerDto getProjectDetail(Long projectId) {
        //查询项目
        ProjectInner proIn = findById(projectId);
        if (proIn == null) {
            throw new BootException(501, "没有项目信息");
        }
        ProjectInnerDto proInDto = ProjectInnerDto.copyFrom(proIn);
        getMatchCarrier(proInDto);
        return proInDto;
    }

    /**
     * 根据项目信息获得匹配的载体集合信息
     *
     * @param proInDto
     */
    private ProjectInnerDto getMatchCarrier(ProjectInnerDto proInDto) {
        /**
         ******必须条件,字段的数值匹配查询多个已通过审核的载体信息
         * 项目租凭价格范围,合计面积,意向区域
         *租赁价格范围(区间小数) leasePriceRange,空置面积(具体值小数) vacantArea,所属区域(多个)  areasToWhichTheyBelong;
         ****** 额外条件判断选项有交集的(具体值)
         * 项目交通需求(多个):  住房说明(多个)
         * 载体提供的交通情况多个 introductionToVehicle   住房说明多个 introductionToHousing
         * 需求属性判空,
         * 项目: 排污设施需求,环评需求,信息化建设需求,公共技术平台开放需求
         * 载体:排污设备介绍说明 introductionToSewageDisposal,环评服务介绍说明,introductionToELAServices
         * 信息化建设介绍说明 introductionToConstruction,公共技术平台介绍 introductionToPublicTechnology
         */
        //是否有推荐的载体的集合
        ResultDto<List<CarrierShowListDTO>> resultData = carrierClientService.getAllRecommendCarrier();
        //伪数据匹配
        if (resultData == null || resultData.getData() == null || resultData.getData().size() == 0) {
            //没有推荐载体匹配返回
            LOGGER.info("获得所有载体resultData的信息为:{}==null", resultData);
            return proInDto;
        } else {
            //获得所有载体,封装
            List<?> carrierData = resultData.getData();
            List<ProAndCarrierDto> proAndCarDtoList = new ArrayList<ProAndCarrierDto>();
            for (Object carrierDatum : carrierData) {
                //每个载体一个对象
                ProAndCarrierDto proAndCarDto = new ProAndCarrierDto();
                //转json字符串,解析成json对象
                String jsonStr = JSON.toJSONString(carrierDatum);
                JSONObject jsonObject = JSON.parseObject(jsonStr);
                //需要判断获取的数据是否为空,无返回数据zhjf_carrier_core_data表
                if (jsonObject != null) {
                    //toObject转实体对(缺少所属区域)
                    CarrierImportCopy carImCopy = jsonObject.toJavaObject(CarrierImportCopy.class);
                    //属性匹配的标识
                    int count = 1;
                    //调用匹配的方法,必须条件匹配
                    count = getCountByStr(count, proInDto.getLeasePriceRange(), carImCopy.getLeasePriceRange());
                    count = getCountByNum(count, proInDto.getVacantArea(), carImCopy.getVacantArea());
                    count = getCountByChar(count, proInDto.getAreasToWhichTheyBelong(), carImCopy.getAreasToWhichTheyBelong());
                    //判读是否满足基本条件
                    if (4 == count) {
                        //额外条件匹配,住房,交通
                        count = getCountInChar(count, proInDto.getHouseDt(), carImCopy.getIntroductionToHousing());
                        count = getCountInChar(count, proInDto.getTrafficDt(), carImCopy.getIntroductionToVehicle());
                        count = getCountIsHave(count, proInDto.getIntroductionToSewageDisposal(), carImCopy.getIntroductionToSewageDisposal());
                        count = getCountIsHave(count, proInDto.getIntroductionToELAServices(), carImCopy.getIntroductionToELAServices());
                        count = getCountIsHave(count, proInDto.getIntroductionToConstruction(), carImCopy.getIntroductionToConstruction());
                        count = getCountIsHave(count, proInDto.getIntroductionToPublicTechnology(), carImCopy.getIntroductionToPublicTechnology());
                        //使用count字段排
                        LOGGER.info("该载体匹配的字段有{}", count);
                        //设置排序的字段
                        proAndCarDto.setSort(count);
                        //封装需要的返回的数据，载体的id,品牌名,所属区域,联系人姓名,租赁价格范围,载体地址,联系人电话
                        if (carImCopy.getId() != null && carImCopy.getId() != 0) {
                            proAndCarDto.setCarrierId(carImCopy.getId());
                        }
                        //载体名
                        if (!StringUtils.isEmpty(carImCopy.getBrandName())) {
                            proAndCarDto.setBrandName(carImCopy.getBrandName());
                        }
                        //所属区域
                        proAndCarDto.setAreasToWhichTheyBelong(carImCopy.getAreasToWhichTheyBelong());
                        //联系人姓名
                        if (!StringUtils.isEmpty(carImCopy.getContactsName())) {
                            proAndCarDto.setContactsName(carImCopy.getContactsName());
                        }
                        //租赁价格范围
                        proAndCarDto.setLeasePriceRange(carImCopy.getLeasePriceRange());
                        //载体地址
                        if (!StringUtils.isEmpty(carImCopy.getIncubationBaseAddress())) {
                            proAndCarDto.setIncubationBaseAddress(carImCopy.getIncubationBaseAddress());
                        }
                        //联系人电话
                        if (!StringUtils.isEmpty(carImCopy.getContactsPhone())) {
                            proAndCarDto.setContactsPhone(carImCopy.getContactsPhone());
                        }
                        //空置面积
                        if (!StringUtils.isEmpty(carImCopy.getVacantArea())) {
                            proAndCarDto.setVacantArea(carImCopy.getVacantArea());
                        }
                        //添加到集合中,判断是否排序
                        proAndCarDtoList.add(proAndCarDto);
                    } else {
                        //未查询到相匹配的载体
                        continue;
                    }
                } else {
                    continue;
                }
            }
            //是否排序全部遍历返回
            if (1 == proAndCarDtoList.size()) {
                proInDto.setProAndCarDtoList(proAndCarDtoList);
                return proInDto;
            } else {
                //根据相匹配的属性的个数
                Collections.sort(proAndCarDtoList, new Comparator<ProAndCarrierDto>() {
                    @Override
                    public int compare(ProAndCarrierDto o1, ProAndCarrierDto o2) {
                        //制定排序规则
                        return o2.getSort() - o1.getSort();//倒序
                    }
                });
                proInDto.setProAndCarDtoList(proAndCarDtoList);
            }
            //封装载体
            return proInDto;
        }
    }

    /**
     * 是否含有该字段
     *
     * @param proField
     * @param carField
     * @return
     */
    private int getCountIsHave(int count, String proField, String carField) {
        if (!StringUtils.isEmpty(proField) && !StringUtils.isEmpty(carField)) {
            //需要判断字符串内容首字是否为无
            if ("无".equals(proField) || "无".equals(carField)) {
                return count;
            }
            count++;
        }
        return count;
    }

    /**
     * 所属区域的匹配,判断字符的所属关系
     * @param count
     * @param projectStr
     * @param carrierStr
     * @return
     */
    private int getCountByChar(int count, String projectStr, String carrierStr) {
        //对象不为空是否有所属:projectStr("布吉街道/龙岗街道"),carrierStr("省/市/区/街道")空为无
        if (!StringUtils.isEmpty(projectStr) && !StringUtils.isEmpty(carrierStr) && !"无".equals(projectStr) && !"无".equals(carrierStr)){
           //判断是否为多个区域
            String carrier = carrierStr.split("/")[3];
            if (projectStr.contains("/")){
                //切割所有的/符号,把对象放到string数组里面,需要判断有/符号不用contains
                String[] projectArr = projectStr.split("/");
                //是否含有项目的信息
                List<String> projectList = Arrays.asList(projectArr);
                if (projectList.contains(carrier)) {
                    count++;
                } else {
                    return count;
                }
            }else {
                if (projectStr.equalsIgnoreCase(carrier)) {
                    count++;
                } else {
                    return count;
                }
            }
        }
        return count;
    }

    /**
     * 根据逗号分割，如果没有则返回当前的，如果为空返回空集合
     *
     * @param fileds 需要被分割的字符串
     * @return 被分隔后的集合
     */
    public static List<String> split(String fileds) {
        if (StringUtils.isBlank(fileds)) {
            return Lists.newArrayList();
        }
        if (fileds.contains(",")) {
            String[] split = fileds.split(",");
            return Arrays.asList(split);
        }
        return Lists.newArrayList(fileds);
    }

    /**
     * 匹配交通,住房,交集,不包括无，英文逗号隔开
     *
     * @param count
     * @param projectStr
     * @param carrierStr
     * @return
     */
    private int getCountInChar(int count, String projectStr, String carrierStr) {
        //对象不为空是否有所属:projectStr("住房"),carrierStr("住房")
        if (StringUtils.isBlank(projectStr) || StringUtils.isBlank(carrierStr) || "无".equals(projectStr) || "无".equals(carrierStr)) {
            return count;
        }
        List<String> projectStrList = split(projectStr);
        List<String> carrierStrList = split(carrierStr);
        for (int i = 0; i < projectStrList.size(); i++) {
            boolean contains = carrierStrList.contains(projectStrList.get(i));
            if (contains) {
                count++;
            }
        }
        return count;
    }


    /**
     * 价格区间的特殊处理
     *
     * @param count
     * @param proInDto
     * @param caImData
     * @return
     */
    private int getCountByStr(int count, String proInDto, String caImData) {
        //判断值是否为空
        if (!StringUtils.isEmpty(proInDto) && !StringUtils.isEmpty(caImData)) {
            //获取价格数据,依照/切,判断是否含有/符号(前端端限制一定会传)
            if (proInDto.contains("/") && caImData.contains("/")) {
                String[] proArr = proInDto.split("/");
                String[] carArr = caImData.split("/");
                //[2-10],卖载体最小值>企业最大值,企业买不了载体
                if (Double.valueOf(carArr[0]) > Double.valueOf(proArr[1])) {
                    return count;
                } else {
                    count++;
                }
            } else {
                return count;
            }
        }
        return count;
    }

    /**
     * 空置区间的特殊处理
     *
     * @param count
     * @param proInDto
     * @param caImData 载体的空置区间带有小数点
     * @return
     */
    private int getCountByNum(int count, String proInDto, String caImData) {
        //判断值是否为空
        if (!StringUtils.isEmpty(proInDto) && !StringUtils.isEmpty(caImData)) {
            //获取价格数据,依照/切,判断是否含有/符号(前端端限制一定会传)是否为多个
            if (proInDto.contains("/")) {
                String[] proArr = proInDto.split("/");
                //项目最小值大于载体值,项目不能买载体
                if (Double.valueOf(proArr[0]) > Double.valueOf(caImData)) {
                    return count;
                } else {
                    count++;
                }
            } else {
                //单个面积项目值大于载体值,项目不能买载体
                if (Double.valueOf(proInDto) > Double.valueOf(caImData)) {
                    return count;
                }else {
                    count++;
                }
            }
        }
        return count;
    }


    /**
     * 获得匹配的参数
     *
     * @param count
     * @param s1
     * @param s2
     * @return
     */
    private int getCount(int count, String s1, String s2) {
        if (Integer.parseInt(s2) > Integer.parseInt(s1)) {
            count++;
            return count;
        } else {
            return count;
        }
    }

    @Override
    public void delProject(Long[] projectId) {
        //查询项目,获得持久化对象
        if (projectId.length != 0) {
            for (Long aLong : projectId) {
                ProjectInner proInner = findById(projectId);
                //删除关联的文件资料详情
                List<ProjectFile> projectFiles = proInner.getProjectFiles();
                for (ProjectFile projectFile : projectFiles) {
                    //删除关联的文件
                    List<ProjectFileInner> proFileInList = projectFile.getProjectFileInerrList();
                    for (ProjectFileInner fileInner : proFileInList) {
                        dao.remove(ProjectFileInner.class, fileInner.getId());
                    }
                    dao.remove(ProjectFile.class, projectFile.getId());
                }
                //删除项目的信息
                dao.remove(ProjectInner.class, proInner.getId());
            }
        }
    }

}
