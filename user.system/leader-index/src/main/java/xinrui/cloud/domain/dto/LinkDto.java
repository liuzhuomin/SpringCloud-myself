package xinrui.cloud.domain.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <B>Title:</B>LinkDto</br>
 * <B>Description:</B>  </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/12 16:23
 */
@ApiModel("环节数据传输对象")
public class LinkDto {
    @ApiModelProperty(value = "当前环节的描述", position = 1)
    private String applyStatus;//转态
    @ApiModelProperty(value = "所申报政策", position = 2)
    private String policyShortTitle; //所申报政策
    @ApiModelProperty(value = "申请创建时间，时间戳毫秒值", position = 3)
     private long applyAt;//申请创建时间，时间戳毫秒值
    @ApiModelProperty(value = "最后更新时间", position = 4)
    private long lastUpdateAt;
    @ApiModelProperty(value = "最后更新人", position = 5)
    private String lastUpdateUser;
    @ApiModelProperty(value = "最后操作原因", position = 6)
    private String reason;
    @ApiModelProperty(value = "驳回列表", position = 7)
    private List<RejectDto> rejectList= Lists.newArrayList();

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getPolicyShortTitle() {
        return policyShortTitle;
    }

    public void setPolicyShortTitle(String policyShortTitle) {
        this.policyShortTitle = policyShortTitle;
    }

    public long getApplyAt() {
        return applyAt;
    }

    public void setApplyAt(long applyAt) {
        this.applyAt = applyAt;
    }

    public long getLastUpdateAt() {
        return lastUpdateAt;
    }

    public void setLastUpdateAt(long lastUpdateAt) {
        this.lastUpdateAt = lastUpdateAt;
    }

    public List<RejectDto> getRejectList() {
        return rejectList;
    }

    public void setRejectList(List<RejectDto> rejectList) {
        this.rejectList = rejectList;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
