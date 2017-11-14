package com.example.kerorodoodesk.mtapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import mtserver.MTGETServer;
import mtserver.MTPOSTServer;

public class HTTPService extends Service {
    MTGETServer myGetServer;
    MTPOSTServer myPostServer;

    public HTTPService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        System.out.println("MyService onCreate()：Called by the system when the service is first created");
        super.onCreate();
        openServer();
    }

    public String openServer() {
        WifiManager wifiMgr = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/mtapp");
        dir.mkdirs();
        try {
            myGetServer = new MTGETServer(this, dir, ipAddress);
        } catch (IOException ioe) {
            Log.d("HTTPServiceError","myGetServerError");
        }
        try {
            myPostServer = new MTPOSTServer(this, dir, ipAddress);
        } catch (IOException ioe) {
            Log.d("HTTPServiceError","myPostServerError");
        }

        //  mUploadServer = new MTUploadServer();
        return ipAddress;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("MyService onUnbind()：Called when all clients have disconnected from a particular interface published by the service.");
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("MyService onStartCommand()：Called by the system every time a client explicitly starts the service by calling android.content.Context.startService, providing the arguments it supplied and a unique integer token representing the start request.");
        return super.onStartCommand(intent, flags, startId);
    }

    public void stopServer() {
        if (myGetServer != null)
            myGetServer.stop();
        if (myPostServer != null)
            myPostServer.stop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopServer();
    }
}
