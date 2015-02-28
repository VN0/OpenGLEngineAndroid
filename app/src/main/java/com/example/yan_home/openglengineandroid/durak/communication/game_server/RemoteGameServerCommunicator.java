package com.example.yan_home.openglengineandroid.durak.communication.game_server;

import com.example.yan_home.openglengineandroid.durak.communication.socket.SocketConnectionManager;
import com.example.yan_home.openglengineandroid.durak.protocol.BaseProtocolMessage;
import com.example.yan_home.openglengineandroid.durak.protocol.messages.BlankProtocolMessage;
import com.example.yan_home.openglengineandroid.durak.protocol.messages.CardMovedProtocolMessage;
import com.example.yan_home.openglengineandroid.durak.protocol.messages.GameSetupProtocolMessage;
import com.example.yan_home.openglengineandroid.durak.protocol.messages.PlayerTakesActionMessage;
import com.example.yan_home.openglengineandroid.durak.protocol.messages.RequestCardForAttackMessage;
import com.example.yan_home.openglengineandroid.durak.protocol.messages.RequestRetaliatePilesMessage;
import com.example.yan_home.openglengineandroid.durak.protocol.messages.RequestThrowInsMessage;
import com.example.yan_home.openglengineandroid.durak.protocol.messages.RetaliationInvalidProtocolMessage;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yan-Home on 1/25/2015.
 * <p/>
 * This class is responsible to handle communication between server and client in an easy manner.
 */
public class RemoteGameServerCommunicator implements IGameServerConnector {

    private static final String SERVER_ADDRESS = "192.168.1.103";
    private static final int SERVER_PORT = 7000;

    private Gson mGson;
    private IGameServerCommunicatorListener mCommunicatorListener;

    //used for easy access to class of the message by it's name
    private Map<String, Class<? extends BaseProtocolMessage>> mNamesToClassMap;

    public RemoteGameServerCommunicator() {
        mGson = new Gson();
        mNamesToClassMap = new HashMap<>();

        //put all message classes and their names into a map
        fillNameToClassMap();
    }

    private void fillNameToClassMap() {
        mNamesToClassMap.put(CardMovedProtocolMessage.MESSAGE_NAME, CardMovedProtocolMessage.class);
        mNamesToClassMap.put(RequestCardForAttackMessage.MESSAGE_NAME, RequestCardForAttackMessage.class);
        mNamesToClassMap.put(RequestRetaliatePilesMessage.MESSAGE_NAME, RequestRetaliatePilesMessage.class);
        mNamesToClassMap.put(GameSetupProtocolMessage.MESSAGE_NAME, GameSetupProtocolMessage.class);
        mNamesToClassMap.put(PlayerTakesActionMessage.MESSAGE_NAME, PlayerTakesActionMessage.class);
        mNamesToClassMap.put(RetaliationInvalidProtocolMessage.MESSAGE_NAME, RetaliationInvalidProtocolMessage.class);
        mNamesToClassMap.put(RequestThrowInsMessage.MESSAGE_NAME, RequestThrowInsMessage.class);
        //TODO : add more protocol message classes as they will be added...
    }

    @Override
    public void connect() {
        SocketConnectionManager.getInstance().connectToRemoteServer(SERVER_ADDRESS, SERVER_PORT);
    }

    @Override
    public void disconnect() {
        SocketConnectionManager.getInstance().disconnectFromRemoteServer();
    }

    @Override
    public void update() {
        //read messages from remote socket server
        readMessageFromServer();
    }

    @Override
    public void setListener(IGameServerCommunicatorListener listener) {
        mCommunicatorListener = listener;
    }

    @Override
    public void sentMessageToServer(BaseProtocolMessage message) {
        SocketConnectionManager.getInstance().sendMessageToRemoteServer(message.toJsonString());
    }

    private void readMessageFromServer() {

        //we are reading messages only if the connector is currently connected
        if (!SocketConnectionManager.getInstance().isConnected())
            return;

        //try to obtain message from the connector
        String msg = SocketConnectionManager.getInstance().readMessageFromRemoteServer();

        //in case there was no message or no listener to process it ,we will do nothing
        if (msg == null || mCommunicatorListener == null)
            return;

        //handle the message
        handleServerMessage(msg);
    }

    private void handleServerMessage(String msg) {
        //read the message as blank , to identify its name
        BlankProtocolMessage message = mGson.fromJson(msg, BlankProtocolMessage.class);

        //forward the actual message to listener
        mCommunicatorListener.handleServerMessage(mGson.fromJson(msg, mNamesToClassMap.get(message.getMessageName())));
    }

}
