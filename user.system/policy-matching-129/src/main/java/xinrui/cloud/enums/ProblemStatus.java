package xinrui.cloud.enums;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <B>Title:</B>ProblemStatus.java</br>
 * <B>Description:</B> 状态描述 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年3月28日
 */
@ApiModel("单选(0)、独选(1),填空(2),空(3)")
public enum ProblemStatus {
    @ApiModelProperty("单选")
    SINGLE_RADIO(0, "单选"),
    @ApiModelProperty("独选")
    SINGLE(1, "独选"),
    @ApiModelProperty("填空")
    TEXT(2, "填空"),
    @ApiModelProperty("空")
    EMPTY(3, "空");

    int status;
    String description;

    ProblemStatus(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }


}
