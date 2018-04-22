package com.moonlight.chatapp

import android.content.Context
import android.graphics.Color
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * Created by songyifeng on 2018/4/23.
 */
class MainViewPagerAdapter(var mContext: Context, var mViewList: List<View>) : PagerAdapter() {

    private var colorList = arrayOf(Color.BLUE, Color.GREEN, Color.GRAY)

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view = mViewList[position]
        view.setBackgroundColor(colorList[position])
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        container.removeView(mViewList[position])// 删除页卡
    }

    override fun getCount(): Int {
        return mViewList.size
    }

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view == any
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return position.toString()
    }

    fun getTabView(position: Int): View {
        val view = LayoutInflater.from(mContext).inflate(R.layout.tabitem_main, null)
        view.setBackgroundColor(colorList[position])
        return view
    }

}