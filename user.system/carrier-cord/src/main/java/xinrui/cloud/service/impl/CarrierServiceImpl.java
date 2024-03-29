package xinrui.cloud.service.impl;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xinrui.cloud.dao.CompanyDao;
import xinrui.cloud.domain.*;
import xinrui.cloud.domain.dto.CarrierDTO;
import xinrui.cloud.domain.dto.CarrierDefaultFileDTO;
import xinrui.cloud.domain.dto.CarrierShowListDTO;
import xinrui.cloud.dto.PageDto;
import xinrui.cloud.dto.UserDto;
import xinrui.cloud.service.CarrierService;
import xinrui.cloud.service.feign.CompanyServiceFeign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jihy
 * @since 2019-06-25 15:46
 */
@Service
public class CarrierServiceImpl extends BaseServiceImpl<Carrier> implements CarrierService {

  @Autowired CompanyDao companyDao;

  @Autowired
  CompanyServiceFeign companyServiceFeign;

  @Override public List<CarrierType> getCarrierTypes() {
    String hql = "FROM  CarrierType";
    return dao.listByHql(hql, CarrierType.class);
  }

  @Override public List<CarrierProfessionalDirection> getCarrierProfessionalDirection() {
    String hql = " FROM CarrierProfessionalDirection";
    return dao.listByHql(hql, CarrierProfessionalDirection.class);
  }

  @Override public List<CarrierDefaultFileDTO> getCarrierDefaultFiles() {
    String hql = "FROM CarrierDefaultFile";
    List<CarrierDefaultFile> sources = dao.listByHql(hql, CarrierDefaultFile.class);
    return CarrierDefaultFileDTO.copyForm(sources);
  }

  @Override public PageDto<List<CarrierShowListDTO>>  getAllCarrier(int page, int pageSize, UserDto user, String name, Long carrierTypeId
  , String street, Long carrierProfessionalDirectionId, String orderByName, Boolean ascending) {
    DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Carrier.class);
    if (StringUtils.equals(user.getUniqueGroup().getGroupType(), GroupType.OTHER)) {
        detachedCriteria.add(Restrictions.eq("carrierSubordinateId", user.getUniqueGroup().getId()));
    }
    detachedCriteria.createAlias("carrierCoreData", "c", JoinType.LEFT_OUTER_JOIN);
    detachedCriteria.createAlias("carrierImportantData", "i", JoinType.LEFT_OUTER_JOIN);
    detachedCriteria.createAlias("carrierCoreData.carrierType", "t", JoinType.LEFT_OUTER_JOIN);
    detachedCriteria.createAlias("carrierCoreData.carrierProfessionalDirection", "p", JoinType.LEFT_OUTER_JOIN);
    if(!StringUtils.isEmpty(name)){
      detachedCriteria.add(Restrictions.ilike("c.brandName", "%" +name + "%"));
    }
    if(carrierTypeId != -1){
      detachedCriteria.add(Restrictions.eq("t.id", carrierTypeId));
    }
    if(!StringUtils.equals("all", street)){
      detachedCriteria.add(Restrictions.ilike("c.areasToWhichTheyBelong", "%" + street + "%"));
    }
    if(carrierProfessionalDirectionId != -1){
      detachedCriteria.add(Restrictions.eq("p.id", carrierProfessionalDirectionId));
    }

    if(ascending){
      detachedCriteria.addOrder(Order.asc(orderByDetachedCriteria(orderByName)));
    }else{
      detachedCriteria.addOrder(Order.desc(orderByDetachedCriteria(orderByName)));
    }
    detachedCriteria.add(Restrictions.eq("status", 2));
    PageDto<List<Carrier>> pg = new PageDto<>(--page * pageSize, pageSize, detachedCriteria);
    dao.pageByCriteria(pg);
    return new PageDto<>(pg.getTotalPage(), CarrierShowListDTO.copyFormByCarrier(pg.getT(), companyServiceFeign));
  }

  private String orderByDetachedCriteria (String orderByName){
      if(StringUtils.equals("brandName", orderByName)){
        return "c.brandName";
      }else if(StringUtils.equals("usedArea", orderByName)){
        return "i.usedArea";
      }else if(StringUtils.equals("vacantArea", orderByName)){
        return "i.vacantArea";
      }else if(StringUtils.equals("inEnterpriseNumber", orderByName)){
        return "c.inEnterpriseNumber";
      }else if(StringUtils.equals("incubationBaseNumber", orderByName)){
        return "c.incubationBaseNumber";
      }
      return "";
  }

  @Override public CarrierDTO findCarrierById(Long id) {
    Carrier carrier = findById(id);
    if (carrier != null) {
      CarrierDTO dto = CarrierDTO.copyForm(carrier, companyServiceFeign);
      return dto;
    }
    return null;
  }

  @Override public PageDto<List<CarrierShowListDTO>> getAllToAuditCarrier(int page, int pageSize) {
    DetachedCriteria d = DetachedCriteria.forClass(Carrier.class).add(Restrictions.eq("status", 1));
    PageDto<List<Carrier>> pg1 = new PageDto<>(--page * pageSize, pageSize, d);
    dao.pageByCriteria(pg1);
    return new PageDto<>(pg1.getTotalPage(), CarrierShowListDTO.copyFormByCarrier(pg1.getT(), companyServiceFeign));
  }

  @Override
  public PageDto<List<CarrierShowListDTO>> getCompanyAllAudit(int page, int pageSize, Long groupId) {
    DetachedCriteria d = DetachedCriteria.forClass(Carrier.class);
    d.add(Restrictions.eq("status", 1));
    d.add(Restrictions.eq("creatorGroupId", groupId));
    PageDto<List<Carrier>> pg = new PageDto<>(--page * pageSize, pageSize, d);
    dao.pageByCriteria(pg);
    return new PageDto<>(pg.getTotalPage(), CarrierShowListDTO.copyFormByCarrier(pg.getT(), companyServiceFeign));
  }

  @Override public  List<Map<String, String>>  getAddress(Long addressId) {
    List<Map<String, String>> result = new ArrayList<>();
    List<Address> addresses;
    if(addressId == -1){
      addresses =  dao.listBydCriteria(DetachedCriteria.forClass(Address.class).add(Restrictions.isNull("parent.id")));
    }else{
      addresses = dao.listBydCriteria(DetachedCriteria.forClass(Address.class).add(Restrictions.eq("parent.id", addressId)));
    }
    for (Address address : addresses) {
      Long id = address.getId();
      String name = address.getName();
      Map<String, String> m = new HashMap<>();
      m.put("id", id.toString());
      m.put("name", name);
      result.add(m);
    }
    return result;
  }

  @Override public List<CarrierShowListDTO> getAllCarrierByCompanyId(Long companyId, Long carrierId) {
    DetachedCriteria d = DetachedCriteria.forClass(Carrier.class);
    d.add(Restrictions.eq("status", 2));
    d.add(Restrictions.eq("carrierSubordinateId", companyId));
    if(carrierId != -1){
      d.add(Restrictions.not(Restrictions.eq("id", carrierId)));
    }
    return CarrierShowListDTO.copyFormByCarrier(dao.<Carrier>listBydCriteria(d), companyServiceFeign);
  }
  @Override
  public List<CarrierShowListDTO> getAllRecommendCarrier() {
    DetachedCriteria d = DetachedCriteria.forClass(Carrier.class);
    d.add(Restrictions.eq("status",2));
    return CarrierShowListDTO.copyFormByCarrier( dao.<Carrier>listBydCriteria(d), companyServiceFeign);
  }

  @Override
  public PageDto<List<CarrierShowListDTO>> getCarrierByPage(int page, int pageSize) {
    DetachedCriteria d = DetachedCriteria.forClass(Carrier.class).add(Restrictions.eq("status", 2));
    PageDto<List<Carrier>> pg1 = new PageDto<>(--page * pageSize, pageSize, d);
    dao.pageByCriteria(pg1);
    return new PageDto<>(pg1.getTotalPage(), CarrierShowListDTO.copyFormByCarrier(pg1.getT(), companyServiceFeign));
  }
}
