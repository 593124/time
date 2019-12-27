package com.exmaple.Keep;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoteActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView periodTextView;
    private String img = "";
    private ImageView imageView;
    private EditText titleEditext;
    private EditText titleDes;
    private TextView timeTextView;
    private Keep keep;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertnote);
        keep = (Keep) getIntent().getSerializableExtra("Keep");
        initView();


    }


    private void initView() {
         toolbar =  findViewById(R.id.toolbar);
        TextView toolbarTitle =  toolbar.findViewById(R.id.toolbarTitle);
        if (keep!=null){
            toolbarTitle.setText("修改");
        }
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleEditext = findViewById(R.id.et_title);
        titleDes = findViewById(R.id.et_content);
        periodTextView = findViewById(R.id.tv_period);
        Button saveButton = findViewById(R.id.btnSave);
        timeTextView = findViewById(R.id.tv_time);
        View llType = findViewById(R.id.llPeriod);
        View rl_img = findViewById(R.id.addimage);
        View rl_time = findViewById(R.id.choseTime);
        imageView = findViewById(R.id.ivPic);
        llType.setOnClickListener(this);
        rl_img.setOnClickListener(this);
        rl_time.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        if (keep !=null){
            titleEditext.setText(keep.title);
            titleDes.setText(keep.des);
            timeTextView.setText(keep.time);
            img = keep.pic;
            Bitmap photo = BitmapFactory.decodeFile(img);
            imageView.setImageBitmap(photo);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSave:
                save();
                break;
            case R.id.llPeriod:
                choosePeriod();
                break;
            case R.id.choseTime:
                chooseTime();
                break;
            case R.id.addimage:
                getPhoto();

                break;
        }
    }

    private void choosePeriod() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择周期");

        final String items[] = { "Week", "Month", "YEAR" };
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = items[which];
                periodTextView.setText(item);
            }
        });

        builder.show();
    }

    private void save() {
        String title = titleEditext.getText().toString();
        String des = titleDes.getText().toString();
        String time = timeTextView.getText().toString();
        String period = periodTextView.getText().toString();
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this,"标题不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(des)) {
            Toast.makeText(this,"请输入介绍",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(time)) {
            Toast.makeText(this,"请选择到期时间",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(img)) {
            Toast.makeText(this,"请选择图片",Toast.LENGTH_SHORT).show();

            return;
        }
        if (keep == null) {
            keep =  new Keep();
            keep.title = title;
            keep.des = des;
            keep.time = time;
            keep.pic = img;
            keep.period = period;
            KeepUtils.getInstance(NoteActivity.this).insertNote(keep);
            Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
        }else{

            keep.title = title;
            keep.des = des;
            keep.time = time;
            keep.period = period;
            keep.pic = img;
            KeepUtils.getInstance(NoteActivity.this).changeKeep(keep);
            Toast.makeText(this,"更新成功",Toast.LENGTH_SHORT).show();
        }

        finish();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode==RESULT_OK){
            Uri uri_ = data.getData();
            if (uri_ != null) {
                try {
                    img =Utils. getPath(NoteActivity.this,uri_);
                    Bitmap photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri_));
                    imageView.setImageBitmap(photo);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private void chooseTime() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(NoteActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                timeTextView.setText(format.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        timeTextView.setText(format.format(calendar.getTime()));

    }


    private void getPhoto() {
        new AlertDialog.Builder(NoteActivity.this)
                .setTitle("提示")
                .setMessage("选择图片")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                        intent.setType("image/*");
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, 100);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();

    }




}
