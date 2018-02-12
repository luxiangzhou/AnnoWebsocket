package org.itshare.websocket.handler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 *
 * @author luxiangzhou
 * @time 2018年2月12日 上午9:36:57
 *
 */
public class WSUtils {
	private static final Map<String, WebSocketSession> wsSessions = new ConcurrentHashMap<>();

	public static void putWsSession(String key, WebSocketSession session) {
		WebSocketSession webSocketSession = wsSessions.get(key);
		if (webSocketSession == null) {
			wsSessions.put(key, session);
		}
	}

	public static WebSocketSession getWsSession(String key) {
		return wsSessions.get(key);
	}

	public static void removeWsSession(String key) {
		wsSessions.remove(key);
	}

	public static void sendMessage(String key, String message) {
		WebSocketSession wsSession = getWsSession(key);
		try {
			if (wsSession != null && wsSession.isOpen()) {
				wsSession.sendMessage(new TextMessage(message));
			} else {
				removeWsSession(key);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
