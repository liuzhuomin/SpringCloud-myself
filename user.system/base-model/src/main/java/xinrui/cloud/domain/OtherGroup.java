package xinrui.cloud.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * 企业
 *
 * @author Administrator
 */
@Entity
@DiscriminatorValue(GroupType.OTHER)
@XmlRootElement
@Table(name = "tu_company")
public class OtherGroup extends Group {

    private static final long serialVersionUID = 5767917979261288338L;

    /**
     * 咨询人
     */
    @Column(name = "cs_person")
    private String csPerson;

    /**
     * 标识
     */
    private String tag;

    /**
     * 是否是系统导入:系统导入1
     */
    private Integer isImport;

    /**
     * 等级
     */
    private String grade;

    /**
     * 企业唯一信用代码
     */
    private String xydm;

    /***
     * 结构调整的新增字段
     */

    /**
     * 收件人
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reciver_user_id")
    private User reciverUser;

    /**
     * 英文名称
     */
    @Column(name = "eng_name", length = 32)
    private String engName;

    /**
     * 企业经济性质(公有经济、国有经济等)
     */
    @Column(name = "ecm_type", length = 32)
    private String ecmType;

    /**
     * 企业规模--L--Large大;M--Medium中;S--Small小
     */
    @Column(name = "group_scale", length = 10)
    private String groupScale;

    /**
     * 注册类型
     */
    @Column(name = "regist_type", length = 10)
    private String registType;

    /**
     * 实收资本(万元)
     */
    @Column(name = "regist_famt", length = 20)
    private String registFamt;

    /**
     * 注册时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registDate;

    /**
     * 办公地址
     */
    @Column(name = "office_addr", length = 128)
    private String officeAddr;

    /**
     * 信用评级
     */
    @Column(name = "credit_level", length = 10)
    private String creditLevel;

    /**
     * 税务登记证号(国税)
     */
    @Column(name = "scot_ctrynbr", length = 128)
    private String scotCtrynbr;

    /**
     * 税务登记证号(地税)
     */
    @Column(name = "scot_placenb", length = 128)
    private String scotPlacenb;

    /**
     * 国籍(预留字段)
     */
    @Column(name = "country_code", length = 10)
    private String countryCode;

    /**
     * 所在省份(冗余字段)
     */
    @Column(name = "province", length = 10)
    private String province;

    /**
     * 城市代码(冗余字段)
     */
    @Column(name = "city_code", length = 10)
    private String cityCode;

    /**
     * 经济区域
     */
    @Column(name = "emregion_id", length = 128)
    private String emregion_id;

    /**
     * 是否优质客户(是否优质客户--1--是;0--否)
     */
    @Column(name = "hight_level", length = 10)
    private String hightLevel;

    /**
     * 是否是黑名单--1--是;0--否
     */
    @Column(name = "blacklist_flag", length = 10)
    private String blacklistFlag;

    /**
     * 上市标志--1--是;0--否
     */
    @Column(name = "listed_tag", length = 10)
    private String listedTag;

    /**
     * 更新时间
     */
    @Temporal(TemporalType.TIMESTAMP)
   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date changeDate;

    /**
     * 点击率
     */
    @Column(name = "click_count", length = 10)
    private Integer clickCount = 0;

    /**
     * 企业评分
     */
    @Column(name = "score", length = 10)
    private Integer score = 0;

    /**
     * 战略性新兴产业 0：不是  1：是
     */
    private Integer isEmerging = 0;

    /**
     * 重点调度企业 0：不是  1：是
     */
    private Integer isImportant = 0;

    /** 修改pdf的url *//*
	private String update_pdf;*/


    /**
     * 公司简称
     */
    private String companyIntro;

    /**
     * 关注的领导
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private User fucosLeader;


    public Integer getIsEmerging() {
        return isEmerging;
    }

    public String getCompanyIntro() {
        return companyIntro;
    }

    public void setCompanyIntro(String companyIntro) {
        this.companyIntro = companyIntro;
    }

    public void setIsEmerging(Integer isEmerging) {
        this.isEmerging = isEmerging;
    }

    public Integer getIsImportant() {
        return isImportant;
    }

    public void setIsImportant(Integer isImportant) {
        this.isImportant = isImportant;
    }

    public OtherGroup() {
    }

    public String getType() {
        return GroupType.OTHER;
    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCsPerson() {
        return csPerson;
    }

    public void setCsPerson(String csPerson) {
        this.csPerson = csPerson;
    }

    public Integer getIsImport() {
        return isImport;
    }

    public void setIsImport(Integer isImport) {
        this.isImport = isImport;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public String getEcmType() {
        return ecmType;
    }

    public void setEcmType(String ecmType) {
        this.ecmType = ecmType;
    }

    public String getGroupScale() {
        return groupScale;
    }

    public void setGroupScale(String groupScale) {
        this.groupScale = groupScale;
    }

    public String getRegistType() {
        return registType;
    }

    public void setRegistType(String registType) {
        this.registType = registType;
    }

    public String getRegistFamt() {
        return registFamt;
    }

    public void setRegistFamt(String registFamt) {
        this.registFamt = registFamt;
    }

    public Date getRegistDate() {
        return registDate;
    }

    public void setRegistDate(Date registDate) {
        this.registDate = registDate;
    }

    public String getOfficeAddr() {
        return officeAddr;
    }

    public void setOfficeAddr(String officeAddr) {
        this.officeAddr = officeAddr;
    }

    public String getCreditLevel() {
        return creditLevel;
    }

    public void setCreditLevel(String creditLevel) {
        this.creditLevel = creditLevel;
    }

    public String getScotCtrynbr() {
        return scotCtrynbr;
    }

    public void setScotCtrynbr(String scotCtrynbr) {
        this.scotCtrynbr = scotCtrynbr;
    }

    public String getScotPlacenb() {
        return scotPlacenb;
    }

    public void setScotPlacenb(String scotPlacenb) {
        this.scotPlacenb = scotPlacenb;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getEmregion_id() {
        return emregion_id;
    }

    public void setEmregion_id(String emregion_id) {
        this.emregion_id = emregion_id;
    }

    public String getHightLevel() {
        return hightLevel;
    }

    public void setHightLevel(String hightLevel) {
        this.hightLevel = hightLevel;
    }

    public String getBlacklistFlag() {
        return blacklistFlag;
    }

    public void setBlacklistFlag(String blacklistFlag) {
        this.blacklistFlag = blacklistFlag;
    }

    public String getListedTag() {
        return listedTag;
    }

    public void setListedTag(String listedTag) {
        this.listedTag = listedTag;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public User getReciverUser() {
        return reciverUser;
    }

    public void setReciverUser(User reciverUser) {
        this.reciverUser = reciverUser;
    }

    public Integer getClickCount() {
        return clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public User getFucosLeader() {
        return fucosLeader;
    }

    public void setFucosLeader(User fucosLeader) {
        this.fucosLeader = fucosLeader;
    }


    public String getXydm() {
        return xydm;
    }

    public void setXydm(String xydm) {
        this.xydm = xydm;
    }

    @Override
    public String getGroupType() {
        return GroupType.OTHER;
    }
}
