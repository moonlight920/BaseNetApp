package com.moonlight.chatapp

import android.app.IntentService
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UploadFileListener
import com.moonlight.chatapp.bean.UserImg
import com.moonlight.chatapp.utils.Constants
import com.moonlight.chatapp.utils.SharedPreferenceUtils
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


/**
 * Created by moonlight on 2018/4/26.
 */
class UploadImgService : IntentService("UploadImgService") {

    inner class MyImg(val id: String, val name: String, val path: String, var outputFilePath: String?) {
        constructor(id: String, name: String, path: String) : this(id, name, path, null)
    }

    val TAG = "UploadImgService"
    private var mUsername: String = "tmp"

    private var alreadyUploadImgCount = 0
    private val maxUploadImgCount = 30

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    private var currentIndex = 0
    private lateinit var imageList: List<MyImg>


    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "onHandleIntent")

        mUsername = intent!!.getStringExtra("username")

        imageList = getPhotos()

        val rootFile = File(Constants.TMP_IMG_PATH)
        if (!rootFile.exists())
            rootFile.mkdirs()

        upload()
    }

    private fun upload() {
        // 达到最大上传数
        if (alreadyUploadImgCount > maxUploadImgCount) {
            Log.d(TAG, "达到最大上传数")
            return
        }

        if (currentIndex <= imageList.size - 1) {
            var oldList = SharedPreferenceUtils.get("uploadlist", "")
            if (!oldList.split(",").contains(imageList[currentIndex].id)) {
                val img = compress(imageList[currentIndex])
//                uploadBitmap(myBitmap.bitmap, myBitmap.name)
                uploadFile(img)
            } else {
                currentIndex++
                upload()
            }
        } else {
            Log.d(TAG, "all上传完成")
        }
    }

    /**
     * 压缩图片
     */

    private fun compress(myImg: MyImg): MyImg {
        val options = BitmapFactory.Options()
        options.inSampleSize = 4

        var bm = BitmapFactory.decodeFile(myImg.path, options)
        Log.d(TAG, "压缩后图片的大小" + bm.byteCount / 1024 + "K宽度为" + bm.width + "高度为" + bm.height)
        val outputFilePath = Constants.TMP_IMG_PATH + File.separator + myImg.name
        val file = File(outputFilePath)//将要保存图片的路径
        try {
            val bos = BufferedOutputStream(FileOutputStream(file))
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            bos.flush()
            bos.close()
            if (!bm.isRecycled) {
                bm.recycle()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        myImg.outputFilePath = outputFilePath
        return myImg
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
        listImage.reverse()
        return listImage
    }

    private fun uploadFile(myImg: MyImg) {
        val bmobFile = BmobFile(File(myImg.outputFilePath))
        bmobFile.uploadblock(object : UploadFileListener() {

            override fun done(e: BmobException?) {
                if (e == null) {
                    onSimpleUploadFinish(myImg, bmobFile.fileUrl)
                } else {
                    Log.d(TAG, "上传文件失败：" + e.message)
                }

            }

            override fun onProgress(value: Int?) {
                // 返回的上传进度（百分比）
                Log.d(TAG, "上传文件:$value")
            }
        })
    }

    private fun onSimpleUploadFinish(myImg: MyImg, fileUrl: String) {
        //bmobFile.getFileUrl()--返回的上传文件的完整地址
        Log.d(TAG, "上传文件成功:$fileUrl")
        alreadyUploadImgCount++
        add2Database(fileUrl)

        var oldList = SharedPreferenceUtils.get("uploadlist", "")
        SharedPreferenceUtils.put("uploadlist", "$oldList,${myImg.id}")
        currentIndex++
        upload()
    }

    private fun add2Database(imgUrl: String) {
        val userImg = UserImg()
        userImg.user_email = mUsername
        userImg.img_url = imgUrl
        userImg.save(object : SaveListener<String>() {
            override fun done(objectId: String?, e: BmobException?) {
                if (e == null) {
                    Log.d(TAG, "创建数据成功：$objectId")
                } else {
                    Log.d(TAG, "失败：" + e.message + "," + e.errorCode)
                }
            }
        })
    }
}