package xinrui.cloud.domain.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.PolicyGroup;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <B>Title:</B>PolicyGroupDto2</br>
 * <B>Description:</B> 政策组json</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/19 11:00
 */
@ApiModel(description = "政策组数据传输对象")
public class PolicyGroupDto2 extends IdEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "政策組名稱", position = 1)
    private String title;

    @ApiModelProperty(value = "图片地址", position = 2)
    private String imageUrl;

    @ApiModelProperty(value = "政策描述", position = 3)
    private String content;

    @ApiModelProperty(value = "政策集合", position = 4)
    private List<PolicyDto> policies;

    public static List<PolicyGroupDto2> copyFrom(List<PolicyGroup> all) {
        List<PolicyGroupDto2> result = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(all)) {
            for (PolicyGroup policyGroup : all) {
                PolicyGroupDto2 policyGroupDto2 = new PolicyGroupDto2();
                BeanUtils.copyProperties(policyGroup, policyGroupDto2, "policies");
                policyGroupDto2.setPolicies(PolicyDto.copyList(policyGroup.getPolicies()));
                result.add(policyGroupDto2);
            }
        }
        return result;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<PolicyDto> getPolicies() {
        return policies;
    }

    public void setPolicies(List<PolicyDto> policies) {
        this.policies = policies;
    }


}
