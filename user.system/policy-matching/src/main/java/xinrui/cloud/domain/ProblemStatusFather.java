package xinrui.cloud.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.IdEntity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@ApiModel("一级二级父结构")
public class ProblemStatusFather extends IdEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 当前一级选项的状态
     */
    @ApiModelProperty(value = "当前的状态", position = 1)
    protected int status;

    /**
     * 当前二级选项的状态
     */
    @ApiModelProperty(value = "子选项的状态", position = 2)
    @Column(name = "childStatus")
    protected int childStatus;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getChildStatus() {
        return childStatus;
    }

    public void setChildStatus(int childStatus) {
        this.childStatus = childStatus;
    }

}
