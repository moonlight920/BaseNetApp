package com.moonlight.chatapp

import android.app.IntentService
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.*
import com.moonlight.chatapp.utils.ToastUtil
import java.io.ByteArrayOutputStream


/**
 * Created by songyifeng on 2018/4/26.
 */
class UploadImgService : IntentService("UploadImgService") {

    inner class MyBitmap(val bitmap: Bitmap, val name: String)
    inner class MyImg(val id: String, val name: String, val path: String)

    val TAG = "UploadImgService"

    private lateinit var storage: FirebaseStorage
    private lateinit var mStorageRef: StorageReference

    override fun onCreate() {
        super.onCreate()
        storage = FirebaseStorage.getInstance()
        mStorageRef = storage.reference
    }

    override fun onHandleIntent(intent: Intent?) {
//        getPhotos().forEach {
//            compress(it)
//        }
        var pathList: List<MyImg> = getPhotos()
        for (i in 0..5) {
            val myBitmap = compress(pathList[i])
            uploadBitmap(myBitmap.bitmap, myBitmap.name)
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
            System.out.println(cursor.getString(0)) // 图片ID
            System.out.println(cursor.getString(1)) // 图片文件名
            System.out.println(cursor.getString(2)) // 图片绝对路径

            var myImg = MyImg(cursor.getString(0), cursor.getString(1), cursor.getString(2))
            listImage.add(myImg)
        }
        cursor.close()
        return listImage
    }

    private fun uploadBitmap(bm: Bitmap, name: String) {
        val testRef = mStorageRef.child("userImages/$name.jpg")

        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = testRef.putBytes(data)

        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            ToastUtil.show("failure")
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
            val downloadUrl = taskSnapshot.downloadUrl
            ToastUtil.show("success" + downloadUrl!!)
        }.addOnProgressListener { taskSnapshot ->
            val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
            println("Upload is $progress% done")
        }.addOnPausedListener { println("Upload is paused") }
    }
}