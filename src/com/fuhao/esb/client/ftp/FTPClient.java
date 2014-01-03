package com.fuhao.esb.client.ftp;

import org.apache.log4j.Logger;

import java.io.InputStream;

/**
 * package name is  com.fuhao.esb.client.ftp
 * Created by fuhao on 14-1-3.
 * Project Name esb-java
 */
public class FTPClient {
    private static Logger logger =  Logger.getLogger(FTPClient.class);

    private String hostname; //ftp地址
    private int port;        //ftp端口
    private String username; //用户名
    private String password; //密码
    private String path;     //ftp存放文件地址
    public String getHostname() {
        return hostname;
    }
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public FTPClient(String hostname, int port, String username,
                     String password, String path) {
        super();
        this.setHostname(hostname);
        this.setPort(port);
        this.setUsername(username);
        this.setPassword(password);
        this.setPath(path);
    }

    public FTPClient() {
    }
    /**
     *
     *@name    中文名称
     *@Description 相关说明
     *@Time    创建时间:Apr 11, 20123:49:33 PM
     *@param ism 输入字符串
     *@param remoteFileName  远程文件名
     * @throws Exception
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public void send(InputStream ism,String remoteFileName) throws Exception{
        ContinueFTP myFtp = new ContinueFTP();
        myFtp.connect(this.getHostname(),this.getPort(),this.getUsername(), this.getPassword());
        myFtp.upload(ism,this.getPath()+"/"+remoteFileName);
        myFtp.disconnect();
    }

    public void send(String localResource,String remoteFileName) throws Exception{
        ContinueFTP myFtp = new ContinueFTP();
        myFtp.connect(this.getHostname(),this.getPort(),this.getUsername(), this.getPassword());
        ContinueFTP.UploadStatus status = myFtp.upload(localResource,this.getPath()+"/"+remoteFileName);
        myFtp.disconnect();
        if(ContinueFTP.UploadStatus.Upload_New_File_Success.equals(status) ||
                ContinueFTP.UploadStatus.Upload_From_Break_Success.equals(status) ||
                ContinueFTP.UploadStatus.File_Exits.equals(status)){
            logger.debug("FTP文件上传成功，上传结果为:["+status+"]");
        }else{
            logger.error("FTP上传文件失败，失败结果为:["+status+"]");
            throw new Exception("FTP上传文件失败，失败结果为:["+status+"]");
        }
    }

	public static void  main(String []args){
		FTPClient ftpClient = new FTPClient("10.23.4.174",21, "test",
				"111111", "/home/test/");

		//ftpClient.send("wweece测试dddd99999999999999999", "1.txt");
	}
}

