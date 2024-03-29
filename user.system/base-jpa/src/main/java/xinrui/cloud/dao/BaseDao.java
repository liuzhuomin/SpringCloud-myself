package xinrui.cloud.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.dto.PageDto;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.jdbc.core.JdbcTemplate;
import xinrui.cloud.service.BaseService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 通用基础的DAO默认注入到每个实现了自{@link BaseService}的service类中
 *
 * @author liuliuliu
 */
public interface BaseDao {

    /**
     * 存储一个实例对象并且返回保存后的对象
     *
     * @param clazz {@code <T extends IdEntity>}继承与{@link IdEntity}的实体类
     * @param obj   实体对象
     * @return {@link IdEntity}及其子类
     */
    public <T extends IdEntity> T persistAndGet(Class<T> clazz, T obj);

    /**
     * 存储一个实例对象
     *
     * @param clazz {@code <T extends IdEntity>}继承与{@link IdEntity}的实体类
     * @param obj   实体对象
     */
    public <T extends IdEntity> void persist(Class<T> clazz, T obj);

    /**
     * 删除一个实例对象
     *
     * @param clazz {@code <T extends IdEntity>}继承与{@link IdEntity}的实体类
     * @param obj   实体对象
     */
    public <T extends IdEntity> void remove(Class<T> clazz, T obj);

    /**
     * 存储或者更新一个实例对象
     *
     * @param clazz {@code <T extends IdEntity>}继承与{@link IdEntity}的实体类
     * @param obj   实体对象
     * @return
     */
    public <T extends IdEntity> T merge(Class<T> clazz, T obj);

    /**
     * 根據id查找
     *
     * @param clazz {@code <T extends IdEntity>}继承与{@link IdEntity}的实体类
     * @param id    id
     * @return {@link IdEntity}及其子类
     */
    public <T extends IdEntity> T getById(Class<T> clazz, Serializable id);

    /**
     * 根据{@link DetachedCriteria}对象查询(hibernate离线查询对象)
     *
     * @param dCriteria {@link DetachedCriteria}
     * @return 集合
     */
    public <T extends IdEntity> T findSingleCriteria(DetachedCriteria dCriteria);

    /**
     * 通过{@link DetachedCriteria}查找集合结果
     *
     * @param dCriteria {@link DetachedCriteria}hibernate离线查询对象
     * @return {@code  <T extends IdEntity> List<T>}
     */
    public <T extends IdEntity> List<T> listBydCriteria(DetachedCriteria dCriteria);

    /**
     * 存储一个实体类
     *
     * @param obj 必须是{@link IdEntity}及其子类
     */
    public void persist(IdEntity obj);

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
    public <T extends IdEntity> List<T> listByHql(String hql, Class<T> clazz, Object... arr);

    /**
     * ?0注入(hql)，查询单个字段的集合使用
     *
     * @param hql     hql语句
     * @param objects 可变参数
     * @return 任意类型的List
     */
    public List<IdEntity> listByHql(String hql, Object... objects);

    /**
     * ?0注入(hql)，查询单个字段的集合使用
     *
     * @param hql        hql语句
     * @param returnType 返回的集合的泛型的类型
     * @param objects    可变参数
     * @return 任意类型的List
     */
    public <T> List<T> pageObjectByHql(String hql, Class<T> returnType, Object... objects);

    /**
     * ?注入(sql)，查询单个字段的集合使用
     *
     * @param sql     hql语句
     * @param clazz   实体类映射的类型
     * @param objects 可变参数
     * @return 任意类型的List
     */
    public <T extends IdEntity> List<T> listObjBySql(String sql, Class<T> clazz, Object... objects);

    /**
     * 根据sql查询
     *
     * @param string
     * @return
     */
    public List<IdEntity> listObjBySql(String string, Object... objects);

    /**
     * 根据id删除
     *
     * @param clazz
     * @param id
     */
    public <T extends IdEntity> void remove(Class<T> clazz, Serializable id);

    /**
     * 刷新缓存
     */
    public void flush();

    /**
     * 获取{@link JdbcTemplate}对象
     *
     * @return {@link JdbcTemplate}
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * 根据hql查询指定的数据并且返回指定格式的集合，可分页。用?0方式注入
     *
     * @param hql         hql语句
     * @param returnType  返回的集合类型
     * @param currentPage 当前页码
     * @param pageSize    最大页码
     * @param objects     可变参数参数数组
     * @return
     */
    public <T extends IdEntity> List<T> pageByHql(String hql, Class<T> returnType, int currentPage, int pageSize, Object... objects);

    /**
     * 通过hql查询指定列
     *
     * @param hql   hql语句
     * @param clazz entity类
     * @param arr   可变参
     * @return 查询到的结果
     */
    public <T extends IdEntity> Object getByHql(String hql, Class<T> clazz, Object... arr);

    /**
     * 根据sql查询单个对象,使用?注入
     *
     * @param sql     sql语句
     * @param objects 可变参数
     * @return 查询结果
     */
    public <T extends IdEntity> Object getObjBySql(String sql, Object... objects);

    /**
     * @param pageTo 分业查询条件对象
     */
    public <T> void pageByCriteria(PageDto<T> pageTo);

    /**
     * 根据{@link ##technologyLoanDateClass}获取到此实体的所有数据库对应实例
     *
     * @param technologyLoanDateClass 类型
     * @param <T>
     * @return
     */
    public <T extends IdEntity> List<T> listObjBy(Class<T> technologyLoanDateClass);

    /**
     * 根据属性查询
     *
     * @param clazz 类型
     * @param key   属性key
     * @param value 属性值
     * @param <T>
     * @return
     */
    public <T extends IdEntity> T findSingleByProperty(Class<T> clazz, String key, Object value);

    /**
     * 查询数量
     *
     * @param setProjection
     * @return
     */
    int count(DetachedCriteria setProjection);

    /**
     * 获取hibernate的session对象
     *
     * @return
     */
    public Session getSession();

    /**
     * 按照类型获取{@link Criteria}对象
     *
     * @param clazz 类型
     * @param <T>   必须时继承自{@link IdEntity}的实体
     * @return {@link Criteria}hibernate查询对象
     */
    public <T extends IdEntity> Criteria getCriteria(Class<T> clazz);

}