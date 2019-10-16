package xinrui.cloud.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 载体资料库
 * @author Jihy
 * @since 2019-06-25 17:14
 */
@Data @Entity @Table(name = "zhjf_carrier_database")
public class CarrierDataBase extends IdEntity {

  @ManyToOne
  @JoinColumn(name = "carrier_id")
  private Carrier carrier;
  /** 名称 */
  private String name;
  /** 描述 */
  private String fileDescribe;
  /** 是否是默认的文件配置 */
  private Boolean isDefaultFile;
  /** 创建人的用户ID(如果是默认的文件配置，取创建者) */
  private Long createUId;
  /** 上传的时间 */
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createAt;
  /** 具体文件 */
  @OneToMany(mappedBy ="carrierDataBase", cascade = { CascadeType.ALL} )
  private List<CarrierDataBaseFiles> carrierDataBaseFiles = new ArrayList<>();

}
