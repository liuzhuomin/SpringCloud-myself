package xinrui.cloud.service.impl;

import java.util.List;
import xinrui.cloud.domain.CarrierDataBase;
import xinrui.cloud.domain.dto.CarrierDataBaseDTO;
import xinrui.cloud.service.CarrierDataBaseService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

/**
 * @author Jihy
 * @since 2019-06-25 23:53
 */
@Service public class CarrierDataBaseServiceImpl extends BaseServiceImpl<CarrierDataBase> implements
    CarrierDataBaseService {

  @Override public List<CarrierDataBaseDTO> getCarrierDataBaseListByCarrierId(Long carrierId) {
    List<CarrierDataBase> carrierDataBases = dao.listBydCriteria(DetachedCriteria.forClass(CarrierDataBase.class).add(
        Restrictions.eq("carrier.id", carrierId)));
    return CarrierDataBaseDTO.copyForm(carrierDataBases);
  }
}
