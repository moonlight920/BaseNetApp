package com.moonlight.chatapp.recyclerviewexample;

import java.io.Serializable;

/**
 * Created by songyifeng on 2018/5/9.
 */

public class Student extends Person implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String emailId;

    public Student() {

    }

    public Student(String name, String emailId) {
        this.name = name;
        this.emailId = emailId;
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
