package xinrui.cloud.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.CollectionUtils;
import xinrui.cloud.domain.Group;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.Organization;
import xinrui.cloud.domain.OtherGroup;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * <B>Title:</B>OtherGroupDto</br>
 * <B>Description:</B>组织数据传输对象可为企业也可为组织对象，通过type进行判断</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * 2019/6/26 20:22
 */
@ApiModel("组织数据传输对象")
public class OtherGroupDto extends IdEntity {

    @ApiModelProperty(value = "咨询人", position = 1)
    private String csPerson;

    @ApiModelProperty(value = "标识", position = 2)
    private String tag;

    @ApiModelProperty(value = "是否是系统导入:系统导入1", position = 3)
    private Integer isImport;

    @ApiModelProperty(value = "等级", position = 4)
    private String grade;

    @ApiModelProperty(value = "收件人", position = 5)
    private UserDto reciverUser;

    @ApiModelProperty(value = "英文名称", position = 6)
    private String engName;

    @ApiModelProperty(value = "企业经济性质(公有经济、国有经济等", position = 7)
    private String ecmType;

    @ApiModelProperty(value = "企业规模--L--Large大;M--Medium中;S--Small小", position = 8)
    private String groupScale;

    @ApiModelProperty(value = "注册类型", position = 9)
    private String registType;

    @ApiModelProperty(value = "实收资本(万元)", position = 10)
    private String registFamt;

    @ApiModelProperty(value = "注册时间", position = 11)
    private String registDate;

    @ApiModelProperty(value = "办公地址", position = 12)
    private String officeAddr;

    @ApiModelProperty(value = "公司简称", position = 13)
    private String companyIntro;

    @ApiModelProperty(value = "关注的领导", position = 14)
    private UserDto fucosLeader;

    @ApiModelProperty(value = "组织类型", position = 15)
    private String groupType;

    @ApiModelProperty(value = "企业名称", position = 16)
    private String name;


    public static OtherGroupDto copy(Group uniqueGroup) {
        if (uniqueGroup == null) {
            return null;
        }
        OtherGroupDto otherGroupDto = new OtherGroupDto();
        BeanUtils.copyProperties(uniqueGroup, otherGroupDto, "fucosLeader", "reciverUser", "managedBy");
        return otherGroupDto;
    }

    /**
     * 通过{@link OtherGroup}获取企业详细情况
     *
     * @param source 需要复制的源对象
     * @return {@link OtherGroupDto}
     */
    public static OtherGroupDto copyDetailedByPojo(OtherGroup source) {
        OtherGroupDto copy = copy(source);
        if (copy == null) {
            return null;
        }
        if (source.getFucosLeader() != null) {
            copy.setFucosLeader(UserDto.copy(source.getFucosLeader()));
        }
        if (source.getReciverUser() != null) {
            copy.setReciverUser(UserDto.copy(source.getReciverUser()));
        }
        return copy;
    }

    public static <T extends Group> List<OtherGroupDto> copy(List<T> sources) {
        if (!CollectionUtils.isEmpty(sources)) {
            List<OtherGroupDto> result = Lists.newArrayList();
            for (Group group : sources) {
                result.add(copy(group));
            }
            return result;
        }
        return null;
    }

    public String getCsPerson() {
        return csPerson;
    }

    public void setCsPerson(String csPerson) {
        this.csPerson = csPerson;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public UserDto getReciverUser() {
        return reciverUser;
    }

    public void setReciverUser(UserDto reciverUser) {
        this.reciverUser = reciverUser;
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

    public String getRegistDate() {
        return registDate;
    }

    public void setRegistDate(String registDate) {
        this.registDate = registDate;
    }

    public String getOfficeAddr() {
        return officeAddr;
    }

    public void setOfficeAddr(String officeAddr) {
        this.officeAddr = officeAddr;
    }

    public String getCompanyIntro() {
        return companyIntro;
    }

    public void setCompanyIntro(String companyIntro) {
        this.companyIntro = companyIntro;
    }

    public UserDto getFucosLeader() {
        return fucosLeader;
    }

    public void setFucosLeader(UserDto fucosLeader) {
        this.fucosLeader = fucosLeader;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
