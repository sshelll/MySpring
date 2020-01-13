package org.litespring.aop.aspectj;

import org.aopalliance.intercept.MethodInvocation;
import org.litespring.aop.Advice;
import org.litespring.aop.Pointcut;

import java.lang.reflect.Method;

/**
 * Description: A MySpring advice which is called after the target method returning.
 *
 * @author ShaoJiale
 * date 2020/1/12
 */
public class AspectJAfterReturningAdvice extends AbstractAdvice {

    public AspectJAfterReturningAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, Object adviceObject) {
        super(adviceMethod, pointcut, adviceObject);
    }

    public Object invoke(MethodInvocation invocation) throws Throwable{
        Object obj = invocation.proceed();
        invokeAdviceMethod();
        return obj;
    }
}
