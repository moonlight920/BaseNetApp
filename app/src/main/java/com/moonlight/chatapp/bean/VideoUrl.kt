package com.moonlight.chatapp.bean

/**
 * Created by moonlight on 2018/4/27.
 */
class VideoUrl(
        val Chinese: List<VideoBean>,
        val Japan: List<VideoBean>) {
    class VideoBean(
            val videoUrl: String,
            val thumbUrl: String)
}