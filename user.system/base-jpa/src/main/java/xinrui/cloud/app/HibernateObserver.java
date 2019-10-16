package xinrui.cloud.app;

import org.hibernate.jpa.HibernateEntityManager;

import xinrui.cloud.domain.IdEntity;

public interface HibernateObserver {
	HibernateEntityManager update(HibernateObservable ob, Class<? extends IdEntity> clazz);
}
