package xinrui.cloud.domain.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.ProblemStatusFather;
import xinrui.cloud.domain.ProblemTrigger;
import xinrui.cloud.dto.TreeDto;
import org.springframework.beans.BeanUtils;

import java.util.List;

@ApiModel("触发结构数据传输对象")
public class ProblemTriggerDto extends ProblemStatusFather {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "所有的选项", position = 5)
    private List<ProblemTriggerInnerDto> child = Lists.newArrayList();

    @ApiModelProperty(value = "存放id和name的键值对", position = 6)
    private List<ProblemLimitInnerDto> variables = Lists.newArrayList();

    @ApiModelProperty(value = "存放触发结果修饰的对象", position = 9)
    private ProblemTriggerResultDto triggerResult;

    @ApiModelProperty(value = "最终生成的name公式", position = 7)
    private String finalLogic;

    @ApiModelProperty(value = "name公式", position = 8)
    private String logic;

    public List<ProblemTriggerInnerDto> getChild() {
        return child;
    }

    public void setChild(List<ProblemTriggerInnerDto> child) {
        this.child = child;
    }

    public String getFinalLogic() {
        return finalLogic;
    }

    public void setFinalLogic(String finalLogic) {
        this.finalLogic = finalLogic;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public ProblemTriggerResultDto getTriggerResult() {
        return triggerResult;
    }

    public void setTriggerResult(ProblemTriggerResultDto triggerResult) {
        this.triggerResult = triggerResult;
    }

    public static ProblemTriggerDto copy(ProblemTrigger trigger) {
        if (trigger == null) {
            return null;
        }
        ProblemTriggerDto bean = new ProblemTriggerDto();
        BeanUtils.copyProperties(trigger, bean, "child", "variables", "triggerResult");
        bean.setChild(ProblemTriggerInnerDto.copyList(trigger.getChild()));
        bean.setVariables(TreeDto.copyList(ProblemLimitInnerDto.class, trigger.getVariables(), 2));
        bean.setTriggerResult(ProblemTriggerResultDto.copy(trigger.getTriggerResult()));
        return bean;
    }

    public List<ProblemLimitInnerDto> getVariables() {
        return variables;
    }

    public void setVariables(List<ProblemLimitInnerDto> variables) {
        this.variables = variables;
    }

}
