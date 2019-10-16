/**
 * Copyright 2019 bejson.com
 */
package xinrui.cloud.domain.vto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import xinrui.cloud.domain.TechnologyUsualProblem;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("科技金融常见问题（接收）")
public class TechnologyUsualProblemsVto implements Serializable {
    @ApiModelProperty(value = "填入的问题标题", position = 1)
    private String answer;
    @ApiModelProperty(value = "填入的问题", position = 2)
    private String question;
    @ApiModelProperty(value = "填入的答案", position = 3)
    private String title;

    public static List<TechnologyUsualProblem> toBean(List<TechnologyUsualProblemsVto> technologyUsualProblems) {
        if (!CollectionUtils.isEmpty(technologyUsualProblems)) {
            List<TechnologyUsualProblem> result = Lists.newArrayList();
            for (TechnologyUsualProblemsVto dto : technologyUsualProblems) {
                TechnologyUsualProblem technologyUsualProblem = new TechnologyUsualProblem();
                BeanUtils.copyProperties(dto, technologyUsualProblem);
                result.add(technologyUsualProblem);
            }
            return result;
        }
        return null;
    }
}