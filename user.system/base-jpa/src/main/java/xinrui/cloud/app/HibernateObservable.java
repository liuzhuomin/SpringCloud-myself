package xinrui.cloud.app;

import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.jpa.HibernateEntityManager;

import xinrui.cloud.domain.IdEntity;

/**
 * 自定义观察者
 * 
 * @author liuliuliu
 *
 */
public class HibernateObservable {

	private Vector<HibernateObserver> hibernateObservers;

	private boolean changed = false;

	private EntityManagerFactory emf;

	private EntityManager mangger;

	public HibernateObservable(EntityManagerFactory emf) {
		hibernateObservers = new Vector<>();
	}

	public EntityManagerFactory getEntityFactory() {
		return this.emf;
	}

	public void setChange() {
		changed = true;
	}

	public synchronized void addObserver(HibernateObserver o) {
		if (o == null) {
			throw new NullPointerException();
		}
		if (!hibernateObservers.contains(o)) {
			hibernateObservers.addElement(o);
		}
	}

	public synchronized void deleteObserver(HibernateObserver o) {
		hibernateObservers.removeElement(o);
	}

	public HibernateEntityManager notifyAll(HibernateObservable ob, Class<? extends IdEntity> clazz) {
		assert emf == null : "EntityManagerFactory Object Must Not Be Null";
		Object[] arrLocal;
		synchronized (this) {
			if (!changed) {
				return null;
			}
			arrLocal = hibernateObservers.toArray();
			clearChanged();
		}
		for (int i = arrLocal.length - 1; i >= 0; i--) {
			HibernateEntityManager update = ((HibernateObserver) arrLocal[i]).update(this, clazz);
			if (update != null) {
				return update;
			}
		}
		return null;
	}

	protected synchronized void clearChanged() {
		changed = false;
	}

	public EntityManagerFactory getEmf() {
		return emf;
	}

	public void setEmf(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public HibernateEntityManager defaultEntityMannager() {
		if (mangger == null) {
			this.mangger = emf.createEntityManager();
		}
		return (HibernateEntityManager) this.mangger;
	}

}
