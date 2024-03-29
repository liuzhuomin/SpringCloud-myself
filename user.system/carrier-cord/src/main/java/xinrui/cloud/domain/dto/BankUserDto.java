package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import xinrui.cloud.app.MyApplication;
import xinrui.cloud.common.utils.BeanUtilsEnhance;
import xinrui.cloud.common.utils.CopyListFilter;
import xinrui.cloud.dto.UserDto;
import xinrui.cloud.service.TechnologyFinancialService;

import java.util.List;

@Data
@Slf4j
@ApiModel("银行组织用户数据传输对象")
public class BankUserDto {

    @ApiModelProperty(value = "展示图片", position = 1)
    private String imageUrl;

    @ApiModelProperty(value = "展示银行", position = 2)
    private String bank;

    @ApiModelProperty(value = "用户登录名", position = 3)
    private String username;

    @ApiModelProperty(value = "联系人", position = 4)
    private String concat;

    @ApiModelProperty(value = "联系电话", position = 5)
    private String phone;

    @ApiModelProperty(value = "联系邮箱", position = 6)
    private String email;

    @ApiModelProperty(value = "发布产品数", position = 7)
    private int publicCount;

    @ApiModelProperty(value = "创建时间", position = 8)
    private int createDate;

    public static List<BankUserDto> copy(List<UserDto> sources) {
        return BeanUtilsEnhance.copyList(sources, BankUserDto.class, new CopyListFilter<BankUserDto, UserDto>() {
            @Override
            public BankUserDto copy(UserDto userDto, BankUserDto target) {
                BeanUtilsEnhance.copyDateFiledEnhance(userDto, target);
                target.setBank(userDto.getUniqueGroup().getName());
                TechnologyFinancialService bean = MyApplication.getBean(TechnologyFinancialService.class);
                log.info("bean is null {}", bean == null);
                if (bean != null) {
                    int count = bean.getPublicCount(userDto.getId());
                    target.setPublicCount(count);
                    String viewImageUrl = bean.getBestNewViewImage(userDto.getId());
                    target.setImageUrl(viewImageUrl);
                }
                return target;
            }
        });
    }
}
