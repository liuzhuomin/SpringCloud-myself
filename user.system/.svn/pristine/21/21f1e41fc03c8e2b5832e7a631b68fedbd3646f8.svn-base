package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

@ApiModel("问题限制必须数据项")
public class ProblemLimitDataDto {

    @ApiModelProperty(value = "企业待填入的信息", position = 0)
    private ProblemLimitInnerDto variables;

    @ApiModelProperty(value = "企业基本信息", position = 1)
    private List<Map<String, Object>> baseInfos;

    public ProblemLimitDataDto() {
    }

    public ProblemLimitDataDto(ProblemLimitInnerDto variables, List<Map<String, Object>> baseInfos) {
        super();
        this.variables = variables;
        this.baseInfos = baseInfos;
    }

    public ProblemLimitInnerDto getVariables() {
        return variables;
    }

    public void setVariables(ProblemLimitInnerDto variables) {
        this.variables = variables;
    }

    public List<Map<String, Object>> getBaseInfos() {
        return baseInfos;
    }

    public void setBaseInfos(List<Map<String, Object>> baseInfos) {
        this.baseInfos = baseInfos;
    }

}
