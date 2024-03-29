package xinrui.cloud.service.impl;


import xinrui.cloud.domain.CarrierCoreData;
import xinrui.cloud.domain.dto.CarrierCoreDataDTO;
import xinrui.cloud.service.CarrierCoreDataService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

/**
 * @author Jihy
 * @since 2019-06-26 16:06
 */
@Service
public class CarrierCoreDataServiceImpl extends BaseServiceImpl<CarrierCoreData> implements
    CarrierCoreDataService {

  @Override public void saveCarrierCoreData(CarrierCoreData data) {
    dao.persist(CarrierCoreData.class, data);
  }

  @Override public CarrierCoreDataDTO getCarrierCoreDataByCarrierId(Long carrierId) {
    CarrierCoreData coreData = findSingleCriteria(
        DetachedCriteria.forClass(CarrierCoreData.class).add(Restrictions.eq("carrier.id", carrierId)));
    CarrierCoreDataDTO dto = CarrierCoreDataDTO.copyForm(coreData);
    return dto;
  }
}
