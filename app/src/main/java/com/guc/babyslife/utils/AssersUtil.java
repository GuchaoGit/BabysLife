package com.guc.babyslife.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guc.babyslife.app.Constants;
import com.guc.babyslife.model.SleepTime;
import com.guc.babyslife.model.StdData;
import com.guc.babyslife.model.VideoInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guc on 2019/7/25.
 * 描述：资源文件读取
 */
public class AssersUtil {
    static Gson gson = new Gson();
    private static String FILE_BOY_STD_HEIGHT = "boy_std_height.json";
    private static String FILE_BOY_STD_WEIGHT = "boy_std_weight.json";
    private static String FILE_GIRL_STD_HEIGHT = "girl_std_height.json";
    private static String FILE_GIRL_STD_WEIGHT = "girl_std_weight.json";
    private static String FILE_REARING_SOURCE = "data_source.json";
    private static String FILE_SLEEP_TIME = "sleep_time.json";

    /**
     * 育儿宝典小视频
     *
     * @param context context
     * @return 数据
     */
    public static List<VideoInfo> getRearingDatas(Context context) {
        String jsonData = getStringFromAssets(context, FILE_REARING_SOURCE);
        return gson.fromJson(jsonData, new TypeToken<List<VideoInfo>>() {
        }.getType());
    }

    /**
     * 获取睡眠时间数据
     *
     * @param context context
     * @return 数据
     */
    public static List<SleepTime> getSleepTimeData(Context context) {
        String jsonData = getStringFromAssets(context, FILE_SLEEP_TIME);
        return gson.fromJson(jsonData, new TypeToken<List<SleepTime>>() {
        }.getType());
    }

    public static List<StdData> getStdDatas(Context context, int type) {
        List<StdData> datas;
        switch (type) {
            case Constants.STD_BOY_HEIGHT:
                datas = getBoyStdHeight(context);
                break;
            case Constants.STD_BOY_WEIGHT:
                datas = getBoyStdWeight(context);
                break;
            case Constants.STD_GIRL_HEIGHT:
                datas = getGirlStdHeight(context);
                break;
            case Constants.STD_GIRL_WEIGHT:
                datas = getGirlStdWeight(context);
                break;
            default:
                datas = new ArrayList<>();
                break;

        }
        return datas;
    }

    /**
     * 获取男孩标准身高
     *
     * @param context c
     * @return datas
     */
    private static List<StdData> getBoyStdHeight(Context context) {
        String jsonData = getStringFromAssets(context, FILE_BOY_STD_HEIGHT);
        return gson.fromJson(jsonData, new TypeToken<List<StdData>>() {
        }.getType());
    }

    /**
     * 获取男孩标准体重
     *
     * @param context c
     * @return datas
     */
    private static List<StdData> getBoyStdWeight(Context context) {
        String jsonData = getStringFromAssets(context, FILE_BOY_STD_WEIGHT);
        return gson.fromJson(jsonData, new TypeToken<List<StdData>>() {
        }.getType());
    }

    /**
     * 获取女孩标准身高
     *
     * @param context c
     * @return datas
     */
    private static List<StdData> getGirlStdHeight(Context context) {
        String jsonData = getStringFromAssets(context, FILE_GIRL_STD_HEIGHT);
        return gson.fromJson(jsonData, new TypeToken<List<StdData>>() {
        }.getType());
    }

    /**
     * 获取女孩标准体重
     *
     * @param context c
     * @return datas
     */
    private static List<StdData> getGirlStdWeight(Context context) {
        String jsonData = getStringFromAssets(context, FILE_GIRL_STD_WEIGHT);
        return gson.fromJson(jsonData, new TypeToken<List<StdData>>() {
        }.getType());
    }

    private static String getStringFromAssets(Context context, String filename) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    context.getAssets().open(filename)));
            String line;
            while ((line = bf.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 读取assets目录下图片资源
     *
     * @param context  c
     * @param fileName 文件名
     * @return bitmap对象
     */
    public static Bitmap getBitmapFromAssets(Context context, String fileName) {
        Bitmap bitmap = null;
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open(fileName);//filename是assets目录下的图片名
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
