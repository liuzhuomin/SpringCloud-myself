package xinrui.cloud.common.utils;

import com.google.common.collect.Lists;
import org.apache.commons.beanutils.ConvertUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuliuliu
 * @version 1.0
 *  2019/8/7 10:32
 */
@SuppressWarnings("rawtypes")
public final class DataUtil {

    private final static String OBJECT_NOT_NULL = "object must not be null";

    private final static String FILED_NOT_NULL = "filed must not be null";

    public static final String BOTTOM_LINE = "_";

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

    public static void test() {

    }

    /**
     * 判断object是否为数组类型
     *
     * @param array
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
     * @param classObj
     * @return
     */
    @SuppressWarnings("unchecked")
    public static boolean isArray(Class classObj) {
        return classObj.isAssignableFrom(boolean[].class) || classObj.isAssignableFrom(byte[].class)
                || classObj.isAssignableFrom(char[].class) || classObj.isAssignableFrom(double[].class)
                || classObj.isAssignableFrom(float[].class) || classObj.isAssignableFrom(int[].class)
                || classObj.isAssignableFrom(long[].class) || classObj.isAssignableFrom(short[].class);
    }

    /**
     * 给字符串按照大小写增加下划线 例子: PointAnnotation --> point_annotation
     *
     * @param str 预期的字符串对象
     */
    public static String addStrBootomLine(String str) {
        char[] charArray = str.toCharArray();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (c >= 'A' && c <= 'Z') {
                if (i != 0) {
                    buffer.append(BOTTOM_LINE);
                }
                buffer.append(String.valueOf(c).toLowerCase());
            } else {
                buffer.append(c);
            }
        }
        return buffer.toString();
    }

    /**
     * 复原增加了下划线的字符串
     *
     * @param string
     * @return
     */
    public static String restoreBottomString(String string) {
        StringBuffer buff = new StringBuffer();
        String[] split = string.split(BOTTOM_LINE);
        for (int i = 0; i < split.length; i++) {
            String str = split[i];
            if (i != 0) {
                buff.append(str.substring(0, 1).toUpperCase());
                buff.append(str.substring(1));
            } else {
                buff.append(str);
            }
        }
        return buff.toString();
    }

    /**
     * 复原增加了下划线的字符串
     *
     * @param string
     * @return
     */
    public static String fieldNameToSetterMthodName(String string) {
        StringBuffer buff = new StringBuffer();
        String[] split = string.split(BOTTOM_LINE);
        for (int i = 0; i < split.length; i++) {
            String str = split[i];
            if (i != 0) {
                buff.append(str.substring(0, 1).toUpperCase());
                buff.append(str.substring(1));
            } else {
                buff.append(str);
            }
        }
        return buff.toString();
    }

    /**
     * java bean 对象转换成map结构
     *
     * @param obj 需要被转换的对象
     * @return
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @SuppressWarnings("serial")
    public static Map<String, Object> beanToMap(Object obj)
            throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>() {
            @Override
            public Object put(String key, Object value) {
                return super.put(DataUtil.addStrBootomLine(key), value);
            }
        };
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass(), Object.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            Method getter = property.getReadMethod();
            @SuppressWarnings("unused")
            /*
             * Class<?> returnType = null; if (getter != null) { returnType =
             * getter.getReturnType(); }
             */
                    Object value = getter != null ? getter.invoke(obj) : null;
            map.put(key, value);
        }
        return map;
    }

    /**
     * map 对象转换成java bean结构
     *
     * @param clazz 需要被转换的对象类型
     * @return 根据字段和字段对应的值生成的Map结构
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    public static<T> T mapToBean(Map<String, Object> mapByObj, Class<T> clazz) throws IntrospectionException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        if (mapByObj == null) {
            return null;
        }
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        T object = clazz.newInstance();
        for (Map.Entry<String, Object> entry : mapByObj.entrySet()) {
            String addStrBootomLine = DataUtil.restoreBottomString(entry.getKey());
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.equals(addStrBootomLine)) {
                    Method setter = property.getWriteMethod();
                    Class<?>[] parameterTypes = setter.getParameterTypes();
                    if (setter != null) {
                        Object value = entry.getValue();
                        Class<?> parameterClass = parameterTypes[0];
                        if (value != null) {
                            if (parameterClass == Long.class) {
                                setter.invoke(object, Long.parseLong(value.toString()));
                            } else if (parameterClass == Integer.class) {
                                setter.invoke(object, Integer.parseInt(value.toString()));
                            } else if (parameterClass == Character.class) {
                                setter.invoke(object, (Character) value);
                            } else if (parameterClass == String.class) {
                                setter.invoke(object, ConvertUtils.convert(value));
                            } else {
                                setter.invoke(object, value);
                            }
                        }
                    }
                    break;
                }
            }
        }
        return object;
    }

    /**
     * 根据obj和field取到指定field的值
     *
     * @param object 指定对象
     * @param field  指定field
     * @return 指定字段生成的getter方法在当前对象执行后返回的结果对象
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object invokeGetByObject(Object object, Field field) throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
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

    /**
     * 根据object和field运行setter方法
     *
     * @param object 指定对象
     * @param field  指定field
     * @return 指定字段生成的setter方法在当前对象执行后返回的结果对象
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object invokeSetByObject(Object object, Field field, Object... objects) throws NoSuchMethodException,
            SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        checkObjectAndField(object, field);
        String name = field.getName();
        StringBuilder builder = new StringBuilder();
        if (name.length() == 1) {
            builder.append("set").append(name.toUpperCase()).toString();
        } else {
            builder.append("set").append(name.substring(0, 1).toUpperCase()).append(name.substring(1)).toString();
        }
        try {
            return object.getClass().getMethod(builder.toString(), new Class[]{field.getType()}).invoke(object,
                    objects);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从当前{@link Field}字段中取出他的泛型类型如果他的泛型不是{@link List}的子类,则返回null 如果没有泛型返回null
     *
     * @param field 预期一个{@link Field}类型的字段
     * @return 返回取出的字段的泛型的类型
     */
    public static Class<?> getGenericClassNameByList(Field field) {
        checkField(field);
        if (field.getType().isAssignableFrom(List.class)) {
//			if (field.getType().getName().equals(field.getGenericType().getTypeName())) {
//				return null;
//			}
            ParameterizedType listGenericType = (ParameterizedType) field.getGenericType();
            Type[] listActualTypeArguments = listGenericType.getActualTypeArguments();
            Type type = listActualTypeArguments[0];
            return (Class<?>) type;
        }
        return null;
    }

    private static void checkObjectAndField(Object object, Field field) {
        checkObject(object);
        checkField(field);

    }

    private static void checkField(Field field) {
        if (field == null) {
            throw new RuntimeException(FILED_NOT_NULL);
        }
    }

    private static void checkObject(Object object) {
        if (object == null) {
            throw new RuntimeException(OBJECT_NOT_NULL);
        }
    }

    /**
     * 根据逗号分割，如果没有则返回当前的，如果为空返回空集合
     *
     * @param fields 需要被分割的字符串
     * @return 被分隔后的集合
     */
    public static List<String> split(String fields) {
        if (fields.contains(",")) {
            String[] split = fields.split(",");
            return Arrays.asList(split);
        }
        return Lists.newArrayList(fields);
    }

    /**
     * 检查vlaue是否村子啊与array中,必须重写equals方法
     * @param array 源数组
     * @param value 被检查的类型
     * @param <T>
     * @return
     */
    public static <T> boolean checkFieldIsIgnore(T[] array, T value) {
        if (array != null && array.length > 0) {
            for (Object s : array) {
                if (s.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

}
