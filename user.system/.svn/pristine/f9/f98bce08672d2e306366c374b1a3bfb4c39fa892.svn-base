package xinrui.cloud.domain.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.TechnologyLoanAmount;
import xinrui.cloud.domain.TechnologyLoanType;

import javax.persistence.Entity;
import java.util.List;


@ApiModel("贷款额度数据传输对象")
@Getter
@Setter
public class TechnologyLoanAmountDto extends IdEntity {

    @ApiModelProperty(value = "贷款额度的文本值", position = 1)
    private String fullAmount;

    public TechnologyLoanAmountDto() {
    }

    public TechnologyLoanAmountDto(Long id, String fullAmount) {
        this.id = id;
        this.fullAmount = fullAmount;
    }

    public static List<TechnologyLoanAmountDto> copy(List<TechnologyLoanAmount> source) {
        List<TechnologyLoanAmountDto> results = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(source)) {
            for (TechnologyLoanAmount t : source) {
                results.add(copy(t));
            }
        }
        return results;
    }

    public static TechnologyLoanAmountDto copy(TechnologyLoanAmount source) {
        return new TechnologyLoanAmountDto(source.getId(), source.getFullAmount());
    }
}
