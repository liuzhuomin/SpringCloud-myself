package xinrui.cloud.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import xinrui.cloud.domain.IdEntity;

/**
 * <B>Title:</B>CompanyMessage</br>
 * <B>Description:</B> 挂点领导消息通知类 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/5 11:00
 */
@Entity(name = "tu_company_message")
public class CompanyMessage extends IdEntity {

     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static enum Type{
         /**
          * 诉求类型的消息通知
          */
         DIFFICULT('A'),
         /**
          * 走访类型的消息通知
          */
          VISITRE('B'),
         /**
          * 资金申报类型的消息通知
          */DECLARE('C');
        private Character type;
        Type(Character type){
            this.type=type;
        }
        public Character getType() {
            return type;
        }
    }

    /**
     * 消息通知4
     */
    private String message;
    /**
     * 收到消息的时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;
    /**
     * 是否阅读了此条消息
     */
    private Boolean readStatus=false;
    /**
     * 当前消息的类型 A代表是诉求的消息，B代表是走访的消息，C代表是申报的消息
     */
    private Character type;
    /**
     * 此条消息关联的id，根据type的不同寻找不同的表格
     */
    private Long refrenceId;
    /**
     * 此条消息是否已经完结，false为未曾完结，true代表是已经办结的消息
     */
    private Boolean endStatus=false;
    /**
     * 当前挂点领导的id
     */
    private Long leaderId;
    /**
     * 为true代表领导对诉求件进行了跟踪或者批示
     */
    private Boolean lookAnyway=false;

    /**
     * 关于走访相关消息的类型， 0任务安排，1时间调整，2取消走访，3走访完成,4时限提醒
     */
    @Column(name="visitreType",columnDefinition="int default -1")
    private Integer visitreType=-1;

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

    public Boolean getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Boolean readStatus) {
        this.readStatus = readStatus;
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
