package xinrui.cloud.service.impl;

import com.google.common.collect.Lists;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import xinrui.cloud.compoment.Application;
import xinrui.cloud.domain.TechnologyFinancial;
import xinrui.cloud.domain.TechnologyFinancialAppointment;
import xinrui.cloud.domain.dto.TechnologyFinancialAppointmentUserDto;
import xinrui.cloud.domain.dto.TechnologyFinancialAppointmentedDto;
import xinrui.cloud.dto.PageDto;
import xinrui.cloud.service.TechnologyFinancialAppointmentService;
import xinrui.cloud.service.TechnologyFinancialService;

import java.util.Date;
import java.util.List;

/**
 * @author liuliuliu
 * @version 1.0
 *  2019/8/15 10:24
 */
@Service
public class TechnologyFinancialAppointmentServiceImpl extends BaseServiceImpl<TechnologyFinancialAppointment>
        implements TechnologyFinancialAppointmentService {

    @Autowired
    TechnologyFinancialService technologyFinancialService;

    @Override
    public PageDto<List<TechnologyFinancialAppointmentUserDto>> listAppointmentBank(Long id, int status, int currentPage, int pageSize) {
        DetachedCriteria technologyFinancialAppointmentCriteria = getCriteria(id);
        if (status == 0) {
            technologyFinancialAppointmentCriteria.add(Restrictions.isNotNull("processDate"));
        } else {
            technologyFinancialAppointmentCriteria.add(Restrictions.isNull("processDate"));
        }
        PageDto<List<TechnologyFinancialAppointment>> listPageDto = new PageDto<>(--currentPage * pageSize, pageSize, technologyFinancialAppointmentCriteria);
        dao.pageByCriteria(listPageDto);
        List<TechnologyFinancialAppointmentUserDto> t = TechnologyFinancialAppointmentUserDto.copy(listPageDto.getT());
        return new PageDto<List<TechnologyFinancialAppointmentUserDto>>(listPageDto.getTotalPage(), t);
    }

    private DetachedCriteria getCriteria(Long id) {
        return DetachedCriteria.forClass(TechnologyFinancialAppointment.class)
                .createAlias("technologyFinancial", "t")
                .add(Restrictions.eq("t.id", id)).addOrder(Order.asc("processDate"));
    }

    @Override
    public void processAppointment(Long appointmentId) {
        TechnologyFinancialAppointment byId = findById(appointmentId);
        Assert.notNull(byId, "找不到预约对象!");
        byId.setProcessDate(new Date());
        merge(byId);
    }

    @Override
    public int getAppointmentCount(Long id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TechnologyFinancialAppointment.class)
                .createAlias("technologyFinancial", "t")
                .add(Restrictions.eq("t.id", "id"))
                .add(Restrictions.isNull("processDate"))
                .setProjection(Projections.rowCount());
        return count(criteria);
    }

    @Override
    public PageDto<List<TechnologyFinancialAppointmentedDto>> listAppointment(int currentPage, int pageSize) {
        //获取所有与当前用户挂钩的预约对象
        DetachedCriteria appointmentUserId = DetachedCriteria.forClass(TechnologyFinancialAppointment.class)
                .add(Restrictions.eq("appointmentUserId", Application.getCurrentUser().getId()));
        List<TechnologyFinancialAppointment> technologyFinancialAppointments = dao.listBydCriteria(appointmentUserId);
        List<Long> ids = Lists.newArrayList();

        for (TechnologyFinancialAppointment technologyFinancialAppointment : technologyFinancialAppointments) {
            ids.add(technologyFinancialAppointment.getTechnologyFinancial().getId());
        }
        if (CollectionUtils.isEmpty(ids)) {
            return new PageDto<>();
        }
        DetachedCriteria statusCriteria = technologyFinancialService.getStatusCriteria(TechnologyFinancial.TechnologyStatus.ONLINE.value());
        statusCriteria.add(Restrictions.in("id", ids)).addOrder(Order.asc("createDate"));

        PageDto<List<TechnologyFinancial>> pageObj = new PageDto<>(--currentPage * pageSize, pageSize, statusCriteria);
        dao.pageByCriteria(pageObj);
        PageDto<List<TechnologyFinancialAppointmentedDto>> pageDto =
                new PageDto<>(pageObj.getTotalPage(), TechnologyFinancialAppointmentedDto.copy(pageObj.getT()));
        return pageDto;
    }

}
