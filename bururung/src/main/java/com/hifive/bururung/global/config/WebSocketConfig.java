package com.hifive.bururung.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 접속할 엔드포인트, SockJS fallback 사용
        registry.addEndpoint("/wss").setAllowedOrigins("https://www.hifive5.shop/").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 간단 메시지 브로커 활성화
        config.enableSimpleBroker("/topic");
        // 클라이언트에서 메시지 전송 시 접두사 설정
        config.setApplicationDestinationPrefixes("/app");
    }
}

