/**
  * Copyright 2019 bejson.com 
  */
package xinrui.cloud.domain.vto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import xinrui.cloud.domain.TechnologyProcess;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("办理流程对象（接收）")
public class TechnologyProcessesVto implements Serializable {

    @ApiModelProperty(value = "填入的办理流程", position = 1)
    private String process;

    @NotNull
    public static List<TechnologyProcess> toBean(List<TechnologyProcessesVto> technologyProcesses) {
        List<TechnologyProcess> result = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(technologyProcesses)) {
            for (TechnologyProcessesVto dto : technologyProcesses) {
                result.add(new TechnologyProcess(dto.getProcess()));
            }
            return result;
        }
        return result;
    }
}