package com.moonlight.chatapp.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import com.moonlight.chatapp.R


/**
 * Created by songyifeng on 2018/4/23.
 */
class HomeFragmentPagerAdapter(private var mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var colorList = arrayOf(Color.BLUE)
//    private var colorList = arrayOf(Color.BLUE, Color.GREEN, Color.GRAY)

    /**
     * 根据id生成fragment，写好这个就好了
     * @param position
     * @return
     */
    override fun getItem(position: Int): Fragment {
        val fragment = HomeFragment()
        val bundle = Bundle()
        bundle.putInt("color", colorList[position])
        fragment.arguments = bundle
        return fragment
    }

    override fun getCount(): Int {
        return colorList.size
    }

    fun getTabView(position: Int): View {
        val view = LayoutInflater.from(mContext).inflate(R.layout.tabitem_main, null)
        view.setBackgroundColor(colorList[position])
        return view
    }
}