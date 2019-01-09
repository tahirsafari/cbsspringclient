package com.todo;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationContext;

@Component
public class SpringApplicationContext implements ApplicationContextAware {

	private  static ApplicationContext context;

    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }
    
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		context = applicationContext;
		
	}
	
	 public ApplicationContext getContext() {
	        return context;
	    }

}
