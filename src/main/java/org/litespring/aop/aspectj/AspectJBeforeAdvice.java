package org.litespring.aop.aspectj;

import org.aopalliance.intercept.MethodInvocation;
import org.litespring.aop.Advice;
import org.litespring.aop.Pointcut;

import java.lang.reflect.Method;

/**
 * Description: A MySpring advice which is called before the target method.
 *
 * @author ShaoJiale
 * date 2020/1/12
 */
public class AspectJBeforeAdvice extends AbstractAdvice {

    public AspectJBeforeAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, Object adviceObject) {
        super(adviceMethod, pointcut, adviceObject);
    }

    public Object invoke(MethodInvocation invocation) throws Throwable{
        invokeAdviceMethod();
        Object obj = invocation.proceed();
        return obj;
    }
}
