package xinrui.cloud.domain.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.ProblemStatusFather;
import xinrui.cloud.domain.ProblemTriggerResult;
import org.springframework.beans.BeanUtils;

import java.util.List;

@ApiModel("触发结构修饰数据传输对象")
public class ProblemTriggerResultDto extends ProblemStatusFather {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "所有的选项", position = 5)
    private List<ProblemTriggerInnerDto> child = Lists.newArrayList();

    @ApiModelProperty(value = "非单选时候的最高限制的值", position = 8)
    private String logic;

    @ApiModelProperty(value = "非单选时候的变量名称", position = 6)
    private String name;

    @ApiModelProperty(value = "待填入的值", position = 7)
    private String value;

    public List<ProblemTriggerInnerDto> getChild() {
        return child;
    }

    public void setChild(List<ProblemTriggerInnerDto> child) {
        this.child = child;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public static ProblemTriggerResultDto copy(ProblemTriggerResult merge) {
        if (merge == null) {
            return null;
        }
        ProblemTriggerResultDto dto = new ProblemTriggerResultDto();
        BeanUtils.copyProperties(merge, dto, "child");
        dto.setChild(ProblemTriggerInnerDto.copyList(merge.getChild()));
        return dto;
    }
}
