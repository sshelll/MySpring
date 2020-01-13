package org.litespring.aop.framework;

import org.litespring.aop.Advice;
import org.litespring.aop.Pointcut;
import org.litespring.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: Base class for AOP proxy configuration managers.
 * These are not themselves AOP proxies, but subclasses of this
 * class are normally factories which AOP proxy instances are obtained
 * directly.
 *
 * <p>This class frees subclasses of the housekeeping of Advices
 * and Advisors, but does not actually implement proxy creation
 * methods, which are provided by subclasses.</p>
 *
 * <p>This class is serializable, but subclasses need not be.
 * This class is used to hold snapshots of proxies.</p>
 *
 * @author Springframework team
 * date 2020/1/13
 */
public class AopConfigSupport implements AopConfig {

    private boolean proxyTargetClass = false;

    private Object targetObject = null;

    private List<Advice> advices = new ArrayList<>();

    private List<Class> interfaces = new ArrayList<>();

    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }

    public Object getTargetObject() {
        return this.targetObject;
    }

    public Class<?> getTargetClass() {
        return this.targetObject.getClass();
    }


    public void addInterface(Class<?> intf) {
        Assert.notNull(intf, "Interface must not be null");
        if (!intf.isInterface()) {
            throw new IllegalArgumentException("[" + intf.getName() + "] is not an interface");
        }
        if (!this.interfaces.contains(intf)) {
            this.interfaces.add(intf);

        }
    }

    public Class<?>[] getProxiedInterfaces() {
        return this.interfaces.toArray(new Class[this.interfaces.size()]);
    }

    public boolean isInterfaceProxied(Class<?> intf) {
        for (Class proxyIntf : this.interfaces) {
            if (intf.isAssignableFrom(proxyIntf)) {
                return true;
            }
        }
        return false;
    }

    public void addAdvice(Advice advice) {
        this.advices.add(advice);
    }


    public boolean isProxyTargetClass() {
        return proxyTargetClass;
    }

    public void setProxyTargetClass(boolean proxyTargetClass) {
        this.proxyTargetClass = proxyTargetClass;
    }

    public List<Advice> getAdvices() {
        return this.advices;
    }

    /**
     * Find the advice which matches the given method.
     *
     * @param method method
     * @return matched advice
     */
    public List<Advice> getAdvices(Method method) {
        List<Advice> result = new ArrayList<Advice>();
        for (Advice advice : this.getAdvices()) {
            Pointcut pc = advice.getPointcut();
            if (pc.getMethodMatcher().matches(method)) {
                result.add(advice);
            }
        }
        return result;
    }
}
