package com.example.kerorodoodesk.mtapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import mtserver.MTGETServer;
import mtserver.MTPOSTServer;

public class VideoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WifiManager wifiMgr = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        HTTPServer.getInstance().setIP(ip);

//        Intent intent = new Intent(this, HTTPService.class);
//        this.startService(intent);

        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/mtapp");

        //String ipAddr = startServer();
        setContentView(R.layout.activity_video);
        TextView info = (TextView) findViewById(R.id.maintext);
        //info.setText("Hello " + ipAddr);
        Button button01 = (Button) findViewById(R.id.look_setbox_video_button);
        button01.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(VideoActivity.this, ListVideoActivity.class);
                startActivity(intent);
                VideoActivity.this.finish();
                /*
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.exoplayer.demo");
                intent.setData(Uri.parse(getPath("dash.mpd")));
                startActivity(intent);
                */
            }
        });
        Button button02 = (Button) findViewById(R.id.pre_recorded_button);
        button02.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthTest a = AuthTest.getInstance();
                try{
                    a.send_msg("PreRecorded","pre_recorded.txt");
                    //a.ask_list();
                }catch (Exception e){
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                intent.setClass(VideoActivity.this, PreRecordedVideoActivity.class);
                startActivity(intent);
                VideoActivity.this.finish();

            }
        });
        Button button03 = (Button) findViewById(R.id.recorded_button);
        button03.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthTest a = AuthTest.getInstance();
                try {
                    a.send_msg("Recorded","recorded.txt");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent();
                intent.setClass(VideoActivity.this, Recorded.class);
                startActivity(intent);
                VideoActivity.this.finish();
            }
        });
        Button button04 = (Button) findViewById(R.id.sb_status_button);
        button04.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthTest a = AuthTest.getInstance();
                try{
                    a.send_msg("Status","sb_status.txt");
                }catch (Exception e){
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                intent.setClass(VideoActivity.this, SBStatus.class);
                startActivity(intent);
                VideoActivity.this.finish();
            }
        });
        Button button05 = (Button) findViewById(R.id.remote_button);
        button05.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(VideoActivity.this, RemoteActivity.class);
                startActivity(intent);
                VideoActivity.this.finish();
            }
        });
        Button button06 = (Button) findViewById(R.id.upload_button);
        button06.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(VideoActivity.this, UploadActivity.class);
                startActivity(intent);
                VideoActivity.this.finish();
            }
        });
    }
/*
    private static String getPath(String fileName) {
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/mtapp");
        return dir + "/" + fileName;
    }
*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
