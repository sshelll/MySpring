package org.litespring.test.version5;

import org.junit.Test;
import org.litespring.service.version5.PetStoreService;
import org.litespring.tx.TransactionManager;
import org.springframework.cglib.proxy.*;

import java.lang.reflect.Method;

/**
 * Description:
 *
 * @author ShaoJiale
 * date 2020/1/12
 */
public class CGLibTest {

    /**
     * Custom Method Interceptor
     */
    public static class TransactionInterceptor implements MethodInterceptor {
        TransactionManager txManager = new TransactionManager();

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            txManager.start();
            Object result = proxy.invokeSuper(obj, args);
            txManager.commit();
            return result;
        }
    }

    /**
     * Custom Callback Filter
     * Only accept method placeOrder() here
     */
    private static class ProxyCallbackFilter implements CallbackFilter {
        @Override
        public int accept(Method method) {
            if(method.getName().startsWith("place"))
                return 0;
            return 1;
        }
    }

    /**
     * Test CGLib AOP
     */
    @Test
    public void testCallBack(){
        Enhancer enhancer = new Enhancer();

        enhancer.setSuperclass(PetStoreService.class);

        enhancer.setCallback(new TransactionInterceptor());
        PetStoreService petStore = (PetStoreService)enhancer.create();
        petStore.placeOrder();
    }

    /**
     * Test CGLib AOP with filter which only accept method which
     * begins with "place".
     */
    @Test
    public void testFilter(){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(PetStoreService.class);

        enhancer.setInterceptDuringConstruction(false);

        Callback[] callbacks = new Callback[]{new TransactionInterceptor(),NoOp.INSTANCE};

        Class<?>[] types = new Class<?>[callbacks.length];
        for (int x = 0; x < types.length; x++) {
            types[x] = callbacks[x].getClass();
        }

        enhancer.setCallbackFilter(new ProxyCallbackFilter());
        enhancer.setCallbacks(callbacks);
        enhancer.setCallbackTypes(types);

        PetStoreService petStore = (PetStoreService)enhancer.create();
        petStore.placeOrder();
        System.out.println(petStore.toString());

    }
}
