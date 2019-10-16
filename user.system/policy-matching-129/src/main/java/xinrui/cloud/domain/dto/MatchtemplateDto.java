package xinrui.cloud.domain.dto;

import java.util.List;

import com.google.common.collect.Lists;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.IdEntity;

@ApiModel("政策匹配选项结果")
public class MatchtemplateDto extends IdEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "政策组对象", position = 1)
    private List<PolicyGroupDto> groups = Lists.newArrayList();

    @ApiModelProperty(value = "标识是否是全空", position = 2)
    private boolean allEmpty;

    public MatchtemplateDto(List<PolicyGroupDto> parseArray) {
        this.groups = parseArray;
    }

    public MatchtemplateDto() {
    }

    public List<PolicyGroupDto> getGroups() {
        return groups;
    }

    public void setGroups(List<PolicyGroupDto> groups) {
        this.groups = groups;
    }

    public boolean isAllEmpty() {
        return allEmpty;
    }

    public void setAllEmpty(boolean allEmpty) {
        this.allEmpty = allEmpty;
    }

    //	@ApiModelProperty(value = "通过选择政策获取到的所有需要的类目", position = 1)
//	private List<PolicyTemplateSimpleDto> temps = Lists.newArrayList();
//
//	public List<PolicyTemplateSimpleDto> getTemps() {
//		return temps;
//	}
//
//	public void setTemps(List<PolicyTemplateSimpleDto> temps) {
//		this.temps = temps;
//	}

}
