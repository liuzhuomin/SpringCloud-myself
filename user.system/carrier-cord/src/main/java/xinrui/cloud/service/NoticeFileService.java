package xinrui.cloud.service;

import xinrui.cloud.domain.NoticeFile;
import xinrui.cloud.domain.dto.NoticeFileDto;

import java.util.List;

public interface NoticeFileService extends BaseService<NoticeFile> {
    List<NoticeFileDto> getFileList(Long NoiceId);
    void deleteFile(Long NoiceId);
    void insertFile(String noticeId, String filesJson);
}
