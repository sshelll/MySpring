package org.litespring.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Description: Proceed a target method with interceptors.
 *
 * @author ShaoJiale
 * date 2020/1/12
 */
public class ReflectiveMethodInvocation implements MethodInvocation {
    protected final Object targetObject;

    protected final Method targetMethod;

    protected Object[] arguments;

    protected final List<MethodInterceptor> interceptors;

    private int currentInterceptorIndex = -1;

    public ReflectiveMethodInvocation(Object targetObject,
                                      Method targetMethod,
                                      Object[] arguments,
                                      List<MethodInterceptor> interceptors) {
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.arguments = arguments;
        this.interceptors = interceptors;
    }

    public Method getTargetMethod() {
        return this.targetMethod;
    }

    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override
    public Method getMethod() {
        return this.targetMethod;
    }

    @Override
    public Object getThis() {
        return this;
    }

    /**
     * Proceed the target method with interceptors.
     * @return agent object
     * @throws Throwable invoke failed
     */
    @Override
    public Object proceed() throws Throwable {
        if (this.currentInterceptorIndex == this.interceptors.size() - 1) {
            return invokeJoinPoint();
        }

        this.currentInterceptorIndex++;

        MethodInterceptor interceptor = this.interceptors.get(this.currentInterceptorIndex);

        // invoke the target method with itself by interceptor
        return interceptor.invoke(this);
    }

    /**
     * Invoke the join point using reflection.
     * Subclasses can override this to use custom invocation.
     *
     * @return return value of the join point
     * @throws Throwable when invoking failed
     */
    protected Object invokeJoinPoint() throws Throwable {
        return this.targetMethod.invoke(this.targetObject, this.arguments);
    }

    public AccessibleObject getStaticPart() {
        return this.targetMethod;
    }
}
