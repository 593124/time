package com.exmaple.Keep;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KeepDetailActivity extends AppCompatActivity{
    Keep keep;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keepdetail);

        initView();
    }

    private void initView() {
        keep = (Keep) getIntent().getSerializableExtra("Keep");
        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView  mEtTitle = findViewById(R.id.tv_title);
        TextView tv_period = findViewById(R.id.tv_period);
        TextView  tvDay = findViewById(R.id.timeDis);
        TextView tvdes = findViewById(R.id.tv_content);
        TextView  timeTextView = findViewById(R.id.tv_time);
        ImageView ivPic = findViewById(R.id.ivPic);

        if (!TextUtils.isEmpty(keep.pic)){
            ivPic.setImageBitmap(getBitMap(keep.pic));
        }
        mEtTitle.setText(keep.title);
        tvdes.setText(keep.des);
        tv_period.setText(keep.period);
        timeTextView.setText(keep.time);
        if (Utils.getDays(keep.time)>=0){
            tvDay.setText(Utils.getDays(keep.time)+" Days");
        }else{
            tvDay.setText("已到期");
        }

    }
    public static Bitmap getBitMap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_del:
                new AlertDialog.Builder(KeepDetailActivity.this)
                        .setTitle("提示")
                        .setMessage("是否确定删除")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                KeepUtils.getInstance(KeepDetailActivity.this).deleteKeepById(keep.id+"");
                                setResult(RESULT_OK);
                                finish();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }


                }).create().show();

                return true;
            case R.id.nav_update:
                Intent intent = new Intent();
                intent.setClass(KeepDetailActivity.this, NoteActivity.class);
                intent.putExtra("Keep", keep);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }






}
