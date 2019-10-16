package xinrui.cloud.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识用户的组织
 * @author admin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Group {
    /**
     * 组织的名称
     * @return  角色的名称
     */
    String value() default "";

    /**
     * 忽略的函数名称，当此注解作用与类上，可指定哪些method忽略当前权限
     * @return
     */
    String[] ignoreMethod() default {};
}
