package org.litespring.aop.framework;

import org.litespring.aop.Advice;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Description:
 *
 * @author ShaoJiale
 * date 2020/1/13
 */
public interface AopConfig {
    Class<?> getTargetClass();

    void setTargetObject(Object object);

    Object getTargetObject();

    boolean isProxyTargetClass();

    Class<?>[] getProxiedInterfaces();

    boolean isInterfaceProxied(Class<?> inf);

    List<Advice> getAdvices();

    void addAdvice(Advice advice);

    List<Advice> getAdvices(Method method);
}
