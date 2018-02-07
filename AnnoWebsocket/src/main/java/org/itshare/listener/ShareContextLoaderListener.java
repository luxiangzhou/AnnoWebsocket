/* 
 * Copyright 2015 itshare.org All rights reserved.
 */
package org.itshare.listener;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.Log4jWebConfigurer;

/**
 * 
 * @author luxiangzhou
 */
public class ShareContextLoaderListener extends ContextLoaderListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
			System.out.println("Init spring beans begin..." + dateFormatter.format(new Date()));
			System.out.println("Current machine ip is:" + InetAddress.getLocalHost().getHostAddress());
			// 保存一些实例的句柄以利于操作
			final ServletContext servletContext = sce.getServletContext();
			// 初始化log4j
			System.out.println("Before init log4j..." + dateFormatter.format(new Date()));
			Log4jWebConfigurer.initLogging(servletContext);
			// 初始化spring
			System.out.println("Before init Spring Container.........." + dateFormatter.format(new Date()));
			final WebApplicationContext applicationContext = initWebApplicationContext(servletContext);
			System.out.println("There are " + applicationContext.getBeanDefinitionCount() + " spring beans in system!");
			String[] str = applicationContext.getBeanDefinitionNames();
			for (int i = 0; i < str.length; i++) {
				System.out.println("Spring Bean-" + (i + 1) + ":" + str[i]);
			}
			System.out.println("Init spring beans done! " + dateFormatter.format(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
