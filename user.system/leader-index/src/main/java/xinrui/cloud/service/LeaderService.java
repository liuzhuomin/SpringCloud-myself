package xinrui.cloud.service;

import xinrui.cloud.domain.CompanyMessage;
import xinrui.cloud.domain.dto.ApplyDto;
import xinrui.cloud.domain.dto.CompanyMessageDto;
import xinrui.cloud.domain.dto.MessageCountDto;
import xinrui.cloud.dto.PageDto;

import java.util.List;

/**
 * 挂点领导相关业务逻辑处理层
 */
public interface LeaderService extends BaseService<CompanyMessage> {

    /**
     * 根据挂点领导id获取他的所有消息通知,并且将已经阅读的消息排序在前面，未曾阅读的消息排序在后面，各自按照时间升序
     * @param leaderId
     *      挂点领导的id即user表的id
     * @param currentPage   当前页码
     * @param pageSize      一页大小
     * @param type
     * @pparm end
     *      代表是否查询已经完结的，传递true查询已经完结的消息，false查询未曾完结的消息，如果传入null则查询所有的消息(完结或者未曾完结)
     * @return  当前领导所有关联的消息
     */
    PageDto<List<CompanyMessageDto>> listMessageByLeaderId(Long leaderId, Boolean end, int currentPage, int pageSize, Character type);


    /**
     * 传递当前消息通知对象的id，如果返回正常即完成阅读
     * @param messageId 消息通知对象的id
     */
    void read(Long messageId);



    /**
     * 根据type类型和关联id获取到消息通知对象
     * @param type  类型  A
     * @param referenceId
     * @return
     */
     CompanyMessage getCompanyMessage(CompanyMessage.Type type, Long referenceId);

    /**
     * 根据挂点领导id获取当前领导的所有消息通知个数
     * @param leaderId     领导id
     * @param type         消息通知的类型
     * @return
     */
    MessageCountDto messageCount(Long leaderId, Character type);

    /**
     * 根据申请实例id获取申请实例相关数据
     * @param applyId
     * @return
     */
    ApplyDto applyInfo(Long applyId);




}
