/* 
 * Copyright 2015 itshare.org All rights reserved.
 */
package org.itshare.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * 
 * @author luxiangzhou
 */
public class ShareDispatcherServlet extends DispatcherServlet {

	private static final long serialVersionUID = 7626951657578524525L;
	private final Log LOGGER = LogFactory.getLog(getClass());

	/**
	 * 覆盖父类方法，计算服务端业务执行时间
	 */
	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 开始时间
		long beginTime = System.currentTimeMillis();
		super.doService(request, response);
		// 结束时间
		long endTime = System.currentTimeMillis();
		// 记录执行时间
		if (StringUtils.isNotBlank(request.getRequestURI()) && !request.getRequestURI().startsWith("/static/")) {
			String queryString = StringUtils.isNotBlank(request.getQueryString()) ? ("?" + request.getQueryString())
					: "";
			LOGGER.warn(request.getRequestURL() + queryString + "," + (endTime - beginTime));
		}
	}

}
