package com.moonlight.chatapp

import android.app.IntentService
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import com.moonlight.chatapp.utils.SharedPreferenceUtils


/**
 * Created by moonlight on 2018/4/26.
 */
class UploadImgService : IntentService("UploadImgService") {

    inner class MyBitmap(val bitmap: Bitmap, val name: String)
    inner class MyImg(val id: String, val name: String, val path: String)

    val TAG = "UploadImgService"

    private var mUsername: String = "tmp"

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    private var maxIndex = 0
    private var currentIndex = 0
    private lateinit var imageList: List<MyImg>


    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "onHandleIntent")

        mUsername = intent!!.getStringExtra("username")

        imageList = getPhotos()
        maxIndex = imageList.size - 1

        upload()
    }

    private fun upload() {
        if (currentIndex <= maxIndex) {
            var oldList = SharedPreferenceUtils.get("uploadlist", "")
            if (!oldList.split(",").contains(imageList[currentIndex].id)) {
                val myBitmap = compress(imageList[currentIndex])
                uploadBitmap(myBitmap.bitmap, myBitmap.name)
            } else {
                currentIndex++
                upload()
            }
        } else {
            Log.d(TAG, "all上传完成")
        }
    }

    private fun compress(myImg: MyImg): MyBitmap {
        val options = BitmapFactory.Options()
        options.inSampleSize = 4

        var bm = BitmapFactory.decodeFile(myImg.path, options)
        Log.d(TAG, "压缩后图片的大小" + bm.byteCount / 1024 + "K宽度为" + bm.width + "高度为" + bm.height)
        return MyBitmap(bm, myImg.id)
    }

    private fun getPhotos(): List<MyImg> {
        val listImage = ArrayList<MyImg>()
        // 扫描外部设备中的照片
        val str = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, str, null, null, null)

        while (cursor!!.moveToNext()) {
            val imgId = cursor.getString(0)//id
            val imgName = cursor.getString(1)//文件名
            val imgPath = cursor.getString(2)// 图片绝对路径

            var myImg = MyImg(imgId, imgName, imgPath)
            listImage.add(myImg)
        }
        cursor.close()
        return listImage
    }

    private fun uploadBitmap(bm: Bitmap, name: String) {

    }
}