package com.moonlight.chatapp.home

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.jzvd.JZVideoPlayerStandard
import com.bumptech.glide.Glide
import com.moonlight.chatapp.R
import com.moonlight.chatapp.bean.VideoHeihei


/**
 * Created by moonlight on 2018/4/27.
 */
class VideoPlayAdapter(private val mContext: Context, private var dataList: List<VideoHeihei>) : RecyclerView.Adapter<VideoPlayAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.videoplayer.setUp(dataList[position].video_url, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "lll")
        Glide.with(mContext).load(dataList[position].thumb_url).into(holder.videoplayer.thumbImageView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_home_video_list, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var videoplayer: JZVideoPlayerStandard = v.findViewById(R.id.videoplayer)
    }

}