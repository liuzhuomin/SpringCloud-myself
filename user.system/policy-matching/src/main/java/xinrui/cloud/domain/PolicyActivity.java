package xinrui.cloud.domain;

import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.Lists;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

/**
 * <B>Title:</B>Activity.java</br>
 * <B>Description:</B> 政策匹配环节对象 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年3月27日
 */
@Entity(name = "policy_activity")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PolicyActivity extends IdEntity {
    private static final long serialVersionUID = 1L;

    public PolicyActivity() {
        super();
    }

    /**
     * 第一个问题编辑项所有的对象
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "activity", orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<Problem> problems = Lists.newArrayList();

    /**
     * 模版对象
     */
    @OneToOne(fetch = FetchType.LAZY, optional = true, targetEntity = PolicyTemplate.class)
    private PolicyTemplate template;

    /**
     * 当前环节进行到哪一步
     */
    private int currentIndex = 0;

    /**
     * 此环节的最大环节数(仅仅作为限制的标识)
     */
    private int maxIndex = 0;

    /**
     * 是否开始(默认为false)
     */
    private boolean start;

    /**
     * 是否结束(默认为false)
     */
    private boolean end;

    @Transient
    private boolean first;

    @Transient
    private boolean last;

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public PolicyTemplate getTemplate() {
        return template;
    }

    public void setTemplate(PolicyTemplate template) {
        this.template = template;
    }

    public List<Problem> getProblems() {
        return problems;
    }

    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getMaxIndex() {
        return maxIndex;
    }

    public void setMaxIndex(int maxIndex) {
        this.maxIndex = maxIndex;
    }

    @Override
    public ToStringHelper toStringHelper() {
        return super.toStringHelper().add("currentIndex", currentIndex).add("maxIndex", maxIndex)
                .add("template", template).add("problems", problems);
    }

}
