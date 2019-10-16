package xinrui.cloud.config;

import org.apache.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import xinrui.cloud.dto.ResultDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <B>Title:</B>ResultDto.java</br>
 * <B>Description:</B> 针对于RestController的异常处理 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年3月27日
 */
@SuppressWarnings("rawtypes")
@RestControllerAdvice
public class SpringExceptionHandle implements ResponseBodyAdvice {

    private final Logger LOGGER = Logger.getLogger(SpringExceptionHandle.class);

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    /**
     * 封装处理正常的controller(为了照顾swagger的api生成，所以未曾使用)
     *
     * @param body
     * @param returnType
     * @param selectedContentType
     * @param selectedConverterType
     * @param request
     * @param response
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        return body;
    }

    @ExceptionHandler(Throwable.class)
    public void handle(Throwable e, HttpServletResponse response) throws IOException {
        e.printStackTrace();
        throwException(e, response);
    }

    @ExceptionHandler(Exception.class)
    public void handle(Exception e, HttpServletResponse response) throws IOException {
        e.printStackTrace();
        throwException(e, response);
    }

    @ExceptionHandler(MultipartException.class)
    public void handle(MultipartException e, HttpServletResponse response) throws IOException {
        e.printStackTrace();
        throwException("上传文件错误", response);
    }

//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public void handle(DataIntegrityViolationException e, HttpServletResponse response) throws IOException {
////        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
////        ConstraintViolation<?> next = constraintViolations.iterator().next();
////        if(next!=null){
////            throwException(" next.getPropertyPath()+next.getMessage()",response);
////        }
//        throwException("数据持久化操作失败!", response);
//
//    }

    private void throwException(Object e, HttpServletResponse response) {
        LOGGER.error(e);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.println(ResultDto.error(e));
            writer.flush();
            writer.close();
        } catch (IOException t) {
            LOGGER.trace(e);
            if (writer != null) {
                writer.close();
            }
        }
    }

}
