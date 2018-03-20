package com.example.akshay.attendancebarcodereaderserver;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class DuplicateChecker {
    private ArrayList<InetAddress> genuineAddresses;

    public DuplicateChecker(){
        genuineAddresses = new ArrayList<>();
    }

    public boolean checkThisINetAddress(InetAddress inetAddress){
        // true == proxy
        // false == not a proxy
        if(genuineAddresses.isEmpty()){
            genuineAddresses.add(inetAddress);
            return false;
        }
        else if(genuineAddresses.contains(inetAddress)){
            return true;
        }
        else{
            genuineAddresses.add(inetAddress);
            return false;
        }
    }
}
