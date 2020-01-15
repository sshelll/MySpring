package org.litespring.beans.factory;

/**
 * Description:
 *
 * @author ShaoJiale
 * date 2020/1/15
 */
public interface FactoryBean<T> {
    T getObject() throws Exception;

    Class<?> getObjectType();
}
