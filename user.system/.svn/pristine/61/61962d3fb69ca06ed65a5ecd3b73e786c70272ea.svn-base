package xinrui.cloud.util;

import com.google.common.collect.Lists;
import xinrui.cloud.dao.BaseDao;
import xinrui.cloud.domain.dto.ProblemTriggerInnerDto;
import xinrui.cloud.domain.*;
import xinrui.cloud.dto.TreeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("rawtypes")
public final class DataUtil {

    private final static Logger LOOGER = LoggerFactory.getLogger(DataUtil.class);

    /**
     * 判断object是否为基本类型
     *
     * @param object
     * @return
     */
    public static boolean isBaseType(Object object) {
        return isBaseType(object);
    }

    /**
     * 判断object是否为基本类型
     *
     * @param clazz
     * @return
     */
    public static boolean isBaseTypeOrPrimate(Class<?> clazz) {
        return isBaseType(clazz) || isPrimate(clazz);
    }

    /**
     * 判断此class类型的对象是否是基本数据类型
     *
     * @param classObj
     * @return
     */
    public static boolean isBaseType(Class classObj) {
        if (classObj.equals(Integer.class) || classObj.equals(Byte.class)
                || classObj.equals(Long.class) || classObj.equals(Double.class)
                || classObj.equals(Float.class) || classObj.equals(Character.class)
                || classObj.equals(Short.class) || classObj.equals(Boolean.class)
                || classObj.equals(String.class)) {
            return true;
        }
        return false;
    }

    /**
     * 根据objct对象获取他的id的值
     *
     * @param object 需要被处理的对象
     * @return 此对象的id
     */
    public static Long getIdByObj(Object object) {
        Assert.notNull(object, "object must not be null");
        Field idField = getIdField(object.getClass());
        Assert.notNull(idField, object.getClass().getName() + " not the jpa Entity~!");
        return (idField == null ? null : (Long) invokeGetByObject(object, idField));
    }

    /**
     * 根据字节码对象获取idfield
     *
     * @param clazz 预期的字节码对象
     * @return 拥有{@link javax.persistence.Id}注解的{@link Field}
     */
    public static Field getIdField(Class<?> clazz) {
        Assert.notNull(clazz, "clazz must not be null");
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(javax.persistence.Id.class)) {
                return field;
            }
        }
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            return getIdField(superclass);
        }
        return null;
    }

    /**
     * 根据object和field运行setter方法
     *
     * @param object 指定对象
     * @param field  指定field
     * @return 指定字段生成的setter方法在当前对象执行后返回的结果对象
     */
    public static Object invokeSetByObject(Object object, Field field, Object... objects) {
        checkObjectAndField(object, field);
        String name = field.getName();
        StringBuilder builder = new StringBuilder();
        if (name.length() == 1) {
            builder.append("set").append(name.toUpperCase()).toString();
        } else {
            builder.append("set").append(name.substring(0, 1).toUpperCase()).append(name.substring(1)).toString();
        }
        try {
            return object.getClass().getMethod(builder.toString()).invoke(object, objects);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据obj和field取到指定field的值
     *
     * @param object 指定对象
     * @param field  指定field
     * @return 指定字段生成的getter方法在当前对象执行后返回的结果对象
     */
    public static Object invokeGetByObject(Object object, Field field) {
        checkObjectAndField(object, field);
        String name = field.getName();
        StringBuilder builder = new StringBuilder();
        if (name.length() == 1) {
            builder.append("get").append(name.toUpperCase()).toString();
        } else {
            builder.append("get").append(name.substring(0, 1).toUpperCase()).append(name.substring(1)).toString();
        }
        try {
            return object.getClass().getMethod(builder.toString()).invoke(object);
        } catch (Exception e) {
            return null;
        }
    }

    private static void checkObjectAndField(Object object, Field field) {
        Assert.notNull(object, "object must not be null!");
        Assert.notNull(field, "field must not be null!");
    }

    /**
     * 判断object是否为数组类型
     *
     * @param object
     * @return
     */
    public static boolean isArray(Object array) {
        if (array instanceof Object[]) {
            return true;
        } else if (array instanceof boolean[]) {
            return true;
        } else if (array instanceof byte[]) {
            return true;
        } else if (array instanceof char[]) {
            return true;
        } else if (array instanceof double[]) {
            return true;
        } else if (array instanceof float[]) {
            return true;
        } else if (array instanceof int[]) {
            return true;
        } else if (array instanceof long[]) {
            return true;
        } else if (array instanceof short[]) {
            return true;
        }
        return false;
    }

    /**
     * 判断classObj的类型是否是基本数据类型的数组
     *
     * @param object
     * @return
     */
    public static boolean isPrimate(Class classObj) {
        return classObj.equals(boolean.class) || classObj.equals(byte.class) || classObj.equals(char.class)
                || classObj.equals(double.class) || classObj.equals(float.class) || classObj.equals(int.class)
                || classObj.equals(long.class) || classObj.equals(short.class);
    }

    /**
     * 根据逗号分割，如果没有则返回当前的，如果为空返回空集合
     *
     * @param fileds 需要被分割的字符串
     * @return 被分隔后的集合
     */
    public static List<String> split(String fileds) {
        if (fileds.contains(",")) {
            String[] split = fileds.split(",");
            return Arrays.asList(split);
        }
        LOOGER.info("不包含英文逗号',',当前传入的字段只有一个:" + fileds);
        return Lists.newArrayList(fileds);
    }

    /**
     * 将接收到的实体集合{@link #listFromDto}
     * 与数据库保存的实体集合{@link #needRemoveSourceList}按照id的值进行匹配；
     * 一旦匹配到，就拷贝dto中的基础值到当前匹配的实体对象中，并且添加到待修改集合{@link #needEditList}中，
     * 并且将{@link #needRemoveSourceList}中的此实体删除,最终{@link #needRemoveSourceList}剩下的便是等待删除的来自数据库的实体集合，
     * 如果{@link #listFromDto}中某实体的id为null，则添加到待添加集合中{@link #needAddList}
     *
     * @param listFromDto          来自数据传输对象的选项集合
     * @param needEditList         用于保存需要修改的实体的集合
     * @param needRemoveSourceList 保存了最终需要删除的实体的集合，也作为数据源必须是{@link CopyOnWriteArrayList}类型的，因为需要并发删除
     * @param needAddList          保存了最终需要添加的实体的集合
     */
    @SuppressWarnings("unchecked")
//	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, readOnly = false)
    public static <E extends TreeEntity<E>, T extends TreeEntity<T>> void subselect(List<T> listFromDto,
                                                                                    List<E> needEditList, CopyOnWriteArrayList<E> needRemoveSourceList, List<E> needAddList) {
        for (TreeEntity<T> problemModelDto : listFromDto) {
            Long id = problemModelDto.getId();
            if (id != null) {
                int intValue = id.intValue();
                for (TreeEntity<E> problemModel : needRemoveSourceList) {
                    int idOfPro = problemModel.getId().intValue();
                    if (intValue == idOfPro) { // 代表是修改
                        BeanUtils.copyProperties(problemModelDto, problemModel, "id", "children", "parent", "problem");
                        needEditList.add((E) problemModel);
                        needRemoveSourceList.remove(problemModel);
                        break;
                    }
                }
            } else { // 增加
                ProblemModel problemModel = new ProblemModel();
                BeanUtils.copyProperties(problemModelDto, problemModel, "id", "children", "parent", "problem");
                needAddList.add((E) problemModel);
            }
        }
    }

    /**
     * 将接收到的实体集合{@link #listFromDto}
     * 与数据库保存的实体集合{@link #needRemoveSourceList}按照id的值进行匹配；
     * 一旦匹配到，就拷贝dto中的基础值到当前匹配的实体对象中，并且添加到待修改集合{@link #needEditList}中，
     * 并且将{@link #needRemoveSourceList}中的此实体删除,最终{@link #needRemoveSourceList}剩下的便是等待删除的来自数据库的实体集合，
     * 如果{@link #listFromDto}中某实体的id为null，则添加到待添加集合中{@link #needAddList}
     *
     * @param listFromDto          来自数据传输对象的选项集合
     * @param needEditList         用于保存需要修改的实体的集合
     * @param needRemoveSourceList 保存了最终需要删除的实体的集合，也作为数据源必须是{@link CopyOnWriteArrayList}类型的，因为需要并发删除
     * @param needAddList          保存了最终需要添加的实体的集合
     */
    @SuppressWarnings("unchecked")
//	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, readOnly = false)
    public static <E extends TreeEntity<E>> void subselect2(List<E> listFromDto, List<E> needEditList,
                                                            CopyOnWriteArrayList<E> needRemoveSourceList, List<E> needAddList) {
        for (TreeEntity<E> problemModelDto : listFromDto) {
            Long id = problemModelDto.getId();
            if (id != null) {
                int intValue = id.intValue();
                for (TreeEntity<E> problemModel : needRemoveSourceList) {
                    int idOfPro = problemModel.getId().intValue();
                    if (intValue == idOfPro) { // 代表是修改
                        BeanUtils.copyProperties(problemModelDto, problemModel, "id", "children", "parent", "problem");
                        needEditList.add((E) problemModel);
                        needRemoveSourceList.remove(problemModel);
                        break;
                    }
                }
            } else { // 增加
                ProblemModel problemModel = new ProblemModel();
                BeanUtils.copyProperties(problemModelDto, problemModel, "id", "children", "parent", "problem");
                needAddList.add((E) problemModel);
            }
        }
        System.out.println("一级的被修改的" + needEditList);
        System.out.println("一级的被删除的" + needRemoveSourceList);
        System.out.println("一级的被添加的的" + needAddList);
    }


    /**
     * 拷贝子集到{@link merge}对象，将{@link #child}复制成pojo对象集合，并且持久化，然后将其关联到{@link #merge}
     * 如果{@link merge}被遍历的元素拥有子集，则持久子集并且与child关联
     *
     * @param child 需要被拷贝的集合
     * @param dao   dao
     * @param merge 最终拷贝到{@link #merge}中的child属性中
     */
    public static void copyProblemTriggerInnerChild(List<ProblemTriggerInnerDto> child, BaseDao dao, ProblemStatusFather merge) {
        List<ProblemTriggerInner> copyList = TreeDto.copyList(ProblemTriggerInner.class, child, 2);
        //循环遍历所有的一级项
        for (ProblemTriggerInner problemTriggerInner : copyList) {
            ArrayList<ProblemTriggerInner> copyChildren = null;
            List<ProblemTriggerInner> children = problemTriggerInner.getChildren();
            //保存之前首先将子集拿出来
            if (!CollectionUtils.isEmpty(children)) {
                // 拷贝当前子集
                copyChildren = new ArrayList<>(children);
                // 清除以前的子集并且保存当前对象
                children.clear();
            }
            problemTriggerInner.setId(null);
            ProblemTriggerInner mergeInner = dao.merge(ProblemTriggerInner.class, problemTriggerInner);
            if (!CollectionUtils.isEmpty(copyChildren)) {
                for (ProblemTriggerInner problemTriggerInnerCopy : copyChildren) {
                    ProblemTriggerInner mergeChild = dao.merge(ProblemTriggerInner.class, problemTriggerInnerCopy);
                    mergeChild.setParent(mergeInner);
                    mergeInner.addChild(mergeChild);
                    LOOGER.info("成功保存一条二级项数据:" + mergeInner.getChildren().size());
                }
            }
            //如果是ProblemTrigger类型的则调用setProblemTrigger函数关联
            if (merge instanceof ProblemTrigger) {
                ProblemTrigger pb = (ProblemTrigger) merge;
                mergeInner.setProblemTrigger(pb);
                pb.getChild().add(mergeInner);
                LOOGER.info(pb.getClass().getSimpleName() + "成功保存一条一级项数据:" + pb.getChild().size());
            }
            //ProblemTriggerResult类型的则调用setTriggerResult函数关联
            if (merge instanceof ProblemTriggerResult) {
                ProblemTriggerResult result = (ProblemTriggerResult) merge;
                mergeInner.setTriggerResult(result);
                result.getChild().add(mergeInner);
                LOOGER.info(result.getClass().getSimpleName() + "成功保存一条一级项数据:" + result.getChild().size());
            }
        }
    }

    /**
     * 根据字段名查找VariableId
     *
     * @param inner 公式记录对象
     * @param name  字段名
     * @return 匹配到的VariableId，未匹配到返回null
     */
    public static Long getVariableIdByName(ProblemLimitInner inner, String name) {
        if (!inner.getHasChild()) {
            return null;
        }
        Assert.notNull(name, "字段名不能为空!");
        List<ProblemLimitInner> children = inner.getChildren();
        for (ProblemLimitInner innerLimit : children) {
            String innerName = innerLimit.getName();
            if (name.equals(innerName)) {
                return innerLimit.getVariableId();
            }
        }
        return null;
    }

    /**
     * ISO-8859-1编码转换成utf-8编码
     *
     * @param str 需要被转换编码的字符串
     * @return 转换完成后的字符串
     */
    public static String iso2Utf8(String str) {
        try {
            return new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private final static DecimalFormat DOUBLE_FORMATE = new DecimalFormat("#.##");

    /**
     * 浮点数保留两位小数点
     *
     * @param db 需要格式化的double数值
     * @return
     */
    public static synchronized Double str2Double(double db) {
        return Double.parseDouble(DOUBLE_FORMATE.format(db));
    }

    /**
     * 浮点数保留两位小数点
     *
     * @param db 需要格式化的double数值
     * @return
     */
    public static synchronized String double2Str(double db) {
        return String.format("%.2f", db);
    }
}
