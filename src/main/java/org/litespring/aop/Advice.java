package org.litespring.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.litespring.aop.aspectj.AspectJBeforeAdvice;

/**
 * Description: Interface of interceptors in MySpring.
 * All interceptors should implement this interface to
 * indicate that it is an interceptor. And as a interceptor,
 * it should contain a Pointcut.
 *
 * @author ShaoJiale
 * date 2020/1/12
 * @see Pointcut
 * @see AspectJBeforeAdvice
 */
public interface Advice extends MethodInterceptor {
    Pointcut getPointcut();
}
