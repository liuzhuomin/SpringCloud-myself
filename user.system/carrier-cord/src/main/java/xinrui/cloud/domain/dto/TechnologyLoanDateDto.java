package xinrui.cloud.domain.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.CollectionUtils;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.TechnologyLoanDate;

import java.util.ArrayList;
import java.util.List;

@ApiModel("贷款期限数据传输对象")
public class TechnologyLoanDateDto extends IdEntity {

    @ApiModelProperty(value = "贷款期限的文本值", position = 1)
    private String dateText;

    public TechnologyLoanDateDto(String dateText) {
        this.dateText = dateText;
    }

    public TechnologyLoanDateDto() {
    }

    public static List<TechnologyLoanDateDto> copy(List<TechnologyLoanDate> source) {
        List<TechnologyLoanDateDto> results = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(source)) {
            for (TechnologyLoanDate t : source) {
                results.add(copy(t));
            }
        }
        return results;
    }

    public TechnologyLoanDateDto(Long id, String fullDate) {
        this.id = id;
        this.dateText = fullDate;
    }

    public static TechnologyLoanDateDto copy(TechnologyLoanDate source) {
        return new TechnologyLoanDateDto(source.getId(), source.getFullDate());
    }

    public String getDateText() {
        return dateText;
    }

    public void setDateText(String dateText) {
        this.dateText = dateText;
    }
}
