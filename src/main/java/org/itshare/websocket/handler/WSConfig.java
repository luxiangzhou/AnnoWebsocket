package org.itshare.websocket.handler;

import org.itshare.websocket.constant.WSConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 *
 * @author luxiangzhou
 * @time 2018年2月8日 下午5:49:47
 *
 */
@Configuration
@EnableWebMvc
@EnableWebSocket
public class WSConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// 允许连接的域,只能以http或https开头
		String[] allowsOrigins = { "*" };

		// WebSocket通道：注意需要加*，没有*则要和websocket请求一致
		registry.addHandler(wsHandler(), WSConstant.WS_URL_REG + "*").setAllowedOrigins(allowsOrigins)
				.addInterceptors(wsInterceptor());
	}

	@Bean
	public WebSocketHandler wsHandler() {
		return new WSHandler();
	}

	@Bean
	public HandshakeInterceptor wsInterceptor() {
		return new WSInterceptor();
	}
}
