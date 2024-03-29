package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.PolicyActivity;
import xinrui.cloud.domain.Problem;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <B>Title:</B>ActivityDto.java</br>
 * <B>Description:</B> 政策模版主体内的环节数据传输对象 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年3月27日
 */
@ApiModel("政策环节对象")
public class PolicyActivityDto extends IdEntity {
    private static final long serialVersionUID = 1L;

//	@ApiModelProperty(value = "政策匹配模板对象", position = 9)
//	private PolicyTemplateDto template;

    @ApiModelProperty(value = "问题编辑数据对象", position = 10)
    private List<ProblemDto> problems;

//	public PolicyTemplateDto getTemplate() {
//		return template;
//	}
//
//	public void setTemplate(PolicyTemplateDto template) {
//		this.template = template;
//	}

    public List<ProblemDto> getProblems() {
        return problems;
    }

    public void setProblems(List<ProblemDto> problems) {
        this.problems = problems;
    }

    public static PolicyActivityDto copyFrom(PolicyActivity merge) {
        PolicyActivityDto dto = new PolicyActivityDto();
        BeanUtils.copyProperties(merge, dto, "problems", "template");
        List<Problem> problemsResource = merge.getProblems();
        if (!CollectionUtils.isEmpty(problemsResource)) {
            dto.setProblems(ProblemDto.copyList(problemsResource));
        }
//		dto.setTemplate(PolicyTemplateDto.copy(merge.getTemplate()));
        return dto;
    }

}
