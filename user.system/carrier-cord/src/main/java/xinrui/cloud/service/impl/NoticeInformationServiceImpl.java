package xinrui.cloud.service.impl;

import com.alibaba.fastjson.JSONObject;
import xinrui.cloud.domain.NoticeInformation;
import xinrui.cloud.domain.dto.NoticeInformationDto;
import xinrui.cloud.service.NoticeFileService;
import xinrui.cloud.service.NoticeInformationService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NoticeInformationServiceImpl extends BaseServiceImpl<NoticeInformation> implements NoticeInformationService {
    private final static Logger LOGGER = LoggerFactory.getLogger(NoticeInformationServiceImpl.class);
    @Autowired
    NoticeFileService noticeFileService;

    /**
     * 政府端----查询通知列表
     *
     * @param title
     * @return
     */
    @Override
    public List<Map<String, Object>> getGovListNotice(String title, String order) {
        StringBuffer sb = new StringBuffer();
        sb.append("select t.id,t.title,t.content,t.begin_time,t.end_time,t.read_num,\n");
        sb.append("(select codedesc from tu_code c where c.code =t.type) as type,img_path \n");
        sb.append("from notice_information t where status=1 \n");
        if (StringUtils.isNotBlank(title)) {
            sb.append(" and title like '%" + title + "%'\n");
        }
        if ("1".equals(order)) {
            //按发布时间升序
            sb.append("order by unix_timestamp(t.begin_time) asc");
        } else if ("2".equals(order)) {
            //按发布时间降序
            sb.append("order by unix_timestamp(t.begin_time) desc");
        } else if ("3".equals(order)) {
            //按发布时间升序
            sb.append("order by unix_timestamp(t.end_time) asc");
        } else if ("4".equals(order)) {
            //按发布时间降序
            sb.append("order by unix_timestamp(t.end_time) desc");
        } else if ("5".equals(order)) {
            //按发布时间升序
            sb.append("order by t.read_num asc");
        } else if ("6".equals(order)) {
            //按发布时间降序
            sb.append("order by t.read_num desc");
        }
        LOGGER.info(" 政府端----查询通知列表：" + sb.toString());
        return dao.getJdbcTemplate().queryForList(sb.toString());
    }

    /**
     * 政府端-------获取草稿箱列表
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getListNoticeDraft() {
        StringBuffer sb = new StringBuffer("SELECT id,title,content,\n");
        sb.append("(select codedesc from tu_code where  protype = 'noticeType' and code =t.type) as type,\n");
        sb.append("CASE \n");
        sb.append("WHEN TIMESTAMPDIFF(DAY, create_time,NOW()) > 0 \n");
        sb.append("THEN CONCAT(TIMESTAMPDIFF(DAY, create_time,NOW()),'天前')\n");
        sb.append("WHEN TIMESTAMPDIFF(HOUR, create_time,NOW()) > 0 \n");
        sb.append("THEN CONCAT(TIMESTAMPDIFF(HOUR, create_time,NOW()),'小时前')\n");
        sb.append("WHEN TIMESTAMPDIFF(MINUTE, create_time,NOW()) > 0 \n");
        sb.append("THEN CONCAT(TIMESTAMPDIFF(MINUTE, create_time,NOW()),'分前')\n");
        sb.append("END AS create_time\n");
        sb.append("FROM notice_information t ");
        sb.append("where status=0 order by create_time desc");
        return dao.getJdbcTemplate().queryForList(sb.toString());
    }

    /**
     * 企业端-------获取活动通知列表
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getCorpListNotice() {
        StringBuffer sb = new StringBuffer();
        sb.append("select t.id,t.title,t.content,t.create_time,t.read_num,\n");
        sb.append("(select codedesc from tu_code c where c.code =t.type) as type, \n");
        sb.append("(select file_path from notice_file f where suffix in ('jpg','png') and t.id = f.notice_id LIMIT 1)as file_path \n");
        sb.append("from notice_information t where t.status=1 \n");
        sb.append("order by t.create_time desc");
        LOGGER.info(" 企业端-------获取活动通知列表 ：" + sb.toString());
        return dao.getJdbcTemplate().queryForList(sb.toString());
    }

    /**
     * 企业端-----右侧通过标题查询活动通知列表
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getCorpListNoticeByTitle(String title) {
        StringBuffer sb = new StringBuffer();
        sb.append("select t.id,t.title,t.content,\n");
        sb.append("(select file_path from notice_file f where suffix in ('jpg','png') and t.id = f.notice_id LIMIT 1)as file_path \n");
        sb.append("from notice_information t where status=1\n");
        if (StringUtils.isNotBlank(title)) {
            sb.append(" and t.title like '%" + title + "%'\n");
        }
        sb.append(" order by create_time desc");
        LOGGER.info("企业端-----右侧通过标题查询活动通知列表 ：" + sb.toString());
        return dao.getJdbcTemplate().queryForList(sb.toString());
    }

    /**
     * 新增活动通知
     *
     * @param filesJson
     * @return
     */
    @Override
    public NoticeInformation addNotice(String noticeInformation, String filesJson) {
        LOGGER.info("开始添加活动通知");
        System.out.println("参数：noticeInformation-------：" + noticeInformation);
        System.out.println("参数：filesJson-------：" + filesJson);
        NoticeInformation notice = JSONObject.parseObject(noticeInformation, NoticeInformation.class);
        notice.setCreateTime(new Date(System.currentTimeMillis()));
        LOGGER.info("NoticeInformation----------------:" + notice);
        NoticeInformation merge = merge(notice);
        System.out.println(merge);
        LOGGER.info("merge：" + merge);
        if (filesJson != null && !"".equals(filesJson)) {
            noticeFileService.insertFile(merge.getId().toString(), filesJson);
            LOGGER.info("添加附件完成");
        }
        LOGGER.info("添加活动通知完成");

        return notice;
    }

    /**
     * 更新活动通知
     *
     * @param filesJson
     * @return
     */
    @Override
    public NoticeInformation editNotice(String noticeInformation, String filesJson) {
        NoticeInformation notice = JSONObject.parseObject(noticeInformation, NoticeInformation.class);
        notice.setReadNum(notice.getReadNum() + 1);
        LOGGER.info("NoticeInformation----------------:" + notice);
        merge(notice);
        LOGGER.info("修改活动通知完成");
        noticeFileService.deleteFile(notice.getId());
        if (filesJson != null && !"".equals(filesJson)) {
            noticeFileService.insertFile(String.valueOf(notice.getId()), filesJson);
            LOGGER.info("添加附件完成");
        }

        return notice;
    }

    /**
     * 下架按钮：将活动通知回退到草稿箱
     */
    @Override
    public void retNotice(Long id) {
        NoticeInformation notice = findById(id);
        notice.setStatus("0");
        merge(notice);
    }


    /**
     * 通过Id获取通知信息和附件信息
     *
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> getNoticeInformationById(Long id) {
        Map<String, Object> result = new HashMap();
        NoticeInformation notice = findById(id);
        result.put("notice", NoticeInformationDto.copy(notice));
        result.put("fileList", noticeFileService.getFileList(id));
        LOGGER.info("获取到的通知和附件信息为----------" + result);
        return result;
    }

    /**
     * 删除活动通知
     *
     * @param id
     */
    @Override
    public void deleteNotice(Long id) {
        NoticeInformation notice = findById(id);
        if (notice != null) {
            remove(id);
            noticeFileService.deleteFile(id);
        }
        LOGGER.info("删除完成");
    }

}
