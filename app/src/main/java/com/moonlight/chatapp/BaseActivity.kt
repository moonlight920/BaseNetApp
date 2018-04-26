package com.moonlight.chatapp

import android.support.v7.app.AppCompatActivity

/**
 * Created by songyifeng on 2018/4/26.
 */
open class BaseActivity:AppCompatActivity() {

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        init()
        findView()
        initViewData()
        bindListener()
    }

    open fun findView(){}
    open fun bindListener(){}
    open fun init(){}
    open fun initViewData(){}
}