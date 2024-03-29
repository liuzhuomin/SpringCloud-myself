package xinrui.cloud.domain.dto;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.CompanyMessage;
import xinrui.cloud.domain.IdEntity;

@ApiModel("挂点领导消息通知类")
public class CompanyMessageDto extends IdEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "当前消息", position = 1)
    private String message;

    @ApiModelProperty(value = "收到消息通知的时间", position = 2)
    private Date date;

    @ApiModelProperty(value = "是否阅读了此条消息，true为阅读了，false为未曾阅读", position = 3)
    private Boolean readStatus;

    @ApiModelProperty(value = "当前消息的类型，A为诉求消息，B为走访消息，C为资金申办消息", position = 4)
    private Character type;

    @ApiModelProperty(value = "当前消息关联的表格的id", position = 5)
    private Long refrenceId;

    @ApiModelProperty(value = "标识此条消息是否已经完结", position = 6)
    private Boolean endStatus;

    @ApiModelProperty(value = "挂点领导id", position = 7)
    private Long leaderId;

    @ApiModelProperty(value = "标识此条消息被领导批示或者跟踪(仅限诉求有值)", position = 8)
    private Boolean lookAnyway=false;
    /**
     *                      	A xx挂点企业有走访任务
     * 	 * 						B xx领导有走访任务(对应走访消息通知类型0，任务安排)
     * 	 * 						C 时间调整((对应走访消息通知类型1，时间调整))
     * 	 * 						D 取消走访((对应走访消息通知类型2，取消走访))
     * 	 * 						E 走访完结((对应走访消息通知类型3，走访完成))
     * 	 * 						F 时间限制提醒((对应走访消息通知类型4，时间限制提醒))
     *
     *         */
    @ApiModelProperty(value = "关于走访相关消息的类型， 0任务安排，1时间调整，2取消走访，3走访完成,4时限提醒", position = 9)
    private Integer visitreType=-1;

    /**
     * messages集合转换成dto的集合
     * @param messages
     * @return
     */
    public static List<CompanyMessageDto> copyFrom(List<CompanyMessage> messages) {
        List<CompanyMessageDto> result= Lists.newArrayList();
        if(!CollectionUtils.isEmpty(messages)){
            for (CompanyMessage message:messages){
                result.add(copy(message));
            }
        }
        return result;
    }

    /**
     * messgae转换成dto
     * @param message
     * @return
     */
    private static CompanyMessageDto copy(CompanyMessage message) {
        CompanyMessageDto dto=new CompanyMessageDto();
        BeanUtils.copyProperties(message,dto);
        return dto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Character getType() {
        return type;
    }

    public void setType(Character type) {
        this.type = type;
    }

    public Long getRefrenceId() {
        return refrenceId;
    }

    public void setRefrenceId(Long refrenceId) {
        this.refrenceId = refrenceId;
    }

    public Boolean getEndStatus() {
        return endStatus;
    }

    public void setEndStatus(Boolean endStatus) {
        this.endStatus = endStatus;
    }

    public Boolean getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Boolean readStatus) {
        this.readStatus = readStatus;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

    public Boolean getLookAnyway() {
        return lookAnyway;
    }

    public void setLookAnyway(Boolean lookAnyway) {
        this.lookAnyway = lookAnyway;
    }

    public Integer getVisitreType() {
        return visitreType;
    }

    public void setVisitreType(Integer visitreType) {
        this.visitreType = visitreType;
    }
}
