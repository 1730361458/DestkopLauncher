package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;

import com.example.myapplication.Tool.AppTool;
import com.example.myapplication.adapter.RecyclerAdapter_main;
import com.example.myapplication.bean.AppInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
{

    private RecyclerView recyclerView;
    RecyclerAdapter_main adapter_main;
    private List<AppInfo> appAddList = new ArrayList<AppInfo>();// 已经添加的应用数据列表
    private GridLayoutManager layoutManager;
    private int spanCount = 5; // 设置网格的列数
    private int mPosition; //点击的格子position
    private ItemTouchHelper itemTouchHelper;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String jsonString;

    //ExecutorService 接口 和
    //ThreadPoolExecutor类来创建和管理线程池
    private ExecutorService executorService;
    private ThreadPoolExecutor threadPoolExecutor;
    int corePoolSize = Runtime.getRuntime().availableProcessors();
    int maxPoolSize = corePoolSize * 2; //最大线程数是CPU核心的2倍
    long keepAliveTime = 10; // 10 seconds 存活时间

    int fromPosition;
    int toPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.gridRecyclerView);

        sharedPreferences = getSharedPreferences(AppTool.ALREADY_ADD_LIST, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        jsonString = sharedPreferences.getString(AppTool.APP_MESSAGE, null);


        //创建固定大小的线程池
//        executorService = Executors.newFixedThreadPool(maxPoolSize);
//        threadPoolExecutor = (ThreadPoolExecutor) executorService;
//        threadPoolExecutor.setKeepAliveTime(keepAliveTime, TimeUnit.SECONDS);

        // 将 JSON 字符串转换回集合对象
        if(jsonString!=null)
        {
            Type listType = new TypeToken<ArrayList<AppInfo>>() {}.getType();
            appAddList = new Gson().fromJson(jsonString, listType);
        }
        else
        {
            if(appAddList.size() == 0)
            {
                for(int i=0; i<10; i++)
                {
                    Log.d("123","dasdadadsada");
                    AppInfo appInfo = new AppInfo();
                    appInfo.setAdd(false);
                    appAddList.add(appInfo);
                }
            }

            else
            {
                for(int i=0; i<10; i++)
                {
                    if(! (appAddList.get(i).isAdd()))
                    {
                        AppInfo appInfo = new AppInfo();
                        appInfo.setAdd(false);
                        appAddList.add(appInfo);
                    }
                }
            }
        }

        Log.d("123","json:=="+jsonString);





        adapter_main = new RecyclerAdapter_main(appAddList,this);
        recyclerView.setAdapter(adapter_main);
        layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);


        adapter_main.setOnItemClickListener(new RecyclerAdapter_main.OnItemClickListener(){
            @Override
            public void onItemClick(int position) {
                // 处理点击事件，根据位置执行相应的操作
                if( ! appAddList.get(position).isAdd())
                {
                    Intent intent =new Intent(MainActivity.this,AddAppActivity.class);
                    launcher.launch(intent);
                    mPosition=position;
                }
                else
                {
                    AppTool.startApp(MainActivity.this,appAddList.get(position).getPackage_name());
                }
            }
        });

         itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // 设置拖拽和滑动的动作标志
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

             @Override
             public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                 super.onSelectedChanged(viewHolder, actionState);
                 if (viewHolder != null && actionState != AppTool.ACTION_STATE_IDLE) {
                     // 非闲置状态下，记录下起始 position
                      fromPosition = viewHolder.getAdapterPosition();
                 }
                 if(actionState == AppTool.ACTION_STATE_IDLE){
                     // 当手势抬起时刷新，endPosition 是在 onMove() 回调中记录下来的
                     if (adapter_main != null) {
                         adapter_main.swapItems(fromPosition,toPosition);
                         adapter_main.notifyItemRangeChanged(Math.min(fromPosition, toPosition), Math.abs(fromPosition - toPosition) + 1);
                         appAddList = adapter_main.getItemList();
                     }
                 }
             }

             @Override
             public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                 super.clearView(recyclerView, viewHolder);
             }

             @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //如果不需要拖拽的时候有拖拽动画的操作就去onSelectedChanged下进行swapItem
                // 处理拖拽动作
                //int fromPosition = viewHolder.getAdapterPosition();
                toPosition = target.getAdapterPosition();
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // 处理滑动动作（如果需要）
            }
        });

         adapter_main.setItemTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 保存到 SharedPreferences
        Log.d("123","1111:"+appAddList.get(0).getLabel());
        String appMessage = AppTool.listToJson(appAddList);
        editor.putString(AppTool.APP_MESSAGE, appMessage);
        editor.apply();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("123","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("123","onStop");
    }

    // 创建ActivityResultLauncher对象
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    // 获取返回的对象
                    AppInfo appInfo = (AppInfo) result.getData().getParcelableExtra("appInfo");
                    appAddList.set(mPosition,appInfo); //修改所点击格子的内容
                    adapter_main.notifyDataSetChanged();

                    Bitmap bitmap = AppTool.drawableToBitmap(appInfo.getIcon());
                    appInfo.setBitmapByte(AppTool.getBytesFromBitmap(bitmap));
                    appInfo.setBitmapString(Base64.encodeToString(appInfo.getBitmapByte(), Base64.DEFAULT));

                    // 保存到 SharedPreferences
                    String appMessage = AppTool.listToJson(appAddList);
                    editor.putString(AppTool.APP_MESSAGE, appMessage);
                    editor.apply();


                }
            }
    );
}