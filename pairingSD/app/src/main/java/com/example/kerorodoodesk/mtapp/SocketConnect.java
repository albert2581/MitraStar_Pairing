package com.example.kerorodoodesk.mtapp;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class SocketConnect {
    private DatagramPacket datagramPacket;
    private DatagramSocket datagramSocket;
    private byte[] receiveData = new byte[60000];

    private DataCallback callback;
//    private Thread background;

    static private SocketConnect connect;

    private SocketConnect() {
        try {
            datagramPacket = new DatagramPacket(receiveData, receiveData.length);
            datagramSocket = new DatagramSocket(5555);

        } catch (SocketException e) {
            e.printStackTrace();
        }


        (new Thread(new Runnable() {
            @Override
            public void run() {
                    while (true) {
                        try {
                            datagramSocket.receive(datagramPacket);
                            if (callback != null) {
                                final String data = new String(receiveData, 0, datagramPacket.getLength());
                                Log.d("Socket Connect", data);
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onDataReceive(data);
                                    }
                                });
                            } //if
                        } catch (Exception e) {
                            e.printStackTrace();
                            break;
                        }
                    }//while
            }
        })).start();

//        this.background = new Thread(new Runnable() {
//            public void run() {
//                while (true) {
//                    try {
//                        datagramSocket.receive(datagramPacket);
//                        if (callback != null) {
//                            final String data = new String(receiveData, 0, datagramPacket.getLength());
//                            Log.d("Socket Connect", data);
//                            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    callback.onDataReceive(data);
//                                }
//                            });
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        break;
//                    }
//                }
//            }
//        });
    }

    public static SocketConnect getInstance() {
        if (connect == null) {
            connect = new SocketConnect();
        }
        return connect;
    }

    public void setDataCallback(DataCallback callback) {
        this.callback = callback;
//        background.start();
    }

    public interface DataCallback {
        void onDataReceive(String data);
    }
}
