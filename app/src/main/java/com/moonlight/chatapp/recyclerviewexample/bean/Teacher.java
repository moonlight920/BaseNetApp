package com.moonlight.chatapp.recyclerviewexample.bean;

import java.io.Serializable;

/**
 * Created by songyifeng on 2018/5/9.
 */

public class Teacher extends Person implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String emailId;

    public Teacher() {

    }

    public Teacher(String name, String emailId,String typeCode) {
        this.name = name;
        this.emailId = emailId;
        this.typeCode = typeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

}
