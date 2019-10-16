package xinrui.cloud.domain.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Wither;

import java.io.Serializable;
import java.util.List;

@ApiModel("科技金融产品审核列表对象")
@Getter
@Setter
public class TechnologyFinalAuditDto implements Serializable {
    @ApiModelProperty(value = "被拒绝的并且未曾阅读的产品数量", position = 1)
    private int refusedCount;
    @ApiModelProperty(value = "被拒绝的产品列表", position = 2)
    private List<TechnologyFinancialApplyingSimpleDto> refusedList;
    @ApiModelProperty(value = "审核中的产品列表", position = 3)
    private List<TechnologyFinancialApplyingSimpleDto> applayingList;

    public TechnologyFinalAuditDto() {
    }

    /**
     * 创建一个科技金融产品审核列表对象
     * @param refusedCount  被拒绝并且未曾阅读的产品数量
     * @param refusedList   被拒绝的产品列表
     * @param applayingList 审核中的产品列表
     */
    public TechnologyFinalAuditDto(int refusedCount,List<TechnologyFinancialApplyingSimpleDto> refusedList, List<TechnologyFinancialApplyingSimpleDto> applayingList) {
        this.refusedCount=refusedCount;
        this.refusedList = refusedList;
        this.applayingList = applayingList;
    }

}
