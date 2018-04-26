package com.moonlight.chatapp.home

import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
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

    private lateinit var storage: FirebaseStorage
    private lateinit var mStorageRef: StorageReference

    val dirPath = Environment.getExternalStorageDirectory().absolutePath + "/moonlight"
    val jsonPath = dirPath + "/heihei.json"

    private lateinit var textview: TextView
    private lateinit var recycler_view: RecyclerView

    override fun getLayoutResId(): Int {
        return R.layout.fragment_main_home
    }

    override fun initView(view: View) {
        textview = view.findViewById(R.id.textview)
        recycler_view = view.findViewById(R.id.recycler_view)

        storage = FirebaseStorage.getInstance()
        mStorageRef = storage.reference
    }

    override fun loadData() {
        arguments?.let {
            textview.setBackgroundColor(it.getInt("color"))
        }
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

        recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adapter = VideoPlayAdapter(context!!, strList)
        recycler_view.adapter = adapter
    }

    private fun downloadFile() {
        val file = File(jsonPath)
        val riversRef = mStorageRef.child("heihei.json")

        riversRef.getFile(file)
                .addOnSuccessListener({
                    // Successfully downloaded data to local file
                    // ...
                    toast("download success")
                    loadVideoData()
                }).addOnFailureListener({
            // Handle failed download
            // ...
        })
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