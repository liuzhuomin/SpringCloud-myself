package xinrui.cloud.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 载体文件资料库具体文件
 * @author Jihy
 * @since 2019-07-04 12:11
 */
@Data @Entity @Table(name = "zhjf_carrier_database_files")
public class CarrierDataBaseFiles extends IdEntity {

  @ManyToOne
  @JoinColumn(name = "carrierDataBase_id")
  private CarrierDataBase carrierDataBase;
  /** 文件名 */
  private String fileName;
  /** 文件路径 */
  private String filePath;
  /** 上传的用户 */
  private Long uid;
  /** 上传的时间 */
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createAt;

}
