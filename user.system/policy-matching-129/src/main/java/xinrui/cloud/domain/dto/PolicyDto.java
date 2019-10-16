package xinrui.cloud.domain.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.Policy;

import java.util.List;

@ApiModel(description = "政策数据传输对象")
public class PolicyDto {

    @ApiModelProperty(value = "唯一主键", position = 0)
    private Long id;

    @ApiModelProperty(value = "政策标题", position = 1)
    private String shortTitle;

    @ApiModelProperty(value = "政策状态(状态 P 为上线 D为下线 R 为删除)", position = 2)
    private String status;

    @ApiModelProperty(value = "政策详细描述(html)", position = 3)
    private String description;

    @ApiModelProperty(value = "当前政策预估的值", position = 4)
    private String money;

    @ApiModelProperty(value = "标识政策组的题目是否都为空问题", position = 5)
    private boolean allEmpty = false;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public static List<PolicyDto> copyList(List<Policy> t) {
        List<PolicyDto> result = Lists.newArrayList();
        for (Policy p : t) {
            result.add(copy(p));
        }
        return result;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PolicyDto(Long id, String shortTitle) {
        super();
        this.id = id;
        this.shortTitle = shortTitle;
    }

    public boolean isAllEmpty() {
        return allEmpty;
    }

    public void setAllEmpty(boolean allEmpty) {
        this.allEmpty = allEmpty;
    }

    public PolicyDto(Long id, String shortTitle, String status) {
        super();
        this.id = id;
        this.shortTitle = shortTitle;
        this.status = status;
    }

    public PolicyDto() {
        super();
    }

    public static PolicyDto copy(Policy source) {
        if (source == null) {
            return null;
        }
        return new PolicyDto(source.getId(), source.getShortTitle(), source.getStatus());
    }

}
