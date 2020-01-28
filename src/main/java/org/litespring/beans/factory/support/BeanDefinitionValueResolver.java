package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.BeansException;
import org.litespring.beans.factory.FactoryBean;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;
import org.litespring.beans.factory.exception.BeanCreationException;

/**
 * Description: Resolve property value
 * As we can see, the value in the 'property' tag
 * can be a bean(ref) or a String(value). If it's a bean,
 * users have to define it in the XML config file, which means
 * this bean is loaded when the method loadBeanDefinition()
 * is called. So the only thing we need to do is get the
 * 'ref' bean from the factory.
 *
 * @author ShaoJiale
 * date 2019/12/12
 * @see org.litespring.beans.factory.xml.XmlBeanDefinitionReader
 * @see DefaultBeanFactory
 */
public class BeanDefinitionValueResolver {

    private final AbstractBeanFactory beanFactory;

    public BeanDefinitionValueResolver(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * Resolve 'ref' or 'value' in the property tag
     * The only thing we need to do is get it from the
     * factory because we've defined it in the XML config file.
     *
     * @param value a RuntimeBean or a String
     * @return a bean or a String
     */
    public Object resolveValueIfNecessary(Object value) {
        if (value instanceof RuntimeBeanReference) {
            RuntimeBeanReference ref = (RuntimeBeanReference) value;
            String refName = ref.getBeanName();
            Object bean = this.beanFactory.getBean(refName);
            return bean;
        } else if (value instanceof TypedStringValue) {
            return ((TypedStringValue) value).getValue();
        } else if (value instanceof BeanDefinition) {
            BeanDefinition bd = (BeanDefinition)value;
            String innerBeanName = "(inner bean)" + bd.getBeanClassName() + "#" +
                    Integer.toHexString(System.identityHashCode(bd));
            return resolveInnerBean(innerBeanName, bd);
        } else {
            return value;
        }
    }

    private Object resolveInnerBean(String innerBeanName, BeanDefinition innerBd) {
        try {
            Object innerBean = this.beanFactory.createBean(innerBd);
            if (innerBean instanceof FactoryBean) {
                try {
                    return ((FactoryBean<?>) innerBean).getObject();
                } catch (Exception e) {
                    throw new BeanCreationException(innerBeanName, "FactoryBean threw exception on getting Object", e);
                }
            } else {
                return innerBean;
            }
        } catch (BeansException ex) {
            throw new BeanCreationException(
                    innerBeanName,
                    "Cannot create inner bean '" + innerBeanName + "' " +
                    (innerBd != null && innerBd.getBeanClassName() != null ? "of type[" + innerBd.getBeanClassName() + "]" : ""),
                    ex
            );
        }
    }
}
