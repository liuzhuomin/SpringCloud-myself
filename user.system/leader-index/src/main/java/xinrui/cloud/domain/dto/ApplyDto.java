package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <B>Title:</B>ApplyDto</br>
 * <B>Description:</B>  </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/11 22:45
 */
@ApiModel("诉求和走访统计数据")
public class ApplyDto {
    @ApiModelProperty(value = "单位名称", position = 1)
    String companyName;
    @ApiModelProperty(value = "单位地址", position = 2)
    String address;
    @ApiModelProperty(value = "项目名称", position = 3)
    String projectName;
    @ApiModelProperty(value = "法定代表人姓名", position = 4)
    String frName;
    @ApiModelProperty(value = "所申报资助", position = 5)
    String sbzz;
    @ApiModelProperty(value = "申报总金额", position = 6)
    String sbzje;
    @ApiModelProperty(value = "法定代表人手机号码", position = 7)
    String frTelephone;
    @ApiModelProperty(value = "项目联系人", position = 8)
    String projectConcat;
    @ApiModelProperty(value = "联系电话", position = 9)
    String projectConcatPhone;
    @ApiModelProperty(value = "联系方式", position = 10)
    String projectConcatWay;
    @ApiModelProperty(value = "目前环节", position = 11)
    LinkDto currentLink=new LinkDto();
    @ApiModelProperty(value = "实际发放", position = 12)
    String realyMoney;

    public ApplyDto() {
    }

    public ApplyDto(String companyName, String address, String projectName, String frName, String sbzz, String sbzje, String frTelephone, String projectConcat, String projectConcatWay) {
        this.companyName = companyName;
        this.address = address;
        this.projectName = projectName;
        this.frName = frName;
        this.sbzz = sbzz;
        this.sbzje = sbzje;
        this.frTelephone = frTelephone;
        this.projectConcat = projectConcat;
        this.projectConcatWay = projectConcatWay;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getFrName() {
        return frName;
    }

    public void setFrName(String frName) {
        this.frName = frName;
    }

    public String getSbzz() {
        return sbzz;
    }

    public void setSbzz(String sbzz) {
        this.sbzz = sbzz;
    }

    public String getSbzje() {
        return sbzje;
    }

    public void setSbzje(String sbzje) {
        this.sbzje = sbzje;
    }

    public String getFrTelephone() {
        return frTelephone;
    }

    public void setFrTelephone(String frTelephone) {
        this.frTelephone = frTelephone;
    }

    public String getProjectConcat() {
        return projectConcat;
    }

    public void setProjectConcat(String projectConcat) {
        this.projectConcat = projectConcat;
    }

    public String getProjectConcatWay() {
        return projectConcatWay;
    }

    public void setProjectConcatWay(String projectConcatWay) {
        this.projectConcatWay = projectConcatWay;
    }

    public LinkDto getCurrentLink() {
        return currentLink;
    }

    public void setCurrentLink(LinkDto currentLink) {
        this.currentLink = currentLink;
    }

    public String getRealyMoney() {
        return realyMoney;
    }

    public void setRealyMoney(String realyMoney) {
        this.realyMoney = realyMoney;
    }

    public String getProjectConcatPhone() {
        return projectConcatPhone;
    }

    public void setProjectConcatPhone(String projectConcatPhone) {
        this.projectConcatPhone = projectConcatPhone;
    }
}
