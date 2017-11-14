package com.example.kerorodoodesk.mtapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Recorded extends ActionBarActivity implements SocketConnect.DataCallback{

    TextView textView;
    AuthTest a = AuthTest.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorded);
        this.textView = (TextView) this.findViewById(R.id.RecordedVideoView);
        //this.setTextView("{\"RecordedVideo\":[{\"video_name\": \"天才衝衝衝\",\"star_time\": \"2015-11-22 22:52:00\",\"end_time\": \"2015-11-22 23:52:00\"},{\"video_name\": \"小小兵\",\"star_time\": \"2015-12-02 11:00:10\",\"end_time\": \"2015-12-02 12:00:10\"}]}");
        SocketConnect.getInstance().setDataCallback(this);
    }

    public void setTextView(String jsonString) {
        String data = "";
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            String msg = jsonObject.optString("msg");
            JSONArray recordArray = (new JSONObject(msg)).optJSONArray("RecordedVideo");
            int lengthJsonString = recordArray.length();

            for (int i = 0; i < lengthJsonString; i++) {
                JSONObject record = recordArray.getJSONObject(i);
                String video_name = record.getString("video_name");
                String start_time = record.getString("star_time");
                String end_time = record.getString("end_time");

                data += "\n節目名稱:" + video_name + "\n開始錄影時間:" + start_time + "\n結束錄影時間" + end_time + "\n";
            }
            textView.setText(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent();
            intent.setClass(Recorded.this, VideoActivity.class);
            startActivity(intent);
            Recorded.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recorded, menu);
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

    @Override
    public void onDataReceive(String data) {
        this.setTextView(data);
    }
}
