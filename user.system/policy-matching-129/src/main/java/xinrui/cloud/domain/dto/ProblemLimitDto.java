package xinrui.cloud.domain.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.Problem;
import xinrui.cloud.domain.ProblemLimit;
import xinrui.cloud.domain.TreeEntity;
import xinrui.cloud.dto.CopySpecailField;
import xinrui.cloud.dto.TreeDto;
import org.springframework.util.CollectionUtils;

import java.util.List;

@ApiModel("公式对象")
public class ProblemLimitDto extends TreeEntity<ProblemLimitDto> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "左边的公式", position = 1)
    private String logicalFormLeft;

    @ApiModelProperty(value = "右边的公式", position = 2)
    private String logicalFormRight;

    @ApiModelProperty(value = "逻辑运算符  and 或者 or", position = 3)
    private String logic;

    @ApiModelProperty(value = "链接符号 +-*/", position = 4)
    private String connector;

    @ApiModelProperty(value = "左边id公式", position = 6)
    private String logicalFormIdLeft;

    @ApiModelProperty(value = "右边id公式", position = 5)
    private String logicalFormIdRight;

    @ApiModelProperty(value = "问题id", position = 7)
    private Long problemId;

    @ApiModelProperty(value = "变量对象", position = 8)
    private ProblemLimitInnerDto variables;

    @ApiModelProperty(value = "触发结构对象", position = 9)
    private ProblemTriggerDto trigger;

    public String getLogicalFormLeft() {
        return logicalFormLeft;
    }

    public void setLogicalFormLeft(String logicalFormLeft) {
        this.logicalFormLeft = logicalFormLeft;
    }

    public String getLogicalFormRight() {
        return logicalFormRight;
    }

    public void setLogicalFormRight(String logicalFormRight) {
        this.logicalFormRight = logicalFormRight;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public String getConnector() {
        return connector;
    }

    public void setConnector(String connector) {
        this.connector = connector;
    }

    public Long getProblemId() {
        return problemId;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    public String getLogicalFormIdLeft() {
        return logicalFormIdLeft;
    }

    public void setLogicalFormIdLeft(String logicalFormIdLeft) {
        this.logicalFormIdLeft = logicalFormIdLeft;
    }

    public String getLogicalFormIdRight() {
        return logicalFormIdRight;
    }

    public void setLogicalFormIdRight(String logicalFormIdRight) {
        this.logicalFormIdRight = logicalFormIdRight;
    }

    public ProblemLimitInnerDto getVariables() {
        return variables;
    }

    public void setVariables(ProblemLimitInnerDto variables) {
        this.variables = variables;
    }

    public static List<ProblemLimitDto> copy(List<ProblemLimit> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<ProblemLimitDto> result = Lists.newArrayList();
        for (ProblemLimit limit : list) {
            result.add(copy(limit));
        }
        return result;
    }

    public static ProblemLimitDto copy(ProblemLimit limit) {
        if (limit == null) {
            return null;
        }
        ProblemLimitDto copy = TreeDto.copy(ProblemLimitDto.class, limit, 1, new CopySpecailField() {

            @Override
            public <E extends TreeEntity<E>, T> void copy(T t, TreeEntity<E> e) {
                ProblemLimitDto dto = (ProblemLimitDto) t;
                ProblemLimit source = (ProblemLimit) e;
                Problem problem = source.getProblem();
                if (problem != null) {
                    dto.setProblemId(problem.getId());
                }
                dto.setVariables(ProblemLimitInnerDto.copy(source.getVariables()));
                dto.setTrigger(ProblemTriggerDto.copy(source.getTrigger()));
            }
        }, "problem", "problemId", "variables", "trigger");
        return copy;
    }

    public ProblemTriggerDto getTrigger() {
        return trigger;
    }

    public void setTrigger(ProblemTriggerDto trigger) {
        this.trigger = trigger;
    }

}
