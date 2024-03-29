package xinrui.cloud.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * 科技金融文件对象
 *
 * @author liuliuliu
 * @version 1.0
 *  2019/8/5 17:16
 */
@Entity
public class TechnologyFinancialFile extends FileObject {

    /**
     * 科技金融对象
     */
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = TechnologyFinancial.class)
    private TechnologyFinancial technologyFinancial;

    public TechnologyFinancial getTechnologyFinancial() {
        return technologyFinancial;
    }

    public void setTechnologyFinancial(TechnologyFinancial technologyFinancial) {
        this.technologyFinancial = technologyFinancial;
    }

}
