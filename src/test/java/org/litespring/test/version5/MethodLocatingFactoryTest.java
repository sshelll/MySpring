package org.litespring.test.version5;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.aop.config.MethodLocatingFactory;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.core.io.Resource;
import org.litespring.tx.TransactionManager;

import java.lang.reflect.Method;

/**
 * Description:
 *
 * @author ShaoJiale
 * date 2020/1/11
 */
public class MethodLocatingFactoryTest {
    @Test
    public void testGetMethod() throws Exception {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("petstore-v5.xml");
        reader.loadBeanDefinitions(resource);

        MethodLocatingFactory methodLocatingFactory = new MethodLocatingFactory();
        methodLocatingFactory.setTargetBeanName("tx");
        methodLocatingFactory.setMethodName("start");
        methodLocatingFactory.setBeanFactory(factory);

        Method method = methodLocatingFactory.getObject();

        Assert.assertTrue(TransactionManager.class.equals(method.getDeclaringClass()));
        Assert.assertTrue(method.equals(TransactionManager.class.getMethod("start")));
    }
}
