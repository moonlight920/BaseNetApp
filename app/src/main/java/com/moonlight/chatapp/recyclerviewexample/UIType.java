package com.moonlight.chatapp.recyclerviewexample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.moonlight.chatapp.R;

/**
 * Created by songyifeng on 2018/5/9.
 */

public enum UIType {
    EMPTY("empty", 0, Object.class, 0),
    STUDENT("student", R.layout.item_student, StudentHolder.class, 1),
    TEACHER("teacher", R.layout.item_teacher, TeacherHolder.class, 2);
    // 成员变量
    private String name;
    private int type;
    private Class clazz;
    private int layout;

    // 构造方法
    UIType(String name, int layoutId, Class clazz, int type) {
        this.name = name;
        this.type = type;
        this.clazz = clazz;
        this.layout = layoutId;
    }

    public RecyclerView.ViewHolder getHolder() {
        return null;
    }

    public static int getTypeByCode(String UITypeCode) {
        for (UIType type : UIType.values()) {
            if (type.name.equals(UITypeCode))
                return type.type;
        }
        return EMPTY.type;
    }

    public static int getLayoutByCode(int type) {
        for (UIType t : UIType.values()) {
            if (t.type == type)
                return t.layout;
        }
        return EMPTY.layout;
    }

    public static Class getClazzByCode(int type) {
        for (UIType t : UIType.values()) {
            if (t.type == type)
                return t.clazz;
        }
        return EMPTY.clazz;
    }
}
