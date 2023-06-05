package com.example.myapplication.Tool;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;

import com.example.myapplication.bean.AppInfo;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AppTool
{

    public static int PARCELMAXSIZE=69999;//1048424
    public static String ALREADY_ADD_LIST = "already_add_list";
    public static String APP_MESSAGE = "app_message";
    public static int ACTION_STATE_IDLE=0;
    /**
     * 获取手机已安装应用列表
     * @param ctx
     * @param isFilterSystem 是否过滤系统应用
     * @return
     */
    public static ArrayList<AppInfo> getAllAppInfo(Context ctx, boolean isFilterSystem)
    {
        ArrayList<AppInfo> appBeanList = new ArrayList<>();
        AppInfo bean = null;

        PackageManager packageManager = ctx.getPackageManager();
        List<PackageInfo> list = packageManager.getInstalledPackages(0);
        for (PackageInfo p : list)
        {
            bean = new AppInfo();
            bean.setIcon(p.applicationInfo.loadIcon(packageManager));
            bean.setLabel(packageManager.getApplicationLabel(p.applicationInfo).toString());
            bean.setPackage_name(p.applicationInfo.packageName);
            int flags = p.applicationInfo.flags;
            // 判断是否是属于系统的apk
            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0 && isFilterSystem)
            {
//                bean.setSystem(true);
            }
            else
            {
                appBeanList.add(bean);
            }
        }
        return appBeanList;
    }

    public static int dpToPx(int dp,Context context)
    {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    //drawable转Bitmap
    public static Bitmap drawableToBitmap(Drawable drawable)
    {
        if (drawable instanceof BitmapDrawable)
        {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    // 将 Bitmap 转换为字节数组
    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    //将集合转为String
    public static String listToJson(List<AppInfo> list)
    {
        return new Gson().toJson(list);
    }

    public static Drawable stringToDrawalbe(String imageString)
    {
        // 将字符串转换回字节数组
        byte[] byteArray = Base64.decode(imageString, Base64.DEFAULT);

    // 将字节数组转换为 Bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

    // 将 Bitmap 转换为 Drawable
        Drawable drawable = new BitmapDrawable(bitmap);

        return drawable;
    }


    public static void startApp(Context context,String packageName)
    {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }

}
