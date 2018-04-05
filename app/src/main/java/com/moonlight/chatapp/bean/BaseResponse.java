package com.moonlight.chatapp.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/7/24.
 */

public class BaseResponse<T>{

    @SerializedName("returnCode")
    String returnCode;

    @SerializedName("returnMsg")
    String returnMsg;
    @SerializedName("data")
    T data;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
