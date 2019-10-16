package xinrui.cloud.common.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Logger;

/**
 * <B>Title:</B>HttpUtil</br>
 * <B>Description:</B>  </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/7/31 17:51
 */
public class HttpUtil {

    private final static Logger LOGGER = Logger.getLogger(HttpUtil.class.getName());

    /**
     * 发送json数据到指定请求(POST)
     *
     * @param requestUrl 请求地址
     * @param parameter  请求参数
     * @return 响应结果
     * @throws IOException
     */
    public static String sendJson(String requestUrl, Map<Object, Object> parameter) throws IOException {
        Assert.state(StringUtils.isEmpty(requestUrl), "RequestUrl must not be null!");

        String json = JSON.toJSONString(parameter);
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "Bearer AcceptToken");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setConnectTimeout(2000);
        connection.setRequestMethod(HttpMethod.POST.name());
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(json.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();
        String responseMessage = connection.getResponseMessage();
        LOGGER.info("ResponseCode : " + responseCode);
        LOGGER.info("ResponseMessage : " + responseMessage);

        InputStream inputStream = connection.getErrorStream();
        if (inputStream == null) {
            inputStream = connection.getInputStream();
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String str;
        StringBuffer buffer = new StringBuffer();
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
        }
        bufferedReader.close();

        return buffer.toString();
    }
}
