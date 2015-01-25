package com.example.yan_home.openglengineandroid.durak.communication.game_server;

import com.example.yan_home.openglengineandroid.durak.protocol.BaseProtocolMessage;

/**
 * Created by Yan-Home on 1/25/2015.
 * This interface is responsible for easy communication between server and a client.
 */
public interface IGameServerConnector {

    /**
     * Listener will be notified of different server events
     */
    public interface IGameServerCommunicatorListener {

        /**
         * New message from the server received.
         */
        void handleServerMessage(BaseProtocolMessage serverMessage);
    }

    /**
     * Connects to remote server
     */
    void connect();

    /**
     * Disconnects from remote server
     */
    void disconnect();

    /**
     * Call this function every frame , to poll any incoming messages
     */
    void update();

    /**
     * Subscribe as a listener to server events.
     */
    void setListener(IGameServerCommunicatorListener listener);

    /**
     * Send a message to remote server
     */
    void sentMessageToServer(BaseProtocolMessage message);

}
