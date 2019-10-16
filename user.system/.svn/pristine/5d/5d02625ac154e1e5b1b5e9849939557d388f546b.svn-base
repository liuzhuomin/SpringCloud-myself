package xinrui.cloud.service;

import org.springframework.stereotype.Service;
import xinrui.cloud.domain.TechnologyFinancial;
import xinrui.cloud.domain.TechnologyFinancialAppointment;
import xinrui.cloud.domain.dto.TechnologyFinancialAppointmentUserDto;
import xinrui.cloud.domain.dto.TechnologyFinancialAppointmentedDto;
import xinrui.cloud.dto.PageDto;

import java.util.List;

@Service
public interface TechnologyFinancialAppointmentService extends BaseService<TechnologyFinancialAppointment> {

    /**
     * 根据科技金融对象id获取对应的所有预约人的详细信息
     * @param id            {@link TechnologyFinancial#getId()}
     * @param status        标识，0代表查找预约的，1代表查找未曾预约的
     * @param currentPage   当前页码
     * @param pageSize      一页大小
     * @return
     */
    PageDto<List<TechnologyFinancialAppointmentUserDto>> listAppointmentBank(Long id, int status, int currentPage, int pageSize);

    /**
     * 处理预约的用户
     * @param  appointmentId   预约对象的id
     */
    void processAppointment(Long appointmentId);

    /**
     * 通过金融产品对象获取当前产品未曾处理的已经预约的数量
     * @param id    {@link TechnologyFinancial#getId()} 金融产品id
     * @return  未曾处理的预约用户数量
     */
    int getAppointmentCount(Long id);

    /**
     * 分页获取当前用户线上预约过的申请
     *
     * @param currentPage 当前页码
     * @param pageSize    一页大小
     * @return
     */
    PageDto<List<TechnologyFinancialAppointmentedDto>> listAppointment(int currentPage, int pageSize);


}
