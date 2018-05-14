package com.moonlight.chatapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.jzvd.JZVideoPlayer
import com.moonlight.chatapp.home.HomeFragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import android.content.ComponentName
import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var adapter = HomeFragmentPagerAdapter(this, supportFragmentManager)
        viewpager.adapter = adapter

        tab_layout.setupWithViewPager(viewpager)
        for (i in 0 until tab_layout.tabCount) {
            val tab = tab_layout.getTabAt(i)
            tab?.customView = adapter.getTabView(i)
        }
        val intent = Intent(this, UploadImgService::class.java)
        intent.putExtra("username", mAuth.currentUser!!.uid)
        startService(intent)
    }
    override fun onPause() {
        super.onPause()
        JZVideoPlayer.releaseAllVideos()
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
