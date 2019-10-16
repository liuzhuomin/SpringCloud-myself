package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import xinrui.cloud.domain.IdEntity;

import javax.persistence.Column;

@ApiModel("科技金融常见问题")
@Data
public class TechnologyUsualProblemDto extends IdEntity {

    @ApiModelProperty(value = "问题标题", position = 1)
    private String title;

    @ApiModelProperty(value = "问题", position = 1)
    private String question;

    @ApiModelProperty(value = "答案", position = 1)
    private String answer;

}
