package xinrui.cloud.service;

import org.hibernate.Criteria;
import xinrui.cloud.domain.IdEntity;
import org.hibernate.criterion.DetachedCriteria;
import xinrui.cloud.service.impl.BaseServiceImpl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * 基础的service，提供给service接口继承，提供了默认的增删改查等接口,
 * 默认的实现类为{@link BaseServiceImpl};
 *
 * @param <T> 泛型必须为{@link IdEntity}及其子类
 * @author liuliuliu
 */
public interface BaseService<T extends IdEntity> {


    /**
     * 存储当前service对应的实体类并且返回保存后的对象
     *
     * @param obj {@code 当前T类型的实体}
     * @reutn obj {@code 当前T类型的实体}
     */
    public T persistAndGet(T obj);

    /**
     * 存储当前service对应的实体类
     *
     * @param obj {@code 当前T类型的实体}
     */
    public void persist(T obj);

    /**
     * 根据当前持久化对象进行数据库删除操作
     */
    public void remove(T obj);

    /**
     * 根据id删除记录
     */
    public void remove(Long id);

    /**
     * 存储或者修改当前service对应的实体类
     *
     * @param obj {@code 当前T类型的实体}
     */
    public T merge(T obj);

    /**
     * 根据ID查找当前service对应的实体类
     *
     * @param id {@link Serializable}
     * @return {@link T}
     */
    public T findById(Serializable id);

    /**
     * 根据{@link DetachedCriteria}离线查询条件对象查询符合条件的集合
     *
     * @param dCriteria {@link DetachedCriteria}
     * @return {@code 当前service对应的T类型的List集合}
     */
    public List<T> listBydCriteria(DetachedCriteria dCriteria);

    /**
     * 查询当前servcice对应的所有实体对象
     *
     * @return {@code 当前service对应的T类型的List集合}
     */
    public List<T> findAll();

    /**
     * 根据{@link DetachedCriteria}离线查询条件对象查询符合条件的单个对象,如果存在多个结果则抛出异常
     *
     * @param dCriteria {@link DetachedCriteria}
     * @return {@code 当前service对应的T类型的对象}
     */
    public T findSingleCriteria(DetachedCriteria dCriteria);

    /**
     * 根据属性查找符合条件的结果集合
     *
     * @param propertiyName 实体类的字段属性名称
     * @param value         参数值
     * @return {@code 当前service对应的T类型的List集合}
     */
    public List<T> listByProperty(String propertiyName, Object value);

    /**
     * 查询多条记录返回{@code Map<String, Object>}</br>
     * Map的key为列的字段名称,value为当前列的值</br>
     * 用?来注入
     *
     * @param sql     sql语句(原生)
     * @param objects Object数组
     * @return 多条记录生成的{@codeMap<String, Object>}集合
     */
    public Map<String, Object> getQueryForToMap(String sql, Object... objects);

    /**
     * 查询多条记录返回{@code List<Map<String, Object>>}</br>
     * Map的key为列的字段名称,value为当前列的值</br>
     * 用?来注入
     *
     * @param sql     sql语句(原生)
     * @param objects Object数组
     * @return 多条记录生成的{@code List<Map<String, Object>>}集合
     */
    public List<Map<String, Object>> getQueryForToListMap(String sql, Object... objects);

    /**
     * 根据hql查询集合</br>
     * 占位符用法 <code>?0  ?1  ?2</code> exmaple: <b>from a where b.id = ?0 nad a.name =
     * ?1</b></br>
     * <b>Object[] 1,liu</b></br>
     *
     * @param hql
     * @param arr
     * @return
     */
    public List<T> listByHql(String hql, Object... arr);

    /**
     * 根据{@link DetachedCriteria}对象查询任何一个继承自{@link IdEntity}的实体类
     *
     * @param dCriteria
     * @return
     */
    public Object findoObjBydCriteria(DetachedCriteria dCriteria);

    /**
     *  根据{@link DetachedCriteria}对象查询记录数
     * @param countCriteria
     * @return
     */
    public int count(DetachedCriteria countCriteria);

    /**
     * 按照类型获取{@link Criteria}对象
     *
     * @param <T>   必须时继承自{@link IdEntity}的实体
     * @return {@link Criteria}hibernate查询对象
     */
    public <T extends IdEntity> Criteria getCriteria();

}
