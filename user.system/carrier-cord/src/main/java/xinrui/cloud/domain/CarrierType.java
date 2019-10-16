package xinrui.cloud.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import xinrui.cloud.domain.IdEntity;

/**
 * 载体类型
 * @author Jihy
 * @since 2019-06-25 14:19
 */
@Data @Entity @Table(name = "zhjf_carrier_type") public class CarrierType extends IdEntity {

  /**
   * 标题
   */
  private String title;


}
