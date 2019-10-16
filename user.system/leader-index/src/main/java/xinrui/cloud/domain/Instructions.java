package xinrui.cloud.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * <B>Title:</B>Instructions</br>
 * <B>Description:</B> 批示对象 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/5 15:06
 */
@Entity
public class Instructions  extends IdEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 批示的文本
     */
    @Column(name="context",columnDefinition ="TEXT")
    private String context;

    /**
     * 批示时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    /**
     * 领导名称
     */
    private String leaderName;

    /**
     * 关联的诉求对象
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private DifficultCompany difficultCompany;


    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public DifficultCompany getDifficultCompany() {
        return difficultCompany;
    }

    public void setDifficultCompany(DifficultCompany difficultCompany) {
        this.difficultCompany = difficultCompany;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }
}
