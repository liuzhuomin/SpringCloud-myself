package xinrui.cloud.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 贷款类别对象
 *
 * @author liuliuliu
 * @version 1.0
 *  2019/8/5 16:26
 */
@Entity
@Getter
@Setter
public class TechnologyLoanType extends IdEntity {

    /**
     * 贷款类别
     */
    @NotNull
    private String type;

    /**
     * 科技金融对象
     */
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = TechnologyFinancial.class)
    private TechnologyFinancial technologyFinancial;
}
