package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;
import xinrui.cloud.app.MyApplication;
import xinrui.cloud.common.utils.BeanUtilsEnhance;
import xinrui.cloud.common.utils.CopyListFilter;
import xinrui.cloud.domain.NameEntity;
import xinrui.cloud.dto.OtherGroupDto;
import xinrui.cloud.service.TechnologyFinancialService;

import java.util.List;

/**
 * @author liuliuliu
 * @version 1.0
 * 2019/8/20 14:34
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("银行组织对象")
@Data
public class BankDto extends NameEntity {
    @ApiModelProperty(value = "用户未曾处理的产品数量", position = 3)
    private int unProcessCount;

    public static List<BankDto> copy(List<OtherGroupDto> data) {
        return BeanUtilsEnhance.copyList(data, BankDto.class, new CopyListFilter<BankDto, OtherGroupDto>() {
            @Override
            public BankDto copy(OtherGroupDto source, BankDto target) {
                BeanUtils.copyProperties(source,target);
                TechnologyFinancialService bean = MyApplication.getBean(TechnologyFinancialService.class);
                int count=bean.listUnProcessCountByBank(target.getName());
                target.setUnProcessCount(count);
                return target;
            }
        });
//        return BeanUtilsEnhance.copyList(data, BankDto.class);
    }
}
