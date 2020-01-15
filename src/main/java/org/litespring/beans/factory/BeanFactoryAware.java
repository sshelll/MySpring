package org.litespring.beans.factory;

import org.litespring.beans.BeansException;

/**
 * Description:
 *
 * @author ShaoJiale
 * date 2020/1/15
 */
public interface BeanFactoryAware {
    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
