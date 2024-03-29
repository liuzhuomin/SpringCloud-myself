package xinrui.cloud.domain.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import xinrui.cloud.app.MyApplication;
import xinrui.cloud.common.utils.BeanUtilsEnhance;
import xinrui.cloud.common.utils.CopyListFilter;
import xinrui.cloud.common.utils.DateUtil;
import xinrui.cloud.compoment.Application;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.TechnologyFinancial;
import xinrui.cloud.domain.TechnologyFinancialAppointment;
import xinrui.cloud.dto.UserDto;
import xinrui.cloud.service.TechnologyFinancialService;

import java.util.List;

/**
 * @author liuliuliu
 * @version 1.0
 *  2019/8/9 17:40
 */
@ApiModel("已预约产品详情杜希昂")
@Getter
@Setter
@Slf4j
public class TechnologyFinancialAppointmentedDto extends IdEntity {
    @ApiModelProperty(value = "图片展示地址", position = 1)
    private String imageUrl;
    @ApiModelProperty(value = "产品名称", position = 2)
    private String name;
    @ApiModelProperty(value = "产品标语", position = 3)
    private String slogan;
    @ApiModelProperty(value = "产品详细信息", position = 4)
    private String information;
    @ApiModelProperty(value = "申请结束时间", position = 5)
    private String applyEndDate;
    @ApiModelProperty(value = "最高贷款金额", position = 6)
    private String loanAmount;
    @ApiModelProperty(value = "总共预约人数", position = 7)
    private int appointmentCount;
    @ApiModelProperty(value = "贷款期限", position = 8)
    private String loanTimeLimit;
    @ApiModelProperty(value = "可抵押物件", position = 9)
    private String technologyLoanTypes;
    @ApiModelProperty(value = "当前用户是否已经预约此产品", position = 10)
    private boolean appointment;
    @ApiModelProperty(value = "预约时间", position = 11)
    private String appointmentDate;


    public static TechnologyFinancialAppointmentedDto copy(TechnologyFinancial technologyFinancial) {

        TechnologyFinancialAppointmentedDto dto = new TechnologyFinancialAppointmentedDto();
        BeanUtilsEnhance.copyPropertiesEnhance(technologyFinancial, dto);

        dto.setLoanAmount(technologyFinancial.getLoanAmount().getFullAmount());
        dto.setLoanTimeLimit(technologyFinancial.getLoanTimeLimit().getFullDate());
        dto.setTechnologyLoanTypes(TechnologyLoanTypeDto.getStringBuilder(technologyFinancial.getTechnologyLoanTypes()).toString());
        dto.setImageUrl(technologyFinancial.getViewIndexImage().getFullPath());

        TechnologyFinancialService technologyFinancialService = MyApplication.getBean(TechnologyFinancialService.class);
        int count = technologyFinancialService.findAppointmentCount(technologyFinancial.getId());
        dto.setAppointmentCount(count);

        List<TechnologyFinancialAppointment> technologyFinancialAppointments =
                technologyFinancial.getTechnologyFinancialAppointments();
        boolean judge = judge(technologyFinancialAppointments, Application.getCurrentUser(), dto);
        dto.setAppointment(judge);

        return dto;
    }

    /**
     * 判断当前用户是否包含在{@code  List<TechnologyFinancialAppointment> }集合中
     * 如果存在，会在dto中设置预约时间
     *
     * @param technologyFinancialAppointments 预约对象集合
     * @param currentUser                     当前用户
     * @param dto                             科技金融产品审核列表对象
     * @return 如果用户在预约用户中返回true，否则返回false
     */
    private static boolean judge(List<TechnologyFinancialAppointment> technologyFinancialAppointments,
                                 UserDto currentUser, TechnologyFinancialAppointmentedDto dto) {
        for (TechnologyFinancialAppointment technologyFinancialAppointment : technologyFinancialAppointments) {
            if (technologyFinancialAppointment.getTechnologyFinancialUser().getId().intValue()
                    == currentUser.getId().intValue()) {
                dto.setAppointmentDate(DateUtil.format(technologyFinancialAppointment.getAppointmentDate()));
                return true;
            }
        }
        return false;
    }

    public static List<TechnologyFinancialAppointmentedDto> copy(final List<TechnologyFinancial> technologyFinancials) {
        log.info("accept list size {}", technologyFinancials.size());
        List<TechnologyFinancialAppointmentedDto> result = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(technologyFinancials)) {

            List<TechnologyFinancialAppointmentedDto> technologyFinancialAppointmentedDtos =
                    BeanUtilsEnhance.copyList(technologyFinancials, TechnologyFinancialAppointmentedDto.class, new CopyListFilter<TechnologyFinancialAppointmentedDto, TechnologyFinancial>() {
                        @Override
                        public TechnologyFinancialAppointmentedDto copy(TechnologyFinancial source, TechnologyFinancialAppointmentedDto target) {
                            return TechnologyFinancialAppointmentedDto.copy(source);
                        }
                    });
            log.info("list size {}", technologyFinancialAppointmentedDtos.size());
            return technologyFinancialAppointmentedDtos;
        }
        return result;
    }
}
