package org.litespring.aop.framework;

/**
 * Description: An interface for all the AopProxy factories, which are able to get the
 * proxied object.
 *
 * @author ShaoJiale
 * date 2020/1/13
 */
public interface AopProxyFactory {

    /**
     * Create a new proxy object.
     * <p>Uses the AopProxy's default class loader (if necessary for proxy creation);
     * usually use the thread context class loader</p>
     *
     * @return the new proxy object (never {@code null})
     * @see Thread#getContextClassLoader()
     */
    Object getProxy() throws AopConfigException;

    /**
     * Create a new proxy object.
     * <p>Use the given class loader (if necessary for proxy creation).
     * {@code null} will simply be passed down and thus lead to the low-level
     * proxy facility's default, which is usually different from the default
     * chosen bt the AopProxy implementation's {@link #getProxy()} method.</p>
     *
     * @param classLoader the class loader to create the proxy with;
     *                    {or {@code null}} for the low-level proxy
     *                    facility's default.
     * @return the new proxy object (never {@code null})
     */
    Object getProxy(ClassLoader classLoader) throws AopConfigException;
}
