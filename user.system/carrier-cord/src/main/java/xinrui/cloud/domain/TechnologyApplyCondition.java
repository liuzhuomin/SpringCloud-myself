package xinrui.cloud.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 申请条件对象
 *
 * @author liuliuliu
 * @version 1.0
 *  2019/8/5 18:07
 */
@Entity
@Getter
@Setter
public class TechnologyApplyCondition extends IdEntity {
    /**
     * 申请条件
     */
    @Column(columnDefinition = "text")
    private String conditions;

    /**
     * 科技金融对象
     */
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = TechnologyFinancial.class)
    private TechnologyFinancial technologyFinancial;

    public TechnologyApplyCondition() {
    }

    public TechnologyApplyCondition(String conditions) {
        this.conditions = conditions;
    }
}
