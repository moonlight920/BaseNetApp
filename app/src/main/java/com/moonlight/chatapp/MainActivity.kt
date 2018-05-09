package com.moonlight.chatapp

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.moonlight.chatapp.recyclerviewexample.*


class MainActivity : AppCompatActivity() {

    private lateinit var tvEmptyView: TextView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: DataAdapter
    private var mLayoutManager: LinearLayoutManager? = null
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

        loadData()

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true)

        mLayoutManager = LinearLayoutManager(this)

        // use a linear layout manager
        mRecyclerView.layoutManager = mLayoutManager

        // create an Object for Adapter
        mAdapter = DataAdapter(personList, mRecyclerView)

        // set the adapter object to the Recyclerview
        mRecyclerView.adapter = mAdapter
        //  mAdapter.notifyDataSetChanged();


        if (personList.isEmpty()) {
            mRecyclerView.visibility = View.GONE
            tvEmptyView.visibility = View.VISIBLE

        } else {
            mRecyclerView.visibility = View.VISIBLE
            tvEmptyView.visibility = View.GONE
        }

        mAdapter.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom

                personList.add(null)
                mAdapter.notifyItemInserted(personList.size - 1)

                handler.postDelayed({
                    //   remove progress item_student
                    personList.removeAt(personList.size - 1)
                    mAdapter.notifyItemRemoved(personList.size)
                    //add items one by one
                    val start = personList.size
                    val end = start + 20
                    for (i in start + 1..end) {
                        if (i % 2 == 1)
                            personList.add(createStudent(i))
                        else
                            personList.add(createTeacher(i))
                        mAdapter.notifyItemInserted(personList.size)
                    }
                    mAdapter.setLoaded()
                    //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                }, 2000)
            }
        })
    }

    // load initial data
    private fun loadData() {
        for (i in 1..20) {
            if (i % 2 == 1)
                personList.add(createStudent(i))
            else
                personList.add(createTeacher(i))

        }
    }

    private fun createStudent(number: Int): Student {
        return Student("Student $number", "androidStudent$number@gmail.com", "student")
    }

    private fun createTeacher(number: Int): Teacher {
        return Teacher("Teacher $number", "androidTeacher$number@gmail.com", "teacher")
    }
}
