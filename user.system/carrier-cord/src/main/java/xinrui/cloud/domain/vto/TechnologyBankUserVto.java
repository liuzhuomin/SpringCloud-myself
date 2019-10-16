package xinrui.cloud.domain.vto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import xinrui.cloud.vto.UserVto;


@ApiModel("科技金融银行账户对象(接收)")
@Data
public class TechnologyBankUserVto {
    @ApiModelProperty(value = "银行id", position = 1)
    private Long bankId;
    @ApiModelProperty(value = "账号名称", position = 2)
    private String username;
    @ApiModelProperty(value = "账号密码", position = 3)
    private String password;
    @ApiModelProperty(value = "联系人", position = 4)
    private String concat;
    @ApiModelProperty(value = "联系电话", position = 5)
    private String phone;
    @ApiModelProperty(value = "联系邮箱", position = 6)
    private String email;

    public static UserVto toUserVto(TechnologyBankUserVto technologyBankUserVto) {
        UserVto vto = new UserVto();
        BeanUtils.copyProperties(technologyBankUserVto, vto);
        vto.setShaPassword(vto.getPassword());
        return vto;
    }
}
