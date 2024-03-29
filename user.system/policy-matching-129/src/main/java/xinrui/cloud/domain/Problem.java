package xinrui.cloud.domain;

import com.google.common.collect.Lists;
import xinrui.cloud.BootException;
import xinrui.cloud.enums.ProblemStatus;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

/**
 * <B>Title:</B>Problem.java</br>
 * <B>Description:</B>问题编辑模板对象-对应四个状态（单选（单选才有子集合），独选，填空，空）</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年3月28日
 */
@Entity(name = "policy_problem")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Problem extends NameEntity {

    public Problem() {
        super();
    }

    public Problem(String name, String description) {
        super(name, description);
    }

    /**
     * 设置当前的问题编辑对象的标题和所属类型
     *
     * @param title  问题编辑模板的标题
     * @param status 当前问题编辑模板的类型
     */
    public Problem(String title, ProblemStatus status) {
        super();
        this.title = title;
        checkMyselfStatus(status.getStatus());
    }

    /**
     * 设置当前的问题编辑对象的标题和所属类型
     *
     * @param status 当前问题编辑模板的类型
     * @param title  问题编辑模板的标题
     */
    public Problem(ProblemStatus status, String title) {
        super();
        this.title = title;
        checkMyselfStatus(status.getStatus());
    }

    /**
     * 设置当前的问题编辑对象的标题和所属类型
     *
     * @param status 当前问题编辑模板的类型
     * @param title  问题编辑模板的标题
     * @param name   当前问题编辑模板的变量名称，或者触发器表达式等
     */
    public Problem(ProblemStatus status, String title, String name) {
        super(name);
        this.title = title;
        checkMyselfStatus(status.getStatus());
    }

    /**
     * 设置当前的问题编辑对象的标题和所属类型以及其子选项的类型
     *
     * @param status       当前问题编辑模板的类型
     * @param childeStatus 子选项的类型
     * @param title        问题编辑模板的标题
     */
    public Problem(ProblemStatus status, ProblemStatus childeStatus, String title) {
        super();
        this.title = title;
        checkMyselfStatus(status.getStatus());
        checkChildStatus(childeStatus.getStatus());
    }

    public Problem(String title) {
        this.title = title;
    }

    private static final long serialVersionUID = 1L;

    /**
     * 问题类型,标识当前问题编辑模板是(单选(0)、独选(1),填空(2),空(3))中的哪一个类型
     */
    private int status = ProblemStatus.SINGLE_RADIO.getStatus();

    /**
     * 只有单选的时候才需要这个值，标识单选的二级选项是什么问题类型,(单选(1),填空(2))
     */
    private int childStatus = ProblemStatus.SINGLE_RADIO.getStatus();

    /**
     * 当前问题的标题
     */
    private String title;

    /**
     * 仅仅单选有效，对单选触发结果百分比计算完成之后的，一个最高限制金额
     */
    private Integer max;

    /**
     * 当前独选按钮是否针对上一个资助额，如果不是则是针对所有的资助额度
     */
    private boolean last = false;

    /**
     * 标识是否是幅度上涨
     */
    private boolean isAmplitude = false;

    /**
     * 一级选项
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "problem")
    @Fetch(FetchMode.SUBSELECT)
    private List<ProblemModel> child = Lists.newArrayList();

    /**
     * 问题限制
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "problem")
    @Fetch(FetchMode.SUBSELECT)
    private List<ProblemLimit> problemLimits = Lists.newArrayList();

    /**
     * 环节实例对象
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private PolicyActivity activity;

    public Problem(String name, ProblemStatus status, String title, Integer max, Boolean last, Boolean isAmplitude) {
        super(name);
        this.status = status.getStatus();
        this.title = title;
        this.max = max;
        this.last = last == null ? null : last;
        this.isAmplitude = isAmplitude == null ? false : isAmplitude;
    }

    public boolean isLast() {
        return last;
    }

    public boolean isAmplitude() {
        return isAmplitude;
    }

    public void setAmplitude(boolean amplitude) {
        isAmplitude = amplitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ProblemModel> getChild() {
        return child;
    }

    public void setChild(List<ProblemModel> child) {
        this.child = child;
    }

    public PolicyActivity getActivity() {
        return activity;
    }

    public void setActivity(PolicyActivity activity) {
        this.activity = activity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(ProblemStatus status) {
        this.status = status.getStatus();
    }


    public int getChildStatus() {
        return childStatus;
    }

    public void setChildStatus(ProblemStatus childStatus) {
        checkChildStatus(childStatus.getStatus());
    }

    private void checkChildStatus(int childStatusInt) {
        if (childStatusInt != ProblemStatus.SINGLE_RADIO.getStatus()
                && childStatusInt != ProblemStatus.TEXT.getStatus()) {
            throw new BootException("子选项必须是单选或者填空类型");
        }
        this.childStatus = childStatusInt;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public boolean getLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public void setStatus(int status) {
        checkMyselfStatus(status);
    }

    /**
     * 检查当前问题的状态设置是否合法
     *
     * @param status 必须为{@link ProblemStatus#values()}每一个枚举的值
     * @return 如果符合返回true，否则返回false
     */
    private void checkMyselfStatus(int status) {
        ProblemStatus[] values = ProblemStatus.values();
        for (ProblemStatus problemStatus : values) {
            if (status == problemStatus.getStatus()) {
                this.status = status;
                return;
            }
        }
        throw new BootException("问题编辑的状态设置出错，必须是" + ProblemStatus.values() + "中的一个");
    }

    public void setChildStatus(int childStatus) {
        checkChildStatus(childStatus);
    }

    public List<ProblemLimit> getProblemLimits() {
        return problemLimits;
    }

    public void setProblemLimits(List<ProblemLimit> problemLimits) {
        this.problemLimits = problemLimits;
    }

//	@Override
//	public ToStringHelper toStringHelper() {
//		return super.toStringHelper().add("activity", activity).add("child", child);
//	}

}
