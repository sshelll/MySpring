package org.litespring.test.version5;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.aop.MethodMatcher;
import org.litespring.aop.aspectj.AspectJExpressionPointcut;
import org.litespring.service.version5.PetStoreService;

import java.lang.reflect.Method;

/**
 * Description:
 *
 * @author ShaoJiale
 * date 2020/1/11
 */
public class PointcutTest {
    @Test
    public void testPointcut() throws Exception{
        String expression = "execution(* org.litespring.service.version5.*.placeOrder(..))";

        AspectJExpressionPointcut pc = new AspectJExpressionPointcut();
        pc.setExpression(expression);

        MethodMatcher matcher = pc.getMethodMatcher();

        {
            Class<?> targetClass = PetStoreService.class;

            Method method1 = targetClass.getMethod("placeOrder");
            Assert.assertTrue(matcher.matches(method1));

            Method method2 = targetClass.getMethod("getAccountDao");
            Assert.assertFalse(matcher.matches(method2));
        }

        {
            Class<?> targetClass = org.litespring.service.version4.PetStoreService.class;

            Method method2 = targetClass.getMethod("getAccountDao");
            Assert.assertFalse(matcher.matches(method2));
        }
    }
}
