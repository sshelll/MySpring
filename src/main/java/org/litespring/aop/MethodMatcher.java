package org.litespring.aop;

import java.lang.reflect.Method;

/**
 * Description: Judge if a method matches the target expression.
 *
 * @author ShaoJiale
 * date 2020/1/11
 * @see org.litespring.aop.aspectj.AspectJExpressionPointcut
 */
public interface MethodMatcher {
    boolean matches(Method method);
}
