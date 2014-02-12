package com.fuhao.esb.client.socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
/**
 * package name is  com.fuhao.esb.client.socket
 * Created by fuhao on 14-1-3.
 * Project Name esb-java
 */


public class SocketUtil {

    private static final String SPACE = " ";
    private static final String CONTENT_LENGTH = "Content-Length:";

    public static StringBuffer getData(Socket socket) throws IOException {
        InputStream is = socket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String head = br.readLine().trim();
        String contentlengh = CONTENT_LENGTH;
        int index1 = head.indexOf(contentlengh);
        int index2 = head.indexOf(SPACE,index1+contentlengh.length());
        if(index2<0){
            index2 = head.length();
        }

        String lengthString = head.substring(index1+contentlengh.length(), index2);
        int length = Integer.parseInt(lengthString);
        int total = 0;
        StringBuffer sb = new StringBuffer();
        char[] cbuf = new char[10];
        while(total<length){
            int len = br.read(cbuf);
            if (len>=0) {
                char[] dest = new char[len];
                System.arraycopy(cbuf, 0, dest, 0, len);
                total = total + new String(dest).getBytes().length;
                sb.append(new String(dest));
            }else{
                break;
            }
        }
        System.out.println(sb.toString());
        return sb;
    }
    public static void WirteResponse(Socket socket,String sb)
            throws IOException {
        OutputStream  out = socket.getOutputStream();
        String res = CONTENT_LENGTH+sb.getBytes().length+"\r\n";
        System.out.println(res+sb);
        out.write((res+sb).getBytes());
        out.flush();
    }
}
