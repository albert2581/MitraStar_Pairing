package com.example.kerorodoodesk.mtapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


public class MainActivity extends Activity implements SocketConnect.DataCallback {

    /* button */
    private Button searchSTB;
    private Button b2;
    private Button b3;
    private Button b4;

    /* listView */
    private ListView L1;
    private ListView L2;
    /* editText */
    private EditText e1;
    private EditText e2;
    /* TextView */
    private TextView t1;
    private TextView t2;
    private TextView t3;
    /* 收入sb之Ip */
    String Ip;
    /* 記錄未配對過機上盒之名稱 */
    String sbname_Unpaired="";
    /* 記錄已配對過機上盒之名稱 */
    String sbname_Paired;
    /* 收進的最大資料量 */
    byte[] recevieData = new byte[60000];
    /* 宣告socket */
    DatagramPacket dp;
    DatagramSocket ds=null;
    /* port */
    int recvport=5555;	//rec
    /* 為紀錄未配對之機上盒(預設最多一次接收三台) */
    String check_list[]=new String [3];
    /* 為紀錄已配對之機上盒(預設最多一次接收三台) */
    String check_list2[]=new String [3];
    /* 使L1開始顯示接收訊息 */
    int startL1=0;
    int check=0,checkPair=0;
    AuthTest a = AuthTest.getInstance();

    //----------------------------------------------------

    /* list view之宣告 */
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items;
    private ArrayAdapter<String> adapter2;
    private ArrayList<String> items2;
    //---------------------------------------------------------------------

    private String MACADDRESS;
    protected void setButton()
    {
        searchSTB=(Button)findViewById(R.id.button);
        b2=(Button)findViewById(R.id.button2);
        b3=(Button)findViewById(R.id.button3);
        b4=(Button)findViewById(R.id.button4);
    }//setButton
    //---------------------------------------------------------------------
    protected void setEditText()
    {
        e1=(EditText)findViewById(R.id.editText);
        e2=(EditText)findViewById(R.id.editText2);
    }//setEditText
    //---------------------------------------------------------------------
    protected void setTextView()
    {
        t1=(TextView)findViewById(R.id.textView);
        t2=(TextView)findViewById(R.id.textView2);
        t3=(TextView)findViewById(R.id.textView3);
    }//setListView
    //---------------------------------------------------------------------
    protected void setListView()
    {
        L1=(ListView)findViewById(R.id.listView);
        L2=(ListView)findViewById(R.id.listView2);
    }//setListView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setButton();
        setListView();
        setEditText();
        setTextView();
        connectSTB();													//選擇與哪個STB要求連線
        list2();
        addListener();
        SocketConnect.getInstance().setDataCallback(this);
        a.SetUID(GetMacAddress());
    }
    //---------------------------------------------------------------------
    protected void addListener(){
        searchSTB.setOnClickListener(new Button.OnClickListener() {                    //尋找目前區網內正在廣播之STB
            public void onClick(View v) {                                            //並顯示於ListView1上

                Log.d("sb1","start srech");
                //並顯示於ListView1上
                for (int i = 0; i < 3; i++) {
                    check_list[i] = "";                                                //預設check_list為空
                }
                t3.setText(a.msg());
                startL1 = 1;                                                            //直到按下此按鈕 ListView1才開始顯示
                pRecvBcast();
            }
        });
        b2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                pairSTB();
            }
        });
        b3.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                send_msg();                                                            //傳出訊息
//                Log.d("THIS",e2.getText().toString());
            }
        });
        b4.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if((a.filing()==true)) {

                    Log.e("LIST2 START", "list2");
                    set_check_list2();
                }
            }
        });
    }
    //---------------list view------------------------------------------
    public void connectSTB(){
        items = new ArrayList<String>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);

        L1.setAdapter(adapter);

		/* 未配對過的機上盒選單 */
        L1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "你選擇的是" + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
                a.ask_connect();
            }
        });
    }


    /*------增加未配對過的電腦名稱於listView1上--------*/
    public void menuAddItem(){
        items.add(sbname_Unpaired.toString());
        L1.setAdapter(adapter);
    }
    //---------------list view 2------------------------------------------
    public void list2(){
        items2 = new ArrayList<String>();
        adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items2);
        L2.setAdapter(adapter2);

			/* 配對過的機上盒選單 */
        L2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "你選擇的是" + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();

                t2.setText(a.list2_check(parent.getItemAtPosition(position).toString()));
                a.selectKey(parent.getItemAtPosition(position).toString());

                //開啟Video Activity
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, VideoActivity.class);
                startActivity(intent);
                MainActivity.this.finish();

            }
        });
        L2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("確定要刪除")
                        .setMessage("刪除的是" + parent.getItemAtPosition(position).toString())
                        .setNegativeButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                a.del(position);
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
                return true;
            }
        });

    }
    /*------增加未配對過的電腦名稱於listView1上--------*/
    public void menuAddItem2(){
        items2.add(sbname_Paired.toString());
        Log.d("sbname",sbname_Paired.toString());
        L2.setAdapter(adapter2);
    }


    public void pRecvBcast(){
        sbname_Unpaired=a.sbname_U();
        if((check_list[0]=="")&&(sbname_Unpaired!="")&&(startL1==1))
        {
            check_list[0]=sbname_Unpaired;
            check_list[1]=sbname_Unpaired;
            check_list[2]=sbname_Unpaired;
            menuAddItem();
        }
        else if((check_list[0]!=check_list[1])&&(sbname_Unpaired!="")&&(startL1==1))
        {
            check_list[1]=sbname_Unpaired;
            check_list[2]=sbname_Unpaired;
            menuAddItem();
        }
        else if((check_list[0]!=check_list[1])&& (check_list[1]!=check_list[2]&&
                (check_list[0]!=check_list[2]))&&(sbname_Unpaired!="")&&(startL1==1))
        {
            check_list[2]=sbname_Unpaired;
            menuAddItem();
        }
    }

//----------------------pairSTB----------------------------------------

    /*---------輸入顯示於STB上之驗證碼--------*/
	/*---------並送出  給STB做驗證-------------*/
    public void pairSTB(){
        a.ask_pairSTB(e1.getText().toString());
    }

    //---------------------顯示已配對列表----------------------------------------

    /*---------判斷配對過的電腦名稱是否重複--------*/
  	/*---------如果未重複  即顯示於ListView2上--------*/
    public void pairedList(String afterSplit){
        sbname_Paired=afterSplit;

        if((check_list2[0]=="")&&(sbname_Paired!=""))
        {
            check_list2[0]=sbname_Paired;
            check_list2[1]=sbname_Paired;
            check_list2[2]=sbname_Paired;
            menuAddItem2();
        }
        else if((check_list2[0]==check_list2[1])&&(sbname_Paired!=""))
        {
            check_list2[1]=sbname_Paired;
            check_list2[2]=sbname_Paired;
            menuAddItem2();
        }
        else if((check_list2[0]!=check_list2[1])&& (check_list2[1]==check_list2[2])&&(sbname_Paired!=""))
        {
            check_list2[2]=sbname_Paired;
            menuAddItem2();
        }
    }

    //-------------send_msg----------------------------------------
    public void send_msg(){
        try{
            a.send_msg(e2.getText().toString(),"");
        }catch (Exception e){
            e.printStackTrace();
        }
        // a.send_msg(e2.getText().toString(),"");
    }
    //-------------send_uuid----------------------------------------
    public void send_uuid(){
        try{
            Log.d("getUid", "get");
            a.send_security_uuid(a.getUID());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void set_check_list2(){
        for (int i = 0; i < 3; i++) {
            check_list2[i] = "";                                                //預設check_list為空
        }

        a.read_paired("/MyAndroid/pairingInformation.txt");
        pairedList(a.pairedList());
        Log.d("sbname1", a.pairedList());
        pairedList(a.pairedList2());
        Log.d("sbname2", a.pairedList2());
        pairedList(a.pairedList3());
        Log.d("sbname3", a.pairedList3());
    }

    public  int setCheckPair(){
        if (a.result()==1)
        {
            Log.d("checkPair", "success");
//            t2.setText("success");
            send_uuid();

//            Toast.makeText(getApplicationContext(),
//                    "success",
//                    Toast.LENGTH_SHORT).show();
        }
        else if (a.result()==0){
            Log.d("checkPair", "fault");
//            t2.setText("fault");
//            Toast.makeText(getApplicationContext(),
//                    "fault",
//                    Toast.LENGTH_SHORT).show();
        }
        else if(a.result()==2)
        {
            a.pairingInformation();
        }
        return -1;
    }










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

    @Override
    public void onDataReceive(String data) {
        a.rec_handler(data);
    }

    // get mac address of device
    public String GetMacAddress() {
        WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifi = wm.getConnectionInfo();

        return wifi.getMacAddress();
    }
}
