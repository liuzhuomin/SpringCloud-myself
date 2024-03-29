package xinrui.cloud.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

/**
 * @author Jihy
 * @since 2019-06-28 09:41
 */
@Data
@Entity
@Table(name = "dm_address")
public class Address extends IdEntity {
  private static final long serialVersionUID = -8259676938220778544L;

  private Long  fianlId;

  /**
   * 当前地址类型,是省还是市区，还是街道等等
   * 0 省
   * 1 市
   * 2 区
   * 3 街
   * 4 居委会不用啦......
   */
  @Column(name="address_class")
  private long addressClass;

  /**
   * 父元素,如果是省或者直辖市父元素则为null
   */
  @ManyToOne(targetEntity=Address.class)
  @JoinColumn(name="p_id")
  private Address parent;

  /**
   * 可为省市区街道
   */
  @Column(length=255,name="name")
  private String name;

  /**
   * 当前地址的子元素,比如省的子元素是当前省下的所有市区
   */
  @OneToMany(mappedBy="parent",cascade= CascadeType.ALL,targetEntity=Address.class,fetch= FetchType.LAZY)
  private List<Address> children;

  /**
   * 统用区划代码
   */
  private Long code;

  /**
   * 连接地址
   */
  private String href;
}
