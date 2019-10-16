/**
  * Copyright 2019 bejson.com 
  */
package xinrui.cloud.domain.vto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import xinrui.cloud.domain.TechnologyApplyCondition;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("申请条件数据传输对象（接收）")
public class TechnologyApplyConditionsVto implements Serializable {

    @ApiModelProperty(value = "填入的申请条件", position = 1)
    private String conditions;

    @NotNull
    public static List<TechnologyApplyCondition> toBean(List<TechnologyApplyConditionsVto> technologyApplyConditions) {
        List<TechnologyApplyCondition>  result = Lists.newArrayList();
        if(!CollectionUtils.isEmpty(technologyApplyConditions)){
            for (TechnologyApplyConditionsVto dto:technologyApplyConditions){
                result.add(new TechnologyApplyCondition(dto.getConditions()));
            }
            return result;
        }
        return result;
    }
}