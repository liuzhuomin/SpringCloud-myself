package xinrui.cloud.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.BootException;
import xinrui.cloud.dto.ResponseDto.Message;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * <B>Title:</B>ResultDto.java</br>
 * <B>Description:</B> 统一返回格式的封装对象 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年3月27日
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@ApiModel(description = "通用响应返回对象")
public class ResultDto<T> implements Serializable {
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "结果代码", position = 0)
    private int code;
    @ApiModelProperty(value = "错误信息", position = 1)
    private String message;
    @ApiModelProperty(value = "结果数据", position = 2)
    private T data;
//	@ApiModelProperty(value = "下次请求地址", position = 3)
//	private String nextUrl;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    private ResultDto() {
    }

    /**
     * 返回指定code码和指定消息的封装结果
     *
     * @param code    指定code编码
     * @param message 指定消息提示
     * @return
     */
    public static ResultDto error(int code, String message) {
        ResultDto<Object> objectResultDto = new ResultDto<>();
        objectResultDto.setMessage(Message.ERROR.value);
        objectResultDto.setCode(code);
        objectResultDto.setData(message);
        return objectResultDto;
    }

    private static ResultDto getResultDto(Throwable e) {
        ResultDto dto = new ResultDto();
        dto.setMessage(ERROR);
        dto.setData(e.getMessage());
        if (e instanceof BootException) {
            BootException boot = (BootException) e;
            Integer bootCode = boot.getCode();
            if (bootCode != null) {
                dto.setCode(bootCode);
            }
        } else {
            dto.setCode(500);
        }
        return dto;
    }

    public static ResultDto error(Exception e) {
        ResultDto dto = getResultDto(e);
        return dto;
    }

    public static ResultDto error(Object e) {
        ResultDto dto = new ResultDto();
        dto.setMessage(ERROR);
        dto.setCode(500);
        String data = null;
        if (e != null) {
            if (e instanceof BootException) {
                BootException b = (BootException) e;
                if (b.getCode() != null) {
                    dto.setCode(b.getCode());
                }
                data = b.getMessage();
            } else {
                data = e.toString();
            }
        }
        dto.setData(data);
        return dto;
    }

    public static ResultDto success(Object body) {
        ResultDto dto = new ResultDto();
        dto.setMessage(SUCCESS);
        dto.setCode(200);
        dto.setData(body);
        return dto;
    }

    public static ResultDto success() {
        ResultDto dto = new ResultDto();
        dto.setMessage(SUCCESS);
        dto.setCode(200);
        return dto;
    }

    public ResultDto(T listCreatedPolicyId) {
        this.data = listCreatedPolicyId;
    }

    public ResultDto(T t, Message success) {
        this.data = t;
        this.message = success.value;
        this.code = success.code;
    }

    /**
     * 指定数据对象、传递消息、code编码、下次请求地址
     *
     * @param t       需要被传输的对象
     * @param message {@link Message}对象
     * @param realUrl 下次请求地址
     */
    public ResultDto(T t, Message message, String realUrl) {
        this.data = t;
        this.message = message.value;
        this.code = message.code;
//		this.nextUrl = realUrl;
    }

    public ResultDto(Message message) {
        this.message = message.value;
        this.code = message.code;
    }

//	public String getNextUrl() {
//		return nextUrl;
//	}
//
//	public void setNextUrl(String nextUrl) {
//		this.nextUrl = nextUrl;
//	}

}
