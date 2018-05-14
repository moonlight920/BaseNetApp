package com.moonlight.chatapp.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

/**
 * Created by songyifeng on 2018/4/23.
 */
abstract class BaseFragment : Fragment() {
    protected val TAG = javaClass.name

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(getLayoutResId(), container, false)
        initView(view)
        loadData()
        return view
    }

    /**
     * @return 布局资源id
     */
    protected abstract fun getLayoutResId(): Int

    /**
     * 初始化View
     */
    protected abstract fun initView(view: View)

    /**
     * 加载数据
     */
    protected abstract fun loadData()

    protected fun Fragment.toast(text: String) {
        Toast.makeText(this.activity, text, Toast.LENGTH_SHORT).show()
    }
}