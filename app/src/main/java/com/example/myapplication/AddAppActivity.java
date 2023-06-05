package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.example.myapplication.Tool.AppTool;
import com.example.myapplication.adapter.RecyclerAdapter_main;
import com.example.myapplication.adapter.RecyelerAdapter;
import com.example.myapplication.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class AddAppActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyelerAdapter adapter;
    GridLayoutManager layoutManager;
    private int spanCount = 5; // 设置网格的列数
    private List<AppInfo> appList = new ArrayList<AppInfo>(); // 设备应用数据列表
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_app);

        recyclerView = (RecyclerView)findViewById(R.id.addAppRecyclerView);
        appList=AppTool.getAllAppInfo(getApplicationContext(),true);

        adapter = new RecyelerAdapter(appList,this);
        recyclerView.setAdapter(adapter);
        layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);


        adapter.setOnItemClickListener(new RecyelerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent();
                AppInfo appInfo=appList.get(position);
                appInfo.setAdd(true);
                intent.putExtra("appInfo",appInfo);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}