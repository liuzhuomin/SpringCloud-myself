package xinrui.cloud.domain.dto;

import lombok.Data;

/**
 * 组信息表
 * @author Jihy
 */
@Data public class GroupTO {
  private long id;
  private String name;
  private String logo;
  private String xydm;
  /**
   * 企业状态
   */
  private Integer status;
  /**
   * 状态描述
   */
  private String statusDescribe;
  private Long creatorId;
  private GroupType groupType;



}
