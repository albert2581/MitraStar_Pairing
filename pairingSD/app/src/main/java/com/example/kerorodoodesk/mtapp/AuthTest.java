package com.example.kerorodoodesk.mtapp;

import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
/**
 * Created by 信宇 on 2015/10/31.
 */
import android.os.Environment;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by 信宇 on 2015/10/31.
 */
public class AuthTest {


    /* 如若要收檔 將會收到ask=2 */
    int ask=0;
    /* 收入sb之Ip */
    String Ip="";
    /* 編碼後之資訊 */
    String encStr;
    /* 確認驗證碼是否輸入正確 */
    int result=-1;
    /* 記錄未配對過機上盒之名稱 */
    String sbname_Unpaired="";
    /* 記錄已配對過機上盒之名稱 */
    String sbname_Paired;
    /* 收入檔案內容 */
    String r_data;
    /* 收入之檔案形式 */
    int filetype;
    /* 收入之檔案名稱 */
    String filename;
    /* 收入之檔案編號 */
    int serialnumber;
    /* 收入之檔案總量 */
    int total;
    /* 機上盒送入之文件清單 */
    String sblist;
    /* 收入sb的公開IP */
    String publicip="";
    /* 收入sb的公開port */
    String publicport="54321";
    /* 從pairingInformation中讀出以配對過之Ip */
    String[] pairedIp=new String[3];
    String pairedIpS="";

    String pairedListS="";
    String[] pairedList=new String[3];


    String[] pairedNetIp=new String[3];
    String pairedNetIpS="";

    String[] pairedNetport=new String[3];
    String pairedNetportS="";


    String[] pairedKey=new String[3];
    String pairedKeyS="";
    String msg="";
    String natIp="";
    String key="0000",pKey="";
    int i=0,sKey=-1;

    /* port */
    int sendport=5556;	//send

    DatagramPacket sendPkg;
    DatagramSocket ds= null;

    String uuid ="";
    public String pairedList()                                                                      //回傳pairedList
    {
        Log.d("pairedList1",pairedList[0]);
        return pairedList[0];
    }
    public String pairedList2()                                                                      //回傳pairedList
    {
        Log.d("pairedList2",pairedList[1]);
        return pairedList[1];
    }
    public String pairedList3()                                                                      //回傳pairedList
    {
        Log.d("pairedList3",pairedList[2]);
        return pairedList[2];
    }
    public String sbname_U()                                                                        //回傳未配對過之sbname
    {
        return sbname_Unpaired;
    }
    public String msg()                                                                        //回傳未配對過之sbname
    {
        return msg;
    }
    public String Ip()                                                                              //回傳sb之Ip
    {
        //return "140.124.183.10";
        return Ip;
    }
    public int result()                                                                              //回傳驗證是否成功
    {
        return result;
    }

    public void SetUID(String uid)
    {
        this.uuid = uid;
    }

    public String getUID()
    {
        return this.uuid;
    }

    /**
     * test
     *
     */
    private static AuthTest authTest = null;

    private AuthTest(){

    }

    public static synchronized AuthTest getInstance(){
        if (authTest == null){
            authTest = new AuthTest();
        }
        return authTest;
    }


    public void rec_handler(String msgString){
        Log.e("Message", msgString);
//        read_paired();
        try{
            JSONObject jsonObject = new JSONObject(msgString);
            key = jsonObject.getString("Key");
            result = 2;
//            Log.d("key!!!",key.toString());
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            //e1.printStackTrace();
        }
        try{
            JSONObject jsonObject = new JSONObject(msgString);
            Ip = jsonObject.getString("ip");
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            //e1.printStackTrace();
        }
        try{
            JSONObject jsonObject = new JSONObject(msgString);
            filetype = jsonObject.getInt("filetype");
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            //e1.printStackTrace();
        }
        try{
            JSONObject jsonObject = new JSONObject(msgString);
            filename = jsonObject.getString("filename");
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            //e1.printStackTrace();
        }
        try{
            JSONObject jsonObject = new JSONObject(msgString);
            serialnumber = jsonObject.getInt("serialnumber");
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            //e1.printStackTrace();
        }
        try{
            JSONObject jsonObject = new JSONObject(msgString);
            total = jsonObject.getInt("total");
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            //e1.printStackTrace();
        }
        try{
            JSONObject jsonObject = new JSONObject(msgString);
            result = jsonObject.getInt("result");

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            //e1.printStackTrace();
        }
        try{
            JSONObject jsonObject = new JSONObject(msgString);
            sbname_Unpaired= jsonObject.getString("sbname");
        } catch (Exception e){
            //e.printStackTrace();
        }
        try{
            JSONObject jsonObject = new JSONObject(msgString);
            r_data= jsonObject.getString("data");
        } catch (Exception e){
            //e.printStackTrace();
        }
        try{
            JSONObject jsonObject = new JSONObject(msgString);
            ask= jsonObject.getInt("ask");
        } catch (Exception e){
            //e.printStackTrace();
        }
        try{
            JSONObject jsonObject = new JSONObject(msgString);
            sblist = jsonObject.getString("list");
        } catch (Exception e){
            //e.printStackTrace();
        }
        try{
            JSONObject jsonObject = new JSONObject(msgString);
            msg= jsonObject.getString("msg");
        } catch (Exception e){
            //e.printStackTrace();
        }
        try{
            JSONObject jsonObject = new JSONObject(msgString);
            publicip= jsonObject.getString("publicip");
        } catch (Exception e){
            //e.printStackTrace();
        }
        try{
            JSONObject jsonObject = new JSONObject(msgString);
//            Log.d("wwwwww","wwwww");
//            Log.d("pb",jsonObject.toString());
            publicport = jsonObject.getString("publicport");
        } catch (Exception e){
//            e.printStackTrace();
        }
        //---------------------------
        MainActivity pair=new MainActivity();
//        Log.d("result",String.valueOf(result));
        result=pair.setCheckPair();
    }
    //------------------------獲得自己的IP---------------------------------------------
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        if(inetAddress instanceof Inet4Address)
                        {
                            return ((Inet4Address)inetAddress).getHostAddress().toString();
                        }
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return "";
    }
    //----------------------pairingInformation----------------------------------------

    /*---------將已配對之必要訊息儲存至文字檔中--------*/
	/*-----------有公開Ip區網內Ip還有sbname-----------*/
    public void pairingInformation(){
        try
        {
            File mSDFile = null;

            //檢查有沒有SD卡裝置
            if(Environment.getExternalStorageState().equals( Environment.MEDIA_REMOVED))
            {
                return ;
            }
            else
            {
                //取得SD卡儲存路徑
                mSDFile = Environment.getExternalStorageDirectory();
            }

            //建立文件檔儲存路徑
            File mFile = new File(mSDFile.getParent() + "/" + mSDFile.getName() + "/MyAndroid");

            //若沒有檔案儲存路徑時則建立此檔案路徑
            if(!mFile.exists())
            {
                mFile.mkdirs();
            }

            if((pairedList[0].equals(sbname_Unpaired))||(pairedList[1].equals(sbname_Unpaired))||(pairedList[2].equals(sbname_Unpaired))){

            }
            else{
                //取得mEdit文字並儲存寫入至SD卡文件裡
                FileWriter mFileWriter = new FileWriter( mSDFile.getParent() + "/" +
                        mSDFile.getName() + "/MyAndroid/pairingInformation.txt",true );
                mFileWriter.write(sbname_Unpaired);
                mFileWriter.write(" ");
                mFileWriter.write(publicip);
                mFileWriter.write(" ");
                mFileWriter.write(Ip);
                mFileWriter.write(" ");
                mFileWriter.write(publicport);
                mFileWriter.write(" ");
                mFileWriter.write(key);
                mFileWriter.write("\n");
                mFileWriter.close();
            }
            result = -1;
            read_paired("/MyAndroid/pairingInformation.txt");

        }catch (Exception e)
        {
        }
    }
    //--------------------Filing-------------------------------
    public boolean filing(){

        try{
            File mSDFile = null;

            //檢查有沒有SD卡裝置
            if(Environment.getExternalStorageState().equals( Environment.MEDIA_REMOVED))
            {
                return false;
            }
            else
            {
                //取得SD卡儲存路徑
                mSDFile = Environment.getExternalStorageDirectory();
            }

            //建立文件檔儲存路徑
            File mFile = new File(mSDFile.getParent() + "/" + mSDFile.getName() + "/MyAndroid/pairingInformation.txt");

            //若沒有檔案儲存路徑時則建立此檔案路徑
            if(!mFile.exists())
            {
                return false;
            }
            else
                return true;
        }
        catch(Exception e)
        {
        }
        return false;

    }
//-------------read_paired------------------------------------------------

    /* 將以配對過的sbname顯示與List View2上 */
	/* 把存入pairingInformation的東西讀出     */
    public void read_paired(String a){
        try{
            //取得SD卡儲存路徑
            File mSDFile = Environment.getExternalStorageDirectory();
            //讀取文件檔路徑
            FileReader mFileReader = new FileReader(mSDFile.getParent() + "/" +
                    mSDFile.getName() + a);

            BufferedReader mBufferedReader = new BufferedReader(mFileReader);
            String mReadText = "";
            String mTextLine;
            for (int i = 0; i < 3; i++) {
                pairedListS = "";
                pairedList[i]="";
                pairedNetIp[i] = "";
                pairedNetIpS="";
                pairedIp[i] = "";
                pairedIpS="";
                pairedNetport[i] = "";
                pairedNetportS="";
                pairedKey[i] ="";
                pairedKeyS = "";
            }
            int j=0;
            do{
                mTextLine = mBufferedReader.readLine();
                String[] AfterSplit = mTextLine.split("[,\\s]+");
                pairedListS=AfterSplit[0];
                pairedNetIpS=AfterSplit[1];
                pairedIpS=AfterSplit[2];
                pairedNetportS=AfterSplit[3];
                pairedKeyS=AfterSplit[4];
                pairedNetIp[j]=pairedNetIpS;
                pairedIp[j]=pairedIpS;
                pairedList[j]=pairedListS;
                pairedNetport[j]=pairedNetportS;
                pairedKey[j]=pairedKeyS;
                j=j+1;Log.d("valueOf(j)", String.valueOf(j));
            }while (mTextLine!=null);

        }
        catch(Exception e)
        {
        }
    }
    /*判斷手機網路於區網內還區網外*/
    public String myipf(){
        String myIp=getLocalIpAddress();
        String[] AfterSplit = myIp.split("[.,\\s]+");
        String myIpf=AfterSplit[0]+AfterSplit[1]+AfterSplit[2];

        return myIpf;
    }

    public String selectIpf(String name){
        if (pairedList[0].equals(name)){
            String[] AfterSplit = pairedIp[0].split("[.,\\s]+");
            String selsectIpf=AfterSplit[0]+AfterSplit[1]+AfterSplit[2];
            return selsectIpf;
        }
        else if (pairedList[1].equals(name))
        {
            String[] AfterSplit = pairedIp[1].split("[.,\\s]+");
            String selsectIpf=AfterSplit[0]+AfterSplit[1]+AfterSplit[2];
            return selsectIpf;
        }
        else if (pairedList[2].equals(name))
        {
            String[] AfterSplit = pairedIp[2].split("[.,\\s]+");
            String selsectIpf=AfterSplit[0]+AfterSplit[1]+AfterSplit[2];
            return selsectIpf;
        }
        return "selsectIpf";
    }

    private String nowKey;
    public String selectKey(String name){
        if (pairedList[0].equals(name)){
            nowKey = pairedKey[0];
            return pairedKey[0];
        }
        else if (pairedList[1].equals(name))
        {
            nowKey = pairedKey[1];
            return pairedKey[1];
        }
        else if (pairedList[2].equals(name))
        {
            nowKey = pairedKey[2];
            return pairedKey[2];
        }
        nowKey = "";
        return "selectKey";
    }
    /*回傳手機區網內Ip*/
    public String selectIp(String name){
        if (pairedList[0].equals(name)){
            return pairedIp[0];
        }
        else if (pairedList[1].equals(name))
        {
            return pairedIp[1];
        }
        else if (pairedList[2].equals(name))
        {
            return pairedIp[2];
        }
        return "selsectIpf";
    }
    /*回傳手機區網外Ip*/
    public String selectNetIp(String name){
        if (pairedList[0].equals(name)){
            return pairedNetIp[0];
        }
        else if (pairedList[1].equals(name))
        {
            return pairedNetIp[1];
        }
        else if (pairedList[2].equals(name))
        {
            return pairedNetIp[2];
        }
        return "pairedNetIp";
    }
    /*回傳手機區網外port*/
    public int selectNetport(String name){
        if (pairedList[0].equals(name)){
            Log.d("qqqqqqqq","qqqqqqq");
            Log.d("PublicPort", pairedNetport[0].toString());
            int i=Integer.parseInt(pairedNetport[0]);
            return i;
        }
        else if (pairedList[1].equals(name))
        {
            int i=Integer.parseInt(pairedNetport[1]);
            return i;
        }
        else if (pairedList[2].equals(name))
        {
            int i=Integer.parseInt(pairedNetport[2]);
            return i;
        }
        return 0;
    }
    //-------------send_msg----------------------------------------
    public String send_msg(final String a,final String b) throws  Exception{
        final Thread background = new Thread(new Runnable() {
            public void run(){
                InetAddress inet;
                try {
                    ds = new DatagramSocket();
                    inet = InetAddress.getByName(Ip());
                    JSONObject jsonRoot = new JSONObject();
                    try	{
                        jsonRoot.put("ip",natIp);
                        jsonRoot.put("msg", a+"/"+ natIp +"/"+b +"/" + uuid);//得出e2的內容後送出
                        sendPkg = new DatagramPacket(jsonRoot.toString().getBytes(),
                                jsonRoot.toString().getBytes().length,inet,sendport);
                        ds.send(sendPkg);
                        ds.close();
                        Log.d("TEST", "12345");
                    }catch (JSONException e)
                    {
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }catch (UnknownHostException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            }
        });
        background.start();
        return a+"/"+Ip+"/"+b;
    }
    //-------------send_msg----------------------------------------
    public String send_security_uuid(final String a) throws  Exception{
        Thread background = new Thread(new Runnable() {
            public void run(){
                InetAddress inet;
                try {
                    ds = new DatagramSocket();
                    inet = InetAddress.getByName(Ip());
                    JSONObject jsonRoot = new JSONObject();
                    try	{

                        jsonRoot.put("uuid", a);//得出e2的內容後送出
                        Log.d("uuid", a.toString());
                        sendPkg = new DatagramPacket(jsonRoot.toString().getBytes(),
                                jsonRoot.toString().getBytes().length,inet,sendport);
                        ds.send(sendPkg);
                        ds.close();
                    }catch (JSONException e)
                    {
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }catch (UnknownHostException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            }
        });
        background.start();
        return a;
    }

    //-------------與sb要求連線----------------------------------------
    public void ask_connect(){
        Thread background = new Thread(new Runnable() {
            public void run() {
                try {
                    ds = new DatagramSocket();
                    InetAddress inet = InetAddress.getByName(Ip());
                    String myIp = getLocalIpAddress();
                    JSONObject jsonRoot = new JSONObject();
                    try {
                        jsonRoot.put("ip", myIp);
                        jsonRoot.put("ask", 0);                                                //與SB要求連線
                        sendPkg = new DatagramPacket(jsonRoot.toString().getBytes(),
                                jsonRoot.toString().getBytes().length, inet, sendport);
                        ds.send(sendPkg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Throwable t) {
                    // just end the background thread
                }
            }
        });
        background.start();
    }
    //-------------與sb要求配對----------------------------------------
    public void ask_pairSTB(final String a) {
        Thread background = new Thread(new Runnable() {
            public void run() {
                try {
                    InetAddress inet = InetAddress.getByName(Ip());
                    String myIp = getLocalIpAddress();
                    JSONObject jsonRoot = new JSONObject();
                    try {
                        jsonRoot.put("pairingkey", a);
                        jsonRoot.put("ip", myIp);
                        jsonRoot.put("ask", 1);                                                            //與SB要求配對
                        sendPkg = new DatagramPacket(jsonRoot.toString().getBytes(),
                                jsonRoot.toString().getBytes().length, inet, sendport);
                        ds.send(sendPkg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Throwable t) {
                    // just end the background thread
                }
            }
        });
        background.start();
    }
    public String getkey() {

        return nowKey;
    }
    //-------------未配對過的機上盒選單----------------------------------------
    public String list2_check(final String a) {
        String myIp = getLocalIpAddress();
        if (selectIpf(a).equals(myipf())){
            Ip=selectIp(a);
            natIp=myIp;
            sendport=5556;
            Log.d("IP",Ip.toString());
            Log.d("Port",String.valueOf(sendport));
        }
        else{
            natIp="140.124.183.10";
            Ip=selectNetIp(a);
            sendport = selectNetport(a);
            Log.d("IP",Ip.toString());
            Log.d("Port",String.valueOf(sendport));
        }
        return Ip;
    }
    public void del(int n){


        read_paired("/MyAndroid/pairingInformation.txt");

        overWrite(n, "/MyAndroid/pairingInformation2.txt");

        read_paired("/MyAndroid/pairingInformation2.txt");
        overWrite(4, "/MyAndroid/pairingInformation.txt");
        //刪除暫存目錄中 test.txt 檔案
        File f = new File (Environment.getExternalStorageDirectory().getParent() + "/" +  Environment.getExternalStorageDirectory().getName() + "/MyAndroid","pairingInformation2.txt");
        f.delete();


    }
    public void overWrite(int n,String a){
        try
        {
            File mSDFile = null;

            //檢查有沒有SD卡裝置
            if(Environment.getExternalStorageState().equals( Environment.MEDIA_REMOVED))
            {
                return ;
            }
            else
            {
                //取得SD卡儲存路徑
                mSDFile = Environment.getExternalStorageDirectory();
            }

            //建立文件檔儲存路徑
            File mFile = new File(mSDFile.getParent() + "/" + mSDFile.getName() + "/MyAndroid");

            //若沒有檔案儲存路徑時則建立此檔案路徑
            if(!mFile.exists())
            {
                mFile.mkdirs();
            }
            //取得mEdit文字並儲存寫入至SD卡文件裡
            FileWriter mFileWriter = new FileWriter( mSDFile.getParent() + "/" +
                    mSDFile.getName() + a );
            int i=0;
            for(i=0;i<3;i++)
            {
                if(i!=n)//如果要修改?容就在?修改line的?容,再存到文件中就行了
                {
                    mFileWriter.write(pairedList[i]);
                    mFileWriter.write(" ");
                    mFileWriter.write(pairedNetIp[i]);
                    mFileWriter.write(" ");
                    mFileWriter.write(pairedIp[i]);
                    mFileWriter.write(" ");
                    mFileWriter.write(pairedNetport[i]);
                    mFileWriter.write(" ");
                    mFileWriter.write(pairedKey[i]);
                    mFileWriter.write("\n");
                }
            }

            mFileWriter.close();

        }catch (Exception e)
        {
        }
    }
}

