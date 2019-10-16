package xinrui.cloud.domain.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.PolicyTemplate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

@ApiModel(description = "政策匹配模版主对象")
public class PolicyTemplateDto {

    @ApiModelProperty(value = "唯一主键", position = 0)
    private Long id;

    /**
     * 单位主体
     */
    @ApiModelProperty(value = "单位主体(个人,机构,企业)", position = 1)
    private List<String> unitSubjectes;

    /**
     * 政策id
     */
    @ApiModelProperty(value = "政策主体", position = 2)
    private String mainName;

    /**
     * 政策描述
     */
    @ApiModelProperty(value = "政策模版主体描述", position = 3)
    private String description;

    @ApiModelProperty(value = "政策对象", position = 4)
    private PolicyDto policy;

    public PolicyTemplateDto(PolicyDto policy) {
        this.policy = policy;
    }

    public PolicyTemplateDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMainName() {
        return mainName;
    }

    public void setMainName(String mainName) {
        this.mainName = mainName;
    }

    public List<String> getUnitSubjectes() {
        return unitSubjectes;
    }

    public void setUnitSubjectes(List<String> unitSubjectes) {
        this.unitSubjectes = unitSubjectes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public PolicyDto getPolicy() {
        return policy;
    }

    public void setPolicy(PolicyDto policy) {
        this.policy = policy;
    }

    public static PolicyTemplateDto copy(PolicyTemplate source) {
        if (source == null) {
            return null;
        }
        PolicyTemplateDto dto = new PolicyTemplateDto();
        BeanUtils.copyProperties(source, dto, "unitSubjectes", "policy");
        String unitSubject = source.getUnitSubject();
        if (StringUtils.isNotBlank(unitSubject)) {
            List<String> strs = Lists.newArrayList();
            if (unitSubject.contains(",")) {
                String[] split = unitSubject.split(",");
                for (String string : split) {
                    strs.add(string);
                }
            } else {
                strs.add(unitSubject);
            }
            dto.setUnitSubjectes(strs);
            dto.setPolicy(PolicyDto.copy(source.getPolicy()));
        }
        return dto;
    }

    public static List<PolicyTemplateDto> copyList(List<PolicyTemplate> findAll) {
        if (CollectionUtils.isEmpty(findAll)) {
            return null;
        }
        List<PolicyTemplateDto> results = Lists.newArrayList();
        for (PolicyTemplate policyTemplate : findAll) {
            results.add(copy(policyTemplate));
        }
        return results;
    }

}
