package xinrui.cloud.app;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

@Service
@DependsOn("entityManagerFactory")
public class MyApplication implements InitializingBean {

	public MyApplication(@Autowired ApplicationContext applicationContext,
			@Autowired EntityManagerFactory entityManagerFactory) {
		MyApplication.applicationContext = applicationContext;
		MyApplication.emf = entityManagerFactory;
	}

	private static EntityManagerFactory emf;

	private static ApplicationContext applicationContext;

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * } 通过name获取bean
	 * 
	 * @param name
	 * @return
	 */
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

	/**
	 * 通过class获取bean
	 * 
	 * @param clazz
	 * @param       <T>
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}

	/**
	 * 通过name和class获取bean
	 * 
	 * @param name
	 * @param clazz
	 * @param       <T>
	 * @return
	 */
	public static <T> T getBean(String name, Class<T> clazz) {
		return getApplicationContext().getBean(name, clazz);
	}

	/**
	 * 根据clazz类型获取spring容器中的对象
	 * 
	 * @param clazz
	 * @param       <T>
	 * @return
	 */
	public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
		return getApplicationContext().getBeansOfType(clazz);
	}

	/**
	 * 根据注解类从容器中获取对象
	 * 
	 * @param clazz
	 * @param       <T>
	 * @return
	 */
	public static <T> Map<String, Object> getBeansOfAnnotation(Class<? extends Annotation> clazz) {
		return getApplicationContext().getBeansWithAnnotation(clazz);
	}

	private static HibernateObservable able;

	public static HibernateObservable getCurrentObServable() {
		if (able == null) {
			MyApplication.able = new HibernateObservable(emf);
		}
		return MyApplication.able;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		getCurrentObServable().setEmf(emf);
	}

}
