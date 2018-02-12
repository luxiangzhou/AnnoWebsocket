package org.itshare.websocket.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
@ComponentScan
public class WSConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(wsHandler(), "/anno/api/websocket").addInterceptors(wsInterceptor()).setAllowedOrigins("*");
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
