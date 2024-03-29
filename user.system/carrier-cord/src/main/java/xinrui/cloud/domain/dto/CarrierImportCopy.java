package xinrui.cloud.domain.dto;

import lombok.Data;

import javax.persistence.Lob;

/**
 * @package:liuzhuomin.cloud.domain.dto
 * @desc: 拷贝载体的需要的数据
 * @author:lpan
 * @version:
 * :2019/7/5 20:17
 */
@Data
public class CarrierImportCopy{

    //载体Id
    private Long id;
    /** 空置面积 */
    private String vacantArea;
    /** 租赁价格范围 */
    private String leasePriceRange;
    //所属区域
    @Lob
    private String areasToWhichTheyBelong;
    /** 载体交通情况介绍说明*/
    @Lob
    private String introductionToVehicle;
    /** 排污设备介绍说明 */
    @Lob
    private String introductionToSewageDisposal;
    /** 环评服务介绍说明 */
    @Lob
    private String introductionToELAServices;
    /** 住房介绍说明 */
    @Lob
    private String introductionToHousing;
    /** 信息化建设介绍说明 */
    @Lob
    private String introductionToConstruction;
    /** 公共技术平台介绍 */
    @Lob
    private String introductionToPublicTechnology;
    //载体名
    private String brandName;
    //载体联系人
    private String contactsName;
    //联系人电话
    private String contactsPhone;
    //载体的地址
    private  String incubationBaseAddress;
}
