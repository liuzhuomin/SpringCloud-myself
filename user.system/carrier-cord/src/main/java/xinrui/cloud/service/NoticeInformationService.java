package xinrui.cloud.service;

import xinrui.cloud.domain.NoticeInformation;

import java.util.List;
import java.util.Map;

public interface NoticeInformationService extends BaseService<NoticeInformation> {
    List<Map<String, Object>> getCorpListNotice();

    List<Map<String, Object>> getCorpListNoticeByTitle(String title);

    List<Map<String, Object>> getListNoticeDraft();

    List<Map<String, Object>> getGovListNotice(String title, String order);

    Map<String, Object> getNoticeInformationById(Long id);

    NoticeInformation addNotice(String noticeInformation, String files);

    NoticeInformation editNotice(String noticeInformation, String files);

    void deleteNotice(Long id);

    void retNotice(Long id);
}
