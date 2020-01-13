package org.litespring.aop.framework;

/**
 * Description: Exception for AopConfig.
 *
 * @author ShaoJiale
 * date 2020/1/13
 */
public class AopConfigException extends Exception {
    public AopConfigException(String message) {
        super(message);
    }

    public AopConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
