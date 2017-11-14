package mtserver;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nanohttpd.NanoHTTPD;
import webserver.SimpleWebServer;

/**
 * Created by kerorodoodesk on 2015/8/28.
 */
public class MTGETServer extends SimpleWebServer {
    File dir = new File(".");
    Context context;
    String ipAddr = "127.0.0.1";
    public MTGETServer(Context context, File dir, String ipAddr) throws IOException {
        //super(8080);
        super(ipAddr,8080,dir,false);//SimpleWebServer
        this.ipAddr = ipAddr;
        this.dir = dir;
        this.context = context;
        start();
        System.out.println( "\nRunning! Point your browers to http://localhost:8080/ \n" );
    }

    public MTGETServer(File dir, String ipAddr) throws IOException {
        //super(8080);
        super(ipAddr,8080,dir,false);//SimpleWebServer
        this.ipAddr = ipAddr;
        this.dir = dir;
        start();
        System.out.println( "\nRunning! Point your browers to http://localhost:8080/ \n" );
    }

    public MTGETServer() throws IOException {
        //super(8080);
        super("127.0.0.1",8080,new File("."),false);//SimpleWebServer
        this.dir = new File(".");
        start();
        System.out.println( "\nRunning! Point your browers to http://localhost:8080/ \n" );
    }

    public static void main(String[] args) {
        try {
            new MTGETServer();
        }
        catch( IOException ioe ) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }
/*
    private NanoHTTPD.Response POST(IHTTPSession session) {
        try {
            Map<String, String> files = new HashMap<String, String>();
            session.parseBody(files);
            Set<String> keys = files.keySet();
            for(String key: keys){
                String name = key;
                String loaction = files.get(key);

                File tempfile = new File(loaction);
                tempfile.renameTo(new File(dir.getPath(), name));

            }
        } catch (IOException | ResponseException e) {
            e.printStackTrace();
            return newFixedLengthResponse("uploadfail");
        }
        return newFixedLengthResponse("upload success");
    }
*/
    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile (String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    @Override
    public NanoHTTPD.Response serve(IHTTPSession session) {
        return super.serve(session);
        /*String content = "";
            try {
                content =  "<form action='?' method='post' enctype='multipart/form-data'>\n" +
                        "    <input type='file' name='file' />`enter code here`\n" +
                        "    <input type='submit'name='submit' value='Upload'/>\n" +
                        "</form>";
            } catch (Exception e) {
                content = e.toString();
            }*/
      //  return newFixedLengthResponse(content);
    }
}
