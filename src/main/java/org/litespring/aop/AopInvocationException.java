package org.litespring.aop;

/**
 * Description: Exception for AOP invocation.
 *
 * @author ShaoJiale
 * date 2020/1/13
 */
public class AopInvocationException extends RuntimeException {
    public AopInvocationException(String message) {
        super(message);
    }

    public AopInvocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
