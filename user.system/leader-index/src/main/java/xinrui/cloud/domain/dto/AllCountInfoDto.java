package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <B>Title:</B>AllCountInfoDto</br>
 * <B>Description:</B> 用于企业信息展示 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/11 18:37
 */
@ApiModel("诉求和走访统计数据")
public class AllCountInfoDto {
    @ApiModelProperty(value = "诉求全部数量", position = 1)
    private int difficultAllCount;
    @ApiModelProperty(value = "办结的诉求数量", position = 2)
    private int endDifficultCount;
    @ApiModelProperty(value = "未曾办结的诉求数量", position = 3)
    private int notEndDifficultCount;
    @ApiModelProperty(value = "走访的所有数量", position = 4)
    private int visitreAllCount;
    @ApiModelProperty(value = "挂点领导的走访数量", position = 5)
    private int visitreLeaderCount;
    @ApiModelProperty(value = "非挂点领导的走访数量", position = 6)
    private int visitreOtherLeaderCount;

    /**
     * 创建一个诉求与走访信息统计的数据传输对象
     *
     * @param gdldVisitreCount        挂点领导走访次数
     * @param otherLeaderVisitreCount 非挂点领导走访次数
     * @param endDifficultCount       办结诉求个数
     * @param notEndDifficultCount    非办结的诉求个数
     */
    public AllCountInfoDto(Long gdldVisitreCount, Long otherLeaderVisitreCount, Long endDifficultCount, Long notEndDifficultCount) {
        this.visitreLeaderCount = gdldVisitreCount == null ? 0 : gdldVisitreCount.intValue();
        this.visitreOtherLeaderCount = otherLeaderVisitreCount == null ? 0 : otherLeaderVisitreCount.intValue();
        this.endDifficultCount = endDifficultCount == null ? 0 : endDifficultCount.intValue();
        this.notEndDifficultCount = notEndDifficultCount == null ? 0 : notEndDifficultCount.intValue();
        this.difficultAllCount = this.endDifficultCount + this.notEndDifficultCount;
        this.visitreAllCount = this.visitreLeaderCount + this.visitreOtherLeaderCount;
    }

    public int getDifficultAllCount() {
        return difficultAllCount;
    }

    public void setDifficultAllCount(int difficultAllCount) {
        this.difficultAllCount = difficultAllCount;
    }

    public int getEndDifficultCount() {
        return endDifficultCount;
    }

    public void setEndDifficultCount(int endDifficultCount) {
        this.endDifficultCount = endDifficultCount;
    }

    public int getNotEndDifficultCount() {
        return notEndDifficultCount;
    }

    public void setNotEndDifficultCount(int notEndDifficultCount) {
        this.notEndDifficultCount = notEndDifficultCount;
    }

    public int getVisitreAllCount() {
        return visitreAllCount;
    }

    public void setVisitreAllCount(int visitreAllCount) {
        this.visitreAllCount = visitreAllCount;
    }

    public int getVisitreLeaderCount() {
        return visitreLeaderCount;
    }

    public void setVisitreLeaderCount(int visitreLeaderCount) {
        this.visitreLeaderCount = visitreLeaderCount;
    }

    public int getVisitreOtherLeaderCount() {
        return visitreOtherLeaderCount;
    }

    public void setVisitreOtherLeaderCount(int visitreOtherLeaderCount) {
        this.visitreOtherLeaderCount = visitreOtherLeaderCount;
    }
}
