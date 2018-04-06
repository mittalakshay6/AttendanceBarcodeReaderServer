package com.pyenoma.android.markp.Connection;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.pyenoma.android.markp.R;

public class ConnectionManager {
    private boolean doAccept;
    private static ServerSocket serverSocket;
    private ArrayList<Socket> sockets;
    private static final int PORT = 1234;

    private final String TAG = "ConnectionManager";

    public boolean createServerSocket() {
//        if(ConnectionManager.serverSocket!=null){
//            return true;
//        }
        try {
            serverSocket = new ServerSocket(PORT);
            Log.d(TAG, "Server Socket creation successful");
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Server Socket Creation failed " + e.getMessage());
            return false;
        }
    }
    public void startAccepting() {
        doAccept=true;
        if (serverSocket.isBound()) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (doAccept) {
                        try {
                            Socket socket = serverSocket.accept();
                            sockets = new ArrayList<>();
                            sockets.add(socket);
                            Log.d(TAG, "Connected to " + socket.getInetAddress().toString());
                        } catch (IOException e) {
                            Log.e(TAG, "Failed to accept connection " + e.getMessage());
                        }
                    }
                }
            }.start();
        }
    }
    public void stopAccepting(){
        doAccept=false;
        if(serverSocket==null){
            return;
        }
        try {
            serverSocket.close();
            Log.d(TAG, "Server Socket closed");
        } catch (IOException e) {
            Log.e(TAG, "Cannot close server socket");
        }
    }


    public Socket getSocket(){
        if(!sockets.isEmpty())
            return sockets.remove(0);
        else
            return null;
    }
    public boolean areAnyPendingConnections(){
        if(sockets!=null) {
            return !sockets.isEmpty();
        }
        else{
            return false;
        }
    }
    public boolean isAccepting(){
        return doAccept;
    }
}