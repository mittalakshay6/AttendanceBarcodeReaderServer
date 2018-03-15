package com.example.akshay.attendancebarcodereaderserver.Connection;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.example.akshay.attendancebarcodereaderserver.R;

public class ConnectionManager {
    private boolean doAccept;
    private ServerSocket serverSocket;
    private ArrayList<Socket> sockets;
    private static final int PORT = 1234;

    private final String TAG = "ConnectionManager";

    public boolean createServerSocket() {
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
        try {
            serverSocket.close();
            Log.d(TAG, "Server Socket closed");
        } catch (IOException e) {
            Log.e(TAG, "Cannot close server socket");
        }
    }

    //TODO Implement the method to serve the accepted sockets

    public Socket getSocket(){
        return sockets.remove(0);
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