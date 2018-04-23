package com.moonlight.chatapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by songyifeng on 2018/4/23.
 */
abstract class BaseLazyFragment : BaseFragment() {
    private var mIsViewCreated = false
    private var mIsDataLoaded = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(getLayoutResId(), container, false)
        initView(view)
        mIsViewCreated = true
        if (userVisibleHint && !mIsDataLoaded) {
            loadData()
            mIsDataLoaded = true
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mIsViewCreated = false
        mIsDataLoaded = false
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && mIsViewCreated && !mIsDataLoaded) {
            loadData()
            mIsDataLoaded = true
        }
    }
}