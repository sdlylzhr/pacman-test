package com.thundersoft.codedog.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Socket管理器
 */
public class SocketManager {

    private static final String TAG = "PACMAN_ConnectivityManager";

    private static final SocketManager mInstance = new SocketManager();

    private Socket mSocket;
    private OutputStream mOutputStream;
    private InputStream mInputStream;

    private boolean mIsInitAvailable = false;

    public void connect(String ip, int port) {
        try {
            mSocket = new Socket(ip,port);
            mOutputStream = mSocket.getOutputStream();
            mInputStream = mSocket.getInputStream();
            mIsInitAvailable = true;
        } catch (IOException e) {
            System.out.println("Socket init error");
            e.printStackTrace();
        }
    }

    public boolean send(String command) {
        if (!checkInit()) {
            return false;
        }
        if (command == null) {
            System.out.println("command error");
            return false;
        }
        System.out.println("send command : " + command);
        try {
            mOutputStream.write(command.getBytes());
        } catch (IOException e) {
            System.out.println("Socket write error");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String callForResult(String command) {
        if (!checkInit()) {
            return null;
        }
        if (send(command)) {
            return read();
        }
        return null;
    }

    public String read() {
        if (!checkInit()) {
            return null;
        }
        try {
            byte[] bytes = new byte[1024];
            mInputStream.read(bytes);
            return new String(bytes).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean checkInit() {
        if (!mIsInitAvailable) {
            System.out.println("Socket is not initiate");
            return false;
        }
        return true;
    }

    public void close() {
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
