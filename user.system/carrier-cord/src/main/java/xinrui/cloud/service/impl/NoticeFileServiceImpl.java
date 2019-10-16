package xinrui.cloud.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import xinrui.cloud.domain.NoticeFile;
import xinrui.cloud.domain.dto.NoticeFileDto;
import xinrui.cloud.service.NoticeFileService;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class NoticeFileServiceImpl extends BaseServiceImpl<NoticeFile> implements NoticeFileService {

    /**
     * 通过通知ID获取附件信息
     *
     * @param NoiceId
     * @return
     */
    @Override
    public List<NoticeFileDto> getFileList(Long NoiceId) {
        StringBuffer sql = new StringBuffer("select id , file_name,file_path,suffix,file_desc from notice_file where notice_id= " + NoiceId);
        ResultSetExtractor<List<NoticeFileDto>> resultSetExtractor = new ResultSetExtractor<List<NoticeFileDto>>() {
            @Override
            public List<NoticeFileDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<NoticeFileDto> groups = Lists.newArrayList();
                while (rs.next()) {
                    groups.add(new NoticeFileDto(rs.getLong("id"),
                            rs.getString("file_name"),
                            rs.getString("file_path"),
                            rs.getString("suffix"),
                            rs.getString("file_desc")));
                }
                return groups;
            }
        };
        List<NoticeFileDto> query = dao.getJdbcTemplate().query(sql.toString(), resultSetExtractor);
//        LOGGER.info("附件集合:{}" ,query);
        return query;
    }

    /**
     * 删除附件
     *
     * @param NoiceId
     */
    @Override
    public void deleteFile(Long NoiceId) {
        String sql = "delete from notice_file where notice_id =" + NoiceId;
        dao.getJdbcTemplate().execute(sql);
    }

    /**
     * 插入附件
     *
     * @param noticeId
     * @param filesJson
     */
    @Override
    public void insertFile(String noticeId, String filesJson) {
        if (StringUtils.isNotBlank(filesJson)) {
            List<NoticeFile> fileArr = JSONArray.parseArray(filesJson, NoticeFile.class);
            for (NoticeFile f : fileArr) {
                NoticeFile file = f;
                file.setNoticeId(noticeId);
                merge(file);
            }
        }
    }

}
