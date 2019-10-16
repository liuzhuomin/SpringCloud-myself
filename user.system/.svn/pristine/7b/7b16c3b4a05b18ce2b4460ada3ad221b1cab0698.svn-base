package xinrui.cloud.aspect;

/**
 * @author liuliuliu
 * @version 1.0
 * 2019/8/20 16:33
 */

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import xinrui.cloud.annotation.Group;
import xinrui.cloud.annotation.Role;
import xinrui.cloud.annotation.XinruiSecurity;
import xinrui.cloud.compoment.Application;
import xinrui.cloud.dto.UserDto;
import xinrui.cloud.util.JudgeFactory;
import xinrui.cloud.util.JudgeImpl;
import xinrui.cloud.util.JudgeInterface;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限操作相关的aop切面类
 *
 * @author liuzhuomin
 */
@Aspect
@Component
@EnableAspectJAutoProxy
@Slf4j
@SuppressWarnings({"rawtypes", "unused"})
public class XinruiSecurityAspect {

    @Pointcut("@annotation(xinrui.cloud.annotation.Group)")
    public void xinruiSecurityPointcut() {
    }

    @Pointcut("@annotation(xinrui.cloud.annotation.Role)")
    public void groupPointcut() {
    }

    @Pointcut("@annotation(xinrui.cloud.annotation.Group)")
    public void rolePointcut() {
    }

//    @Before(value = "execution(* xinrui.cloud.controller.*.*(..))")
//    public void validateGroup(JoinPoint joinPoint) throws Exception {
//        System.out.println("before");
//        Object[] args = joinPoint.getArgs();
//        Class<?> targetClassObj = joinPoint.getTarget().getClass();
//        //TODO 判断类上的注解权限
//        MethodSignature msig = (MethodSignature) joinPoint.getSignature();
//        Method currentMethod = targetClassObj.getMethod(msig.getName(), msig.getParameterTypes());
//        //TODO 判断方法上的注解权限
//        Map<String, Object> fieldsName = getFieldsName(currentMethod, args);
//    }

//    @Before(value = "xinruiSecurityPointcut() || groupPointcut() ||  rolePointcut()")
//    public void validateGroup(JoinPoint joinPoint) throws Exception {
//        System.out.println("before");
//        Object[] args = joinPoint.getArgs();
//        Class<?> targetClassObj = joinPoint.getTarget().getClass();
//        //TODO 判断类上的注解权限
//        MethodSignature msig = (MethodSignature) joinPoint.getSignature();
//        Method currentMethod = targetClassObj.getMethod(msig.getName(), msig.getParameterTypes());
//        //TODO 判断方法上的注解权限
//
//        Map<String, Object> fieldsName = getFieldsName(currentMethod, args);
//    }

    @Around("xinruiSecurityPointcut() || groupPointcut() ||  rolePointcut()")
    public Object validateGroup(final ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("invoke around method");
        Object[] args = joinPoint.getArgs();
        Class<?> targetClassObj = joinPoint.getTarget().getClass();

        JudgeInterface judgeInterface = JudgeFactory.newInstance(JudgeImpl.class);
        List<String> judge = judgeInterface.judge(targetClassObj.getAnnotations());
        //TODO 判断类上的注解权限

        MethodSignature msig = (MethodSignature) joinPoint.getSignature();
        Method currentMethod = targetClassObj.getMethod(msig.getName(), msig.getParameterTypes());
        //TODO 判断方法上的注解权限
        judgeInterface.judge(currentMethod.getAnnotations());

//        Map<String, Object> fieldsName = getFieldsName(currentMethod, args);
        return joinPoint.proceed();
    }

    boolean judge(Class<?> invokeClass) {
        Annotation[] annotations = invokeClass.getAnnotations();
        return false;
    }


    /**
     * 根据类型<code>Class cls</code>和方法<code>String methodName</code>名称和传入的参数<code>Object[] args</code>
     * 数组查询此被调用的函数的参数名称和对应的参数实体
     *
     * @param method 此方法被调用时候传入的参数对象数组
     * @param args   此方法被调用时候传入的参数对象数组
     * @return 参数名字对应的参数值
     * @throws Exception 可能会发生异常
     * @see Exception
     * @see Class
     */
    private Map<String, Object> getFieldsName(Method method, Object[] args) throws Exception {
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] params = u.getParameterNames(method);
        Map<String, Object> map = Maps.newHashMap();
//        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
//        boolean parameterAnnotationIsEmpty = ArrayUtils.isEmpty(parameterAnnotations);
        for (int i = 0; i < params.length; i++) {
            Object object = args[i];
            map.put(params[i], args[i]);
        }
        return map;
    }
}
