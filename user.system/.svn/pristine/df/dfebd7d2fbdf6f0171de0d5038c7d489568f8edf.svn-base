package xinrui.cloud.domain;

import javax.persistence.Entity;

/**
 * 贷款额度对象
 *
 * @author liuliuliu
 * @version 1.0
 *  2019/8/5 17:48
 */
@Entity
public class TechnologyLoanAmount extends IdEntity {

    /**
     * 贷款额度值(must be null)
     */
    private Integer amount;
    /**
     * 贷款额度后缀(不限定)
     */
    private String suffix;

    /**
     * 获取完整的贷款额度;
     * <li/>例如:
     * {@link #amount}(100) + {@link #suffix} (万元)
     * <li/>如果{@link #amount}为null,返回{@link #suffix}，{@link #suffix}的值可能为文本描述
     *
     * @return
     */
    public String getFullAmount() {
        if (amount == null) {
            return suffix;
        }
        return amount + suffix;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
