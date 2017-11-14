package com.example.kerorodoodesk.mtapp;

import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import mtserver.MTGETServer;
import mtserver.MTPOSTServer;

public class HTTPServer {
    MTGETServer myGetServer;
    MTPOSTServer myPostServer;
    int ip;

    private static HTTPServer single = new HTTPServer();
    private HTTPServer(){}
    public static HTTPServer getInstance(){
        return single;
    }

    public void setIP(int ip)
    {
        this.ip = ip;
    }

    public String openServer(int ip) {
//        WifiManager wifiMgr = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
//        int ip = wifiInfo.getIpAddress();
        this.ip = ip;
        String ipAddress = Formatter.formatIpAddress(this.ip);
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/mtapp");
        dir.mkdirs();
        try {
            myGetServer = new MTGETServer(dir, ipAddress);
        } catch (IOException ioe) {
            Log.d("HTTPServiceError", "myGetServerError");
        }
        try {
            myPostServer = new MTPOSTServer(dir, ipAddress);
        } catch (IOException ioe) {
            Log.d("HTTPServiceError","myPostServerError");
        }
        //  mUploadServer = new MTUploadServer();
        return ipAddress;
    }

    public void openServer() {
//        WifiManager wifiMgr = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
//        int ip = wifiInfo.getIpAddress();
          this.openServer(this.ip);
    }


    public void stopServer() {
        if (myGetServer != null)
            myGetServer.stop();
        if (myPostServer != null)
            myPostServer.stop();

    }
}
