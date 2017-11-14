package mtserver;

import android.content.Context;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.text.format.Formatter;

import com.example.kerorodoodesk.mtapp.AuthTest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import nanohttpd.NanoHTTPD;
import security.Security;
import webserver.ServerRunner;
import webserver.SimpleWebServer;

/**
 * Created by kerorodoodesk on 2015/9/4.
 */
public class MTPOSTServer extends SimpleWebServer {
    File dir = new File(".");
    Context context;
    String ipAddr = "127.0.0.1";
    public MTPOSTServer(Context context, File dir, String ipAddr) throws IOException {
        //super(8080);
        super(ipAddr,8888,dir,false);//SimpleWebServer
        this.ipAddr = ipAddr;
        this.dir = dir;
        this.context = context;
        start();
        System.out.println( "\nRunning! Point your browers to http://localhost:8888/ \n" );
    }

    public MTPOSTServer(File dir, String ipAddr) throws IOException {
        //super(8080);
        super(ipAddr,8888,dir,false);//SimpleWebServer
        this.ipAddr = ipAddr;
        this.dir = dir;
        start();
        System.out.println( "\nRunning! Point your browers to http://localhost:8888/ \n" );
    }

    public MTPOSTServer() throws IOException {
        //super(8080);
        super("127.0.0.1",8888,new File("."),false);//SimpleWebServer
        this.dir = new File(".");
        start();
        System.out.println( "\nRunning! Point your browers to http://localhost:8888/ \n" );
    }
    @Override
    public NanoHTTPD.Response serve(IHTTPSession session) {
        Response pis = super.serve(session);

        Map<String, String> headers = session.getHeaders();
        Map<String, String> parms = session.getParms();
        Method method = session.getMethod();
        String uri = session.getUri();
        Map<String, String> files = new HashMap<>();

        if (Method.POST.equals(method) || Method.PUT.equals(method)) {
            try {
                session.parseBody(files);
            } catch (IOException ioe) {
                return new Response("Internal Error IO Exception: " + ioe.getMessage());
            } catch (ResponseException re) {
                return new Response(re.getStatus(),MIME_PLAINTEXT, re.getMessage());
            }
        }
        File mCurrentDir = dir;
        if (uri.indexOf("/uploadfile")==0) {
            String filename = parms.get("uploadfile");
            String tmpFilePath = files.get("uploadfile");
            if (null == filename || null == tmpFilePath) {
                // Response for invalid parameters
            }
            File dst = new File(mCurrentDir, filename);
            if (dst.exists()) {
                // Response for confirm to overwrite
            }
            File src = new File(tmpFilePath);
            try {
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dst);
                byte[] buf = new byte[65536];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                //Security To DecryptionFromFile
                //Security security = new Security("V4YohVD/UyLSqNo7vBcGSEnx9jDUC78PSzkBFt9DXTk=");
                Security security = new Security(AuthTest.getInstance().getkey());
                String dstPath = dst.getAbsolutePath();
                security.decryptionFromFile(dstPath, dstPath.substring(0,dstPath.length()-2));
//                boolean deleted = dst.delete();
            } catch (IOException ioe) {
                // Response for failed
                return new Response("Internal Error IO Exception: " + ioe.getMessage());
            }
            // Response for success
            return new Response("Upload success.."+"at <a href=\"http://" + ipAddr + ":8080/"+ filename + "\">Visit it</a>");
        }
         if(Method.GET.equals(method)){

                return new Response("<html>\n" +
                        " <body>\n" +
                        "  <p>This is a file upload test for NanoHTTPD.</p>\n" +
                        "   <form action=\"http://" + ipAddr + ":8888/uploadfile\" enctype=\"multipart/form-data\" method=\"post\">\n" +
                        "    <label for=\"textline\">Text:</label>\n" +
                        "       <input type=\"text\" id=\"textline\" name=\"textline\" size=\"30\"><br>\n" +
                        "    <label for=\"datafile1\">First File:</label>\n" +
                        "       <input type=\"file\" id=\"uploadfile\" name=\"uploadfile\" size=\"40\"><br>\n" +
                        "    <input type=\"submit\">\n" +
                        "   </form>\n" +
                        " </body>\n" +
                        "</html>");
        }
        return new Response(printSession(session));
    }


    public static void main(String[] args) {
        try {
            new MTPOSTServer();
        }
        catch( IOException ioe ) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    //for debug
    private String printSession(IHTTPSession session){
        Map<String, List<String>> decodedQueryParameters =
                decodeParameters(session.getQueryParameterString());

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head><title>Debug Server</title></head>");
        sb.append("<body>");
        sb.append("<h1>Debug Server</h1>");

        sb.append("<p><blockquote><b>URI</b> = ").append(
                String.valueOf(session.getUri())).append("<br />");

        sb.append("<b>Method</b> = ").append(
                String.valueOf(session.getMethod())).append("</blockquote></p>");

        sb.append("<h3>Headers</h3><p><blockquote>").
                append(toString(session.getHeaders())).append("</blockquote></p>");

        sb.append("<h3>Parms</h3><p><blockquote>").
                append(toString(session.getParms())).append("</blockquote></p>");

        sb.append("<h3>Parms (multi values?)</h3><p><blockquote>").
                append(toString(decodedQueryParameters)).append("</blockquote></p>");

        try {
            Map<String, String> files = new HashMap<String, String>();
            session.parseBody(files);
            sb.append("<h3>Files</h3><p><blockquote>").
                    append(toString(files)).append("</blockquote></p>");
        } catch (Exception e) {
            e.printStackTrace();
        }

        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }
    //for debug
    private String toString(Map<String, ? extends Object> map) {
        if (map.size() == 0) {
            return "";
        }
        return unsortedList(map);
    }
    //for debug
    private String unsortedList(Map<String, ? extends Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("<ul>");
        for (Map.Entry entry : map.entrySet()) {
            listItem(sb, entry);
        }
        sb.append("</ul>");
        return sb.toString();
    }
    //for debug
    private void listItem(StringBuilder sb, Map.Entry entry) {
        sb.append("<li><code><b>").append(entry.getKey()).
                append("</b> = ").append(entry.getValue()).append("</code></li>");
    }
}
