package com.example.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.Tool.AppTool;
import com.example.myapplication.bean.AppInfo;

import java.util.Collections;
import java.util.List;

public class RecyclerAdapter_main extends RecyclerView.Adapter<RecyclerAdapter_main.AppViewHolder>
{
    private List<AppInfo> appList; // 应用列表数据
    private Context context;
    private ItemTouchHelper itemTouchHelper;
    public RecyclerAdapter_main(List<AppInfo> appList, Context context)
    {
        this.appList = appList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerAdapter_main.AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        // 创建应用项的视图持有者
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_already_add_item, parent, false);
        return new RecyclerAdapter_main.AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter_main.AppViewHolder holder, int position)
    {
        // 绑定数据到应用项视图
        AppInfo appItem = appList.get(position);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        holder.itemView.setLayoutParams(layoutParams);
        holder.bind(appItem);

        // 设置拖拽的触摸监听器
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
                    // 开始拖拽
                    itemTouchHelper.startDrag(holder);
                }
                return false;
            }

        });
    }


    @Override
    public int getItemCount()
    {
        return appList.size();
    }

    public class AppViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView appIcon;
        private TextView appName;

        public AppViewHolder(View itemView)
        {
            super(itemView);
            appIcon = itemView.findViewById(R.id.app_icon);
            appName = itemView.findViewById(R.id.app_name);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (onItemClickListener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });
        }

        public void bind(AppInfo appItem)
        {
            // 设置应用项视图的数据
            if(appItem.isAdd())
            {
                appIcon.setImageDrawable( AppTool.stringToDrawalbe(appItem.getBitmapString()));
                appName.setText(appItem.getLabel());
            }
        }
    }

    private  OnItemClickListener onItemClickListener;
    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.onItemClickListener = listener;
    }


    public void swapItems(int fromPosition, int toPosition) {
        Collections.swap(appList, fromPosition, toPosition);
        notifyItemMoved(fromPosition,toPosition);
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper)
    {
        this.itemTouchHelper=itemTouchHelper;
    }

    public List getItemList()
    {
        return appList;
    }


}
