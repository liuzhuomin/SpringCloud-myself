package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <B>Title:</B>DifficultHistoryDto</br>
 * <B>Description:</B>  </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/5 16:45
 */
@ApiModel("办理意见对象")
public class DifficultHistoryDto {

    @ApiModelProperty(value = "办理意见", position =1)
    private String message;
    @ApiModelProperty(value = "部门名称", position =2)
    private String orgName;
    @ApiModelProperty(value = "办理时间", position =3)
    private String date;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
