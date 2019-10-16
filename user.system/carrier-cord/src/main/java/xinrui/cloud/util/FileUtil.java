package xinrui.cloud.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;
import xinrui.cloud.BootException;
import xinrui.cloud.interfaces.FileExtensionFilter;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author liuliuliu
 * @version 1.0
 *  2019/8/7 10:32
 */
public class FileUtil {

    /**
     * 保存文件操作
     * 1.文件名中的.会被替换
     * 2.文件名包括后缀重复了的会显示成原本文件名(当前重复数量)+后缀的格式
     *
     * @param path   绝对路径(不包含文件名)
     * @param file   文件流对象
     * @param filter 过滤器
     * @return
     * @throws IOException
     */
    public static File saveFile(String path, MultipartFile file, FileExtensionFilter filter) throws IOException {
        if (filter != null) {
            boolean b = filter.canContinue(file);
            if (!b) {
                throw new BootException("验证不通过无法继续");
            }
        }
        String fileName = file.getOriginalFilename();
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
        String fileNameNotPrefix = fileName.substring(0, fileName.lastIndexOf("."));
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        fileNameNotPrefix = fileNameNotPrefix.replace(".", "");
        fileName = fileNameNotPrefix + prefix;
        if (fileNameNotPrefix.contains(File.separator) || fileNameNotPrefix.contains("\\")) {
            throw new BootException("文件名非法格式!");
        }
        File isNotExist = new File(path);
        if (!isNotExist.exists()) {
            isNotExist.mkdir();
        }
        FilenameUtils.getExtension(file.getOriginalFilename());
        File target = new File(path + File.separator + fileName);
        if (target.exists()) {
            String name = target.getName();
            File[] listFiles = target.getParentFile().listFiles();
            int max = 0;
            for (File file$ : listFiles) {
                if (file$.isFile()) {
                    String fileNames = file$.getName();
                    String suffix = fileNames.substring(fileNames.lastIndexOf('.'));
                    fileNames = fileNames.substring(0, fileNames.lastIndexOf('.'));
                    if (fileNames.indexOf(fileNameNotPrefix) != -1 && suffix.equals(prefix)) {
                        max++;
                    }
                }
            }
            name = fileNameNotPrefix + URLEncoder.encode("(" + max + ")", StandardCharsets.UTF_8.name()) + prefix;
            target = new File(path + File.separator + name);
            file.transferTo(target);
        } else {
            File parentFile = target.getParentFile();
            if (!parentFile.exists()) {
                boolean mkdirs = parentFile.mkdirs();
                if (mkdirs) {
                    file.transferTo(target);
                }
            } else {
                file.transferTo(target);
            }
        }
        return target;
    }
}
