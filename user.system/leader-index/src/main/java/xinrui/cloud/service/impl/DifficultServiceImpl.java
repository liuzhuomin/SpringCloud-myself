package xinrui.cloud.service.impl;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import xinrui.cloud.domain.dto.AllCountInfoDto;
import xinrui.cloud.domain.dto.CompanyDto;
import xinrui.cloud.domain.dto.CompanyInfoDto;
import xinrui.cloud.domain.dto.DifficultCompanyDto;
import xinrui.cloud.domain.dto.DifficultCourseTO;
import xinrui.cloud.domain.dto.DifficultHistoryDto;
import xinrui.cloud.domain.dto.EchartDto;
import xinrui.cloud.service.DifficultService;
import xinrui.cloud.service.LeaderService;
import xinrui.cloud.service.OtherGroupService;
import xinrui.cloud.domain.*;

/**
 * <B>Title:</B>DifficultServiceImpl</br>
 * <B>Description:</B> 企业诉求相关接口</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/5 16:53
 */
@Service
public class DifficultServiceImpl extends BaseServiceImpl<DifficultCompany> implements DifficultService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DifficultServiceImpl.class);

    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private LeaderService leaderService;

    @Autowired
    private OtherGroupService otherGroupService;

    @Transactional
    @Override
    public DifficultCompanyDto getDifficultByDifficultId(Long difficultId) {
        DifficultCompany byId = findById(difficultId);
        Assert.notNull(byId,"该诉求件已经被删除了！无法查看详情！");
        final DifficultCompanyDto dto = DifficultCompanyDto.copyFrom(byId);
        LOGGER.info("byid==null :" + (byId == null ? "true" : "byId.getDifficultHistory()==null :" + (byId.getDifficultHistory() == null)));
        if (byId.getDifficultHistory() != null) {
            List<DifficultHistoryToInvolve> difficultHistoryToInvolve = byId.getDifficultHistory().getDifficultHistoryToInvolve();
            if (!CollectionUtils.isEmpty(difficultHistoryToInvolve)) {
                JdbcTemplate jdbcTemplate = dao.getJdbcTemplate();
                jdbcTemplate.query("select start_date,receiver_id,opinions from qybf_difficult_history where difficult_id = ? order by id desc  LIMIT 1 ;", new ResultSetExtractor<DifficultHistoryDto>() {

                    @Override
                    public DifficultHistoryDto extractData(ResultSet rs) throws SQLException, DataAccessException {
                        rs.next();
                        DifficultHistoryDto historyDto = new DifficultHistoryDto();
                        Date date = rs.getDate(1);
                        Long aLong = rs.getLong(2);
                        if (aLong != null) {
                            String query = dao.getJdbcTemplate().query("select name from tu_organization\twhere id =(select unique_group_id from tu_user where id=?)", new ResultSetExtractor<String>() {
                                @Override
                                public String extractData(ResultSet rs) throws SQLException, DataAccessException {
                                    rs.next();
                                    return rs.getString(1);
                                }
                            });
                            if (!StringUtils.isEmpty(query)) {
                                historyDto.setOrgName(query);
                            }
                        }
                        String string = rs.getString(3);
                        historyDto.setMessage(string);
                        if (date != null) {
                            historyDto.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
                        }
                        dto.setDifficultHistoryDto(historyDto);
                        return null;
                    }
                }, byId.getId());
            }
        }
        dto.setCompanyName(getCompanyName(byId.getCompanyId()));
        return dto;
    }

    /**
     *  跟据companyid获取企业名称
     * @param companyId
     * @return
     */
    private String getCompanyName(Long companyId) {
        return dao.getJdbcTemplate().query("select name from tu_company where id=?", new ResultSetExtractor<String>() {
            @Override
            public String extractData(ResultSet rs) throws SQLException, DataAccessException {
                if(rs.next()) {
                    return rs.getString(1);
                }
                return null;
            }
        },companyId);
    }


    @Override
    public void flowDifficult(Long difficultId, int type) {
        DifficultCompany byId = findById(difficultId);
        Assert.notNull(byId, "诉求对象找不到!");
        byId.setFlow(type == 0);
        merge(byId);
    }

    @Override
    @Transactional
    public void processDifficult(Long leaderId, final Long difficultId, String context, long time) {
        final DifficultCompany byId = findById(difficultId);
        final Date agoDate = new Date(time);
        Assert.notNull(byId, "诉求对象找不到");
        if (time != 0) {
            long lessTime = time - System.currentTimeMillis() - (1000 * 60 * 60 * 24);
            if (lessTime < 0) {
                lessTime = (1000 * 60 * 60 * 24);
            }
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    CompanyMessage singleCriteria = leaderService.getCompanyMessage(CompanyMessage.Type.DIFFICULT, difficultId);
                    if (singleCriteria != null) {
                        DifficultCompany currentDifficult = findById(difficultId);
                        if (currentDifficult != null) {
                            if (currentDifficult.getProcessDate().getTime() == agoDate.getTime()) {
                                singleCriteria.setMessage(findCompanyNameByCompanyId(byId.getCompanyId()) + "的诉求办理时限还有一天");
                                singleCriteria.setDate(new Date(System.currentTimeMillis()));
                            }
                        }
                    }
                }
            }, lessTime);
        }
        Assert.notEmpty(Collections.singleton(context), "批示内容不可为空!");
        Instructions ins = new Instructions();
        ins.setContext(context);
        ins.setDate(new Date(System.currentTimeMillis()));
        ins.setLeaderName(findUserNameByUserId(leaderId));
        ins.setDifficultCompany(byId);
        byId.getDifficultInstructions().add(ins);
        byId.setProcessDate(agoDate);
        merge(byId);
        CompanyMessage messageByDifficultId = leaderService.getCompanyMessage(CompanyMessage.Type.DIFFICULT, difficultId);
        messageByDifficultId.setLookAnyway(true);
        leaderService.merge(messageByDifficultId);
    }


    /**
     * 根据企业id获取企业名称
     *
     * @param id
     * @return
     */
    public String findCompanyNameByCompanyId(Long id) {
        return dao.getJdbcTemplate().query("select name from tu_company where id=?", new ResultSetExtractor<String>() {
            @Override
            public String extractData(ResultSet rs) throws SQLException, DataAccessException {
                rs.next();
                return rs.getString("name");
            }
        }, id);
    }

    public String findUserNameByUserId(Long id) {
        return dao.getJdbcTemplate().query("select realname from tu_user where id=?", new ResultSetExtractor<String>() {
            @Override
            public String extractData(ResultSet rs) throws SQLException, DataAccessException {
                rs.next();
                return rs.getString(1);
            }
        }, id);
    }

    @Override
    @Transactional
    public List<DifficultCourseTO> listHistory(Long difficultId) {
        DifficultCompany byId = findById(difficultId);
        Assert.notNull(byId, "诉求对象找不到");
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DifficultHistory.class);
        detachedCriteria.createAlias("difficult", "d");
        detachedCriteria.add(Restrictions.eq("d.id", difficultId));
        List<DifficultHistory> difficutDifficultHistories = dao.listBydCriteria(detachedCriteria);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// yyyy-mm-dd, 会出现时间不对, 因为小写的mm是代表: 秒
        List<DifficultCourseTO> courseTo = new ArrayList<DifficultCourseTO>();
        if (!CollectionUtils.isEmpty(difficutDifficultHistories)) {
            for (int i = 0; i < difficutDifficultHistories.size(); i++) {
                DifficultCourseTO to = new DifficultCourseTO();
                DifficultHistory transfer = difficutDifficultHistories.get(i);
                to.setDifficultTransferId(transfer.getDifficultTransfer().getId());
                to.setDifficultCompanyId(difficultId);
                to.setTransactorName(transfer.getReceiver().getRealname());
                if (!StringUtils.isEmpty(transfer.getOpinions())) {
                    to.setOpinions(transfer.getOpinions());
                }
                if (transfer.getStartDate() != null) {
                    to.setStartDate(sdf.format(transfer.getStartDate()));
                    System.out.println("start getTime:" + transfer.getStartDate().getTime());
                    System.out.println("end getTime:" + transfer.getAcceptDate().getTime());
                    to.setAcceptHandling(
                            formatDateTime(transfer.getStartDate().getTime() - transfer.getAcceptDate().getTime()));
                }
                if (transfer.getAcceptDate() != null) {
                    to.setAcceptDate(sdf.format(transfer.getAcceptDate()));
                }
                if (transfer.getEstimate() != null) {
                    Calendar calen = Calendar.getInstance();
                    Date now = new Date(System.currentTimeMillis()); // 当前时间
                    calen.setTime(now);
                    calen.add(Calendar.DAY_OF_YEAR, 1);
                    if (calen.getTime().after(transfer.getEstimate())) {
                        to.setShowUrge(true);
                    }
                }
                courseTo.add(to);
            }
        }
        return courseTo;
    }

    /**
     * 获取当前date的本月第一天
     *
     * @param date
     * @return
     */
    private java.util.Date getStart(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        return c.getTime();
    }

    /**
     * 获取当前date的本月最后一天
     *
     * @param date
     * @return
     */
    private java.util.Date getEnd(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = format.format(ca.getTime());
        System.out.println("===============last:" + last);
        return ca.getTime();
    }


    @Override
    public List<EchartDto> difficultEcharts(Long leaderId, Long timestamp) {
        List<CompanyDto> companyDtos = otherGroupService.listCompany(leaderId);
        if (!CollectionUtils.isEmpty(companyDtos)) {
            List<EchartDto> result = Lists.newArrayList();
            Date date = new Date(timestamp);
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startDate = format2.format(getStart(date));
            String endDate = format2.format(getEnd(date));
            for (int i = 0; i < companyDtos.size(); i++) {
                CompanyDto companyDto = companyDtos.get(i);
                result.add(getEchartByCompanyId(companyDto.getId(), startDate, endDate));
            }
            return result;
        }
        return null;
    }

    @Override
    public List<EchartDto> visitreEchart(Long leaderId, Long timestamp) {
        Date date = new Date(timestamp);
        List<CompanyDto> companyDtos = otherGroupService.listCompany(leaderId);
        if (!CollectionUtils.isEmpty(companyDtos)) {
            JdbcTemplate jdbcTemplate = dao.getJdbcTemplate();
            List<EchartDto> result = Lists.newArrayList();
            for (CompanyDto dto : companyDtos) {
                Long id = dto.getId();
                Long gdldVisitreCount = getGdldVisitreCount(date, jdbcTemplate, id);
                Long otherLeaderVisitreCount = getOtherLeaderVisitreCount(date, jdbcTemplate, id);
                result.add(new EchartDto(dto.getName(), gdldVisitreCount.intValue(), otherLeaderVisitreCount.intValue()));
            }
            return result;
        }
        return null;
    }

    /**
     * 获取指定date当前月时间范围内，其他领导走访的次数
     *
     * @param date         某个月份的date
     * @param jdbcTemplate
     * @param id           企业id
     * @return
     */
    private Long getOtherLeaderVisitreCount(Date date, JdbcTemplate jdbcTemplate, Long id) {
        return jdbcTemplate.query("select count(v.id) from qybf_visitre_cord v left join qybf_visitre_leader_ship s on v.id=s.visitre_cord_id where status=1  and company_id =?  and commit_date between ? and ? ", getRse(), id, simpleDateFormat.format(getStart(date)), simpleDateFormat.format(getEnd(date)));
    }

    /**
     * 获取当前企业被指定挂点领导访问的次数，按照时间区分
     *
     * @param date         时间
     * @param jdbcTemplate
     * @param id           企业id
     * @return
     */
    private Long getGdldVisitreCount(Date date, JdbcTemplate jdbcTemplate, Long id) {
        return jdbcTemplate.query("select count(id) from qybf_visitre_cord where status=1 and gdld_id is not null  and company_id =?  and commit_date between ? and ? ",
                getRse(), id, simpleDateFormat.format(getStart(date)), simpleDateFormat.format(getEnd(date)));
    }

    private ResultSetExtractor<Long> getRse() {
        return new ResultSetExtractor<Long>() {
            @Override
            public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                return 0L;
            }
        };
    }

    @SuppressWarnings({"deprecation", "AlibabaAvoidCallStaticSimpleDateFormat"})
	@Override
    public List<EchartDto> applyEchart(Long leaderId, Long timestamp) {
        List<CompanyDto> companyDtos = otherGroupService.listCompany(leaderId);
        if (!CollectionUtils.isEmpty(companyDtos)) {
            Date date = new Date(timestamp);
            date.setMonth(1);
            String start = simpleDateFormat.format(date);
            date.setMonth(12);
            String end = simpleDateFormat.format(date);
            LOGGER.info("start=============" + start);
            LOGGER.info("end=============" + end);
            JdbcTemplate jdbcTemplate = dao.getJdbcTemplate();
            List<EchartDto> result = Lists.newArrayList();
            for (final CompanyDto dto : companyDtos) {
                Long id = dto.getId();
                EchartDto applyAmount = jdbcTemplate.query("select prize_info from zhjf_archive_apply_form where apply_id in (select id from zhjf_apply where company_id=? and create_at between ? and ?)", new ResultSetExtractor<EchartDto>() {

                    @Override
                    public EchartDto extractData(ResultSet rs) throws SQLException, DataAccessException {
                        Long finalMoney = new Long(0L);
                        while (rs.next()) {
                            String string = rs.getString(1);
                            if (!StringUtils.isEmpty(string)) {
                                Long applyAmount = JSON.parseObject(string).getLong("applyAmount");
                                if (applyAmount != null) {
                                    finalMoney += applyAmount;
                                }
                            }
                        }
                        return new EchartDto(dto.getName(), finalMoney.intValue(), 0);
                    }
                }, id, start, end);
                result.add(applyAmount);
            }
            return result;
        }
        return null;
    }

    @Override
    public AllCountInfoDto simpleCompanyInfo(Long companyId, Long timestamp) {
        Date date = new Date(timestamp);
        JdbcTemplate jdbcTemplate = dao.getJdbcTemplate();
        Long gdldVisitreCount = getGdldVisitreCount(date, jdbcTemplate, companyId);
        Long otherLeaderVisitreCount = getOtherLeaderVisitreCount(date, jdbcTemplate, companyId);
        Long notEndDifficultCount = selectNotEndDifficultCount(companyId, simpleDateFormat.format(getStart(date)), simpleDateFormat.format(getEnd(date)));
        Long endDifficultCount = selectEndDifficultCount(companyId, simpleDateFormat.format(getStart(date)), simpleDateFormat.format(getEnd(date)));
        return new AllCountInfoDto(gdldVisitreCount, otherLeaderVisitreCount, endDifficultCount, notEndDifficultCount);
    }

    @Override
    public CompanyInfoDto companyInfo(Long companyId, Long year) {
        try{
            return  dao.getJdbcTemplate().queryForObject("select j.output_value as allPut,0 as changePut,j.total_export as exportPut ,j.total_import as importPut,j.total_assets as zc,j.total_liabilities as fz,j.total_tax_amount as ss,0 as personCount from qy_base_corp_essence b  left join qy_research_eco_corp_jj j on b.id=j.corp_id where year=? and corp_id=?",
                    CompanyInfoDto.class, year,companyId);
        }catch (Exception e){
            LOGGER.info("暂无在"+year+"年份的此企业数据，id为"+companyId);
        }
        return new CompanyInfoDto();
    }


    /**
     * 获取EcharDto对象，诉求相关的
     *
     * @param companyId
     * @param start
     * @param end
     * @return
     */
    private EchartDto getEchartByCompanyId(Long companyId, String start, String end) {
        OtherGroup company = otherGroupService.findById(companyId);
        Assert.notNull(company, "企业对象找不到");
        Long endCount = selectEndDifficultCount(companyId, start, end);
        Long notEndCount = selectNotEndDifficultCount(companyId, start, end);
        return new EchartDto(company.getName(), endCount == null ? 0 : endCount.intValue(), notEndCount == null ? 0 : notEndCount.intValue());
    }

    /**
     * 按照查询总结的诉求件的数量
     *
     * @param companyId 企业的id
     * @param start     开始的时间字符串
     * @param end       结束的时间字符串
     * @return 最终查询出来的数量
     */
    private Long selectEndDifficultCount(Long companyId, String start, String end) {
        return dao.getJdbcTemplate().query("select count(id) from qybf_difficult_company where company_id=? and  status=2 and commit_date between ? and ? ", new ResultSetExtractor<Long>() {

            @Override
            public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                rs.next();
                return rs.getLong(1);
            }
        }, companyId, start, end);
    }

    /**
     * 按照查询总结的诉求件的数量
     *
     * @param companyId 企业的id
     * @param start     开始的时间字符串
     * @param end       结束的时间字符串
     * @return 最终查询出来的数量
     */
    private Long selectNotEndDifficultCount(Long companyId, String start, String end) {
        return dao.getJdbcTemplate().query("select count(id) from qybf_difficult_company where company_id=? and  status!=2 and commit_date between ? and ? ", new ResultSetExtractor<Long>() {

            @Override
            public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                rs.next();
                return rs.getLong(1);
            }
        }, companyId, start, end);
    }


    public static String formatDateTime(long mss) {
        String DateTimes = null;
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        if (days > 0) {
            DateTimes = days + "天" + hours + "小时" + minutes + "分钟" + seconds + "秒";
        } else if (hours > 0) {
            DateTimes = hours + "小时" + minutes + "分钟" + seconds + "秒";
        } else if (minutes > 0) {
            DateTimes = minutes + "分钟" + seconds + "秒";
        } else {
            DateTimes = seconds + "秒";
        }

        return DateTimes;
    }


}
