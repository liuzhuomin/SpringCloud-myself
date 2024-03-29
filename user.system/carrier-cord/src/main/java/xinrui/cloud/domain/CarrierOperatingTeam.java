package xinrui.cloud.domain;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

/**
 * 运营管理团队人员信息
 * @author Jihy
Datasince 2019-06-25 14:34
 */
@Data @Entity @Table(name = "zhjf_carrier_operating_team") public class CarrierOperatingTeam extends IdEntity {

  @ManyToOne
  @JoinColumn(name = "carrierCoreData_id")
  private CarrierCoreData carrierCoreData;
  /** 姓名*/
  private String name;
  /** 性别 */
  private String sex;
  /** 年龄 */
  private String age;
  /** 学历 */
  private String education;
  /** 职位*/
  private String position;
  /** 负责工作 */
  @Lob private String responsibleForWork;
  /** 主要履历 */
  @Lob private String theMainRecord;
}
