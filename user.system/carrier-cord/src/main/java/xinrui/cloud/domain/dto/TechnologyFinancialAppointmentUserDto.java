package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xinrui.cloud.common.utils.BeanUtilsEnhance;
import xinrui.cloud.common.utils.CopyListFilter;
import xinrui.cloud.domain.TechnologyFinancialAppointment;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@ApiModel("科技金融预约用户数据传输对象")
@Data
public class TechnologyFinancialAppointmentUserDto extends TechnologyFinancialBaseUserDto {
    @ApiModelProperty(value = "预约时间", position = 8)
    private String appointmentDate;
    @ApiModelProperty(value = "处理时间", position = 9)
    private String processDate;


    public static List<TechnologyFinancialAppointmentUserDto> copy(List<TechnologyFinancialAppointment> technologyFinancialAppointments) {
        return BeanUtilsEnhance.copyList(technologyFinancialAppointments, TechnologyFinancialAppointmentUserDto.class, new CopyListFilter<TechnologyFinancialAppointmentUserDto, TechnologyFinancialAppointment>() {
            @Override
            public TechnologyFinancialAppointmentUserDto copy(TechnologyFinancialAppointment source, TechnologyFinancialAppointmentUserDto target) {
                BeanUtilsEnhance.copyPropertiesEnhance(source,target);
                BeanUtilsEnhance.copyPropertiesEnhance(source.getTechnologyFinancialUser(),target);
                return target;
            }
        });
    }
}
