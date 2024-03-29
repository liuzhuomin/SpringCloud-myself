package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Wither;

import java.util.List;

@ApiModel("科技金融产品线上列表对象")
@Getter
@Setter
public class TechnologyFinancialOnlineDto {

    @ApiModelProperty(value = "信用贷的", position = 1)
    private List<TechnologyFinancialOnlineSimpleDto> creditLoanList;
    @ApiModelProperty(value = "抵押贷的", position = 2)
    private List<TechnologyFinancialOnlineSimpleDto> mortgagesList;

    public TechnologyFinancialOnlineDto() { }

    /**
     * @param creditLoanList    信用贷
     * @param mortgagesList     抵押贷
     */
    public TechnologyFinancialOnlineDto(List<TechnologyFinancialOnlineSimpleDto> creditLoanList, List<TechnologyFinancialOnlineSimpleDto> mortgagesList) {
        this.creditLoanList = creditLoanList;
        this.mortgagesList = mortgagesList;
    }

}
