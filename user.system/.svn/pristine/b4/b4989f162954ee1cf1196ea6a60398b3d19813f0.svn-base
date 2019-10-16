package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <B>Title:</B>MessageCountDto</br>
 * <B>Description:</B>  </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/11 22:14
 */
@ApiModel("消息通知数量统计")
public class MessageCountDto {
    @ApiModelProperty(value = "所有消息的数量", position = 6)
    private int allCount;
    @ApiModelProperty(value = "办结消息的数量", position = 6)
    private int endCount;
    @ApiModelProperty(value = "在办消息的数量", position = 6)
    private int notEndCount;

    public MessageCountDto(int allCount, int endCount, int notEndCount) {
        this.allCount=allCount;
        this.endCount=endCount;
        this.notEndCount=notEndCount;
    }

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    public int getEndCount() {
        return endCount;
    }

    public void setEndCount(int endCount) {
        this.endCount = endCount;
    }

    public int getNotEndCount() {
        return notEndCount;
    }

    public void setNotEndCount(int notEndCount) {
        this.notEndCount = notEndCount;
    }
}
