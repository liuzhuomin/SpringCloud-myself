package xinrui.cloud.domain;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 *  项目入驻实体
 */
@Data
@Entity
@Table(name="project_inner")
public class ProjectInner extends IdEntity {
    //企业团队名称
    private String companyGroupName;
    // 项目类型
    private String projectType;
    /**
     * 项目初始化状态为1未审核(审核中),2通过审核,3审核未通过
     */
    private Integer projectState;
    /**
     * 统一社会信用代码
     */
    private String socialCreditCode;
    // 孵化面积
     private String hatchArea;

    /**
     * 意向区域
     */
    private String areasToWhichTheyBelong;
    /**
     * 项目名称
     */
    private String projectName;
    //经营范围scope
    private String manageScope;
    /**
     * 项目需求,申请人名称
     */
    private String applicantName;

    /**
     * 申请人联系方式
     */
    private String applicantContactInfo;

    //项目的租凭价格范围
    private String leasePriceRange;

    /**
     * 办公场地面积
     */
    private String officeArea;

    /**
     * 意向载体类型
     */
    private String carrierType;

    /**
     * 研发场地面积
     */
    private String devArea;
    /**
     * 合计总面积
     */
    private String vacantArea;

    /**
     * 场地层高
     */
    private String areaHeight;
    /**
     * 用电需求
     */
    private String electricDemand;

    /**
     * 地面承重
     */
    private String groundBear;

    /**
     * 货梯需求
     */
    @Lob
    private String elevatorDemand;

    /**
     * 楼层
     */
    private String storey;
    //交通需求文本
    @Lob
    private String trafficDt;
    //排污设施需求文本
    @Lob
    private String introductionToSewageDisposal;
    //环评需求文本
    @Lob
    private String introductionToELAServices;
    //住房需求文本
    @Lob
    private String houseDt;
    //信息化建设需求文本
    @Lob
    private String introductionToConstruction;
    //公共技术平台开放需求文本
    @Lob
    private String introductionToPublicTechnology;
    //备注文本
    @Lob
    private String remarkText;
    /**
     * 关联的审核对象
     */
   /* @OneToMany(cascade = {CascadeType.ALL},mappedBy = "projectInner")
    private List<ProjectAudit> proAudits= Lists.newArrayList();*/
    /**
     * 上传项目关联的资料
     */
    @OneToMany(cascade = {CascadeType.ALL},mappedBy = "projectInner")
    private List<ProjectFile> projectFiles= Lists.newArrayList();

    /** 项目创建类型 A 是政府代创建 B是企业自建 */
    private char createType;
    /** 创建者的uid */
    private Long creatorId;
    /** 创建人的组织ID（如果是企业就是企业ID 如果是政府就是政府ID）*/
    private Long creatorGroupId;
    /** 创建新项目的时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;
    //关联载体的Id就行
//    private Long carrierId;
    //项目行业领域
    private  String industryField;
    //项目创业类型
    private  String businessType;

}
