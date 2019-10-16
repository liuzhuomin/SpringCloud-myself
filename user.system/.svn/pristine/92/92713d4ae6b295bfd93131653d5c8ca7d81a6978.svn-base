package xinrui.cloud.domain.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.TechnologyApplyCondition;

import java.util.List;

@ApiModel("申请条件数据传输对象")
@Data
public class TechnologyApplyConditionDto extends IdEntity {

    @ApiModelProperty(value = "申请条件", position = 1)
    private String conditions;

    public static List<TechnologyApplyCondition> toBean(List<TechnologyApplyConditionDto> technologyApplyConditions) {
        if(!CollectionUtils.isEmpty(technologyApplyConditions)){
            List<TechnologyApplyCondition>  result = Lists.newArrayList();
            for (TechnologyApplyConditionDto dto:technologyApplyConditions){
                result.add(new TechnologyApplyCondition(dto.getConditions()));
            }
            return result;
        }
        return null;
    }
}
