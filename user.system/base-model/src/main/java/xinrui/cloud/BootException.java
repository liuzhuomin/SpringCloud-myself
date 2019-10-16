package xinrui.cloud;

/**
 * 自定义异常
 * 
 * @author liuliuliu
 *
 */
public class BootException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Integer code;

	private Exception e;

	public BootException(String string) {
		super(string);
	}

	public BootException(Integer code, String string) {
		super(string);
		this.code = code;
	}

	public BootException(Exception e, String string) {
		super(string);
		this.e = e;
	}

	public Integer getCode() {
		return code;
	}

	public Exception getE() {
		return e;
	}

	public void setE(Exception e) {
		this.e = e;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}
