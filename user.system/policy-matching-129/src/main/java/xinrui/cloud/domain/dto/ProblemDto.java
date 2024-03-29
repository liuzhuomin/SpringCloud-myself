package xinrui.cloud.domain.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.NameEntity;
import xinrui.cloud.domain.Problem;
import xinrui.cloud.domain.ProblemModel;
import xinrui.cloud.enums.ProblemStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

@ApiModel("问题内容对象")
public class ProblemDto extends NameEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "问题的标题", position = 3)
    private String title;

    @ApiModelProperty(value = "当前问题的类型单选(0),独选(1),填空(2),空(3)", position = 4)
    private int status = ProblemStatus.SINGLE_RADIO.getStatus();

    @ApiModelProperty(value = "只有单选的时候才需要这个值，标识单选的二级选项是什么问题类型,(单选(0),填空(2))", position = 5)
    private int childStatus = ProblemStatus.SINGLE_RADIO.getStatus();

    @ApiModelProperty(value = "问题编辑项的一级项", position = 6)
    private List<ProblemModelDto> child;

    @ApiModelProperty(value = "问题限制对象集合", position = 7)
    private List<ProblemLimitDto> problemLimits = Lists.newArrayList();

    @ApiModelProperty(value = "仅仅单选有效，对单选触发结果百分比计算完成之后的，一个最高限制金额", position = 8)
    private Integer max;

    @ApiModelProperty(value = "当前独选按钮是否针对上一个资助额，如果不是则是针对所有的资助额度", position = 9)
    private boolean last;

    @ApiModelProperty(value = "标识是否是幅度上涨", position = 10)
    private boolean isAmplitude = false;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setChildStatus(int childStatus) {
        this.childStatus = childStatus;
    }

    public List<ProblemLimitDto> getProblemLimits() {
        return problemLimits;
    }

    public void setProblemLimits(List<ProblemLimitDto> problemLimits) {
        this.problemLimits = problemLimits;
    }

    public List<ProblemModelDto> getChild() {
        return child;
    }

    public void setChild(List<ProblemModelDto> child) {
        this.child = child;
    }

    public int getChildStatus() {
        return childStatus;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public boolean getLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public boolean isLast() {
        return last;
    }

    public boolean isAmplitude() {
        return isAmplitude;
    }

    public void setAmplitude(boolean amplitude) {
        isAmplitude = amplitude;
    }

    public static ProblemDto copy(Problem pb, boolean... booleans) {
        if (pb == null) {
            return null;
        }
        ProblemDto dto = new ProblemDto();
        BeanUtils.copyProperties(pb, dto, "child", "problemLimits");
        if (booleans == null || booleans.length == 0 || (booleans.length == 1 && booleans[0])) {
            dto.setProblemLimits(ProblemLimitDto.copy(pb.getProblemLimits()));
        }
        List<ProblemModel> child = pb.getChild();
        dto.setChild(ProblemModelDto.copyList(child));
        return dto;
    }

    public static List<ProblemDto> copyList(List<Problem> problems, boolean... booleans) {
        List<ProblemDto> dtos = Lists.newArrayList();
        if (CollectionUtils.isEmpty(problems)) {
            return dtos;
        }
        for (Problem pb : problems) {
            dtos.add(copy(pb, booleans));
        }
        return dtos;
    }

}
