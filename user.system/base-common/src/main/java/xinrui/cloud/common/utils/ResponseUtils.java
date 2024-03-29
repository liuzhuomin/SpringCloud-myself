package xinrui.cloud.common.utils;

import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <B>Title:</B>ResponseUtils</br>
 * <B>Description:</B>  </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/7/25 9:35
 */
public class ResponseUtils {

    /**
     * 响应Object对象
     * @param response
     * @param object
     */
    public static void sendResponse(HttpServletResponse response ,Object object){
        PrintWriter writer = null;
        try {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            writer = response.getWriter();
            writer.println(object);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }
}
