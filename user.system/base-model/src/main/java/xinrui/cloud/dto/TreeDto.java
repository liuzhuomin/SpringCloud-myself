package xinrui.cloud.dto;

import java.util.List;
import java.util.RandomAccess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

import io.swagger.annotations.ApiModel;
import xinrui.cloud.domain.TreeEntity;

/**
 * @author liuliuliu
 *
 */
@ApiModel("树结构对象")
public class TreeDto extends TreeEntity<TreeDto> {

	private static final Logger LOG = LoggerFactory.getLogger(TreeDto.class);

	private static final long serialVersionUID = 1L;

	/**
	 * 默认复制继承自{@link TreeEntity}的树结构(默认copy父节点和所子节点并且子节点深度为1(只递归一次))
	 * 
	 * @param resource 需要被复制的原对象
	 * @return 最終获得的根对象
	 */
	public static <E extends TreeEntity<E>, T extends TreeEntity<T>> T copy(Class<T> dtoClass, TreeEntity<E> resource) {
		return copy(dtoClass, resource, true, true, null);
	}

	/**
	 * 默认复制继承自{@link TreeEntity}的树结构(默认copy父节点和所子节点)深度為deep
	 * 
	 * @param resource 需要被复制的原对象
	 * @param deep     需要遍历的深度
	 * @return 最終获得的根对象
	 */
	public static <E extends TreeEntity<E>, T extends TreeEntity<T>> T copy(Class<T> dtoClass, TreeEntity<E> resource,
			int deep) {
		return copy(dtoClass, resource, deep, true, true, null);
	}

	/**
	 * 默认复制继承自{@link TreeEntity}的树结构(默认copy父节点和所子节点)深度為deep
	 * 
	 * @param resource         需要被复制的原对象
	 * @param deep             需要遍历的深度
	 * @param copySpecailField 拷贝特殊字段
	 * @param ignoreFileds     被忽略的字段
	 * @return 最終获得的根对象
	 */
	public static <E extends TreeEntity<E>, T extends TreeEntity<T>> T copy(Class<T> dtoClass, TreeEntity<E> resource,
			int deep, CopySpecailField copySpecailField, String... ignoreFileds) {
		return copy(dtoClass, resource, deep, true, true, copySpecailField, ignoreFileds);
	}

	/**
	 * 默认复制继承自{@link TreeEntity}的树结构(子节点深度为1(只递归一次))
	 * 
	 * @param resource     需要被复制的原对象
	 * @param needChildren 为true代表需要copy子节点
	 * @param needParent   为true代表需要copy父节点
	 * @return 最終获得的根对象
	 */
	public static <E extends TreeEntity<E>, T extends TreeEntity<T>> T copy(Class<T> dtoClass, TreeEntity<E> resource,
			boolean needChildren, boolean needParent) {
		return copy(dtoClass, resource, needChildren, needParent, null);
	}

	/**
	 * 默认复制继承自{@link TreeEntity}的树结构
	 * 
	 * @param resource     需要被复制的原对象
	 * @param needChildren 为true代表需要copy子节点
	 * @param needParent   为true代表需要copy父节点
	 * @param deep         需要遍历的深度(小于0与0相同)
	 * @return 最終获得的根对象
	 */
	public static <E extends TreeEntity<E>, T extends TreeEntity<T>> T copy(Class<T> dtoClass, TreeEntity<E> resource,
			int deep, boolean needChildren, boolean needParent) {
		return copy(dtoClass, resource, deep, needChildren, needParent, null);
	}

	/**
	 * 默认复制继承自{@link TreeEntity}的树结构(默认copy父节点和所子节点并且子节点深度为1(只递归一次))
	 * 
	 * @param resource         需要被复制的原对象
	 * @param copySpecailField {@link CopySpecailField}如果实现了此接口，则可自定义copy的参数
	 * @param ignoreFileds     需要設置的被忽略的字段
	 * @return 最終获得的根对象
	 */
	public static <E extends TreeEntity<E>, T extends TreeEntity<T>> T copy(Class<T> dtoClass, TreeEntity<E> resource,
			CopySpecailField copySpecailField, String... ignoreFileds) {
		return copy(dtoClass, resource, true, true, copySpecailField, ignoreFileds);
	}

	/**
	 * 默认复制继承自{@link TreeEntity}的树结构(子节点深度为1(只递归一次))
	 * 
	 * @param resource         需要被复制的原对象
	 * @param needChildren     为true代表需要copy子节点
	 * @param needParent       为true代表需要copy父节点
	 * @param copySpecailField {@link CopySpecailField}如果实现了此接口，则可自定义copy的参数
	 * @param ignoreFileds     需要設置的被忽略的字段
	 * @return 最終获得的根对象
	 */
	public static <E extends TreeEntity<E>, T extends TreeEntity<T>> T copy(Class<T> dtoClass, TreeEntity<E> resource,
			boolean needChildren, boolean needParent, CopySpecailField copySpecailField, String... ignoreFileds) {
		return copy(dtoClass, resource, 1, needChildren, needParent, copySpecailField, ignoreFileds);
	}

	/**
	 * 默认复制继承自{@link TreeEntity}树结构的{@code List}集合(默认copy父节点和所子节点并且子节点深度为1(只递归一次))
	 * 
	 * @param resource 需要被复制的原对象集合
	 * @return 最終获得的根对象集合
	 */
	public static <E extends TreeEntity<E>, T extends TreeEntity<T>> List<T> copyList(Class<T> dtoClass,
			List<E> resource) {
		return copyList(dtoClass, resource, 1, true, true, null);
	}

	/**
	 * 默认复制继承自{@link TreeEntity}树结构的{@code List}集合(默认copy父节点和所子节点)子节点深度需要自定义
	 * 
	 * @param resource 需要被复制的原对象集合
	 * @param deep     需要遍历的子节点深度
	 * @return 最終获得的根对象集合
	 */
	public static <E extends TreeEntity<E>, T extends TreeEntity<T>> List<T> copyList(Class<T> dtoClass,
			List<E> resource, int deep) {
		return copyList(dtoClass, resource, deep, true, true, null);
	}

	/**
	 * 默认复制继承自{@link TreeEntity}树结构的{@code List}集合(子节点深度为1(只递归一次))
	 * 
	 * @param resource     需要被复制的原对象集合
	 * @param needChildren 为true代表需要copy子节点
	 * @param needParent   为true代表需要copy父节点
	 * @return 最終获得的根对象集合
	 */
	public static <E extends TreeEntity<E>, T extends TreeEntity<T>> List<T> copyList(Class<T> dtoClass,
			List<E> resource, boolean needChildren, boolean needParent) {
		return copyList(dtoClass, resource, 1, needChildren, needParent, null);
	}

	/**
	 * 默认复制继承自{@link TreeEntity}树结构的{@code List}集合
	 * 
	 * @param resource     需要被复制的原对象集合
	 * @param deep         需要遍历的深度
	 * @param needChildren 为true代表需要copy子节点
	 * @param needParent   为true代表需要copy父节点
	 * @return 最終获得的根对象集合
	 */
	public static <E extends TreeEntity<E>, T extends TreeEntity<T>> List<T> copyList(Class<T> dtoClass,
			List<E> resource, int deep, boolean needChildren, boolean needParent) {
		return copyList(dtoClass, resource, deep, needChildren, needParent, null);
	}

	/**
	 * 默认复制继承自{@link TreeEntity}树结构的{@code List}集合(默认查找父节点和子节点)
	 * 
	 * @param resource         需要被复制的原对象集合
	 * @param deep             需要遍历的深度
	 * @param copySpecailField {@link CopySpecailField}对象,用于copy特殊字段
	 * @return 最終获得的根对象集合
	 */
	public static <E extends TreeEntity<E>, T extends TreeEntity<T>> List<T> copyList(Class<T> dtoClass,
			List<E> resource, int deep, CopySpecailField copySpecailField) {
		return copyList(dtoClass, resource, deep, true, true, copySpecailField);
	}

	/**
	 * 默认复制继承自{@link TreeEntity}树结构的{@code List}集合(默认查找父节点和子节点)
	 * 
	 * @param resource         需要被复制的原对象集合
	 * @param deep             需要遍历的深度
	 * @param copySpecailField {@link CopySpecailField}对象,用于copy特殊字段
	 * @param ignoreFileds     需要忽略的字段
	 * @return 最終获得的根对象集合
	 */
	public static <E extends TreeEntity<E>, T extends TreeEntity<T>> List<T> copyList(Class<T> dtoClass,
			List<E> resource, int deep, CopySpecailField copySpecailField, String... ignoreFileds) {
		return copyList(dtoClass, resource, deep, true, true, copySpecailField, ignoreFileds);
	}

	/**
	 * 默认复制继承自{@link TreeEntity}的树结构
	 * 
	 * @param resource         需要被复制的原对象
	 * @param deep             需要遍历的深度(小于0与0相同)
	 * @param needChildren     为true代表需要copy子节点
	 * @param needParent       为true代表需要copy父节点
	 * @param copySpecailField {@link CopySpecailField}如果实现了此接口，则可自定义copy的参数
	 * @param ignoreFileds     需要設置的被忽略的字段
	 * @return 最終获得的根对象
	 */
	public static <E extends TreeEntity<E>, T extends TreeEntity<T>> T copy(Class<T> clazz, TreeEntity<E> e, int deep,
			boolean needChildren, boolean needParent, CopySpecailField copySpecailField, String... ignoreFileds) {
		T treeDto = null;
		try {
			try {
				treeDto = clazz.newInstance();
			} catch (IllegalArgumentException | SecurityException e1) {
				LOG.warn("寻找构造参数失败:" + e1);
			}
		} catch (InstantiationException e1) {
			LOG.warn(
					"the class object represents an abstract class, an interface, an array class, a primitive type, or void • the class has no nullary constructor ");
		} catch (IllegalAccessException e1) {
			LOG.warn("参数不对应，非法调用！");
		}
		if (e == null) {
            return treeDto;
        }
		List<String> asList = Lists.newArrayList();
		if (ignoreFileds != null && ignoreFileds.length != 0) {
			for (String string : ignoreFileds) {
				asList.add(string);
			}
		}
		asList.add("parent");
		asList.add("children");
		asList.add("hasChild");
		BeanUtils.copyProperties(e, treeDto, asList.toArray(new String[asList.size()]));
		if (needParent && e.getParent() != null) {
			treeDto.setParent(copy(clazz, e.getParent(), false, false, copySpecailField, ignoreFileds));
		}
		if (copySpecailField != null) {
            copySpecailField.copy(treeDto, e);
        }
		if (needChildren && deep > 0) {
			treeDto.setChildren(copyList(clazz, e.getChildren(), deep, true, false, copySpecailField, ignoreFileds));
		}
		return treeDto;
	}

	/**
	 * 
	 * @param e                需要被复制的原对象集合
	 * @param deep             需要遍历的深度
	 * @param needChildren     为true代表需要copy子节点
	 * @param needParent       为true代表需要copy父节点
	 * @param copySpecailField {@link CopySpecailField}如果实现了此接口，则可自定义copy的参数
	 * @param ignoreFileds     需要設置的被忽略的字段
	 * @return
	 */
	public static <E extends TreeEntity<E>, T extends TreeEntity<T>> List<T> copyList(Class<T> dtoClass, List<E> e,
			int deep, boolean needChildren, boolean needParent, CopySpecailField copySpecailField,
			String... ignoreFileds) {
		List<T> result = Lists.newArrayList();
		if (CollectionUtils.isEmpty(e)) {
			return result;
		}
		if (e instanceof RandomAccess) {
			for (int i = 0; i < e.size(); i++) {
				result.add(
						copy(dtoClass, e.get(i), deep - 1, needChildren, needParent, copySpecailField, ignoreFileds));
			}
		} else {
			for (TreeEntity<E> tree : e) {
				result.add((copy(dtoClass, tree, deep - 1, needChildren, needParent, copySpecailField, ignoreFileds)));
				deep++;
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [parent=" + parent + ", children=" + children + ", name=" + name
				+ ", description=" + description + ", id=" + id + "]";
	}

}
