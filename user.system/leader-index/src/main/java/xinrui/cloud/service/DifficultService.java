package xinrui.cloud.service;

import xinrui.cloud.domain.DifficultCompany;
import xinrui.cloud.domain.dto.*;

import java.util.List;

/**
 * <B>Title:</B>DifficultService</br>
 * <B>Description:</B>  </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/5 16:52
 */
public interface DifficultService extends BaseService<DifficultCompany> {

    /**
     * 根据诉求id获取诉求dto对象
     * @param difficultId   诉求对象
     * @return      获取到的诉求详情对象
     */
    DifficultCompanyDto getDifficultByDifficultId(Long difficultId);

    /**
     * 跟踪或者取消跟踪当前诉求件
     * @param difficultId   诉求的id
     *
     * @param type      操作的类型，0代表跟踪，1代表取消跟踪
     */
    void flowDifficult(Long difficultId, int type);

    /**
     * 对当前诉求件进行批示或者进行办理时间的限制
     * @param leaderId
     * @param difficultId   诉求件的id
     * @param context       对诉求的批示文本
     * @param time          诉求的办理时间限制
     */
    void processDifficult(Long leaderId, Long difficultId, String context, long time);

    /**
     * 列出当前诉求的所有办理历程
     * @param difficultId       诉求的id
     * @return  诉求办理历程对象
     */
    List<DifficultCourseTO> listHistory(Long difficultId);

    /**
     * 根据挂点领导获取他挂点企业和关注企业的诉求相关echart数据传输对象
     * @param leaderId  领导id
     * @param timestamp 时间戳
     * @return
     */
    List<EchartDto> difficultEcharts(Long leaderId, Long timestamp);

    /**
     * 根据挂点领导获取他挂点企业和关注企业的走访相关echart数据传输对象
     * @param leaderId  领导id
     * @param timestamp 时间戳
     * @return
     */
    List<EchartDto> visitreEchart(Long leaderId, Long timestamp);

    /**
     * 根据挂点领导获取他挂点企业和关注企业的申报相关echart数据传输对象
     * @param leaderId  领导id
     * @param timestamp 时间戳
     * @return
     */
    List<EchartDto> applyEchart(Long leaderId, Long timestamp);

    /**
     * 按照时间获取当月这个企业的诉求和走访统计数据
     * @param companyId     企业id
     * @param timestamp     时间毫秒值
     * @return
     */
    AllCountInfoDto simpleCompanyInfo(Long companyId, Long timestamp);

    /**
     *根据企业id和年份获取当年这个企业的简略信息
     * @param companyId 企业id
     * @param year      当前年份
     * @return CompanyInfoDto   企业简略信息对象
     */
    CompanyInfoDto companyInfo(Long companyId, Long year);
}
