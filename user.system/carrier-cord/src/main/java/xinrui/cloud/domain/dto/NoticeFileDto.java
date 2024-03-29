package xinrui.cloud.domain.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.NoticeFile;
import org.springframework.util.CollectionUtils;

import java.util.List;


@ApiModel(description = "通知公告附件表")
public class NoticeFileDto extends IdEntity {

    @ApiModelProperty(value = "通知id", position = 0)
    private String noticeId;
    @ApiModelProperty(value = "附件名称", position = 1)
    private String fileName;
    @ApiModelProperty(value = "附件地址", position = 2)
    private String filePath;
    @ApiModelProperty(value = "附件后缀名", position = 3)
    private String suffix;
    @ApiModelProperty(value = "上传用户", position = 4)
    private String userId;
    @ApiModelProperty(value = "附件描述", position = 5)
    private String fileDesc;

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public NoticeFileDto(Long id, String fileName, String filePath, String suffix, String fileDesc) {
        super();
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.suffix = suffix;
        this.fileDesc = fileDesc;
    }


    public static List<NoticeFileDto> copyList(List<NoticeFile> findAll) {
        if (CollectionUtils.isEmpty(findAll)) {
            return null;
        }
        List<NoticeFileDto> results = Lists.newArrayList();
        for (NoticeFile file : findAll) {
            results.add(copy(file));
        }
        return results;
    }

    public static NoticeFileDto copy(NoticeFile file) {
        if (file == null) {
            return null;
        }
        return new NoticeFileDto(file.getId(), file.getFileName(), file.getFilePath(), file.getSuffix(), file.getFileDesc());
    }


    public String getFileDesc() {
        return fileDesc;
    }

    public void setFileDesc(String fileDesc) {
        this.fileDesc = fileDesc;
    }
}
