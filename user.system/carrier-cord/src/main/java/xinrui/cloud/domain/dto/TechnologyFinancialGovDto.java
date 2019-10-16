package xinrui.cloud.domain.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import xinrui.cloud.common.utils.BeanUtilsEnhance;
import xinrui.cloud.common.utils.CopyListFilter;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.TechnologyFinancial;
import xinrui.cloud.domain.TechnologyLoanType;

import java.util.List;

@ApiModel("科技金融产品审核列表对象")
@Data
public class TechnologyFinancialGovDto extends IdEntity {

    @ApiModelProperty(value = "科技金融产品名称", position = 1)
    private String name;

    @ApiModelProperty(value = "展示的图片", position = 2)
    private String viewImage;

    @ApiModelProperty(value = "科技金融类别，只可能为 信用贷/抵押贷", position = 3)
    private String category;

    @ApiModelProperty(value = "产品标语", position = 4)
    private String slogan;

    @ApiModelProperty(value = "申请发布时间", position = 5)
    private String publicDate;

    @ApiModelProperty(value = "申请结束时间", position = 6)
    private String applyEndDate;

    @ApiModelProperty(value = "最高贷款金额", position = 7)
    private String amount;

    @ApiModelProperty(value = "抵押物件(价证券/票据/股票等)", position = 8)
    private String loanTypes;

    @ApiModelProperty(value = "科技产品的状态具体类型(1,未审核，2.拒绝，3.通过审核，4.下架)", position = 9)
    private Integer status;

    @ApiModelProperty(value = "科技产品的审核被处理与否，为false是未曾处理，为true是处理了", position = 10)
    private boolean processed=true;


    public static List<TechnologyFinancialGovDto> copy(final List<TechnologyFinancial> source) {
        return BeanUtilsEnhance.copyList(source, TechnologyFinancialGovDto.class, new CopyListFilter<TechnologyFinancialGovDto, TechnologyFinancial>() {
            @Override
            public TechnologyFinancialGovDto copy(TechnologyFinancial technologyFinancial, TechnologyFinancialGovDto target) {

                BeanUtilsEnhance.copyDateFiledEnhance(technologyFinancial, target,
                        "loanTypes", "amount");
                target.setAmount(technologyFinancial.getLoanAmount().getFullAmount());
                target.setViewImage(technologyFinancial.getViewIndexImage().getFullPath());
                target.setProcessed(TechnologyFinancial.TechnologyStatus.APPLYING.value()!=technologyFinancial.getStatus().intValue());

                List<TechnologyLoanType> technologyLoanTypes = technologyFinancial.getTechnologyLoanTypes();
                StringBuilder builder = TechnologyLoanTypeDto.getStringBuilder(technologyLoanTypes);
                target.setLoanTypes(builder.toString());
                return target;
            }
        });
    }


}
