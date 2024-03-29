package xinrui.cloud.domain.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.IdEntity;

/**
 * <B>Title:</B>PolicyTemplateSimpleDto</br>
 * <B>Description:</B>简单的政策匹配模板对象，不要随意修改数据格式，因为前面的政府端功能用上了这个类 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0 2019/4/10 9:49
 */
@ApiModel("政策匹配选项(题目)对象")
public class PolicyTemplateSimpleDto extends IdEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "当前题目对应的政策对象", position = 3)
	private PolicyDto policyDto;

	@ApiModelProperty(value = "问题对象", position = 3)
	private List<ProblemSimpleDto> problems;

	public PolicyTemplateSimpleDto(PolicyDto policyDto) {
		this.policyDto = policyDto;
	}

	public PolicyTemplateSimpleDto() {
	}

	public PolicyDto getPolicyDto() {
		return policyDto;
	}

	public void setPolicyDto(PolicyDto policyDto) {
		this.policyDto = policyDto;
	}

	public List<ProblemSimpleDto> getProblems() {
		return problems;
	}

	public void setProblems(List<ProblemSimpleDto> problems) {
		this.problems = problems;
	}
}
