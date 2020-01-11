package org.litespring.service.version5;

import org.litespring.beans.factory.annotation.Autowired;
import org.litespring.dao.version5.AccountDao;
import org.litespring.dao.version5.ItemDao;
import org.litespring.util.MessageChecker;

/**
 * Description:
 *
 * @author ShaoJiale
 * date 2020/1/11
 */
public class PetStoreService {
    @Autowired
    private AccountDao accountDao;

    @Autowired
    private ItemDao itemDao;

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public ItemDao getItemDao() {
        return itemDao;
    }

    public PetStoreService() {
    }

    public void placeOrder() {
        System.out.println("place order");
        MessageChecker.addMsg("place order");
    }
}
