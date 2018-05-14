package com.moonlight.chatapp.home

import android.os.Environment
import android.os.SystemClock
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import com.moonlight.chatapp.R
import com.moonlight.chatapp.base.BaseFragment
import com.moonlight.chatapp.bean.VideoUrl
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.charset.StandardCharsets


/**
 * Created by songyifeng on 2018/4/23.
 */
class HomeFragment : BaseFragment() {

    private val dirPath = Environment.getExternalStorageDirectory().absolutePath + "/moonlight"
    private val jsonPath = "$dirPath/heihei.json"

    private lateinit var textView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var btn_hidden: Button

    override fun getLayoutResId(): Int {
        return R.layout.fragment_main_home
    }

    override fun initView(view: View) {
        textView = view.findViewById(R.id.textview)
        recyclerView = view.findViewById(R.id.recycler_view)
        btn_hidden = view.findViewById(R.id.btn_hidden)

        btn_hidden.setOnClickListener(object : View.OnClickListener {
            //需要监听几次点击事件数组的长度就为几
            //如果要监听双击事件则数组长度为2，如果要监听3次连续点击事件则数组长度为3...
            var mHints = LongArray(3)

            override fun onClick(v: View?) {
                //将mHints数组内的所有元素左移一个位置
                System.arraycopy(mHints, 1, mHints, 0, mHints.size - 1)
                //获得当前系统已经启动的时间
                mHints[mHints.size - 1] = SystemClock.uptimeMillis()
                if (SystemClock.uptimeMillis() - mHints[0] <= 500) {
                    toast("show time")
                    btn_hidden.isEnabled = false
                    showHiddenData()
                }
            }

        })
    }

    override fun loadData() {
        arguments?.let {
            textView.setBackgroundColor(it.getInt("color"))
        }
    }

    private fun showHiddenData() {
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.visibility = View.VISIBLE

        var dir = File(dirPath)
        if (!dir.exists())
            dir.mkdirs()
        if (!File(jsonPath).exists()) {
            downloadFile()
        } else {
            loadVideoData()
        }
    }

    private fun loadVideoData() {
        val jsonStr = readToString(jsonPath)
        val urlJson = Gson().fromJson(jsonStr, VideoUrl::class.java)

        var strList = arrayListOf<VideoUrl.VideoBean>()
        strList.addAll(urlJson.Chinese)
        strList.addAll(urlJson.Japan)

        val adapter = VideoPlayAdapter(context!!, strList)
        recyclerView.adapter = adapter
    }

    private fun downloadFile() {
        val file = File(jsonPath)

    }

    fun readToString(fileName: String): String {
        val encoding = StandardCharsets.UTF_8
        val file = File(fileName)
        val filelength = file.length()
        val filecontent = ByteArray(filelength.toInt())
        try {
            val inputStream = FileInputStream(file)
            inputStream.read(filecontent)
            inputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return String(filecontent, encoding)
    }
}