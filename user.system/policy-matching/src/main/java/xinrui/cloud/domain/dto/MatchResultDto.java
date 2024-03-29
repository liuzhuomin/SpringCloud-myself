package xinrui.cloud.domain.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <B>Title:</B>MatchResultDto</br》
 * <B>Description:</B> 政策匹配结果数据传输对象 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/4/11 18:47
 */
@ApiModel("政策匹配结果数据传输对象")
public class MatchResultDto {

    @ApiModelProperty(value = "最终获取的资助额度", position = 1)
    private String money;

    @ApiModelProperty(value = "政策组对象", position = 2)
    private List<PolicyGroupDto> groups = Lists.newArrayList();

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public List<PolicyGroupDto> getGroups() {
        return groups;
    }

    public void setGroups(List<PolicyGroupDto> groups) {
        this.groups = groups;
    }
}
