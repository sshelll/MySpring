package org.litespring.aop.framework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.litespring.aop.Advice;
import org.litespring.util.Assert;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.*;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: CGLib-based {@link AopProxyFactory} implementation for the Spring AOP framework.
 *
 * @author ShaoJiale
 * date 2020/1/13
 */
public class CglibProxyFactory implements AopProxyFactory {

    // Constants for CGLib callback array indices
    private static final int AOP_PROXY = 0;

    // no use currently
    private static final int INVOKE_TARGET = 1;
    private static final int NO_OVERRIDE = 2;
    private static final int DISPATCH_TARGET = 3;
    private static final int DISPATCH_ADVISE = 4;
    private static final int INVOKE_EQUALS = 5;
    private static final int INVOKE_HASHCODE = 6;

    // Logger available to subclasses; static to optimize serialization
    protected static final Log logger = LogFactory.getLog(CglibProxyFactory.class);

    protected final AopConfig config;

    private Object[] constructorArgs;

    private Class<?>[] constructorArgTypes;

    public CglibProxyFactory(AopConfig config) throws AopConfigException {
        Assert.notNull(config, "AdvisedSupport must not be null");

        if (config.getAdvices().size() == 0) {
            throw new AopConfigException("No advisors specified");
        }
        this.config = config;
    }

    public void setConstructorArguments(Object[] constructorArgs, Class<?>[] constructorArgTypes) {
        if (constructorArgs == null || constructorArgTypes == null) {
            throw new IllegalArgumentException("Both 'constructorArgs' and 'constructorArgTypes' must not be null");
        }

        if (constructorArgs.length != constructorArgTypes.length) {
            throw new IllegalArgumentException("Number of 'constructorArgs' (" + constructorArgs.length +
                    ") must match number of 'constructorArgTypes' (" + constructorArgTypes.length);
        }

        this.constructorArgs = constructorArgs;
        this.constructorArgTypes = constructorArgTypes;
    }

    @Override
    public Object getProxy() throws AopConfigException {
        return getProxy(null);
    }

    /**
     * Get proxied object with class loader.
     *
     * @param classLoader the class loader to create the proxy with;
     *                    {or {@code null}} for the low-level proxy
     *                    facility's default.
     * @return proxied object
     * @throws AopConfigException get proxied object failed
     */
    @Override
    public Object getProxy(ClassLoader classLoader) throws AopConfigException {
        if (logger.isDebugEnabled()) {
            logger.debug("Creating CGLib proxy: target source is " + this.config.getTargetClass());
        }

        try {
            Class<?> rootClass = this.config.getTargetClass();

            Enhancer enhancer = new Enhancer();

            if (classLoader != null) {
                enhancer.setClassLoader(classLoader);
            }

            enhancer.setSuperclass(rootClass);
            enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
            enhancer.setInterceptDuringConstruction(false);

            Callback[] callbacks = getCallbacks(rootClass);
            Class<?>[] types = new Class<?>[callbacks.length];

            for (int i = 0; i < types.length; i++) {
                types[i] = callbacks[i].getClass();
            }

            enhancer.setCallbackFilter(new ProxyCallbackFilter(this.config));
            enhancer.setCallbackTypes(types);
            enhancer.setCallbacks(callbacks);

            // create proxied object
            Object proxy = enhancer.create();
            //if (this.constructorArgs != null) {
            //    proxy = enhancer.create(this.constructorArgTypes, this.constructorArgs);
            //} else {
            //    proxy = enhancer.create();
            //}
            return proxy;

        } catch (CodeGenerationException ex) {
            throw new AopConfigException("Could not generate CGLib subclass of class[" +
                    this.config.getTargetClass() + "]: " +
                    "Common causes of this problem include using a final class or a non-visible class",
                    ex);
        } catch (IllegalArgumentException ex) {
            throw new AopConfigException("Could not generate CGLIB subclass of class [" +
                    this.config.getTargetClass() + "]: " +
                    "Common causes of this problem include using a final class or a non-visible class",
                    ex);
        } catch (Exception ex) {
            throw new AopConfigException("Unexpected AOP exception", ex);
        }
    }

    /**
     * Get callbacks.
     *
     * @param rootClass target class
     * @return callbacks
     * @throws Exception get callbacks failed
     * @see DynamicAdvisedInterceptor
     */
    private Callback[] getCallbacks(Class<?> rootClass) throws Exception {
        Callback aopInterceptor = new DynamicAdvisedInterceptor(this.config);

        Callback[] callbacks = new Callback[]{aopInterceptor};

        return callbacks;
    }

    /**
     * General purpose AOP callback. Used when the target is
     * dynamic or when the proxy is not frozen.
     */
    private class DynamicAdvisedInterceptor implements MethodInterceptor, Serializable {
        private final AopConfig config;

        public DynamicAdvisedInterceptor(AopConfig config) {
            this.config = config;
        }

        @Override
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            Object target = this.config.getTargetObject();

            List<Advice> chain = this.config.getAdvices(method);

            Object retVal;

            // Check whether we only have one InvokeInterceptor: that is,
            // no real advice, but just reflective invocation of the target.
            if (chain.isEmpty() && Modifier.isPublic(method.getModifiers())) {
                retVal = methodProxy.invoke(target, args);
            } else {
                List<org.aopalliance.intercept.MethodInterceptor> interceptors =
                        new ArrayList<>();

                interceptors.addAll(chain);

                retVal = new ReflectiveMethodInvocation(target, method, args, interceptors).proceed();
            }

            return retVal;
        }
    }

    /**
     * CallbackFilter to assign Callbacks to methods.
     */
    private static class ProxyCallbackFilter implements CallbackFilter {
        private final AopConfig config;

        public ProxyCallbackFilter(AopConfig advised) {
            this.config = advised;
        }

        /**
         * Always return 0 to simplify it, which means
         * all methods will be considered as target methods.
         *
         * @param method target method which is proxied
         * @return 0 all the time
         */
        @Override
        public int accept(Method method) {
            return AOP_PROXY;
        }
    }
}
