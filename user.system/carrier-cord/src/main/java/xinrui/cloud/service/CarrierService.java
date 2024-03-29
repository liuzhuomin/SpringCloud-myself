package xinrui.cloud.service;

import java.util.List;
import java.util.Map;
import xinrui.cloud.domain.Carrier;
import xinrui.cloud.domain.CarrierProfessionalDirection;
import xinrui.cloud.domain.CarrierType;
import xinrui.cloud.domain.dto.CarrierDTO;
import xinrui.cloud.domain.dto.CarrierDefaultFileDTO;
import xinrui.cloud.domain.dto.CarrierShowListDTO;
import xinrui.cloud.dto.PageDto;
import xinrui.cloud.dto.UserDto;

/**
 * @author Jihy
 * @since 2019-06-25 15:44
 */
 public interface CarrierService extends BaseService<Carrier> {

  /**
   * Get CarrierType lists.
   *
   * @return
   */
  List<CarrierType> getCarrierTypes();

  /**
   * Get CarrierProfessionalDirection lists.
   *
   * @return
   */
  List<CarrierProfessionalDirection> getCarrierProfessionalDirection();


  /**
   * Get CarrierDefaultFile To lists.
   * @return
   */
  List<CarrierDefaultFileDTO> getCarrierDefaultFiles();

  /**
   * Get All The Carrier And the query conditions have
   * ( 1 user current User, 2 name brandName, 3 carrierTypeId  of the carrierType, 4 street  carrier of street )
   * @param page 页码
   * @param pageSize 页数
   * @return
   */
  PageDto<List<CarrierShowListDTO>> getAllCarrier(int page, int pageSize, UserDto user, String name, Long carrierTypeId, String street
  , Long  carrierProfessionalDirectionId, String orderByName, Boolean ascending);

  /**
   * Query the Carrier By Id
   * @param id
   * @return
   */
  CarrierDTO findCarrierById(Long id);

  /**
   * [Gov] Query all carriers (audit)
   * @param page
   * @param pageSize
   * @return
   */
  PageDto<List<CarrierShowListDTO>> getAllToAuditCarrier(int page, int pageSize);

  /**
   * 获取当前企业提交的所有待审核的科技载体
   * @param page
   * @param pageSize
   * @param groupId
   * @return
   */
  PageDto<List<CarrierShowListDTO>> getCompanyAllAudit(int page, int pageSize, Long groupId);

  /**
   * 通过父parentId获取子节点的地址
   * @param addressId
   * @return
   */
  List<Map<String, String>>  getAddress(Long addressId);

  /**
   * 通过企业ID获取科技载体里所属者是该企业的所有载体
   * @param companyId
   * @return
   */
  List<CarrierShowListDTO> getAllCarrierByCompanyId(Long companyId, Long carrierId);

 /**
  * 获取所有的已通过的载体
  * @return
  */
 List<CarrierShowListDTO> getAllRecommendCarrier();

 /**
  * 获取分页的入库载体
  * @param page
  * @param pageSize
  * @return
  */
 PageDto<List<CarrierShowListDTO>> getCarrierByPage(int page, int pageSize);

}
