package com.example.yan_home.openglengineandroid.durak.communication.socket;

import com.yan.glengine.util.YANLogger;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Yan-Home on 12/24/2014.
 * <p/>
 * Implemented as a singleton.
 * Manages connection to remote socket server
 */
public class SocketConnectionManager {

    private static final SocketConnectionManager INSTANCE = new SocketConnectionManager();
    private SocketClient mSocketClient;
    private volatile boolean mConnected;
    private Queue<String> mMessageQueue;

    public static final SocketConnectionManager getInstance() {
        return INSTANCE;
    }

    private SocketConnectionManager() {
        mMessageQueue = new LinkedList<>();
    }

    /**
     * Connects to remote socket server
     *
     * @return true if connection was established , false otherwise
     */
    public boolean connectToRemoteServer(final String serverAddress, final int serverPort) {

        if (isConnected())
            return false;

        //TODO : this might be not an appropriate way to maintain connection
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocketClient = new RemoteClient(new Socket(serverAddress, serverPort));
                    mConnected = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                while (isConnected()) {
                    String msg = mSocketClient.readMessage();
                    if (msg != null) {
                        synchronized (mMessageQueue) {
                            YANLogger.log("[RECEIVED] " + msg);
                            mMessageQueue.add(msg);
                        }
                    }
                }

            }
        })).start();

        return true;
    }

    public void disconnectFromRemoteServer() {
        mSocketClient.disconnect();
        mConnected = false;
    }

    public void sendMessageToRemoteServer(String msg) {

        YANLogger.log("[SENT] " + msg);
        mSocketClient.sendMessage(msg);
    }

    public String readMessageFromRemoteServer() {
        synchronized (mMessageQueue) {
            return mMessageQueue.poll();
        }
    }

    public boolean isConnected() {
        return mConnected;
    }
}
