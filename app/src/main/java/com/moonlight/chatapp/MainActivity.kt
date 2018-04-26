package com.moonlight.chatapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.jzvd.JZVideoPlayer
import com.moonlight.chatapp.home.HomeFragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val TAG: String = "MainActivity"


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
        startService(intent)
//        startActivity(Intent(this,FirebaseActivity::class.java))
    }

    override fun onPause() {
        super.onPause()
        JZVideoPlayer.releaseAllVideos()
    }
}
