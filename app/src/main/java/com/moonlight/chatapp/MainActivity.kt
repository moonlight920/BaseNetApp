package com.moonlight.chatapp

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.moonlight.chatapp.recyclerviewexample.adapter.MyMainAdapter
import com.moonlight.chatapp.recyclerviewexample.base.BaseListItemBean
import com.moonlight.chatapp.recyclerviewexample.base.BaseRecyclerViewAdapter
import com.moonlight.chatapp.recyclerviewexample.base.OnLoadMoreListener
import com.moonlight.chatapp.recyclerviewexample.bean.Person
import com.moonlight.chatapp.recyclerviewexample.bean.Student
import com.moonlight.chatapp.recyclerviewexample.bean.Teacher


class MainActivity : AppCompatActivity() {

    private lateinit var tvEmptyView: TextView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: BaseRecyclerViewAdapter
    private var personList = ArrayList<BaseListItemBean?>()
    private var handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        RetrofitFactory.getInstance().login("haha")
//                .compose(RxSchedulers.compose())
//                .subscribe (object : BaseObserver<User>(){
//                    override fun onHandleSuccess(t: User?) {
//                        ToastUtil.show("Success")
//                    }
//
//                    override fun onHandleError(code: String?, message: String?) {
//                        ToastUtil.show("Failure")
//                    }
//                })
        mRecyclerView = findViewById(R.id.recycler_view)
        tvEmptyView = findViewById(R.id.empty_view)

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true)

        // use a linear layout manager
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        // create an Object for Adapter
        mAdapter = MyMainAdapter(personList, mRecyclerView)

        // set the adapter object to the Recyclerview
        mRecyclerView.adapter = mAdapter
        //  mAdapter.notifyDataSetChanged();

        personList.addAll(getData())
        if (personList.isEmpty()) {
            mRecyclerView.visibility = View.GONE
            tvEmptyView.visibility = View.VISIBLE

        } else {
            mRecyclerView.visibility = View.VISIBLE
            tvEmptyView.visibility = View.GONE
        }

        mAdapter.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                handler.postDelayed({
                    mAdapter.loadMoreFinish(getData())
                }, 2000)
            }

            override fun onLoadMoreFinish(list: List<BaseListItemBean>?) {
                list?.forEach {
                    //add items one by one
                    personList.add(it)
                    mAdapter.notifyItemInserted(personList.size)
                    //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                }
            }
        })
    }

    // load initial data
    private fun getData(): List<Person> {
        var tmpList = ArrayList<Person>()
        for (i in 1..20) {
            if (i % 2 == 1)
                tmpList.add(createStudent(i))
            else
                tmpList.add(createTeacher(i))
        }
        return tmpList
    }

    private fun createStudent(number: Int): Student {
        return Student("Student $number", "androidStudent$number@gmail.com", "student")
    }

    private fun createTeacher(number: Int): Teacher {
        return Teacher("Teacher $number", "androidTeacher$number@gmail.com", "teacher")
    }
}
