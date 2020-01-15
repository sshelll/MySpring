package org.litespring.test.version5;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.aop.aspectj.AspectJBeforeAdvice;
import org.litespring.aop.aspectj.AspectJExpressionPointcut;
import org.litespring.aop.config.AspectInstanceFactory;
import org.litespring.aop.config.MethodLocatingFactory;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.tx.TransactionManager;

import java.util.List;

/**
 * Description:
 *
 * @author ShaoJiale
 * date 2020/1/15
 */
public class BeanDefinitionTestV5 extends AbstractV5Test{
    @Test
    public void testAOPBean() {
        DefaultBeanFactory factory = (DefaultBeanFactory) this.getBeanFactory("petstore-v5.xml");

        // check the bd with the name "tx"
        {
            BeanDefinition bd = factory.getBeanDefinition("tx");
            Assert.assertTrue(bd.getBeanClassName().equals(TransactionManager.class.getName()));
        }

        // check placeOrder
        {
            BeanDefinition bd = factory.getBeanDefinition("placeOrder");
            Assert.assertTrue(bd.isSynthetic());
            Assert.assertTrue(bd.getBeanClass().equals(AspectJExpressionPointcut.class));

            PropertyValue pv = bd.getPropertyValues().get(0);
            Assert.assertEquals("expression", pv.getName());
            Assert.assertEquals("execution(* org.litespring.service.version5.*.placeOrder(..))", pv.getValue());
        }

        //  check AspectJBeforeAdvice
        {
            String name = AspectJBeforeAdvice.class.getName() + "#0";
            BeanDefinition bd = factory.getBeanDefinition(name);

            Assert.assertTrue(bd.isSynthetic());

            List<ConstructorArgument.ValueHolder> args = bd.getConstructorArgument().getArgumentValues();
            Assert.assertEquals(3, args.size());

            // 1st arg
            {
                BeanDefinition innerBd = (BeanDefinition)args.get(0).getValue();
                Assert.assertTrue(innerBd.isSynthetic());
                Assert.assertTrue(innerBd.getBeanClass().equals(MethodLocatingFactory.class));

                List<PropertyValue> pvs = innerBd.getPropertyValues();
                Assert.assertEquals("targetBeanName", pvs.get(0).getName());
                Assert.assertEquals("tx", pvs.get(0).getValue());
                Assert.assertEquals("methodName", pvs.get(1).getName());
                Assert.assertEquals("start", pvs.get(1).getValue());
            }

            // 2nd arg
            {
                RuntimeBeanReference ref = (RuntimeBeanReference)args.get(1).getValue();
                Assert.assertEquals("placeOrder", ref.getBeanName());
            }

            // 3rd arg
            {
                BeanDefinition innerBd = (BeanDefinition)args.get(2).getValue();
                Assert.assertTrue(innerBd.isSynthetic());
                Assert.assertTrue(innerBd.getBeanClass().equals(AspectInstanceFactory.class));

                List<PropertyValue> pvs = innerBd.getPropertyValues();
                Assert.assertEquals("aspectBeanName", pvs.get(0).getName());
                Assert.assertEquals("tx", pvs.get(0).getValue());
            }
        }
    }
}
