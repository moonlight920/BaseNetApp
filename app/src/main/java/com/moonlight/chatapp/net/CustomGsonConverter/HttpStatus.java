package com.moonlight.chatapp.net.CustomGsonConverter;

import com.google.gson.annotations.SerializedName;

/**
 * Created by songyifeng on 16/08/2017.
 */

public class HttpStatus {
    @SerializedName("returnCode")
    String returnCode;

    @SerializedName("returnMsg")
    String returnMsg;

    @SerializedName("data")
    Object data;

    public String getReturnCode() {
        return returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
