package xinrui.cloud.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <ul>鑫锐权限注解(可作用于类或者方法)
 * <li/>当标注在类上，作用于整个类的所有method;
 * <li/>当标注与方法上，作用于当前method;
 * </ul>
 * @author admin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface XinruiSecurity {
    /**
     * 该用户必须拥有的组织
     * @return  {@link Group}
     */
    Group[] groups() default {};
    /**
     * 该用户必须拥有的角色
     * @return  {@link Group}
     */
    Role[] roles() default {};
}
