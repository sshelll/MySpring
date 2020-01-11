package org.litespring.aop.aspectj;

import org.aspectj.weaver.reflect.ReflectionWorld;
import org.aspectj.weaver.tools.*;
import org.litespring.aop.MethodMatcher;
import org.litespring.aop.Pointcut;
import org.litespring.util.ClassUtils;
import org.litespring.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Description: Test if a method matches the expression.
 *
 * @author Springframework team
 * date 2020/1/11
 * @see #matches(Method)
 */
public class AspectJExpressionPointcut implements Pointcut, MethodMatcher {
    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<PointcutPrimitive>();

    static {
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
    }

    private String expression;

    private PointcutExpression pointcutExpression;

    private ClassLoader pointcutClassLoader;

    public AspectJExpressionPointcut() {

    }

    /**
     * This class is a method matcher.
     * @return this class
     */
    public MethodMatcher getMethodMatcher() {
        return this;
    }

    public String getExpression() {
        return this.expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    /**
     * Judge if a method method matches the target method.
     *
     * @param method current method
     * @return matches or not
     * @see #checkReadyToMatch()
     * @see #getShadowMatch(Method)
     */
    public boolean matches(Method method) {
        checkReadyToMatch();

        ShadowMatch shadowMatch = getShadowMatch(method);

        if (shadowMatch.alwaysMatches()) {
            return true;
        }

        return false;
    }

    private ShadowMatch getShadowMatch(Method method) {
        ShadowMatch shadowMatch = null;

        try {
            shadowMatch = this.pointcutExpression.matchesMethodExecution(method);
        } catch (ReflectionWorld.ReflectionWorldException ex) {
            throw new RuntimeException("not implemented yet");
        }

        return shadowMatch;
    }

    /**
     * Check if it's ready to march and build pointcut expression.
     *
     * @see #matches(Method)
     */
    private void checkReadyToMatch() {
        if (getExpression() == null) {
            throw new IllegalStateException("Must set property 'expression' before attempting to match");
        }
        if (this.pointcutExpression == null) {
            this.pointcutClassLoader = ClassUtils.getDefaultClassLoader();
            this.pointcutExpression = buildPointcutExpression(this.pointcutClassLoader);
        }
    }

    /**
     * Build pointcut expression.
     *
     * @param classLoader class loader
     * @return pointcut expression
     */
    private PointcutExpression buildPointcutExpression(ClassLoader classLoader) {
        PointcutParser parser = PointcutParser
                .getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(
                        SUPPORTED_PRIMITIVES, classLoader);

        return parser.parsePointcutExpression(replaceBooleanOperators(getExpression()),
                null, new PointcutParameter[0]);
    }

    /**
     * Replace boolean expression with boolean operators.
     *
     * @param pcExpr pointcut expression
     * @return replaced pointcut expression
     */
    private String replaceBooleanOperators(String pcExpr) {
        String result = StringUtils.replace(pcExpr, " and ", " && ");
        result = StringUtils.replace(result, " or ", " || ");
        result = StringUtils.replace(result, " not ", " ! ");
        return result;
    }
}
