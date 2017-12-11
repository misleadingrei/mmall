package com.mmall.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by misleadingrei on 12/11/17.
 */
public class FTPUtil {
    //use propertiesUtil to get propertiesUtil
    public static String ftpIP = PropertiesUtil.getProperty("ftp.server.ip");
    public static String ftpUser=PropertiesUtil.getProperty("ftp.user");
    public static String password=PropertiesUtil.getProperty("ftp.pass");

    // instance filed
    private String ip ;
    private int  port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    // logger
    private static Logger logger = LoggerFactory.getLogger(FTPUtil.class);


    public  FTPUtil(String ip,int port,String user,String pwd){
        this.ip=ip;
        this.port=port;
        this.pwd=pwd;
        this.user=user;
    }
    public static boolean uploadFile(List<File> fileList){
        FTPUtil ftpUtil = new FTPUtil(ftpIP,21,ftpUser,password);
        logger.info("start connect");
        boolean result = ftpUtil.uploadFile("img",fileList);
        logger.info("end connect");
        return result;
    }
    // real impls of uploadFile only for internal usage
    private  boolean uploadFile(String remotePath,List<File> fileList)  {
        boolean uploaded = false;
        FileInputStream fis = null;
        //connect to the ftp user
        if(connect(ip,port,user,pwd))
        {
           try{
               // set properity for ftp client;
               ftpClient.changeWorkingDirectory(remotePath);
               ftpClient.setBufferSize(1024);
               ftpClient.setControlEncoding("utf-8");
               ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
               ftpClient.enterLocalPassiveMode();

               for(File file :fileList)
               {
                   fis = new FileInputStream(file);
                   ftpClient.storeFile(file.getName(),fis);
               }
               uploaded=true;
           }
           catch (IOException e){
               logger.error("upload failed");
               e.printStackTrace();
           }
            finally {
               //close stream and ftpClient

               try {
                   ftpClient.disconnect();
                   fis.close();
               } catch (IOException e) {
                   logger.error("close failed");
                   e.printStackTrace();
               }
           }
        }
        return uploaded;
    }

    private boolean connect(String ip,int port,String user,String pwd){
         boolean isSuccess = true;
         ftpClient = new FTPClient();

        try {
            ftpClient.connect(ip,port);
            ftpClient.login(user,pwd);
        } catch (IOException e) {
           logger.error("connect failed");
            isSuccess=false;
        }
        finally{
            return isSuccess ;
        }

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }


}
