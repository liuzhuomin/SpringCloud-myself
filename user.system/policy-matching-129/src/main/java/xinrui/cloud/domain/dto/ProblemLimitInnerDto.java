package xinrui.cloud.domain.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.ProblemLimitInner;
import xinrui.cloud.domain.TreeEntity;
import xinrui.cloud.dto.TreeDto;

@Api("用于问题限制的参数出传递")
public class ProblemLimitInnerDto extends TreeEntity<ProblemLimitInnerDto> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("当前问题的变量的id")
    private Long variableId;

    public ProblemLimitInnerDto() {
    }

    public ProblemLimitInnerDto(Long variableId, String name) {
        super(name);
        this.variableId = variableId;
    }

    public Long getVariableId() {
        return variableId;
    }

    public void setVariableId(Long variableId) {
        this.variableId = variableId;
    }

    public static ProblemLimitInnerDto copy(ProblemLimitInner variables) {
        return TreeDto.copy(ProblemLimitInnerDto.class, variables);
    }

}