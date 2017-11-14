package com.example.kerorodoodesk.mtapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;
import java.io.IOException;

import mtserver.MTGETServer;
import mtserver.MTPOSTServer;


public class UploadActivity extends ActionBarActivity {
    TextView filename;
    MTGETServer myGetServer;
    MTPOSTServer myPostServer;
    String choose_filename;

    private static final int REQUEST_CHOOSER = 1234;
//    public String startServer() {
//        WifiManager wifiMgr = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
//        int ip = wifiInfo.getIpAddress();
//        String ipAddress = Formatter.formatIpAddress(ip);
//        File sdCard = Environment.getExternalStorageDirectory();
//        File dir = new File(sdCard.getAbsolutePath() + "/mtapp");
//        dir.mkdirs();
//        try {
//            myGetServer = new MTGETServer(this, dir, ipAddress);
//        } catch (IOException ioe) {
//            Toast.makeText(UploadActivity.this, "GetServer fail:" + ioe.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//        try {
//            myPostServer = new MTPOSTServer(this, dir, ipAddress);
//        } catch (IOException ioe) {
//
//            Toast.makeText(UploadActivity.this, "PostServer fail:" + ioe.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//
//        //  mUploadServer = new MTUploadServer();
//        return ipAddress;
//    }
//
//    public void stopServer() {
//        if (myGetServer != null)
//            myGetServer.stop();
//        if (myPostServer != null)
//            myPostServer.stop();
//
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HTTPServer.getInstance().openServer();
//        Intent intent = new Intent(this, HTTPService.class);
//        this.startService(intent);

        setContentView(R.layout.activity_upload);
        filename = (TextView) findViewById(R.id.filename);
        Button choose_file_button = (Button) findViewById(R.id.choose_file_button);
        Button upload_button = (Button) findViewById(R.id.upload_button);

        choose_file_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the ACTION_GET_CONTENT Intent
                Intent getContentIntent = FileUtils.createGetContentIntent();
                Intent intent = Intent.createChooser(getContentIntent, "Select a file");
                startActivityForResult(intent, REQUEST_CHOOSER);
            }
        });

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthTest a = AuthTest.getInstance();
                try{
                    a.send_msg("Push",choose_filename);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Context context1 = getApplicationContext();
                CharSequence upload_success = "上傳成功";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context1, upload_success,duration );
                toast.setGravity(Gravity.TOP| Gravity.LEFT, 30,30);
                toast.show();
//                Intent intent = new Intent();
//                intent.setClass(UploadActivity.this,VideoActivity.class);
//                startActivity(intent);
//                UploadActivity.this.finish();
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//            stopService(new Intent(this,HTTPService.class));
            HTTPServer.getInstance().stopServer();
            Intent intent = new Intent();
            intent.setClass(UploadActivity.this, VideoActivity.class);
            startActivity(intent);
            UploadActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        File sdCard = Environment.getExternalStorageDirectory();
        switch (requestCode) {
            case REQUEST_CHOOSER:
                if (resultCode == RESULT_OK) {
                    final Uri uri = data.getData();

                    // Get the File path from the Uri
                    String path = FileUtils.getPath(this, uri);

                    // Alternatively, use FileUtils.getFile(Context, Uri)
                    if (path != null && FileUtils.isLocal(path)) {
                        File file = new File(path);
                        filename.setText(file.getName());
                        File dir = new File(sdCard.getAbsolutePath() + "/mtapp/" + file.getName());
                        try {
                            Utility.copyFile(file, dir);
                            choose_filename = file.getName().toString() ;
                            /*
                            AuthTest sentToSBString = new AuthTest();
                            sentToSBString.send_msg("Push"+"/"+file.getName().toString());
*/
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upload, menu);
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
