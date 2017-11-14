package com.example.kerorodoodesk.mtapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.exoplayer.demo.PlayerActivity;

import java.io.File;

public class ListVideoActivity extends AppCompatActivity {
    int ip;
    private static String vPlaySegment(String fileName) {
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/mtapp");
        return dir + "/" + fileName;
    }

    public static String videoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Intent intent = new Intent(this, HTTPService.class);
//        startService(intent);

        //startService(new Intent(ListVideoActivity.this,HTTPService.class));
        setContentView(R.layout.activity_list_video);
        String[] vData = {"news", "model", "syfy"};
        ListView lv = new ListView(this);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ArrayAdapter<String> vArrayData = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, vData);
        lv.setAdapter(vArrayData);
        setContentView(lv);
        lv.setClickable(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                ListView listView = (ListView) arg0;
                AuthTest a = AuthTest.getInstance();
                try{
                    videoName = listView.getItemAtPosition(position).toString();
                    //startService(new Intent(ListVideoActivity.this,HTTPService.class));
                    HTTPServer.getInstance().openServer();


                    a.send_msg("Stream",listView.getItemAtPosition(position).toString()+".ts");
                }catch (Exception e){
                    e.printStackTrace();
                }
                Intent intent = new Intent(ListVideoActivity.this, PlayerActivity.class);
                intent.setData(Uri.parse(vPlaySegment(listView.getItemAtPosition(position).toString()+".mpd")));
                startActivity(intent);
                //listView.getItemAtPosition(position).toString()
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent();
            intent.setClass(ListVideoActivity.this, VideoActivity.class);
            startActivity(intent);
            ListVideoActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_video, menu);
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
