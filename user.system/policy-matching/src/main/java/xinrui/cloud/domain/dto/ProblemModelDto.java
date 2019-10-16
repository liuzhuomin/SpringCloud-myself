package xinrui.cloud.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.ProblemModel;
import xinrui.cloud.domain.TreeEntity;
import xinrui.cloud.dto.TreeDto;
import org.springframework.util.CollectionUtils;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@ApiModel("针对问题编辑单选的，子选项")
public class ProblemModelDto extends TreeEntity<ProblemModelDto> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "等待填入的值", position = 4)
    private String value;

    @Transient
    @JsonIgnore
    private String index;

    public ProblemModelDto(String name) {
        this.name = name;
    }

    public ProblemModelDto() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 将数据传输对象{@link models}集合转换成{@code  List<ProblemModelDto>}类型的对象，并且从第一个父选项获取到子选项设置到每一级子选项
     *
     * @param models 需要被转换的数据传输对象集合
     * @return 转换后的javabean对象
     */
    public static List<ProblemModelDto> copyList(final List<ProblemModel> models) {
        if (CollectionUtils.isEmpty(models)) {
            return Lists.newArrayList();
        }
        ProblemModel problemModel = models.get(0);
        List<ProblemModel> children = problemModel.getChildren();
        if (!CollectionUtils.isEmpty(children)) {
            for (ProblemModel problemModelCopy : models) {
                if (problemModelCopy.getId().intValue() != problemModel.getId().intValue()) {
                    problemModelCopy.setChildren(new ArrayList<ProblemModel>(children));
                }
            }
        }
        return TreeDto.copyList(ProblemModelDto.class, models, 2);
    }

//	/**
//	 * 将数据传输对象{@link models}集合转换成{@code  List<ProblemModelDto>}类型的对象，并且从第一个父选项获取到子选项设置到每一级子选项
//	 * 
//	 * @param models 需要被转换的数据传输对象集合
//	 * @return 转换后的javabean对象
//	 */
//	public static List<ProblemModelDto> copyList2(final List<ProblemModel> models) {
//		if (CollectionUtils.isEmpty(models))
//			return Lists.newArrayList();
//		ProblemModel problemModel = models.get(0);
//		List<ProblemModel> children = problemModel.getChildren();
//		if (!CollectionUtils.isEmpty(children)) {
//			for (ProblemModel problemModelCopy : models) {
//				if (problemModelCopy.getId().intValue() != problemModel.getId().intValue()) {
//					problemModelCopy.setChildren(new ArrayList<ProblemModel>(children));
//				}
//			}
//		}
//		List<ProblemModelDto> copyList = TreeDto.copyList(ProblemModelDto.class, models, 2, new CopySpecailField() {
//
//			@Override
//			public <E extends TreeEntity<E>, T> void copy(T t, TreeEntity<E> e) {
//				ProblemModelDto dto = (ProblemModelDto) t;
//				ProblemModel model = (ProblemModel) e;
//				dto.setValue(model.getValue());
//				Field[] declaredFields = dto.getClass().getDeclaredFields();
//				for (Field field : declaredFields) {
//					if (field.getType().equals(String.class)) {
//						Object invokeGetByObject = DataUtil.invokeGetByObject(dto, field);
//						if (invokeGetByObject == null)
//							System.out.println(field.getName());
//						DataUtil.invokeSetByObject(dto, field, "");
//					}
//				}
//				dto.setId(System.currentTimeMillis());
//			}
//		});
//		return copyList;
//	}

    /**
     * 将{@link ProblemModelDto}转换成javabean对象
     *
     * @param resource 需要被转换的数据传输对象
     * @return 数据传输对象对应的javabean对象
     */
    public static List<ProblemModel> toEntity(List<ProblemModelDto> resource) {
        List<ProblemModel> result = Lists.newArrayList();
        if (CollectionUtils.isEmpty(resource)) {
            return result;
        }
        for (ProblemModelDto dto : resource) {
            ProblemModel model = new ProblemModel(dto.getName());
            ProblemModelDto parent = dto.getParent();
            model.setChildren(toEntity(dto.getChildren()));
//			model.setValue(dto.getValue());
            if (parent != null) {
                model.setParent(TreeDto.copy(ProblemModel.class, parent));
            }
            result.add(model);
        }
        return result;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
