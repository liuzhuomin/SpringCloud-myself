package xinrui.cloud.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import xinrui.cloud.domain.CompanyMessage;
import xinrui.cloud.domain.dto.*;
import xinrui.cloud.dto.PageDto;
import xinrui.cloud.service.LeaderService;
import xinrui.cloud.service.UserService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * <B>Title:</B>LeaderServiceImpl</br>
 * <B>Description:</B>  </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/5 11:31
 */
@Service
public class LeaderServiceImpl extends BaseServiceImpl<CompanyMessage> implements LeaderService {

    private final static Logger LOOGER = LoggerFactory.getLogger(LeaderServiceImpl.class);

    @Autowired
    UserService userService;

    @Transactional
    @Override
    public PageDto<List<CompanyMessageDto>> listMessageByLeaderId(Long leaderId, Boolean end, int currentPage, int pageSize, Character type) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CompanyMessage.class);
        if (end != null) {
            detachedCriteria.add(Restrictions.eq("endStatus", end));
        }
        if (type != null) {
            detachedCriteria.add(Restrictions.eq("type", type));
        }
        detachedCriteria.add(Restrictions.eq("leaderId", leaderId)).addOrder(Order.asc("date"));
        PageDto<List<CompanyMessage>> page = new PageDto<>(--currentPage * pageSize, pageSize, detachedCriteria);
        dao.pageByCriteria(page);
        List<CompanyMessage> idEntities = page.getT();
        Collections.sort(idEntities, new Comparator<CompanyMessage>() {
            @Override
            public int compare(CompanyMessage o1, CompanyMessage o2) {
                if (o1.getReadStatus() == null || !o1.getReadStatus()) {
                    return 1;
                }
                if (o2.getReadStatus() == null || !o2.getReadStatus()) {
                    return -1;
                }
                return 0;
            }
        });
        CompanyMessageDto.copyFrom(idEntities);
        PageDto<List<CompanyMessageDto>> result = new PageDto<>();
        result.setT(CompanyMessageDto.copyFrom(idEntities));
        result.setMaxResult(page.getMaxResult());
        result.setFirstResult(page.getFirstResult());
        result.setTotalPage(page.getTotalPage());
        return result;
    }

    @Override
    public void read(Long messageId) {
        CompanyMessage byId = findById(messageId);
        Assert.notNull(byId, "消息通知对象不能为空!");
        byId.setReadStatus(true);
        merge(byId);
    }

    @Override
    public CompanyMessage getCompanyMessage(CompanyMessage.Type type, Long referenceId) {
        return findSingleCriteria(DetachedCriteria.forClass(CompanyMessage.class).add(Restrictions.eq("type", type.getType())).add(Restrictions.eq("refrenceId", referenceId)));
    }

    @Override
    public MessageCountDto messageCount(Long leaderId, Character type) {
        StringBuffer sql = new StringBuffer("SELECT count(id)  from tu_company_message where leader_id=?");
        if (type != null) {
            sql.append(" and type='"+type+"'");
        }
        Long allCount = getCountBySql(sql.toString(), leaderId);
        Long notEndCount = getCountBySql(sql.toString() + " and end_status=0", leaderId);
        Long endCount = getCountBySql(sql.toString() + " and end_status=1", leaderId);
        LOOGER.info("SQL:\t"+sql.toString());
        LOOGER.info("type:\t"+type);
        return new MessageCountDto(allCount.intValue(), endCount.intValue(), notEndCount.intValue());
    }

    @Override
    public ApplyDto applyInfo(Long applyId) {
        ApplyDto applyDto = new ApplyDto();
        LinkDto currentLink = applyDto.getCurrentLink();
        currentLink.setApplyAt(getCreateTime(applyId));
        currentLink.setPolicyShortTitle(getPolicy(applyId));
        currentLink.setApplyStatus(getCurrentActivy(applyId));
        setLastUserAndAt(currentLink, applyId);
        currentLink.setRejectList(getRejectList(applyId));
        LOOGER.info(applyDto.toString());
        String value = getOnLineData(applyId);
        JSONObject jsonObject = JSON.parseObject(value);
        if (jsonObject != null) {
            JSONArray items = jsonObject.getJSONArray("items");
            if (items != null) {
                for (int i = 0; i < items.size(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    String title = item.getString("title");
                    if (!StringUtils.isEmpty(title)) {
                        switch (title) {
                            case "单位名称":
                            case "单位名称（盖章）":
                                applyDto.setCompanyName(item.getString("value"));
                                break;
                            case "单位地址":
                                applyDto.setAddress(item.getString("value"));
                                break;
                            case "项目名称":
                                applyDto.setProjectName(item.getString("value"));
                                break;
                            case "法定代表人姓名":
                                applyDto.setFrName(item.getString("value"));
                                break;
                            case "法定代表人":
                                setFr(applyDto, item);
                                break;
                            case "所申报资助":
                                JSONArray szbzzObjectItem = item.getJSONArray("value");
                                if (szbzzObjectItem != null) {
                                    JSONObject ssbzzValueObj = szbzzObjectItem.getJSONObject(0);
                                    if (ssbzzValueObj != null) {
                                        applyDto.setSbzz(ssbzzValueObj.getString("title"));
                                    }
                                }
                                break;
                            case "申报总金额":
                                applyDto.setSbzje(item.getString("value"));
                                break;
                            case "法定代表人手机号码":
                                applyDto.setFrTelephone(item.getString("value"));
                                break;
                            case "项目联系人":
                                setProjectConcat(applyDto, item);
                                break;
                            case "联系人":
                                setProjectConcat2(applyDto, item);
                                break;
                        }
                    }
                }
            }
        }
        return applyDto;
    }

    private void setProjectConcat2(ApplyDto applyDto, JSONObject item) {
        JSONArray projectConcatItem = item.getJSONArray("items");
        LOOGER.info(projectConcatItem.toJSONString());
        if (projectConcatItem != null) {
            for (int j = 0; j < projectConcatItem.size(); j++) {
                JSONObject projectItemIndexObj = projectConcatItem.getJSONObject(j);
                String projectItemIndexTitle = projectItemIndexObj.getString("title");
                if (!StringUtils.isEmpty(projectItemIndexTitle)) {
                    switch (projectItemIndexTitle) {
                        case "姓名":
                            applyDto.setProjectConcat(projectItemIndexObj.getString("value"));
                            break;
                        case "联系电话":
                            applyDto.setProjectConcatPhone(projectItemIndexObj.getString("value"));
                            break;
                        case "联系方式":
                            applyDto.setProjectConcatWay(projectItemIndexObj.getString("value"));
                            break;
                    }
                }
            }
        }
    }

    private void setProjectConcat(ApplyDto applyDto, JSONObject item) {
        JSONArray projectConcatItem = item.getJSONArray("items");
        LOOGER.info(projectConcatItem.toJSONString());
        if (projectConcatItem != null) {
            for (int j = 0; j < projectConcatItem.size(); j++) {
                JSONObject projectItemIndexObj = projectConcatItem.getJSONObject(j);
                String projectItemIndexTitle = projectItemIndexObj.getString("title");
                if (!StringUtils.isEmpty(projectItemIndexTitle)) {
                    switch (projectItemIndexTitle) {
                        case "姓名":
                            applyDto.setProjectConcat(projectItemIndexObj.getString("value"));
                            break;
                        case "联系方式":
                            applyDto.setProjectConcatWay(projectItemIndexObj.getString("value"));
                            break;
                    }
                }
            }
        }
    }

    private void setFr(ApplyDto applyDto, JSONObject item) {
        JSONArray frItem = item.getJSONArray("items");
        if (frItem != null && frItem.size() != 0) {
            for (int j = 0; j < frItem.size(); j++) {
                JSONObject frItemJSONObject = frItem.getJSONObject(j);
                if (frItemJSONObject != null) {
                    String frItemTitle = frItemJSONObject.getString("title");
                    if (!StringUtils.isEmpty(frItemTitle)) {
                        switch (frItemTitle) {
                            case "法定代表人(签名)":
                                applyDto.setFrName(frItemJSONObject.getString("value"));
                                break;
                            case "手机":
                                applyDto.setFrName(frItemJSONObject.getString("value"));
                                break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 设置当前申请的最后操作人和最后操作时间
     *
     * @param currentLink
     * @param applyId
     */
    private void setLastUserAndAt(final LinkDto currentLink, Long applyId) {
        dao.getJdbcTemplate().query("select s.operate_at as rejectOperateAt,u.realname as operator,s.text from  zhjf_apply_status s left join tu_user u on u.id=s.operator_id where s.id =(select current_status from zhjf_apply where id=?) ", new ResultSetExtractor<String>() {
            @Override
            public String extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    java.util.Date date = rs.getDate(1);
                    currentLink.setLastUpdateAt(date == null ? 0 : date.getTime());
                    currentLink.setLastUpdateUser(rs.getString(2));
                    currentLink.setReason(rs.getString(3));
                }
                return null;
            }
        }, applyId);
    }

    private List<RejectDto> getRejectList(Long applyId) {
        return dao.getJdbcTemplate().query("select s.operate_at as rejectOperateAt,u.realname as operator,s.text as rejectReason from zhjf_apply a left join zhjf_apply_status s on a.current_status=s.id left join tu_user u on u.id=s.operator_id where s.apply_status=? and a.id=? order by s.operate_at desc",
                new ResultSetExtractor<List<RejectDto>>() {
                    @Override
                    public List<RejectDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        List<RejectDto> result = Lists.newArrayList();
                        while (rs.next()) {
                            result.add(new RejectDto(rs.getDate(1), rs.getString(2), rs.getString(3)));
                        }
                        return result;
                    }
                }, 29, applyId);

    }

    /**
     * 获取当前申请实例对应的线上组件相关填写的镜像数据(取申报概要)
     *
     * @param applyId 申请实例id
     * @return 最终此份申请对应的组件镜像实例
     */
    private String getOnLineData(Long applyId) {
        return dao.getJdbcTemplate().query("select archived from zhjf_archive_component where component_id in (select id from zhjf_component where title=?) and apply_id=?",
                new ResultSetExtractor<String>() {
                    @Override
                    public String extractData(ResultSet rs) throws SQLException, DataAccessException {
                        if (rs.next()) {
                            return rs.getString(1);
                        }
                        return null;
                    }
                }, "申报概要", applyId);
    }

    /**
     * 获取申请创建时间
     *
     * @param applyId 申请实例id
     * @return 创建的时间字符串，如果没有找到返回null
     */
    private long getCreateTime(Long applyId) {
        Date query = dao.getJdbcTemplate().query("select create_at from zhjf_apply where id=?", new ResultSetExtractor<Date>() {
            @Override
            public Date extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    return rs.getDate(1);
                }
                return null;
            }
        }, applyId);
        if (query != null) {
            return query.getTime();
        }
        return 0L;
    }

    /**
     * 获取申报政策
     *
     * @param applyId 申请实例id
     * @return 返回当前申请对应的申报政策
     */
    private String getPolicy(Long applyId) {
        return dao.getJdbcTemplate().query("select short_title from zhjf_policy where id=(select policy_id from zhjf_apply where id=?)", new ResultSetExtractor<String>() {
            @Override
            public String extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    return rs.getString(1);
                }
                return null;
            }
        }, applyId);
    }

    /**
     * 获取当前流程对应的描述文字
     *
     * @param applyId 当前申请实例
     * @return 返回针对于当前申请状态的文字描述，如果未曾匹配状态返回null
     */
    private String getCurrentActivy(Long applyId) {
        return dao.getJdbcTemplate().query("select apply_status from zhjf_apply_status where id=(select current_status from zhjf_apply where id=?)",
                new ResultSetExtractor<Integer>() {
                    @Override
                    public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                        if (rs.next()) {
                            return rs.getInt(1);
                        }
                        return 0;
                    }
                }, applyId) + "";
    }

    /**
     * 获取领导对应的消息通知表格相应的数量
     *
     * @param sql      sql
     * @param leaderId 当前挂点领导的id
     * @return
     */
    private Long getCountBySql(String sql, Long leaderId) {
        return dao.getJdbcTemplate().query(sql, set, leaderId);
    }

    private final ResultSetExtractor<Long> set = new ResultSetExtractor<Long>() {
        @Override
        public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0L;
        }
    };

}
