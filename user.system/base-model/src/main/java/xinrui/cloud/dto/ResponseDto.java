package xinrui.cloud.dto;

/**
 * 请求作用工具类
 * 
 * @author liuliuliu
 *
 */
public class ResponseDto {
	enum Message {
		/** 代表请求成功 */
		SUCCESS(200, "success"),
		/** 代表请求失败 */
		ERROR(400, "error");

		int code;
		String value;

		private Message(int code, String value) {
			this.code = code;
			this.value = value;
		}

		public int code() {
			return this.code;
		}

		public String value() {
			return this.value();
		}
	}

	public static <T> ResultDto<T> success(T t) {
		return new ResultDto<T>(t, Message.SUCCESS);
	}

	public static <T> ResultDto<T> success() {
		return new ResultDto<T>(Message.SUCCESS);
	}

	public static <T> ResultDto<T> error(T t) {
		return new ResultDto<T>(t, Message.ERROR);
	}

	public static <T> ResultDto<T> success(T t, String realUrl) {
		return new ResultDto<T>(t, Message.SUCCESS, realUrl);
	}
}
