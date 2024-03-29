package xinrui.cloud.service.impl;

import xinrui.cloud.domain.CarrierDataBaseFiles;
import xinrui.cloud.service.CarrierDataBaseFilesService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

/**
 * @author Jihy
 * @since 2019-07-04 15:10
 */
@Service public class CarrierDataBaseFilesServiceImpl extends BaseServiceImpl<CarrierDataBaseFiles> implements
    CarrierDataBaseFilesService {
  @Override
  public CarrierDataBaseFiles getCarrierDataBaseFilesByDataId(Long carrierDataBaseId, Long fileId) {
    return dao.findSingleCriteria(DetachedCriteria.forClass(CarrierDataBaseFiles.class).add(
        Restrictions.eq("carrierDataBase.id", carrierDataBaseId)).add(Restrictions.eq("id", fileId)));
  }
}
