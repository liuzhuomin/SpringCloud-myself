package xinrui.cloud.app;

import org.hibernate.jpa.HibernateEntityManager;

import xinrui.cloud.domain.IdEntity;

public class HibernateEntitymanngerEvent implements HibernateObserver {
	HibernateEntityManager entityMannger;
	Class<? extends IdEntity> currentServiceClass;

	public HibernateEntitymanngerEvent(Class<? extends IdEntity> clazz) {
		assert clazz == null : "clazz Object Must Not Be Null";
		currentServiceClass = clazz;
	}

	@Override
	public HibernateEntityManager update(HibernateObservable ob, Class<? extends IdEntity> clazz) {
		if (clazz == null) {
			return getCurrent(ob);
		}
		if (currentServiceClass.equals(clazz)) {
			return getCurrent(ob);
		}
		return null;
	}

	private HibernateEntityManager getCurrent(HibernateObservable ob) {
		if (this.entityMannger == null) {
			this.entityMannger = (HibernateEntityManager) ob.getEntityFactory().createEntityManager();
		}
		return this.entityMannger;
	}

}
