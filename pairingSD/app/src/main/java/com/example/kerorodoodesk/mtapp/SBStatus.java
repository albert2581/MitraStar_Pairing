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


public class SBStatus extends ActionBarActivity implements SocketConnect.DataCallback {
    TextView textView;
    AuthTest a = AuthTest.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sbstatus);
        this.textView = (TextView) this.findViewById(R.id.SBStatusVideoView);
        //this.setTextView("{\"SBStatus\":[{\"error_code\": \"E102\",\"excause\": \"授權失敗, 請聯絡客服中心\"},{\"error_code\": \"E302\",\"excause\": \"錄影裝置認證不符,請使用本公司授權的錄影裝置\"}]}");
        SocketConnect.getInstance().setDataCallback(this);
    }

    public void setTextView(String jsonString) {
        String data = "";
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            String msg = jsonObject.optString("msg");
            JSONArray recordArray = (new JSONObject(msg)).optJSONArray("SBStatus");
            int lengthJsonString = recordArray.length();

            for (int i = 0; i < lengthJsonString; i++) {
                JSONObject record = recordArray.getJSONObject(i);
                String error_code = record.getString("error_code");
                String excause = record.getString("excause");

                data += "\n錯誤代碼:" + error_code + "\n錯誤原因:" + excause + "\n";
            }
            textView.setText(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sbstatus, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent();
            intent.setClass(SBStatus.this, VideoActivity.class);
            startActivity(intent);
            SBStatus.this.finish();
        }
        return super.onKeyDown(keyCode, event);
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
