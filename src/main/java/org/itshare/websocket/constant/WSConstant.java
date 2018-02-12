package org.itshare.websocket.constant;

import java.util.HashMap;
import java.util.Map;

import org.itshare.websocket.annotation.WSRequestMappingBean;

/**
 *
 * @author luxiangzhou
 * @time 2018年2月8日 上午10:37:00
 *
 */
public class WSConstant {
	/**
	 * websocket mvc注解扫描包
	 */
	public static final String WS_SCAN_PACKAGE = "org.itshare";

	/**
	 * websocket mvc注解扫描url开始段
	 */
	public static final String WS_URL_REG = "/api/websocket";

	/**
	 * websocket mvc注解扫描获得的注解对象
	 */
	public static Map<String, WSRequestMappingBean> WS_CLAZZ_MAP = new HashMap<>();

}
