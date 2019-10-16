package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <B>Title:</B>CompanyInfoDto</br>
 * <B>Description:</B>  </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/11 20:42
 */
@ApiModel("企业简略信息对象")
public class CompanyInfoDto {

    @ApiModelProperty(value = "产值", position = 1)
    private String allPut;

    @ApiModelProperty(value = "产值变动", position = 2)
    private String changePut;

    @ApiModelProperty(value = "出口", position = 3)
    private String exportPut;

    @ApiModelProperty(value = "进口", position = 4)
    private String importPut;

    @ApiModelProperty(value = "资产", position = 5)
    private String zc;

    @ApiModelProperty(value = "负债", position = 6)
    private String fz;

    @ApiModelProperty(value = "税收", position = 7)
    private String ss;

    @ApiModelProperty(value = "员工数", position = 8)
    private String personCount;

    public String getAllPut() {
        return allPut;
    }

    public void setAllPut(String allPut) {
        this.allPut = allPut;
    }

    public String getChangePut() {
        return changePut;
    }

    public void setChangePut(String changePut) {
        this.changePut = changePut;
    }

    public String getExportPut() {
        return exportPut;
    }

    public void setExportPut(String exportPut) {
        this.exportPut = exportPut;
    }

    public String getImportPut() {
        return importPut;
    }

    public void setImportPut(String importPut) {
        this.importPut = importPut;
    }

    public String getZc() {
        return zc;
    }

    public void setZc(String zc) {
        this.zc = zc;
    }

    public String getFz() {
        return fz;
    }

    public void setFz(String fz) {
        this.fz = fz;
    }

    public String getSs() {
        return ss;
    }

    public void setSs(String ss) {
        this.ss = ss;
    }

    public String getPersonCount() {
        return personCount;
    }

    public void setPersonCount(String personCount) {
        this.personCount = personCount;
    }
}
