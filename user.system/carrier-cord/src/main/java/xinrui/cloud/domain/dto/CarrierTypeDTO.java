package xinrui.cloud.domain.dto;

import xinrui.cloud.domain.CarrierType;
import xinrui.cloud.domain.IdEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @author Jihy
 * @since 2019-06-25 15:38
 */
@Data public class CarrierTypeDTO extends IdEntity {

  /**
   * 标题
   */
  private String title;

  public CarrierTypeDTO copyForm(CarrierType source){
    BeanUtils.copyProperties(source, this);
    return this;
  }
}
