package org.litespring.test.version5;

import org.aopalliance.intercept.MethodInterceptor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.litespring.aop.aspectj.AspectJAfterReturningAdvice;
import org.litespring.aop.aspectj.AspectJAfterThrowingAdvice;
import org.litespring.aop.aspectj.AspectJBeforeAdvice;
import org.litespring.aop.framework.ReflectiveMethodInvocation;
import org.litespring.service.version5.PetStoreService;
import org.litespring.tx.TransactionManager;
import org.litespring.util.MessageTracker;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author ShaoJiale
 * date 2020/1/12
 */
public class ReflectiveMethodInvocationTest {
    private AspectJBeforeAdvice beforeAdvice = null;

    private AspectJAfterReturningAdvice afterAdvice = null;

    private AspectJAfterThrowingAdvice afterThrowingAdvice = null;

    private PetStoreService petStore = null;

    private TransactionManager tx;

    @Before
    public void setUp() throws Exception {
        petStore = new PetStoreService();
        tx = new TransactionManager();
        MessageTracker.clearMsg();

        beforeAdvice = new AspectJBeforeAdvice(
                TransactionManager.class.getMethod("start"),
                null,
                tx
        );

        afterAdvice = new AspectJAfterReturningAdvice(
                TransactionManager.class.getMethod("commit"),
                null,
                tx
        );

        afterThrowingAdvice = new AspectJAfterThrowingAdvice(
                TransactionManager.class.getMethod("rollback"),
                null,
                tx
        );
    }

    @Test
    public void testMethodInvocation() throws Throwable {
        Method targetMethod = PetStoreService.class.getMethod("placeOrder");

        List<MethodInterceptor> interceptors = new ArrayList<>();
        interceptors.add(beforeAdvice);
        interceptors.add(afterAdvice);

        ReflectiveMethodInvocation invocation = new ReflectiveMethodInvocation(petStore, targetMethod, new Object[0], interceptors);

        invocation.proceed();

        List<String> messages = MessageTracker.getMsg();
        Assert.assertEquals(3, messages.size());
        Assert.assertEquals("start tx", messages.get(0));
        Assert.assertEquals("place order", messages.get(1));
        Assert.assertEquals("commit tx", messages.get(2));
    }

    /**
     * Make sure that the order of the interceptors will not
     * effect the result.
     */
    @Test
    public void testMethodInvocation2() throws Throwable {
        Method targetMethod = PetStoreService.class.getMethod("placeOrder");

        List<MethodInterceptor> interceptors = new ArrayList<>();
        // change order here
        interceptors.add(afterAdvice);
        interceptors.add(beforeAdvice);

        ReflectiveMethodInvocation invocation = new ReflectiveMethodInvocation(petStore, targetMethod, new Object[0], interceptors);

        invocation.proceed();

        List<String> messages = MessageTracker.getMsg();
        Assert.assertEquals(3, messages.size());
        Assert.assertEquals("start tx", messages.get(0));
        Assert.assertEquals("place order", messages.get(1));
        Assert.assertEquals("commit tx", messages.get(2));
    }

    @Test
    public void testAfterThrowing() throws Throwable {
        Method targetMethod = PetStoreService.class.getMethod("placeOrderWithException");

        List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
        interceptors.add(beforeAdvice);
        interceptors.add(afterThrowingAdvice);

        ReflectiveMethodInvocation invocation = new ReflectiveMethodInvocation(petStore, targetMethod, new Object[0], interceptors);

        try {
            invocation.proceed();
        } catch (Throwable t) {
            List<String> messages = MessageTracker.getMsg();
            Assert.assertEquals(2, messages.size());
            Assert.assertEquals("start tx", messages.get(0));
            Assert.assertEquals("rollback tx", messages.get(1));
            return;
        }

        Assert.fail("No exception threw");
    }
}
