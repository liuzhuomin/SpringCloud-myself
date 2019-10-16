package xinrui.cloud;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * zuul网关代理
 *
 * @author liuliuliu
 */
@SpringBootApplication
@RestController
@RequestMapping
public class WebUtil {

    private final static Logger log = LoggerFactory.getLogger(WebUtil.class);
    private final String ROOT = System.getProperty("user.dir") + File.separator + "file";
    private final int MIN_LENGTH = 5;

    public static void main(String[] args) throws IOException {
        new SpringApplicationBuilder(WebUtil.class).web(true).run(args);
    }

    @RequestMapping("/download")
    public String downLoad(@RequestParam String path) throws IOException {
        down(path);
        return "下载中";
    }

    private void down(String path) throws IOException {
        File a = new File(ROOT);
        if (!a.exists()) {
            a.mkdirs();
        }
        downloadFirst(path);
    }

    /**
     * 分层的最大数量
     */
    private final int MAX_COUNT = 800;

    private volatile int allCount = 0;

    private void downloadFirst(String relativePath) throws IOException {
        String property = System.getProperty("user.dir");
        final String fallPath = property + File.separator + relativePath;
        log.info("fallpath:{}",fallPath);
//        ClassLoader classLoader = ApplicationZuul.class.getClassLoader();
//        InputStream resourceAsStream = classLoader.getResourceAsStream(relativePath);
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                new FileInputStream(new File(fallPath))));
        String str;
        List<List<String>> result = Lists.newCopyOnWriteArrayList();
        int i = 0;
        int max = 0;

        List<String> one = Lists.newCopyOnWriteArrayList();
        while ((str = bufferedReader.readLine()) != null) {
            one.add(str);
            i++;
            max++;
            if (MAX_COUNT - i <= 0) {
                i = 0;
                result.add(one);
                one = Lists.newArrayList();
            }
        }
        if (max < MAX_COUNT) {
            result.add(one);
        }
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (final List<String> list : result) {
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    for (String url : list) {
                        try {
                            log.info("开始下载....{}", url);
                            download(url);
                            allCount++;
                            log.info("下载完毕...{}", url);
                        } catch (Throwable e) {
                            log.warn("未完成下载:{}", url);
                        }
                    }
                }
            });
        }
        log.error("下载完成!!!!!!!!!!!!!!!!!!!!!!!!!所有的数量为:{}", allCount);
        cachedThreadPool.shutdown();
    }


    private synchronized void download(String path) throws IOException {
        if (path.length() < MIN_LENGTH) {
            return;
        }
        String notFileNameUrl = path.substring(0, path.lastIndexOf("/"));
        String substring = notFileNameUrl.substring(notFileNameUrl.lastIndexOf("/") + 1);
        String saveFilePath = ROOT + File.separator + substring;

        String fileName = path.substring(path.lastIndexOf("/") + 1);
//        log.info("saveFilePath:{}", saveFilePath);
//        log.info("fileName:{}", fileName);
        String fallPath = saveFilePath + File.separator + fileName;
//        log.info("fallPath:{}", fallPath);
//        log.info("path:{}", path);
        File saveFile = new File(fallPath);
        byte[] bytes = HttpUtil.doGet(path);
        if (ArrayUtils.isEmpty(bytes)) {
            log.warn("当前请求获取的字节为空:{}", path);
        } else {
            File file = new File(saveFilePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            log.warn("当前请求获取的字节为:{}", bytes.length * 1024);
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(saveFile));
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
            outputStream.close();
        }
    }

}
