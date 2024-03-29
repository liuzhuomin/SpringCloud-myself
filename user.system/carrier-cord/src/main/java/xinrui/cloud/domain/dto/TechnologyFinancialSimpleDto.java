package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import xinrui.cloud.common.utils.BeanUtilsEnhance;
import xinrui.cloud.common.utils.CopyListFilter;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.TechnologyFinancial;

import java.util.List;

/**
 * @author liuliuliu
 * @version 1.0
 *  2019/8/19 10:41
 */
@Data
@ApiModel("金融产品简单数据对象")
public class TechnologyFinancialSimpleDto extends IdEntity {
    @ApiModelProperty(value = "展示的图片", position = 1)
    protected String viewIndexImage;
    @ApiModelProperty(value = "科技金融产品名称", position = 2)
    protected String name;
    @ApiModelProperty(value = "产品标语", position = 3)
    protected String slogan;

    public static List<TechnologyFinancialSimpleDto> copy(List<TechnologyFinancial> listBydCriteria) {
        return BeanUtilsEnhance.copyList(listBydCriteria, TechnologyFinancialSimpleDto.class, new CopyListFilter<TechnologyFinancialSimpleDto, TechnologyFinancial>() {
            @Override
            public TechnologyFinancialSimpleDto copy(TechnologyFinancial source, TechnologyFinancialSimpleDto target) {
                BeanUtilsEnhance.copyPropertiesEnhance(source, target);
                return target;
            }
        });
    }

}


