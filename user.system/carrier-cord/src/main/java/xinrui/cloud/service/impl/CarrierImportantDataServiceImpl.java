package xinrui.cloud.service.impl;

import xinrui.cloud.domain.CarrierImportantData;
import xinrui.cloud.domain.dto.CarrierImportantDataDTO;
import xinrui.cloud.service.CarrierImportantDataService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

/**
 * 创新载体-重要数据
 * @author Jihy
 * @since 2019-06-25 23:45
 */
@Service  public class CarrierImportantDataServiceImpl extends BaseServiceImpl<CarrierImportantData> implements
    CarrierImportantDataService {

  @Override public CarrierImportantDataDTO getCarrierImportantDataByCarrierId(Long carrierId) {
    CarrierImportantData carrierImportantData = findSingleCriteria(
        DetachedCriteria.forClass(CarrierImportantData.class).add(
            Restrictions.eq("carrier.id", carrierId)));
    return CarrierImportantDataDTO.copyForm(carrierImportantData);
  }
}
