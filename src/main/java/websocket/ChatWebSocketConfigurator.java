package websocket;

import javax.websocket.server.ServerEndpointConfig;

public class ChatWebSocketConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        try {
            return endpointClass.newInstance();
        } catch (IllegalAccessException e) {
            throw new InstantiationException(e.getMessage());
        }
    }
} 