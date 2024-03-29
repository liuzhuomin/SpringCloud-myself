package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Date;

/**
 * <B>Title:</B>RejectDto</br>
 * <B>Description:</B>  </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/12 16:27
 */
@ApiModel("驳回数据传输对象")
public class RejectDto {
    @ApiModelProperty(value = "退回操作人", position = 1)
    private String operator; //退回操作人
    @ApiModelProperty(value = "退回时间，时间戳毫秒值", position = 2)
    private long rejectOperateAt;//退回时间，时间戳毫秒值
    @ApiModelProperty(value = "退回理由", position = 3)
    private String rejectReason;//退回理由

    public RejectDto(Date rejectOperateAt, String operator, String rejectReason) {
        this.rejectOperateAt=rejectOperateAt==null ? 0L :rejectOperateAt.getTime();
        this.operator=operator;
        this.rejectReason=rejectReason;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public long getRejectOperateAt() {
        return rejectOperateAt;
    }

    public void setRejectOperateAt(long rejectOperateAt) {
        this.rejectOperateAt = rejectOperateAt;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
}
