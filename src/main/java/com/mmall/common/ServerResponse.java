package com.mmall.common;


import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * Created by misleadingrei on 11/26/17.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
// to ensure null value won't be serialized by json
public class ServerResponse<T> implements Serializable {

    private int status ;
    private String msg;
    private T data;



    private ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status,String msg)
    {

        this.status=status;
        this.msg=msg;
    }
    private ServerResponse(int status,T data)
    {
      this.status=status;
      this.data=data;
    }
    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    //ensure this not serialized by json
    @JsonIgnore
    public boolean isSuccess(){
        return this.status==ResponseCode.SUCCESS.getCode();
    }


    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public int getStatus() {
        return status;
    }


    /*
         create by success status
     */

    //created by status
    public static <T> ServerResponse<T> createBySuccess()
    {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }
    //created by msg and status
    public static <T> ServerResponse<T> createBySuccessrMsg(String msg)
    {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }
    // created by data and status
    public static <T> ServerResponse<T> createBySuccessData(T Data)
    {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),Data);
    }
   // created by data and status and msg
    public static <T> ServerResponse<T> createBySuccess(String msg,T Data)
    {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,Data);
    }


    /*
         created by Error ,when encountered error we don't send data
     */

    // created by Errorstatus
    public static <T> ServerResponse<T> createByError()
    {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode());
    }
    //created by Errormsg and status
    public static <T> ServerResponse<T> createByErrorMsg(String ErrorMsg)
    {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ErrorMsg);
    }

   //other type of ERRORmsg ,see ResponseCode
   public  static  <T> ServerResponse<T> createByErrorCodeAndMsg(int errorCode ,String errorMsg)
   {
       return new ServerResponse<T>(errorCode,errorMsg);
   }

}
