package xinrui.cloud.domain;

import javax.persistence.Entity;

/**
 * 贷款期限对象
 *
 * @author liuliuliu
 * @version 1.0
 *  2019/8/5 17:48
 */
@Entity
public class TechnologyLoanDate extends IdEntity {
    /**
     * 贷款期限的值
     */
    private int date;

    /**
     * 后缀
     */
    private String suffix;

    /**
     * 获取完整的时间值
     * @return
     */
    public String getFullDate() {
        return date + suffix;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }


}
