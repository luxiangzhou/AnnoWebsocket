package org.itshare.websocket.handler;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.itshare.websocket.annotation.WSRequestMappingBean;
import org.itshare.websocket.constant.WSConstant;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import net.sf.json.JSONObject;

/**
 *
 * @author luxiangzhou
 * @time 2018年2月8日 下午5:52:16
 *
 */
public class WSHandler implements WebSocketHandler {
	private static final Log LOGGER = LogFactory.getLog(WSHandler.class);

	// 线程数
	public static final int THREAD_NUM = 20;
	// 线程池
	public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(THREAD_NUM);

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleMessage(final WebSocketSession session, final WebSocketMessage<?> message) throws Exception {
		EXECUTOR_SERVICE.submit(new Runnable() {
			@Override
			public void run() {
				try {
					handle(session, message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void handle(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		// websocket url
		URI uri = session.getUri();
		String wsUrl = uri.toString();
		// websocket message
		String wsMessage = (String) message.getPayload();
		JSONObject jsonObject = JSONObject.fromObject(wsMessage);

		// 反射调用业务方法，并将业务方法返回的数据返回给websocket
		if (WSConstant.WS_CLAZZ_MAP.containsKey(wsUrl)) {
			WSRequestMappingBean wsBean = WSConstant.WS_CLAZZ_MAP.get(wsUrl);
			Object bean = wsBean.getBean();
			// 方法
			Method wsBeanMethod = wsBean.getMethod();
			// 方法参数
			Class<?>[] parameterTypes = wsBeanMethod.getParameterTypes();
			LocalVariableTableParameterNameDiscoverer paramterDiscover = new LocalVariableTableParameterNameDiscoverer();
			String[] parameterNames = paramterDiscover.getParameterNames(wsBeanMethod);
			Object[] args = new Object[parameterTypes.length];
			for (int i = 0; i < parameterTypes.length; i++) {
				Class<?> pClazz = parameterTypes[i];
				Object pValue = null;
				if ("java.lang.String".equals(pClazz.getName()) || "java.lang.Booealn".equals(pClazz.getName())
						|| "boolean".equals(pClazz.getName()) || "int".equals(pClazz.getName())) {
					pValue = jsonObject.get(parameterNames[i]);
				} else {
					pValue = JSONObject.toBean(jsonObject, pClazz);
				}
				args[i] = pValue;
			}
			// 反射调用业务方法
			Object resObj = wsBean.getMethod().invoke(bean, args);
			// 将业务方法返回的数据返回给websocket

		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean supportsPartialMessages() {
		// TODO Auto-generated method stub
		return false;
	}

}
