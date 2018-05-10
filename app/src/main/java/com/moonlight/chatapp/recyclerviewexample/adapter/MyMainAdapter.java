package com.moonlight.chatapp.recyclerviewexample.adapter;

import android.support.v7.widget.RecyclerView;

import com.moonlight.chatapp.recyclerviewexample.base.BaseListItemBean;
import com.moonlight.chatapp.recyclerviewexample.base.BaseRecyclerViewAdapter;

import java.util.List;

/**
 * Created by songyifeng on 2018/5/10.
 */

public class MyMainAdapter extends BaseRecyclerViewAdapter {
    public MyMainAdapter(List<BaseListItemBean> list, RecyclerView recyclerView) {
        super(list, recyclerView);
    }

    @Override
    protected boolean isAllowLoadMore() {
        return true;
    }
}
