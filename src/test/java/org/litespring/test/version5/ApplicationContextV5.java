package org.litespring.test.version5;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.litespring.context.ApplicationContext;
import org.litespring.context.support.ClassPathXmlApplicationContext;
import org.litespring.service.version5.PetStoreService;
import org.litespring.util.MessageChecker;

import java.util.List;

/**
 * Description:
 *
 * @author ShaoJiale
 * date 2020/1/11
 */
public class ApplicationContextV5 {

    @Before
    public void setUp() {
        MessageChecker.clearMsg();
    }

    @Test
    public void testPlaceOrder() {
        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("petstore-v5.xml");
        PetStoreService petStore =
                (PetStoreService)applicationContext.getBean("petStore");

        Assert.assertNotNull(petStore.getAccountDao());
        Assert.assertNotNull(petStore.getItemDao());

        petStore.placeOrder();

        List<String> msg = MessageChecker.getMsg();
        Assert.assertEquals(3, msg.size());
        Assert.assertEquals("start tx", msg.get(0));
        Assert.assertEquals("place order", msg.get(1));
        Assert.assertEquals("commit tx", msg.get(2));
    }

    //@Test
    //public void testPlaceOrderWithException() {
    //    MessageChecker.addMsg("xxxx");
    //}
}
