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

import java.util.List;

/**
 * Created by songyifeng on 2018/5/9.
 */

public class DataAdapter extends RecyclerView.Adapter {
    private final int VIEW_STUDENT = 1;
    private final int VIEW_TEACHER = 2;
    private final int VIEW_PROG = 0;

    private List<Person> personList;

    // The minimum amount of items to have below your current scroll position
// before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public DataAdapter(List<Person> person, RecyclerView recyclerView) {
        personList = person;

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

        if (personList.get(position) == null)
            return VIEW_PROG;
        if (personList.get(position) instanceof Student)
            return VIEW_STUDENT;
        if (personList.get(position) instanceof Teacher)
            return VIEW_TEACHER;

        return VIEW_STUDENT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_STUDENT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_student, parent, false);

            vh = new StudentViewHolder(v);
        } else if (viewType == VIEW_TEACHER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_teacher, parent, false);
            vh = new TeacherViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StudentViewHolder) {
            Student singleStudent = (Student) personList.get(position);
            ((StudentViewHolder) holder).tvName.setText(singleStudent.getName());
            ((StudentViewHolder) holder).tvEmailId.setText(singleStudent.getEmailId());
            ((StudentViewHolder) holder).student = singleStudent;
        } else if (holder instanceof TeacherViewHolder) {
            Teacher singleTeacher = (Teacher) personList.get(position);
            ((TeacherViewHolder) holder).tvName.setText(singleTeacher.getName());
            ((TeacherViewHolder) holder).tvEmailId.setText(singleTeacher.getEmailId());
            ((TeacherViewHolder) holder).teacher = singleTeacher;
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    public static class TeacherViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;

        public TextView tvEmailId;

        public Teacher teacher;

        public TeacherViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);

            tvEmailId = v.findViewById(R.id.tvEmailId);

            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),
                            "OnClick :" + teacher.getName() + " \n " + teacher.getEmailId(),
                            Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;

        public TextView tvEmailId;

        public Student student;

        public StudentViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);

            tvEmailId = v.findViewById(R.id.tvEmailId);

            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),
                            "OnClick :" + student.getName() + " \n " + student.getEmailId(),
                            Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }
}
