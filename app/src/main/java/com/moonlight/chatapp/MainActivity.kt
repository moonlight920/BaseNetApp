package com.moonlight.chatapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.bmob.v3.Bmob
import com.moonlight.chatapp.bean.User
import com.moonlight.chatapp.net.BaseObserver
import com.moonlight.chatapp.net.RetrofitFactory
import com.moonlight.chatapp.net.RxSchedulers
import com.moonlight.chatapp.utils.ToastUtil
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.BmobUser



class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun initViewData() {
        Bmob.initialize(this, "Your Application ID")

        val bu = BmobUser()
        bu.username = "sendi"
        bu.setPassword("123456")
        bu.email = "sendi@163.com"
        //注意：不能用save方法进行注册
        bu.signUp<User>(object : SaveListener<User>() {
            override fun done(s: User, e: BmobException?) {
                if (e == null) {
                    toast("注册成功:" + s.toString())
                } else {
//                    loge(e)
                }
            }
        })
    }
}
