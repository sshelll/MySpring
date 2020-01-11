package org.litespring.beans.factory;

/**
 * Description: Container of my Spring
 * Define some behaviors of container.
 *
 * @author ShaoJiale
 * date 2019/12/10
 * @see org.litespring.beans.factory.support.DefaultBeanFactory
 * @see org.litespring.beans.factory.config.ConfigurableBeanFactory
 */
public interface BeanFactory {

    Object getBean(String beanID);

    /**
     * Get Class of a bean with its bean ID.
     * @param name bean id
     * @return Class of the bean
     * @throws NoSuchBeanDefinitionException when the container dose not contain it
     */
    Class<?> getType(String name) throws NoSuchBeanDefinitionException;
}
