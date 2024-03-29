package xinrui.cloud.domain.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.BootException;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.Problem;
import xinrui.cloud.domain.ProblemModel;
import xinrui.cloud.enums.ProblemStatus;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

@ApiModel("用于单选类型(添加，修改时候json参数的接收，和返回编辑的时候返回的数据)")
public class ProblemDto2 extends IdEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "问题的标题", position = 3)
    private String title;

    @ApiModelProperty(value = "问题的名称", position = 4)
    private String name;

    @ApiModelProperty(value = "问题的状态", position = 6)
    private int status;

    @ApiModelProperty(value = "只有单选的时候才需要这个值，标识单选的二级选项是什么问题类型,(单选(0),填空(2))", position = 7)
    private int childStatus = ProblemStatus.SINGLE_RADIO.getStatus();

    @ApiModelProperty(value = "一级选项", position = 8)
    private List<ProblemModelDto> first = Lists.newArrayList();

    @ApiModelProperty(value = "二级选项", position = 9)
    private List<ProblemModelDto> second = Lists.newArrayList();

    @ApiModelProperty(value = "仅仅单选有效，对单选触发结果百分比计算完成之后的，一个最高限制金额", position = 10)
    private Integer max;

    @ApiModelProperty(value = "当前独选按钮是否针对上一个资助额，如果不是则是针对所有的资助额度", position = 11)
    private boolean last;

    @ApiModelProperty(value = "标识是否是幅度上涨", position = 10)
    private boolean isAmplitude = false;

    public ProblemDto2() {
    }

    public ProblemDto2(ProblemStatus childStatus, String title) {
        this.childStatus = childStatus.getStatus();
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getChildStatus() {
        return childStatus;
    }

    public void setChildStatus(int childStatus) {
        this.childStatus = childStatus;
    }

    public List<ProblemModelDto> getFirst() {
        return first;
    }

    public void setFirst(List<ProblemModelDto> first) {
        this.first = first;
    }

    public List<ProblemModelDto> getSecond() {
        return second;
    }

    public void setSecond(List<ProblemModelDto> second) {
        this.second = second;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }


    public boolean isAmplitude() {
        return isAmplitude;
    }

    public void setAmplitude(boolean amplitude) {
        isAmplitude = amplitude;
    }

    public static Problem toProblem2(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(json);
        Problem problem = new Problem(jsonObject.getString("title"));
        problem.setChildStatus(jsonObject.getIntValue("childStatus"));
//		if (jsonObject.containsKey("value"))
//			problem.setValue(jsonObject.getString("value"));
        if (jsonObject.containsKey("id")) {
            problem.setId(jsonObject.getLong("id"));
        }
        JSONArray first = jsonObject.getJSONArray("first");
        JSONArray second = jsonObject.getJSONArray("second");
        List<ProblemModel> models = getProblemModel(first);
        if (CollectionUtils.isEmpty(first)) {
            throw new BootException("一级选项不能为空");
        }
        List<ProblemModel> models2 = getProblemModel(second);
        for (ProblemModel problemModel : models) {
            for (ProblemModel problemModel2 : models2) {
                problemModel2.setParent(problemModel);
            }
            problemModel.setChildren(models2);
            problemModel.setProblem(problem);
        }
        problem.setChild(models);
        return problem;
    }

    private static List<ProblemModel> getProblemModel(JSONArray first) {
        List<ProblemModel> result = Lists.newArrayList();
        if (first == null || first.size() == 0) {
            return result;
        }
        for (int i = 0; i < first.size(); i++) {
            JSONObject jsonObject = first.getJSONObject(i);
            ProblemModel model = new ProblemModel();
            if (jsonObject.containsKey("id")) {
                model.setId(jsonObject.getLong("id"));
            }
            if (jsonObject.containsKey("name")) {
                model.setName(jsonObject.getString("name"));
            }
//			if (jsonObject.containsKey("value"))
//				model.setValue(jsonObject.getString("value"));
            result.add(model);
        }
        return result;
    }

    public static ProblemDto2 copy(Problem findById) {
        ProblemDto2 dto2 = new ProblemDto2();
        List<ProblemModel> child = findById.getChild();
        List<ProblemModel> children = null;
        for (int i = 0; i < child.size(); i++) {
            ProblemModel problemModel = child.get(i);
            if (i == 0) {
                children = problemModel.getChildren();
            }
            problemModel.setChildren(null);
        }
        if (!CollectionUtils.isEmpty(child)) {
            dto2.getFirst().addAll(ProblemModelDto.copyList(child));
        }
        if (!CollectionUtils.isEmpty(children)) {
            dto2.getSecond().addAll(ProblemModelDto.copyList(children));
        }
        BeanUtils.copyProperties(findById, dto2, "first", "second");
        return dto2;
    }


}
