package org.litespring.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: Check if aop works by saving messages from aop.
 *
 * @author ShaoJiale
 * date 2020/1/11
 */
public class MessageTracker {
    private static List<String> TRACKER_MESSAGES = new ArrayList<>();

    public static void addMsg(String msg) {
        TRACKER_MESSAGES.add(msg);
    }

    public static void clearMsg() {
        TRACKER_MESSAGES.clear();
    }

    public static List<String> getMsg() {
        return TRACKER_MESSAGES;
    }
}
