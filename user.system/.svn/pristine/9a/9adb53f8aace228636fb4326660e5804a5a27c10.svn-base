package xinrui.cloud.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="project_audit")
public class ProjectAudit  extends IdEntity {
    //填写处理意见
    @Lob
    private String handleOpinion;

    //处理时间
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date handleTime;

    //所关联的项目
    @ManyToOne
    @JoinColumn(name = "projectInner_id")
    private ProjectInner projectInner;

    /** 审核人的ID */
    private Long uid;
    /** 审核人的姓名 */
    private String uName;

    //载体的ID
    private Long carrierId;

     // 项目审核状态的标识初始化状态为1未审核(审核中),2通过审核,3审核未通过
    private Integer proAuditFlag;
}
