package com.moonlight.chatapp.recyclerviewexample;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.moonlight.chatapp.R;

/**
 * Created by songyifeng on 2018/5/9.
 */

public class StudentHolder extends BaseRecyclerViewHolder {
    public TextView tvName;

    public TextView tvEmailId;

    public Student student;

    public StudentHolder(View itemView) {

        super(itemView);
        tvName = itemView.findViewById(R.id.tvName);

        tvEmailId = itemView.findViewById(R.id.tvEmailId);

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),
                        "OnClick :" + student.getName() + " \n " + student.getEmailId(),
                        Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    void showData(BaseListItemBean data) {
        student = (Student)data;
        tvName.setText(student.getName());
        tvEmailId.setText(student.getEmailId());
    }
}
