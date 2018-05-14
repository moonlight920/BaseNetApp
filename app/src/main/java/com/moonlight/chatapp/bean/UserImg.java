package com.moonlight.chatapp.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by moonlight on 2018/5/15.
 */

public class UserImg extends BmobObject {
    private String user_email;
    private String img_url;

    public UserImg() {
        this.setTableName("upload_img");
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
