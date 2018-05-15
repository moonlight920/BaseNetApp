package com.moonlight.chatapp.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by songyifeng on 2018/5/15.
 */

public class VideoHeihei extends BmobObject {
    private String video_url;
    private String thumb_url;
    private String from;

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
