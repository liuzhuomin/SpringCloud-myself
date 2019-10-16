package xinrui.cloud.common.enums;

/**
 * The class Error code enum.
 *
 * @author Jihy
 * @since 2019-07-11 10:17
 */
public enum ResultCode {

  /**
   * En 200 success code enum.
          */
  success(200, "成功"),

  /**
   * En 401 success code enum.
   */
  NOT_POWER(401, "身份验证失败"),

  /**
   * En 99990001 error code enum.
   */
  EN99990001(99990001, "参数无效!"),
  /**
   * En 99990100 error code enum.
   */
  EN99990002(99990002, "参数异常!"),

  /**
   * En 99990003 error code enum.
   */
  EN99990003(99990003, "数据未找到!"),

  /**
   * En 99990101 error code enum.
   */
  EN99990101(99990101, "科技载体数据不存在！"),

  /**
   * En 99990102 error code enum.
   */
  EN99990102(99990102, "科技载体专业方向数据不存在！"),

  /**
   * En 99990103 error code enum.
   */
  EN99990103(99990103, "科技载体类型数据不存在！"),

  /**
   * En 99990104 error code enum.
   */
  EN99990104(99990104, "科技载体资料库数据不存在！"),

  /**
   * En 99990105 error code enum.
   */
  EN99990105(99990105, "专业服务合作机构和专家信息不存在！");


  private Integer code;
  private String msg;

  ResultCode(int code, String msg){
      this.code = code;
      this.msg = msg;
  }

  public Integer code() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String msg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  @Override public String toString() {
    return this.name();
  }
}
