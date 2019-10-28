package com.guc.babyslife.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.guc.babyslife.app.Profile;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by guc on 2018/4/28.
 * 描述：图片工具
 */
public class ImageUtils {
    public static int imgWidth = 1080;
    public static int imgHeight = 1920;


    public static List<String> getImagesStr(String images) {
        List<String> dataImages = new ArrayList<>();
        if (!TextUtils.isEmpty(images)) {
            String[] data = images.split(",");
            List<String> items = Arrays.asList(data);
            dataImages.addAll(items);
        }
        return dataImages;
    }

    /**
     * 从图片路径获取bitmap
     *
     * @param path 文件路径
     * @return
     */
    public static Bitmap getImageBitmapFromPath(String path) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bit = BitmapFactory.decodeFile(path, opts);
        return bit;
    }

    /**
     * 文件保存至应用缓存中
     *
     * @param originalPath
     * @param fileName
     */
    public static String pictureSaveToCache(String originalPath, String fileName) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeFile(originalPath, opts);//原始图片
        String newFile = Profile.getInstance().getImagesDir();

        File fil = new File(newFile);
        if (!fil.exists()) {
            fil.mkdirs();
        }
        return compressBitmap2FileIn500K(bitmap, newFile + File.separator + fileName);
    }

    /**
     * 图片压缩
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= imgWidth)
                    && (options.outHeight >> i <= imgHeight)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                options.inScaled = true;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    /**
     * 将Bitmap对象 保存到本地
     * 需要读写内存卡的权限，否则会发生异常
     *
     * @param context
     * @param mBitmap
     * @param filePath 图片路径  xxx/xxx/xx.jpg
     * @return
     */
    public static String saveBitmap(Context context, Bitmap mBitmap, String filePath) {
        File filePic;
        try {
            filePic = new File(filePath);
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return filePic.getAbsolutePath();
    }

    /**
     * 压缩bitmap,并保存到指定路径
     *
     * @param options  压缩比例
     * @param bitmap
     * @param filePath
     * @return 保存的文件路径  “”表示保存失败
     */
    public static String compressBitmapToFile(int options, Bitmap bitmap, String filePath) {
        // 0-100 100为不压缩
        if (options < 10) options = 10;
        if (options > 100) options = 100;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            } else {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            // 把压缩后的数据存放到baos中
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, fos);
            fos.flush();
            fos.close();
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 压缩图片至500K以内
     *
     * @param bitmap
     * @param filePath
     * @return
     */
    public static String compressBitmap2FileIn500K(Bitmap bitmap, String filePath) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            if (options < 0) options = 0;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            if (options == 0) break;
        }
        try {
            File file = new File(filePath);
            FileOutputStream fos = new FileOutputStream(file);//将压缩后的图片保存的本地上指定路径中
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 在bitmap左上角添加水印
     *
     * @param context
     * @param bitmap      原bitmap
     * @param text        水印文字
     * @param size        文字大小
     * @param color       文字颜色
     * @param paddingLeft 距左侧距离
     * @param paddingTop  距顶部距离
     * @return 添加过水印后的Bitmap
     */
    public static Bitmap drawTextToLeftTop(Context context, Bitmap bitmap, String text,
                                           int size, int color, int paddingLeft, int paddingTop) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(bitmap, text, paint,
                dp2px(context, paddingLeft),
                dp2px(context, paddingTop) + bounds.height());
    }

    /**
     * 在bitmap左下角添加文字水印
     *
     * @param context
     * @param bitmap        原bitmap
     * @param text          水印文字
     * @param size          文字大小
     * @param color         文字颜色
     * @param paddingLeft   距左侧距离
     * @param paddingBottom 距底部距离
     * @return 添加过水印后的Bitmap
     */
    public static Bitmap drawTextToLeftBottom(Context context, Bitmap bitmap, String text,
                                              int size, int color, int paddingLeft, int paddingBottom) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(bitmap, text, paint, dp2px(context, paddingLeft),
                bitmap.getHeight() - dp2px(context, paddingBottom));
    }

    //图片上绘制文字
    private static Bitmap drawTextToBitmap(Bitmap bitmap, String text,
                                           Paint paint, int paddingLeft, int paddingTop) {
        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap bitmapNew = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmapNew);
        //在画布 0，0坐标上开始绘制原始图片
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawText(text, paddingLeft, paddingTop, paint);
        canvas.save();//Canvas.ALL_SAVE_FLAG
//        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        bitmap.recycle();
        return bitmapNew;
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    /**
     * 根据Uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 19) { // api >= 19
            return getRealPathFromUriAboveApi19(context, uri);
        } else { // api < 19
            return getRealPathFromUriBelowAPI19(context, uri);
        }
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
}
