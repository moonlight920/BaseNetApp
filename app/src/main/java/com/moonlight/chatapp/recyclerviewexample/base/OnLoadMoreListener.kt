package com.moonlight.chatapp.recyclerviewexample.base

/**
 * Created by songyifeng on 2018/5/9.
 */
interface OnLoadMoreListener {
    fun onLoadMore()
    fun onLoadMoreFinish(list:List<BaseListItemBean>?)
}