package org.litespring.aop.aspectj;

import org.litespring.aop.Advice;
import org.litespring.aop.Pointcut;

import java.lang.reflect.Method;

/**
 * Description: common filed of advices.
 *
 * @author ShaoJiale
 * date 2020/1/12
 */
public abstract class AbstractAdvice implements Advice {
    protected Method adviceMethod;

    protected AspectJExpressionPointcut pointcut;

    protected Object adviceObject;

    public AbstractAdvice(Method adviceMethod,
                          AspectJExpressionPointcut pointcut,
                          Object adviceObject) {
        this.adviceMethod = adviceMethod;
        this.pointcut = pointcut;
        this.adviceObject = adviceObject;
    }

    public void invokeAdviceMethod() throws Throwable {
        adviceMethod.invoke(adviceObject);
    }

    public Pointcut getPointcut() {
        return this.pointcut;
    }

    public Method getAdviceMethod() {
        return this.adviceMethod;
    }
}
