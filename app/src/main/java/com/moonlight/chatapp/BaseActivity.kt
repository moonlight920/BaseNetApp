package com.moonlight.chatapp

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by songyifeng on 2018/4/26.
 */
open class BaseActivity:AppCompatActivity() {

    protected val TAG = javaClass.name
    protected lateinit var mAuth: FirebaseAuth

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        init()
        findView()
        initViewData()
        bindListener()
        mAuth = FirebaseAuth.getInstance()
    }

    open fun findView(){}
    open fun bindListener(){}
    open fun init(){}
    open fun initViewData(){}

    protected fun Context.toast(text:String){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
    }
}