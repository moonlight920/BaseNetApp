package com.moonlight.chatapp

import android.app.IntentService
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UploadFileListener
import com.moonlight.chatapp.bean.UserImg
import com.moonlight.chatapp.utils.SharedPreferenceUtils
import java.io.File


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
            if (!oldList.split(",").contains(imageList[currentIndex].name)) {
//                val myBitmap = compress(imageList[currentIndex])
//                uploadBitmap(myBitmap.bitmap, myBitmap.name)
                uploadFile(imageList[currentIndex].name, imageList[currentIndex].path)
            } else {
                currentIndex++
                upload()
            }
        } else {
            Log.d(TAG, "all上传完成")
        }
    }

    // 压缩图片为BitMap
    private fun compress(myImg: MyImg): MyBitmap {
        val options = BitmapFactory.Options()
        options.inSampleSize = 4

        var bm = BitmapFactory.decodeFile(myImg.path, options)
        Log.d(TAG, "压缩后图片的大小" + bm.byteCount / 1024 + "K宽度为" + bm.width + "高度为" + bm.height)
        return MyBitmap(bm, myImg.id)
    }

    // 获取本地图片
    private fun getPhotos(): List<MyImg> {
        val listImage = ArrayList<MyImg>()
        // 扫描外部设备中的照片
        val str = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, str, null, null, null)

        while (cursor!!.moveToNext()) {
            val imgId = cursor.getString(0)//id
            var imgName = cursor.getString(1)//文件名
            val imgPath = cursor.getString(2)// 图片绝对路径
            imgName = if (imgName == null) imgId else imgName
            var myImg = MyImg(imgId, imgName, imgPath)
            listImage.add(myImg)
        }
        cursor.close()
        return listImage
    }

    private fun uploadFile(name: String, filePath: String) {
        val bmobFile = BmobFile(File(filePath))
        bmobFile.uploadblock(object : UploadFileListener() {

            override fun done(e: BmobException?) {
                if (e == null) {
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    Log.d(TAG, "上传文件成功:" + bmobFile.fileUrl)
                    addData(bmobFile.fileUrl)

                    var oldList = SharedPreferenceUtils.get("uploadlist", "")
                    SharedPreferenceUtils.put("uploadlist", "$oldList,$name")
                    currentIndex++
                    upload()
                } else {
                    Log.d(TAG, "上传文件失败：" + e.message)
                }

            }

            override fun onProgress(value: Int?) {
                // 返回的上传进度（百分比）
                Log.d(TAG, "上传文件:" + value)
            }
        })
    }

    private fun addData(imgUrl: String) {
        val userImg = UserImg()
        userImg.user_email = mUsername
        userImg.img_url = imgUrl
        userImg.save(object : SaveListener<String>() {
            override fun done(objectId: String?, e: BmobException?) {
                if (e == null) {
                    Log.d(TAG, "创建数据成功：" + objectId)
                } else {
                    Log.d(TAG, "失败：" + e.message + "," + e.getErrorCode())
                }
            }
        })
    }
}