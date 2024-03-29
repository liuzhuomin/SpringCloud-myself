package xinrui.cloud.domain.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.TechnologyLoanDate;
import xinrui.cloud.domain.TechnologyLoanType;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("贷款类别数据传输对象")
@Getter
@Setter
public class TechnologyLoanTypeDto extends IdEntity {

    @ApiModelProperty(value = "贷款类别的文本值", position = 1)
    private String type;

    public TechnologyLoanTypeDto() {
    }

    public TechnologyLoanTypeDto(Long id, String type) {
        this.id = id;
        this.type = type;
    }

    public static List<TechnologyLoanTypeDto> copy(List<TechnologyLoanType> source) {
        List<TechnologyLoanTypeDto> results = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(source)) {
            for (TechnologyLoanType t : source) {
                results.add(copy(t));
            }
        }
        return results;
    }

    public static TechnologyLoanTypeDto copy(TechnologyLoanType source) {
        return new TechnologyLoanTypeDto(source.getId(), source.getType());
    }

    public static  StringBuilder getStringBuilder(List<TechnologyLoanType> technologyLoanTypes) {
        StringBuilder builder = new StringBuilder();
        for (int i=0;i<technologyLoanTypes.size();i++) {
            TechnologyLoanType type=technologyLoanTypes.get(i);
            builder.append(type.getType());
            if(i!=technologyLoanTypes.size()-1){
                builder.append("/");
            }
        }
        return builder;
    }
}
