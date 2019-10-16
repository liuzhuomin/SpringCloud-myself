package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.ProblemTriggerInner;
import xinrui.cloud.domain.TreeEntity;
import xinrui.cloud.dto.TreeDto;

import java.util.List;

@ApiModel("触发结构内部传输对象")
public class ProblemTriggerInnerDto extends TreeEntity<ProblemTriggerInnerDto> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id公式", position = 5)
    private String logicOne;

    @ApiModelProperty(value = "name公式", position = 6)
    private String logicTwo;

    public String getLogicOne() {
        return logicOne;
    }

    public void setLogicOne(String logicOne) {
        this.logicOne = logicOne;
    }

    public String getLogicTwo() {
        return logicTwo;
    }

    public void setLogicTwo(String logicTwo) {
        this.logicTwo = logicTwo;
    }

    public static List<ProblemTriggerInnerDto> copyList(List<ProblemTriggerInner> child) {
        return TreeDto.copyList(ProblemTriggerInnerDto.class, child, 2);
    }

}
