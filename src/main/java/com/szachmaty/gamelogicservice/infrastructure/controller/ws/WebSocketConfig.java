package com.szachmaty.gamelogicservice.infrastructure.controller.ws;

import com.szachmaty.gamelogicservice.application.manager.GameDTOManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static com.szachmaty.gamelogicservice.infrastructure.controller.constant.APIRoutes.*;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final AuthenticationChannelInterceptor authenticationInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/game-handshake").setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/game");
//        config.setUserDestinationPrefix("/secured/user");
        config.enableSimpleBroker("/queue");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration channelRegistration) {
        channelRegistration.interceptors(
                authenticationInterceptor
        );
    }


}
