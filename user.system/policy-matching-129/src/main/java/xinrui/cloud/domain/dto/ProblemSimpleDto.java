package xinrui.cloud.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.NameEntity;
import xinrui.cloud.domain.Problem;
import xinrui.cloud.enums.ProblemStatus;
import org.springframework.beans.BeanUtils;

import javax.persistence.Transient;
import java.util.List;

/**
 * <B>Title:</B>ProblemSimpleDto</br>
 * <B>Description:</B>  </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/4/11 15:49
 */
@ApiModel("简单题目对象")
public class ProblemSimpleDto extends NameEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "问题的标题", position = 3)
    private String title;

    @ApiModelProperty(value = "当前问题的类型单选(0),独选(1),填空(2),空(3)", position = 4)
    private int status = ProblemStatus.SINGLE_RADIO.getStatus();

    @ApiModelProperty(value = "只有单选的时候才需要这个值，标识单选的二级选项是什么问题类型,(单选(0),填空(2))", position = 5)
    private int childStatus = ProblemStatus.SINGLE_RADIO.getStatus();

    @ApiModelProperty(value = "问题编辑项的一级项", position = 6)
    private List<ProblemModelDto> child;

    @ApiModelProperty(value = "待填入的值", position = 7)
    private String value;

    @ApiModelProperty(value = "单选+单选时候，选中的二级的id", position = 7)
    private String chooseChildId;


    /**
     * 无任何实际作用，仅仅为了传递程序中的标识变量
     */
    @Transient
    @JsonIgnore
    boolean canContinue = false;

    /**
     * 无任何实际作用，仅仅为了传输程序中的变量
     */
    @Transient
    @JsonIgnore
    private String index;


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

    public List<ProblemModelDto> getChild() {
        return child;
    }

    public void setChild(List<ProblemModelDto> child) {
        this.child = child;
    }

    public int getChildStatus() {
        return childStatus;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getChooseChildId() {
        return chooseChildId;
    }

    public void setChooseChildId(String chooseChildId) {
        this.chooseChildId = chooseChildId;
    }

    public boolean isCanContinue() {
        return canContinue;
    }

    public void setCanContinue(boolean canContinue) {
        this.canContinue = canContinue;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public static ProblemSimpleDto copy(Problem pb, boolean... booleans) {
        if (pb == null) {
            return null;
        }
        ProblemSimpleDto dto = new ProblemSimpleDto();
        BeanUtils.copyProperties(pb, dto, "child");
        dto.setChild(ProblemModelDto.copyList(pb.getChild()));
        return dto;
    }

    public static List<ProblemSimpleDto> copyList(List<Problem> problems, boolean... booleans) {
        List<ProblemSimpleDto> dtos = Lists.newArrayList();
        for (Problem pb : problems) {
            dtos.add(copy(pb, booleans));
        }
        return dtos;
    }

}
