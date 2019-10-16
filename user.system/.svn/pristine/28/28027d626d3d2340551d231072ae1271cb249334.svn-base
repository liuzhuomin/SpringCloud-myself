package xinrui.cloud.domain;

import javax.persistence.*;

/**
 * 常见问题对象
 * @author liuliuliu
 * @version 1.0
 *  2019/8/5 18:14
 */
@Entity
public class TechnologyUsualProblem extends  IdEntity {

    /**
     * 问题标题
     */
    private String title;

    /**
     * 问题
     */
    private String question;

    /**
     * 答案
     */
    @Column(columnDefinition = "text")
    private String answer;

    /**
     * 科技金融对象
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private TechnologyFinancial technologyFinancial;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public TechnologyFinancial getTechnologyFinancial() {
        return technologyFinancial;
    }

    public void setTechnologyFinancial(TechnologyFinancial technologyFinancial) {
        this.technologyFinancial = technologyFinancial;
    }
}
