package xinrui.cloud.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

/**
 * 办理流程对象
 *
 * @author liuliuliu
 * @version 1.0
 *  2019/8/5 18:14
 */
@Entity
@Data
public class TechnologyProcess extends IdEntity {
    /**
     * 办理流程
     */
    @Column(columnDefinition = "text")
    private String process;

    /**
     * 科技金融对象
     */
    @OneToOne(fetch = FetchType.LAZY)
    private TechnologyFinancial technologyFinancial;

    public TechnologyProcess(String process) {
        this.process = process;
    }
    public TechnologyProcess() {
    }
}
