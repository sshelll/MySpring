package org.litespring.aop.aspectj;

import org.litespring.aop.Advice;
import org.litespring.aop.MethodMatcher;
import org.litespring.aop.Pointcut;
import org.litespring.aop.framework.AopConfigException;
import org.litespring.aop.framework.AopConfigSupport;
import org.litespring.aop.framework.AopProxyFactory;
import org.litespring.aop.framework.CglibProxyFactory;
import org.litespring.beans.BeansException;
import org.litespring.beans.factory.config.BeanPostProcessor;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Description:
 *
 * @author ShaoJiale
 * date 2020/1/26
 */
public class AspectJAutoProxyCreator implements BeanPostProcessor {
    ConfigurableBeanFactory beanFactory;

    public void setBeanFactory(ConfigurableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object beforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object afterInitialization(Object bean, String beanName) throws BeansException{
        if (isInfrastructureClass(bean.getClass()))
            return bean;

        List<Advice> advices = getCandidateAdvices(bean);
        if (advices.isEmpty())
            return bean;
        return createProxy(advices, bean);
    }

    private List<Advice> getCandidateAdvices(Object bean) {
        List<Object> advices = this.beanFactory.getBeansByType(Advice.class);

        List<Advice> result = new ArrayList<>();
        for (Object o : advices) {
            Pointcut pc = ((Advice) o).getPointcut();
            if (canApply(pc, bean.getClass())) {
                result.add((Advice) o);
            }
        }

        return result;
    }

    protected Object createProxy(List<Advice> advices, Object bean)  {
        AopConfigSupport config = new AopConfigSupport();
        for (Advice advice : advices)
            config.addAdvice(advice);

        Set<Class> targetInterfaces = ClassUtils.getAllInterfacesAsSet(bean.getClass());

        for (Class<?> targetInterface : targetInterfaces)
            config.addInterface(targetInterface);

        config.setTargetObject(bean);

        AopProxyFactory proxyFactory = null;
        if (config.getProxiedInterfaces().length == 0) {
            try {
                proxyFactory = new CglibProxyFactory(config);
            } catch (AopConfigException e) {
                e.printStackTrace();
            }
        }
        else {
            // TODO implementing JDK dynamic proxy
            // proxyFactory = new JdkAopProxyFactory(config);
        }

        try {
            return proxyFactory.getProxy();
        } catch (AopConfigException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Judge if the bean is a kind of Advice.
     *
     * @param beanClass class of bean
     * @return the bean is a kind of Advice or not.
     */
    protected boolean isInfrastructureClass(Class<?> beanClass) {
        boolean retVal = Advice.class.isAssignableFrom(beanClass);
        return retVal;
    }

    public static boolean canApply(Pointcut pc, Class<?> targetClass) {
        MethodMatcher matcher = pc.getMethodMatcher();

        Set<Class> classes = new LinkedHashSet<>(ClassUtils.getAllInterfacesForClassAsSet(targetClass));
        classes.add(targetClass);

        for (Class<?> clazz : classes) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (matcher.matches(method))
                    return true;
            }
        }
        return false;
    }
}
