package org.itshare.websocket.handler;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 *
 * @author luxiangzhou
 * @time 2018年2月12日 上午8:31:52
 *
 */
public class WSInterceptor implements HandshakeInterceptor {
	private static final Log LOGGER = LogFactory.getLog(WSInterceptor.class);

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		LOGGER.info("AnnoWebsocket before handshake ...");
		return false;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		LOGGER.info("AnnoWebsocket after handshake !");

	}

}
