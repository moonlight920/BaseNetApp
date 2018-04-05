package com.moonlight.chatapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.moonlight.chatapp.bean.User
import com.moonlight.chatapp.net.BaseObserver
import com.moonlight.chatapp.net.RetrofitFactory
import com.moonlight.chatapp.net.RxSchedulers
import com.moonlight.chatapp.utils.ToastUtil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RetrofitFactory.getInstance().login("haha")
                .compose(RxSchedulers.compose())
                .subscribe (object : BaseObserver<User>(){
                    override fun onHandleSuccess(t: User?) {
                        ToastUtil.show("Success")
                    }

                    override fun onHandleError(code: String?, message: String?) {
                        ToastUtil.show("Failure")
                    }
                })
    }
}
