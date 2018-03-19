package com.example.akshay.attendancebarcodereaderserver;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class DuplicateChecker {
    private ArrayList<InetAddress> addresses;
    private ArrayList<InetAddress> duplicateAddresses;
    private ArrayList<InetAddress> genuineAddresses;
    private DuplicateCheckerListener listener;
    private boolean stopChecking;

    public DuplicateChecker(DuplicateCheckerListener listener){
        this.listener=listener;
        addresses = new ArrayList<>();
        duplicateAddresses = new ArrayList<>();
        genuineAddresses = new ArrayList<>();
    }

    public void addSocket(Socket socket){
        addresses.add(socket.getInetAddress());
    }

    public void startChecking() {
        stopChecking=false;
        Thread checkerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!stopChecking){
                    while(!addresses.isEmpty()){
                        InetAddress inetAddress = addresses.remove(0);
                        if(genuineAddresses.contains(inetAddress)){
                            duplicateAddresses.add(inetAddress);
                            listener.onFound();
                        }
                        else{
                            genuineAddresses.add(inetAddress);
                        }
                    }
                }
            }
        });
        listener.onStart();
        checkerThread.start();
    }
    public void stopChecking(){
        stopChecking= true;
        listener.onStop();
    }
    public interface DuplicateCheckerListener{
        void onStart();
        void onFound();
        void onStop();
    }

    public ArrayList<InetAddress> getDuplicateSockets() {
        return duplicateAddresses;
    }
}
