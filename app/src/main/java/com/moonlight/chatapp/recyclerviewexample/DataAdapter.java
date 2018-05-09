package com.moonlight.chatapp.recyclerviewexample;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.moonlight.chatapp.R;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Created by songyifeng on 2018/5/9.
 */

public class DataAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {
    private final int VIEW_PROG = -1;

    private List<BaseListItemBean> mList;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public DataAdapter(List<BaseListItemBean> list, RecyclerView recyclerView) {
        mList = list;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                            if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (mList.get(position) == null)
            return VIEW_PROG;
        else
            return UIType.getTypeByCode(mList.get(position).getTypeCode());
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseRecyclerViewHolder vh;
        View v;
        if (viewType == VIEW_PROG) {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar, parent, false);
            vh = new ProgressViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(UIType.getLayoutByCode(viewType), parent, false);
            Class clazz = UIType.getClazzByCode(viewType);
            try {
                Constructor c = clazz.getConstructor(View.class);
                vh = (BaseRecyclerViewHolder) c.newInstance(v);
            } catch (Exception e) {
                e.printStackTrace();
                vh = null;
            }
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        if (holder instanceof ProgressViewHolder) {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        } else {
            holder.showData(mList.get(position));
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public static class ProgressViewHolder extends BaseRecyclerViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
        }

        @Override
        void showData(BaseListItemBean data) {

        }
    }
}