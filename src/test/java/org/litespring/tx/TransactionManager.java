package org.litespring.tx;

import org.litespring.util.MessageChecker;

/**
 * Description:
 *
 * @author ShaoJiale
 * date 2020/1/11
 */
public class TransactionManager {
    public void start() {
        System.out.println("start rx");
        MessageChecker.addMsg("start rx");
    }

    public void commit() {
        System.out.println("commit tx");
        MessageChecker.addMsg("commit rx");
    }

    public void rollback() {
        System.out.println("rollback tx");
        MessageChecker.addMsg("rollback rx");
    }
}
