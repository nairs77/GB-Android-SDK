package com.gebros.platform.auth.ui.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.gebros.platform.log.GBLog;

import java.io.File;

/**
 * Created by jce_platform on 2016. 5. 31..
 */
public class ImageCache {

    public static final String TAG = "ImageCache";
    public static final int DEFAULT_MEM_CACHE_SIZE = 5242880;
    public static final int DEFAULT_DISK_CACHE_SIZE = 10485760;
    public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
    public static final int DEFAULT_COMPRESS_QUALITY = 70;
    public static final boolean DEFAULT_MEM_CACHE_ENABLED = true;
    public static final boolean DEFAULT_DISK_CACHE_ENABLED = true;
    public static final boolean DEFAULT_CLEAR_DISK_CACHE_ON_START = false;

    private DiskLruCache mDiskCache;
    private LruCache<String, Bitmap> mMemoryCache;

    public ImageCache(Context context, ImageCacheParams cacheParams) {
        init(context, cacheParams);
    }

    public ImageCache(Context context, String uniqueName) {
        init(context, new ImageCacheParams(uniqueName));
    }

    private void init(Context context, ImageCacheParams cacheParams) {
        File diskCacheDir = DiskLruCache.getDiskCacheDir(context,
                cacheParams.uniqueName);

        if (cacheParams.diskCacheEnabled) {
            this.mDiskCache = DiskLruCache.openCache(context, diskCacheDir,
                    cacheParams.diskCacheSize);
            try {
                this.mDiskCache.setCompressParams(cacheParams.compressFormat,
                        cacheParams.compressQuality);
            } catch (Exception e) {
                this.mDiskCache.clearCache();
                GBLog.e(e, "%s", e.getMessage());
            }
            if (cacheParams.clearDiskCacheOnStart) {
                this.mDiskCache.clearCache();
            }
        }

        if (cacheParams.memoryCacheEnabled)
            this.mMemoryCache = new LruCache<String, Bitmap>(cacheParams.memCacheSize) {
                protected int sizeOf(String key, Bitmap bitmap) {
                    return ImageUtils.getBitmapSize(bitmap);
                }
            };
    }

    public void addBitmapToCache(String data, Bitmap bitmap) {
        if ((data == null) || (bitmap == null)) {
            return;
        }

        if ((this.mMemoryCache != null)
                && (this.mMemoryCache.get(data) == null)) {
            this.mMemoryCache.put(data, bitmap);
        }

        if ((this.mDiskCache != null) && (!(this.mDiskCache.containsKey(data))))
            this.mDiskCache.put(data, bitmap);
    }

    public Bitmap getBitmapFromMemCache(String data) {
        if (this.mMemoryCache != null) {
            Bitmap memBitmap = (Bitmap) this.mMemoryCache.get(data);
            if (memBitmap != null) {
                return memBitmap;
            }
        }
        return null;
    }

    public Bitmap getBitmapFromDiskCache(String data) {
        if (this.mDiskCache != null) {
            return this.mDiskCache.get(data);
        }
        return null;
    }

    public void clearCaches() {
        this.mDiskCache.clearCache();
        this.mMemoryCache.evictAll();
    }

    public static class ImageCacheParams {
        public String uniqueName;
        public int memCacheSize = 5242880;
        public int diskCacheSize = 10485760;
        public Bitmap.CompressFormat compressFormat = ImageCache.DEFAULT_COMPRESS_FORMAT;
        public int compressQuality = 70;
        public boolean memoryCacheEnabled = true;
        public boolean diskCacheEnabled = true;
        public boolean clearDiskCacheOnStart = false;

        public ImageCacheParams(String uniqueName) {
            this.uniqueName = uniqueName;
        }
    }
}
