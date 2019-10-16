package xinrui.cloud.dao.impl;

import org.hibernate.criterion.Restrictions;
import xinrui.cloud.dao.BaseDao;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.dto.PageDto;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.jpa.HibernateEntityManager;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@SuppressWarnings("unchecked")
@Repository("baseDao")
public class BaseDaoImpl implements InitializingBean, BaseDao {

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    private Logger logger = Logger.getLogger(BaseDaoImpl.class.getName());

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info(this.getClass().getSimpleName() + " init");
    }

    @Override
    public <T extends IdEntity> T persistAndGet(Class<T> clazz, T obj) {
        Serializable save = getEntityManager(clazz).getSession().save(obj);
        return getById(clazz, save);
    }

    @Override
    public <T extends IdEntity> void persist(Class<T> clazz, T obj) {
        getEntityManager(clazz).persist(obj);
    }

    @Override
    public void persist(IdEntity obj) {
        getEntityManager().persist(obj);
    }

    @Override
    public <T extends IdEntity> void remove(Class<T> clazz, T obj) {
        getEntityManager().remove(obj);
    }

    @Override
    public <T extends IdEntity> void remove(Class<T> clazz, Serializable id) {
        T byId = getById(clazz, id);
        getSession().delete(clazz.getName(), byId);
    }

    @Override
    public <T extends IdEntity> T merge(Class<T> clazz, T obj) {
        T entity = getEntityManager(clazz).merge(obj);
        flush();
        return entity;
    }

    @Override
    public <T extends IdEntity> T getById(Class<T> clazz, Serializable id) {
        return getEntityManager(clazz).find(clazz, id);
    }

    @Override
    public <T extends IdEntity> T findSingleCriteria(DetachedCriteria dCriteria) {
        return (T) dCriteria.getExecutableCriteria(getSession()).uniqueResult();
    }

    @Override
    public <T extends IdEntity> List<T> listBydCriteria(DetachedCriteria dCriteria) {
        return dCriteria.getExecutableCriteria(getSession()).list();
    }

    @Override
    public Map<String, Object> getQueryForToMap(String sql, Object... objects) {
        return (Map<String, Object>) createResultTransfer(sql, objects).uniqueResult();
    }

    @Override
    public List<Map<String, Object>> getQueryForToListMap(String sql, Object... objects) {
        return createResultTransfer(sql, objects).list();
    }

    @Override
    public <T extends IdEntity> List<T> listByHql(String hql, Class<T> clazz, Object... arr) {
        Query createQuery = createQuery(hql, clazz, arr);
        return (List<T>) createQuery.getResultList();
    }

    @Override
    public <T extends IdEntity> Object getByHql(String hql, Class<T> clazz, Object... arr) {
        Query createQuery = createQuery(hql, clazz, arr);
        return createQuery.getSingleResult();
    }

    @Override
    public void flush() {
        getEntityManager().flush();
    }

    private HibernateEntityManager getEntityManager() {
        return (HibernateEntityManager) entityManager;
    }

    private HibernateEntityManager getEntityManager(Class<? extends IdEntity> clazz) {
        return (HibernateEntityManager) entityManager;
    }

    @Override
    public Session getSession() {
        HibernateEntityManager hgetEntityManager = (HibernateEntityManager) getEntityManager();
        return hgetEntityManager.getSession();
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     * 根据hql和query创建参数
     *
     * @param hql hql
     * @param arr 注入的参数
     * @return {@link Query}对象
     */
    private Query createQuery(String hql, Class<? extends IdEntity> clazz, Object... arr) {
        Query createQuery = getEntityManager(clazz).createQuery(hql, clazz);
        getInnerQuery(createQuery, arr);
        return createQuery;
    }

    private void getInnerQuery(Query createQuery, Object[] arr) {
        Set<Parameter<?>> parameters = createQuery.getParameters();
        if (parameters.size() != arr.length) {
            throw new IllegalArgumentException("参数与占位符个数不对应");
        }
        for (int i = 0; i < arr.length; i++) {
            createQuery.setParameter(i, arr[i]);
        }
    }

    /**
     * 设置返回传输对象的格式
     *
     * @param sql     sql语句
     * @param objects 可变参数
     * @return {@link Query}
     */
    private org.hibernate.Query createResultTransfer(String sql, Object... objects) {
        Query createNativeQuery = getEntityManager().createNativeQuery(sql);
        org.hibernate.Query setResultTransformer = createNativeQuery.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        for (int i = 0; i < objects.length; i++) {
            setResultTransformer.setParameter(i, objects[i]);
        }
        return setResultTransformer;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ, timeout = 2000, readOnly = true)
    public <T> List<T> pageObjectByHql(String hql, Class<T> returnType, Object... objects) {
        Query createQuery = getQuery(hql, objects);
        return createQuery.getResultList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ, timeout = 2000, readOnly = true)
    public <T extends IdEntity> List<T> pageByHql(String hql, Class<T> returnType, int currentPage, int pageSize, Object... objects) {
        Query createQuery = getQuery(hql, objects);
        createQuery.setFirstResult(--currentPage * pageSize);
        createQuery.setMaxResults(pageSize);
        return createQuery.getResultList();
    }

    private Query getQuery(String hql, Object[] objects) {
        Query createQuery = getEntityManager().createQuery(hql);
        getInnerQuery(createQuery, objects);
        return createQuery;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ, timeout = 2000, readOnly = true)
    public <T extends IdEntity> List<T> listObjBySql(String sql, Class<T> clazz, Object... objects) {
        SQLQuery createSQLQuery = getSession().createSQLQuery(sql);
        org.hibernate.Query setResultTransformer = createSQLQuery.setResultTransformer(Transformers.aliasToBean(clazz));
        setParameter(createSQLQuery, objects);
        return setResultTransformer.list();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ, timeout = 2000, readOnly = true)
    public <T extends IdEntity> Object getObjBySql(String sql, Object... objects) {
        SQLQuery createSQLQuery = getSession().createSQLQuery(sql);
        setParameter(createSQLQuery, objects);
        return createSQLQuery.uniqueResult();
    }

    private void setParameter(SQLQuery createSQLQuery, Object[] objects) {
        for (int i = 0; i < objects.length; i++) {
            createSQLQuery.setParameter(i, objects[i]);
        }
    }

    @Override
    public List<IdEntity> listByHql(String hql, Object... objects) {
        return pageObjectByHql(hql, null, objects);
    }

    @Override
    public List<IdEntity> listObjBySql(String sql, Object... objects) {
        return listObjBySql(sql, null, objects);
    }

    @Override
    public <T> void pageByCriteria(PageDto<T> pageTo) {
        HibernateEntityManager hEntityManager = (HibernateEntityManager) entityManager;
        Session session = hEntityManager.getSession();
        DetachedCriteria criteria = pageTo.getCriteria();
        pageTo.setTotalPage(Integer.parseInt(criteria.setProjection(Projections.rowCount())
                .getExecutableCriteria(session).uniqueResult().toString()));
        Criteria criteriaCurrent = criteria.getExecutableCriteria(session);
        CriteriaImpl criteriaImpl = (CriteriaImpl) criteriaCurrent;
        if (criteriaImpl.getProjection() != null) {
            criteria.setProjection(null);
            criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        }
        criteriaCurrent.setFirstResult(pageTo.getFirstResult());
        criteriaCurrent.setMaxResults(pageTo.getMaxResult());
        pageTo.setT((T) criteria.getExecutableCriteria(session).list());
    }

    @Override
    public <T extends IdEntity> List<T> listObjBy(Class<T> technologyLoanDateClass) {
        return DetachedCriteria.forClass(technologyLoanDateClass).getExecutableCriteria(getSession()).list();
    }

    @Override
    public <T extends IdEntity> T findSingleByProperty(Class<T> clazz, String key, Object value) {
        DetachedCriteria add = DetachedCriteria.forClass(clazz).add(Restrictions.eq(key, value));
        return (T) add.getExecutableCriteria(getSession()).uniqueResult();
    }

    @Override
    public int count(DetachedCriteria detachedCriteria) {
        Object o = detachedCriteria.getExecutableCriteria(getSession()).uniqueResult();
        if (o == null) {
            return 0;
        }
        return Integer.parseInt(o.toString());
    }

    @Override
    public <T extends IdEntity> Criteria getCriteria(Class<T> clazz) {
        return getSession().createCriteria(clazz);
    }


}
