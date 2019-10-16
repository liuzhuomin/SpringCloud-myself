package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import xinrui.cloud.common.utils.BeanUtilsEnhance;
import xinrui.cloud.common.utils.CopyListFilter;
import xinrui.cloud.domain.TechnologyFinancial;

import java.util.List;

@Data
@ApiModel("金融产品简单数据对象（审核时）")
public class TechnologyFinancialApplyingSimpleDto extends  TechnologyFinancialSimpleDto{
    @ApiModelProperty(value = "为true代表未曾阅读，否则就是已经阅读", position = 4)
    private boolean refuseReason;

    public static List<TechnologyFinancialApplyingSimpleDto> copyApplyings(List<TechnologyFinancial> listBydCriteria) {
        return BeanUtilsEnhance.copyList(listBydCriteria, TechnologyFinancialApplyingSimpleDto.class, new CopyListFilter<TechnologyFinancialApplyingSimpleDto, TechnologyFinancial>() {
            @Override
            public TechnologyFinancialApplyingSimpleDto copy(TechnologyFinancial source, TechnologyFinancialApplyingSimpleDto target) {
                BeanUtilsEnhance.copyPropertiesEnhance(source,target);
                return target;
            }
        });
    }

}