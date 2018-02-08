package org.itshare.websocket.annotation;

import java.lang.reflect.Method;

/**
 *
 * @author luxiangzhou
 * @time 2018年2月8日 上午10:43:06
 *
 */

public class WSRequestMappingBean {
	// 类bean
	private Object bean;

	// 类上WSRequestMapping注解url
	private String clazzWsUrl;

	// 方法
	private Method method;

	// 方法上WSRequestMapping注解url
	private String methodWsUrl;

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}

	public String getClazzWsUrl() {
		return clazzWsUrl;
	}

	public void setClazzWsUrl(String clazzWsUrl) {
		this.clazzWsUrl = clazzWsUrl;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getMethodWsUrl() {
		return methodWsUrl;
	}

	public void setMethodWsUrl(String methodWsUrl) {
		this.methodWsUrl = methodWsUrl;
	}

	@Override
	public String toString() {
		return "WSRequestMappingBean [bean=" + bean + ", clazzWsUrl=" + clazzWsUrl + ", method=" + method
				+ ", methodWsUrl=" + methodWsUrl + "]";
	}

}
