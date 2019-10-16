package xinrui.cloud.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface FileExtensionFilter {

    /**
     * 验证文件操作是否能够继续，如果文件内容或者文件名符合预期则返回true即可，返回false则会抛出异常
     * @param file
     * @return
     */
    public boolean canContinue( MultipartFile file);
}
