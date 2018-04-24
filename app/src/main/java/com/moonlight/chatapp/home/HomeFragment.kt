package com.moonlight.chatapp.home

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.moonlight.chatapp.R
import com.moonlight.chatapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_main_home.view.*

/**
 * Created by songyifeng on 2018/4/23.
 */
class HomeFragment : BaseFragment() {

    private lateinit var textview: TextView

    override fun getLayoutResId(): Int {
        return R.layout.fragment_main_home
    }

    override fun initView(view: View) {
        textview = view.findViewById(R.id.textview)
    }

    override fun loadData() {
        arguments?.let {
            textview.setBackgroundColor(it.getInt("color"))
        }
    }
}