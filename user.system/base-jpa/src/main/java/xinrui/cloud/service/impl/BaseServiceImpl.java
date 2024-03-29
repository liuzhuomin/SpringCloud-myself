package xinrui.cloud.service.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import xinrui.cloud.app.HibernateEntitymanngerEvent;
import xinrui.cloud.app.MyApplication;
import xinrui.cloud.dao.BaseDao;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.service.BaseService;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
@Transactional
public class BaseServiceImpl<T extends IdEntity> implements BaseService<T>, InitializingBean {

    private Logger logger = Logger.getLogger(BaseServiceImpl.class.getName());

    protected Class<T> entityClass;

    @Autowired
    protected BaseDao dao;

    @SuppressWarnings("unchecked")
    public BaseServiceImpl() {
        Type t = getClass().getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            Type[] p = ((ParameterizedType) t).getActualTypeArguments();
            try {
                entityClass = (Class<T>) p[0];
                MyApplication.getCurrentObServable().addObserver(new HibernateEntitymanngerEvent(entityClass));
            } catch (ClassCastException e) {
                logger.warning("entityClass has be null,can't init entityClass for current service");
                entityClass = null;
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info(this.getClass().getSimpleName() + "\tinit...");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public T findById(Serializable id) {
        return dao.getById(entityClass, id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void persist(T obj) {
        dao.persist(entityClass, obj);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void remove(T obj) {
        dao.remove(entityClass, obj);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public T merge(T obj) {
        return dao.merge(entityClass, obj);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<T> listBydCriteria(DetachedCriteria dCriteria) {
        return dao.listBydCriteria(dCriteria);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<T> findAll() {
        return dao.listBydCriteria(DetachedCriteria.forClass(entityClass));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<T> listByProperty(String propertiyName, Object value) {
        StringBuffer append = new StringBuffer().append("FROM ").append(entityClass.getName()).append(" WHERE ")
                .append(propertiyName).append(" = ?0");
        return dao.listByHql(append.toString(), entityClass, value);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public T findSingleCriteria(DetachedCriteria dCriteria) {
        return dao.findSingleCriteria(dCriteria);
    }

    @Override
    public Object findoObjBydCriteria(DetachedCriteria dCriteria) {
        return dao.findSingleCriteria(dCriteria);
    }

    @Override
    public Map<String, Object> getQueryForToMap(String sql, Object... objects) {
        return dao.getQueryForToMap(sql, objects);
    }

    @Override
    public List<Map<String, Object>> getQueryForToListMap(String sql, Object... objects) {
        return dao.getQueryForToListMap(sql, objects);
    }

    @Override
    public List<T> listByHql(String hql, Object... arr) {
        return dao.listByHql(hql, entityClass, arr);
    }

    @Override
    public T persistAndGet(T obj) {
        return dao.persistAndGet(entityClass, obj);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ, timeout = 2000)
    public void remove(Long id) {
        dao.remove(entityClass, id);
    }

    @Override
    public int count(DetachedCriteria countCriteria) {
        return dao.count(countCriteria.setProjection(Projections.rowCount()));
    }

    @Override
    public <T extends IdEntity> Criteria getCriteria() {
        return dao.getCriteria(entityClass);
    }

}
