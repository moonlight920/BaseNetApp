package com.moonlight.chatapp.recyclerviewexample.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by songyifeng on 2018/5/9.
 */

public abstract class BaseRecyclerViewHolder<T extends BaseListItemBean> extends RecyclerView.ViewHolder {
    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void showData(BaseListItemBean data);
}
