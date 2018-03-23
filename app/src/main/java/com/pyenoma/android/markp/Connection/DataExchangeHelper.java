package com.pyenoma.android.markp.Connection;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DataExchangeHelper {

    private Socket socket;
    private boolean isDataSent=false;
    private String receivedData;
    private String dataToBeSent;
    private boolean isDataReceived=false;

    private static final String TAG = "DataExchangeHelper";
    public  DataExchangeHelper(Socket socket) {
        this.socket=socket;
    }

    public void sendData(String data){
        this.isDataSent=false;
        new DataSender().execute(data);
    }

    public void receiveData(){
        this.isDataReceived=false;
        new DataReceiver().execute();
    }

    public void setDataToBeSent(String dataToBeSent) {
        this.dataToBeSent = dataToBeSent;
    }

    public String getReceivedData() {
        return receivedData;
    }

    public boolean isDataSent(){
        return this.isDataSent;
    }

    public boolean isDataReceived() {
        return isDataReceived;
    }

    class DataSender extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            DataOutputStream dataOutputStream;
            try {
                Log.d(TAG, "Data Transfer Initiated");
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(strings[0]);
                Log.d(TAG, "Data Transfer Completed Successfully");
                return true;
            } catch (IOException e) {
                Log.e(TAG, "Data Transfer failed "+e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            isDataSent=aBoolean;
        }
    }

    class DataReceiver extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            DataInputStream dataInputStream;
            Log.d(TAG,"Data Receiving initiated");
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
                receivedData = dataInputStream.readUTF();
                Log.d(TAG, "Data Received successfully: " + receivedData);
                sendData("S");
                return true;
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            isDataReceived=aBoolean;
        }
    }
}