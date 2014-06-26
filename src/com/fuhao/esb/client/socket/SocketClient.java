package com.fuhao.esb.client.socket;
import com.fuhao.esb.client.IBaseESBClientMessageSender;
import com.fuhao.esb.client.IXmlMessageReceiver;
import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.common.response.IESBReturnMessage;

import java.net.InetSocketAddress;
import java.net.Socket;


/**
 * package name is  com.fuhao.esb.client.socket
 * Created by fuhao on 14-1-3.
 * Project Name esb-java
 */
public class SocketClient implements IBaseESBClientMessageSender {
    private String toIP;
    private int port;

    public SocketClient( String address) throws Exception {
        checkParseURI(address);
    }

    private void checkParseURI(String url) throws Exception {
        if(url.length()<=0){
            throw new Exception("套接字url不合法:"+url);
        }
        char c = url.charAt(0);
        if(c<48||c>57){
            throw new Exception("套接字url不合法"+url);
        }
        int index = url.indexOf(":");
        if(index<=0){
            throw new Exception("套接字url不合法"+url);
        }
        toIP = url.substring(0,index);
        String portString = url.substring(index+1);
        try {
            port = Integer.parseInt(portString);
        } catch (Exception e) {
            throw new Exception("套接字url不合法"+url);
        }
    }

    public static void main(String[] args) throws Exception {
        new SocketClient("127.0.0.1:9081").sendXML("ddddddddddddddd");
    }

    @Override
    public IESBReturnMessage send(IESBAccessMessage message) throws Exception {
        return null;
    }

    @Override
    public String sendXML(String xml) throws Exception {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(toIP,port),10000);
        socket.setSoTimeout(180000);

        SocketUtil.WirteResponse(socket, xml);
        String result = SocketUtil.getData(socket).toString();
        return result;
    }

    @Override
    public IXmlMessageReceiver getReceiver() throws Exception {
        return null;
    }

	@Override
	public Object sendObject(String tranID, Object message) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}

