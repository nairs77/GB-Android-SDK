package com.gebros.platform.auth.ui.image;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.TypedValue;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by jce_platform on 2016. 5. 31..
 */
public class ImageUtils {

    public static final int IO_BUFFER_SIZE = 8192;
    private static final int ROUND_PIXEL = 150;//90;

    public static void disableConnectionReuseIfNecessary() {
        if (hasHttpConnectionBug())
            System.setProperty("http.keepAlive", "false");
    }

    @SuppressLint({ "NewApi" })
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= 12) {
            return bitmap.getByteCount();
        }

        return (bitmap.getRowBytes() * bitmap.getHeight());
    }

    @SuppressLint({ "NewApi" })
    public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= 9) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    @SuppressLint({ "NewApi" })
    public static File getExternalCacheDir(Context context) {
        if (hasExternalCacheDir()) {
            return context.getExternalCacheDir();
        }

        String cacheDir = "/Android/data/" + context.getPackageName()
                + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath()
                + cacheDir);
    }

    @SuppressLint({ "NewApi" })
    public static long getUsableSpace(File path) {

        if (Build.VERSION.SDK_INT >= 9) {
            return path.getUsableSpace();
        }

        StatFs stats = new StatFs(path.getPath());
        return (stats.getBlockSize() * stats.getAvailableBlocks());
    }

    public static int getMemoryClass(Context context) {
        return ((ActivityManager) context.getSystemService("activity"))
                .getMemoryClass();
    }

    public static boolean hasHttpConnectionBug() {
        return (Build.VERSION.SDK_INT < 8);
    }

    public static boolean hasExternalCacheDir() {
        return (Build.VERSION.SDK_INT >= 8);
    }

    public static boolean hasActionBar() {
        return (Build.VERSION.SDK_INT >= 11);
    }

    public static String encryptString(String str) {

        if(str.contains("?")) {
            String[] strArr = str.split("\\?");
            str = strArr[0];
            String[] strArrAgain = str.split("\\/");
            str = strArrAgain[strArrAgain.length-1];
        }

        String key = "joyple-android-sdk";
        StringBuffer sb = new StringBuffer(str);

        int lenStr = str.length();
        int lenKey = key.length();

        int i = 0;
        for (int j = 0; i < lenStr; ++j) {
            if (j >= lenKey)
                j = 0;
            sb.setCharAt(i, (char) (str.charAt(i) ^ key.charAt(j)));

            ++i;
        }

        return sb.toString();
    }

    public static String getFilePathFromUri(Context context, Uri uri)
            throws Exception {
        if (uri == null) {
            throw new FileNotFoundException("Uri is null.");
        }
        if (context == null) {
            throw new NullPointerException("Context is null.");
        }
        if ("file".equals(uri.getScheme())) {
            return uri.getPath();
        }

        String[] proj = { "_data", "orientation" };
        String filePath = null;

        Cursor cursor = context.getContentResolver().query(uri, proj, null,	null, null);
        try {

            if ((cursor != null) && (cursor.moveToFirst())) {
                int columnIndex = cursor.getColumnIndex(proj[0]);
                filePath = cursor.getString(columnIndex);
            }

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }

        if (filePath == null) {

            if (uri.getPath() == null) {
                throw new FileNotFoundException("File path is null.");
            }

            filePath = uri.getPath();
        }

        return filePath;
    }

    public static String writeStoryImage(Context context, Bitmap bitmap) throws IOException {

        File diskCacheDir = DiskLruCache.getDiskCacheDir(context, "story");

        if (!(diskCacheDir.exists())) {
            diskCacheDir.mkdirs();
        }

        String file = diskCacheDir.getAbsolutePath() + File.separator + "temp_"	+ System.currentTimeMillis() + ".jpg";

        Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
        int compressQuality = 100;

        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(file), 8192);
            bitmap.compress(compressFormat, compressQuality, out);
        } finally {
            if (out != null) {
                out.close();
            }
        }

        return file;
    }

    /**
     * 프로필 이미지 모서리 라운딩
     * @param bitmap
     * @return
     */
    public static Bitmap getRoundCornerBitmap(Bitmap bitmap, Context context) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ROUND_PIXEL, context.getResources().getDisplayMetrics());;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
