package com.moonlight.chatapp

import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity

class LoadPhotoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_photo)
    }

    fun getPhotos(): List<String> {
        val listImage = ArrayList<String>()
        // 扫描外部设备中的照片
        val str = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, str, null, null, null)

        while (cursor!!.moveToNext()) {
            System.out.println(cursor.getString(0)) // 图片ID
            System.out.println(cursor.getString(1)) // 图片文件名
            System.out.println(cursor.getString(2)) // 图片绝对路径
            listImage.add(cursor.getString(2))
        }
        cursor.close()
        return listImage
    }
}
