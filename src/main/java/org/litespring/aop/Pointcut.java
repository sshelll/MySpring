package org.litespring.aop;

/**
 * Description: Pointcut which contains MethodMatcher and Expression.
 *
 * @author ShaoJiale
 * date 2020/1/11
 * @see MethodMatcher
 * @see org.litespring.aop.aspectj.AspectJExpressionPointcut
 */
public interface Pointcut {
    MethodMatcher getMethodMatcher();

    String getExpression();
}
