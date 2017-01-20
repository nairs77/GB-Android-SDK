package com.gebros.platform.auth.ui.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.gebros.platform.log.GBLog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jce_platform on 2016. 5. 31..
 */
public class ImageFetcher extends ImageResizer {

    public static final int HTTP_CACHE_SIZE = 10485760;
    public static final String HTTP_CACHE_DIR = "http";

    public ImageFetcher(Context context, int imageWidth, int imageHeight) {
        super(context, imageWidth, imageHeight);
        init(context);
    }

    public ImageFetcher(Context context, int imageSize) {
        super(context, imageSize);
        init(context);
    }

    private void init(Context context) {
        checkConnection(context);
    }

    private void checkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService("connectivity");

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if ((networkInfo == null) || (!(networkInfo.isConnectedOrConnecting())))
            Toast.makeText(context, "No network connection found.", Toast.LENGTH_LONG).show();
    }

    private Bitmap processBitmap(String data) {

        File f = downloadBitmap(this.mContext, data);
        if (f != null) {
            return decodeSampledBitmapFromFile(f.toString(), this.mImageWidth, this.mImageHeight);
        }

        return null;
    }

    protected Bitmap processBitmap(Object data) {
        return processBitmap(String.valueOf(data));
    }

    public static File downloadBitmap(Context context, String urlString) {
        File cacheDir = DiskLruCache.getDiskCacheDir(context, "http");

        DiskLruCache cache = DiskLruCache.openCache(context, cacheDir,
                10485760L);

        File cacheFile = new File(cache.createFilePath(urlString));

        if (cache.containsKey(urlString)) {
            return cacheFile;
        }

        ImageUtils.disableConnectionReuseIfNecessary();
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(
                    urlConnection.getInputStream(), 8192);//8192
            GBLog.d("BufferedOutputStream before:%s", cache.createFilePath(urlString));
            FileOutputStream cacheFileStream = new FileOutputStream(cacheFile);////////////////////////
            GBLog.d("BufferedOutputStream after11111:%s", cache.createFilePath(urlString));
            out = new BufferedOutputStream(cacheFileStream,
                    8192);//8192
            GBLog.d("BufferedOutputStream after22222:%s", cache.createFilePath(urlString));

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }

            return cacheFile;
        } catch (IOException e) {
            GBLog.d("Image resource URL:%s, downloadBitmapError:%s", urlString, e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    GBLog.e(e, "%s", e.getMessage());
                }
            }
        }

        return null;
    }
}