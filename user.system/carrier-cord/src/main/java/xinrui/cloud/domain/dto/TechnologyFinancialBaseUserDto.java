package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("科技金融预约用户基础数据传输对象")
@Data
public class TechnologyFinancialBaseUserDto {
    @ApiModelProperty(value = "用户名称", position = 1)
    protected String username;
    @ApiModelProperty(value = "用户性别", position = 2)
    protected String gender;
    @ApiModelProperty(value = "用户所在区域", position = 3)
    protected String region;
    @ApiModelProperty(value = "用户所在街道", position = 4)
    protected String street;
    @ApiModelProperty(value = "用户手机", position = 5)
    protected String phone;
    @ApiModelProperty(value = "用户邮箱", position = 6)
    protected String email;
    @ApiModelProperty(value = "预约状态(已预约，未预约)", position = 7)
    protected String status;


}
