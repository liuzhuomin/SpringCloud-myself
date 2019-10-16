package xinrui.cloud.domain.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.TechnologyProcess;

import java.util.List;

@ApiModel("办理流程对象")
@Data
public class TechnologyProcessDto extends IdEntity {

    @ApiModelProperty(value = "办理流程", position = 1)
    private String process;
}
