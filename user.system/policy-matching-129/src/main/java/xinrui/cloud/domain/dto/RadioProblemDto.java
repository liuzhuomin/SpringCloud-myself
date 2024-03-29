package xinrui.cloud.domain.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.BootException;
import xinrui.cloud.domain.NameEntity;
import xinrui.cloud.enums.ProblemStatus;

import java.util.List;

@ApiModel("单选类型的对象数据")
@Deprecated
public class RadioProblemDto extends NameEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("表示子选项的类型是单选(0)还是填空(2)")
    private int status = ProblemStatus.SINGLE_RADIO.getStatus();

    @ApiModelProperty("二级选项")
    private List<RadioProblemDto> seconds = Lists.newArrayList();

    public List<RadioProblemDto> getSeconds() {
        return seconds;
    }

    public void setSeconds(List<RadioProblemDto> seconds) {
        this.seconds = seconds;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(ProblemStatus ps) {
        int pbStatus = ps.getStatus();
        if (pbStatus != 0 && pbStatus != 2) {
            throw new BootException("单选问题的子选项必须是(单选(1)或者填空(2)!");
        }
        this.status = ps.getStatus();
    }

}
