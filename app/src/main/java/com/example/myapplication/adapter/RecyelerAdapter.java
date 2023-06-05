package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.Tool.AppTool;
import com.example.myapplication.bean.AppInfo;

import java.util.List;

public class RecyelerAdapter extends RecyclerView.Adapter<RecyelerAdapter.AppViewHolder>
{
    private List<AppInfo> appList; // 应用列表数据
    private Context context;

    public RecyelerAdapter(List<AppInfo> appList,Context context)
    {
        this.appList = appList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyelerAdapter.AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        // 创建应用项的视图持有者
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position)
    {
        // 绑定数据到应用项视图
        AppInfo appItem = appList.get(position);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        holder.itemView.setLayoutParams(layoutParams);

        holder.bind(appItem);
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
            appIcon.setImageDrawable(appItem.getIcon());
            appName.setText(appItem.getLabel());
        }
    }

    private RecyelerAdapter.OnItemClickListener onItemClickListener;
    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(RecyelerAdapter.OnItemClickListener listener)
    {
        this.onItemClickListener = listener;
    }

}
