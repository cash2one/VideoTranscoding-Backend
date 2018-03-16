package es.urjc.videotranscoding.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * This class return the application Context of spring
 * 
 * @author luisca
 * @since 0.5
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

	private static ApplicationContext context;

	public static ApplicationContext getApplicationContext() {
		return context;
	}

	@Override
	public void setApplicationContext(ApplicationContext ac) throws BeansException {
		context = ac;
	}
}