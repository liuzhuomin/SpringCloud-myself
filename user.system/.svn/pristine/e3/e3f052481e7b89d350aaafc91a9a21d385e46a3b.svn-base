package xinrui.cloud.domain.dto;

import lombok.Data;

/**
 * 用户
 * @author Jihy
 */
@Data public class UserTO {
  private long userId;
  private String userName;
  private String password;
  private String shaPassword;
  private String phone;
  private String realName;
  private GroupType groupType;
  private Long companyId;
  /**
   * 当前用户的状态
   */
  private String status;
  /**
   * 是否是管理员
   */
  private Boolean isAdmin;
  /**
   * 企业信息
   */
  private GroupTO groupTo;

  private String logo;


	@Override
	public String toString() {
		return "UserTO [userId=" + userId + ", userName=" + userName + ", password=" + password + ", shaPassword="
				+ shaPassword + ", phone=" + phone + ", realName=" + realName + ", groupType=" + groupType
				+ ", companyId=" + companyId + ", status=" + status + ", isAdmin=" + isAdmin + ", groupTo=" + groupTo
				+ ", logo=" + logo + "]";
	}


}
