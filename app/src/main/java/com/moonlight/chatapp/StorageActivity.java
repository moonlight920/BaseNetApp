package com.moonlight.chatapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class StorageActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button btn_upload;
    private Button btn_download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        btn_upload = findViewById(R.id.btn_upload);
        btn_download = findViewById(R.id.btn_download);
        imageView = findViewById(R.id.iv);

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadByView();
            }
        });
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download2memory();
            }
        });
    }


    private void uploadByView() {

    }

    private void uploadByInputStream() {

    }

    private void uploadByFile() {

    }

    private void download2memory() {

    }

    private void download2storage() {

    }

    private void downloadByUrl() {

    }

    private void download2setImg() {

    }
}
