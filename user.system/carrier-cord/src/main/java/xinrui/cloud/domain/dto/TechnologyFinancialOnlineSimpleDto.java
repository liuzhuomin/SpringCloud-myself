package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import xinrui.cloud.app.MyApplication;
import xinrui.cloud.common.utils.BeanUtilsEnhance;
import xinrui.cloud.common.utils.CopyListFilter;
import xinrui.cloud.domain.TechnologyFinancial;
import xinrui.cloud.service.TechnologyFinancialAppointmentService;

import java.util.List;

@Data
@ApiModel("金融产品简单数据对象（上线产品）")
public class TechnologyFinancialOnlineSimpleDto extends TechnologyFinancialSimpleDto {

    @ApiModelProperty(value = "预约的并且未曾处理的数量,为0不用显示", position = 4)
    private int appointmentCount;

    public static List<TechnologyFinancialOnlineSimpleDto> copyOnlies(List<TechnologyFinancial> listBydCriteria) {
        return BeanUtilsEnhance.copyList(listBydCriteria, TechnologyFinancialOnlineSimpleDto.class, new CopyListFilter<TechnologyFinancialOnlineSimpleDto, TechnologyFinancial>() {
            @Override
            public TechnologyFinancialOnlineSimpleDto copy(TechnologyFinancial source, TechnologyFinancialOnlineSimpleDto target) {
                BeanUtilsEnhance.copyPropertiesEnhance(source, target);
                //设置未曾处理的预约对象数量
                TechnologyFinancialAppointmentService bean = MyApplication.getBean(TechnologyFinancialAppointmentService.class);
                target.setAppointmentCount(bean.getAppointmentCount(source.getId()));
                return target;
            }
        });
    }
}