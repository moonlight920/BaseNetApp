package com.moonlight.chatapp.home

import android.os.SystemClock
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.moonlight.chatapp.R
import com.moonlight.chatapp.base.BaseFragment
import com.moonlight.chatapp.bean.VideoHeihei


/**
 * Created by songyifeng on 2018/4/23.
 */
class HomeFragment : BaseFragment() {

    private lateinit var textView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var btn_hidden: Button
    private lateinit var btn_logout: Button

    override fun getLayoutResId(): Int {
        return R.layout.fragment_main_home
    }

    override fun initView(view: View) {
        textView = view.findViewById(R.id.textview)
        recyclerView = view.findViewById(R.id.recycler_view)
        btn_hidden = view.findViewById(R.id.btn_hidden)
        btn_logout = view.findViewById(R.id.btn_logout)

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
        btn_logout.setOnClickListener { logout() }
    }

    private fun logout() {
        BmobUser.logOut()   //清除缓存用户对象
        activity!!.finish()
    }

    override fun loadData() {
        arguments?.let {
            textView.setBackgroundColor(it.getInt("color"))
        }
    }

    private fun showHiddenData() {
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.visibility = View.VISIBLE


        val query = BmobQuery<VideoHeihei>()
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("from", "Chinese")
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50)
        //执行查询方法
        query.findObjects(object : FindListener<VideoHeihei>() {
            override fun done(videoList: List<VideoHeihei>?, e: BmobException?) {
                if (e == null) {
                    loadVideoData(videoList!!)
                    toast("查询成功：共" + videoList!!.size + "条数据。")
                } else {
                    Log.i("bmob", "失败：" + e.message + "," + e.errorCode)
                }
            }
        })
    }

    private fun loadVideoData(videoList: List<VideoHeihei>) {
//        val adapter = VideoPlayAdapter(context!!, videoList)
        val adapter = VideoPlayAdapter2(context!!, videoList)
        recyclerView.adapter = adapter
    }
}