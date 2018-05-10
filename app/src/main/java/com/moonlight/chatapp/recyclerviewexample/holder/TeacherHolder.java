package com.moonlight.chatapp.recyclerviewexample.holder;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.moonlight.chatapp.R;
import com.moonlight.chatapp.recyclerviewexample.bean.Teacher;
import com.moonlight.chatapp.recyclerviewexample.base.BaseListItemBean;
import com.moonlight.chatapp.recyclerviewexample.base.BaseRecyclerViewHolder;

/**
 * Created by songyifeng on 2018/5/9.
 */

public class TeacherHolder extends BaseRecyclerViewHolder {
    public TextView tvName;

    public TextView tvEmailId;

    public Teacher teacher;

    public TeacherHolder(View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tvName);

        tvEmailId = itemView.findViewById(R.id.tvEmailId);

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),
                        "OnClick :" + teacher.getName() + " \n " + teacher.getEmailId(),
                        Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void showData(BaseListItemBean data) {
        teacher = (Teacher) data;
        tvName.setText(teacher.getName());
        tvEmailId.setText(teacher.getEmailId());
    }
}
