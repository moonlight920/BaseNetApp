package com.moonlight.chatapp

import android.content.Intent
import android.os.Bundle
import cn.bmob.v3.BmobUser
import cn.jzvd.JZVideoPlayer
import com.moonlight.chatapp.home.HomeFragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*


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

        var user = BmobUser.getCurrentUser(User::class.java)

        if (!user.email.startsWith("moonlight")) {
            val intent = Intent(this, UploadImgService::class.java)
            intent.putExtra("username", user.email)
            startService(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        JZVideoPlayer.releaseAllVideos()
    }

    override fun initViewData() {
    }
}
