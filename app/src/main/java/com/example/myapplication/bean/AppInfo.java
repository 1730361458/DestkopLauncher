package com.example.myapplication.bean;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.myapplication.Tool.AppTool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class AppInfo implements Parcelable
{
    private int uid;
    private String label;//应用名称
    private String package_name;//应用包名
    private transient Drawable icon;//应用icon
    private boolean IsAdd;
    private byte[] bitmapByte;
    private String bitmapString;

    public AppInfo()
    {
        uid = 0;
        label = "";
        package_name = "";
        icon = null;
    }

    protected AppInfo(Parcel in) {
        uid = in.readInt();
        label = in.readString();
        package_name = in.readString();
        Bitmap bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        icon = new BitmapDrawable(bitmap);
        IsAdd = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeString(label);
        dest.writeString(package_name);
        Bitmap bitmap = AppTool.drawableToBitmap(icon);
        dest.writeParcelable(bitmap, flags);
        dest.writeByte((byte) (IsAdd ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
        @Override
        public AppInfo createFromParcel(Parcel in) {
            return new AppInfo(in);
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };


    public boolean isAdd() {
        return IsAdd;
    }

    public void setAdd(boolean add) {
        IsAdd = add;
    }

    public int getUid()
    {
        return uid;
    }

    public void setUid(int uid)
    {
        this.uid = uid;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getPackage_name()
    {
        return package_name;
    }

    public void setPackage_name(String package_name)
    {
        this.package_name = package_name;
    }

    public Drawable getIcon()
    {
        return icon;
    }

    public void setIcon(Drawable icon)
    {
        this.icon = icon;
    }

    public byte[] getBitmapByte() {
        return bitmapByte;
    }

    public void setBitmapByte(byte[] bitmapByte) {
        this.bitmapByte = bitmapByte;
    }

    public String getBitmapString() {
        return bitmapString;
    }

    public void setBitmapString(String bitmapString) {
        this.bitmapString = bitmapString;
    }
}