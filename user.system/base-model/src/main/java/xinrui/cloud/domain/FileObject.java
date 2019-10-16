package xinrui.cloud.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * 文件对象
 *
 * @author liuliuliu
 * @version 1.0
 *  2019/8/5 17:11
 */
@MappedSuperclass
public abstract class FileObject extends NameEntity {

    /**
     * 文件名称
     */
    private String name;
    /**
     * 存储的uri路径
     */
    private String serverUri;
    /**
     * 存储的目录
     */
    private String menu;
    /**
     * 创建的时间
     */
    private Date createDate=new Date();
    /**
     * 创建的人
     */
    private Long userId;

    /**
     * 完整下载路径
     */
    private String fullPath;

    /**
     * 下载的uri
     */
    private String fileDownLoadPath;

    /**
     * 文件完整的物理存储路径
     */
    private String fileSavePath;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getServerUri() {
        return serverUri;
    }

    public void setServerUri(String serverUri) {
        this.serverUri = serverUri;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getFileDownLoadPath() {
        return fileDownLoadPath;
    }

    public void setFileDownLoadPath(String fileDownLoadPath) {
        this.fileDownLoadPath = fileDownLoadPath;
    }

    public String getFileSavePath() {
        return fileSavePath;
    }

    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }
}
