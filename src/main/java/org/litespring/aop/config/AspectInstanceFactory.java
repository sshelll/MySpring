package org.litespring.aop.config;


import org.litespring.beans.BeansException;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.BeanFactoryAware;
import org.litespring.util.StringUtils;

/**
 * Description: Use this factory to get aspect bean instance.
 *
 * @author ShaoJiale
 * date 2020/1/14
 */
public class AspectInstanceFactory implements BeanFactoryAware {
    private String aspectBeanName;

    private BeanFactory beanFactory;

    public void setAspectBeanName(String aspectBeanName) {
        this.aspectBeanName = aspectBeanName;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        if (!StringUtils.hasText(this.aspectBeanName)) {
            throw new IllegalArgumentException("'aspectBeanName' is required");
        }
    }

    public Object getAspectInstance() throws Exception{
        return this.beanFactory.getBean(aspectBeanName);
    }
}
