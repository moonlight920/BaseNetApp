package com.moonlight.chatapp.home

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.moonlight.chatapp.R
import com.moonlight.chatapp.VideoPlayActivity
import com.moonlight.chatapp.bean.VideoHeihei
import com.moonlight.chatapp.utils.IntentParams


/**
 * Created by moonlight on 2018/4/27.
 */
class VideoPlayAdapter2(private val mContext: Context, private var dataList: List<VideoHeihei>) : RecyclerView.Adapter<VideoPlayAdapter2.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(mContext).load(dataList[position].thumb_url).into(holder.iv_thumb)
        holder.btn_play.setOnClickListener {
            val intent = Intent(mContext,VideoPlayActivity::class.java)
            intent.putExtra(IntentParams.PARAM1,dataList[position].video_url)
            mContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_home_video_list2, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var iv_thumb: ImageView = v.findViewById(R.id.iv_thumb)
        var btn_play: Button = v.findViewById(R.id.btn_play)
    }

}