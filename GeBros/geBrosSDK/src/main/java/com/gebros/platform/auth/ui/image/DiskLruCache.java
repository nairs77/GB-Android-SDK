package com.gebros.platform.auth.ui.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.gebros.platform.log.GBLog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by jce_platform on 2016. 5. 31..
 */
public class DiskLruCache {

    public static final String CACHE_FILENAME_PREFIX = "cache_";
    public static final int MAX_REMOVALS = 4;
    public static final int INITIAL_CAPACITY = 32;
    public static final float LOAD_FACTOR = 0.75F;

    private final File mCacheDir;
    private int cacheSize = 0;
    private int cacheByteSize = 0;
    private long maxCacheByteSize = 5242880L;
    private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.JPEG;
    private int mCompressQuality = 70;

    private final Map<String, String> mLinkedHashMap = Collections.synchronizedMap(new LinkedHashMap<String, String>(32, 0.75F, true));

    private static final FilenameFilter cacheFileFilter = new FilenameFilter() {
        public boolean accept(File dir, String filename) {
            return filename.startsWith("cache_");
        }
    };

    public static DiskLruCache openCache(Context context, File cacheDir,
                                         long maxByteSize) {
        if (!(cacheDir.exists())) {
            cacheDir.mkdir();
        }

        if ((cacheDir.isDirectory()) && (cacheDir.canWrite())
                && (ImageUtils.getUsableSpace(cacheDir) > maxByteSize)) {
            return new DiskLruCache(cacheDir, maxByteSize);
        }

        return null;
    }

    private DiskLruCache(File cacheDir, long maxByteSize) {
        this.mCacheDir = cacheDir;
        this.maxCacheByteSize = maxByteSize;
    }

    public void put(String key, Bitmap data) {
        synchronized (this.mLinkedHashMap) {
            if (this.mLinkedHashMap.get(key) == null) {
                try {
                    String file = createFilePath(this.mCacheDir, key);
                    if (writeBitmapToFile(data, file)) {
                        put(key, file);
                        flushCache();
                    }
                } catch (FileNotFoundException e) {
                    GBLog.e(e, "%s", e.getMessage());
                } catch (IOException e) {
                    GBLog.e(e, "%s", e.getMessage());
                }
            }
        }
    }

    private void put(String key, String file) {
        this.mLinkedHashMap.put(key, file);
        this.cacheSize = this.mLinkedHashMap.size();
        this.cacheByteSize = (int) (this.cacheByteSize + new File(file).length());
    }

    private void flushCache() {
        int count = 0;

        while ((count < 4)
                && (((this.cacheSize > 64) || (this.cacheByteSize > this.maxCacheByteSize)))) {

            @SuppressWarnings("rawtypes")
            Map.Entry eldestEntry = (Map.Entry) this.mLinkedHashMap.entrySet().iterator().next();

            File eldestFile = new File((String) eldestEntry.getValue());
            long eldestFileSize = eldestFile.length();
            this.mLinkedHashMap.remove(eldestEntry.getKey());
            eldestFile.delete();
            this.cacheSize = this.mLinkedHashMap.size();
            this.cacheByteSize = (int) (this.cacheByteSize - eldestFileSize);
        }
    }

    public Bitmap get(String key) {
        synchronized (this.mLinkedHashMap) {
            String file = this.mLinkedHashMap.get(key);
            if (file != null) {
                return BitmapFactory.decodeFile(file);
            }
            String existingFile = createFilePath(this.mCacheDir, key);
            if (new File(existingFile).exists()) {
                put(key, existingFile);
                return BitmapFactory.decodeFile(existingFile);
            }

            return null;
        }
    }

    public boolean containsKey(String key) {
        if (this.mLinkedHashMap.containsKey(key)) {
            return true;
        }

        String existingFile = createFilePath(this.mCacheDir, key);
        if (new File(existingFile).exists()) {
            put(key, existingFile);
            return true;
        }
        return false;
    }

    public void clearCache() {
        clearCache(this.mCacheDir);
    }

    public static void clearCache(Context context, String uniqueName) {
        File cacheDir = getDiskCacheDir(context, uniqueName);
        clearCache(cacheDir);
    }

    private static void clearCache(File cacheDir) {
        File[] files = cacheDir.listFiles(cacheFileFilter);
        for (int i = 0; i < files.length; ++i)
            files[i].delete();
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        File cacheFile = (("mounted".equals(Environment
                .getExternalStorageState())) && (!(ImageUtils
                .isExternalStorageRemovable()))) ? ImageUtils
                .getExternalCacheDir(context) : null;

        if (cacheFile == null) {
            cacheFile = context.getCacheDir();
        }

        String cachePath = cacheFile.getAbsolutePath();
        return new File(cachePath + File.separator + uniqueName);
    }

    public static String createFilePath(File cacheDir, String key) {
        try {
            key = ImageUtils.encryptString(key);

            return cacheDir.getAbsolutePath()
                    + File.separator
                    + "cache_"
                    + URLEncoder.encode(key.replace("*", ""), "UTF-8").replace(
                    " ", "");

        } catch (UnsupportedEncodingException e) {
            GBLog.e(e, "%s", e.getMessage());
        }

        return null;
    }

    public String createFilePath(String key) {
        return createFilePath(this.mCacheDir, key);
    }

    public void setCompressParams(Bitmap.CompressFormat compressFormat,
                                  int quality) {
        this.mCompressFormat = compressFormat;
        this.mCompressQuality = quality;
    }

    private boolean writeBitmapToFile(Bitmap bitmap, String file) throws IOException, FileNotFoundException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(file), 8192);
            return bitmap.compress(this.mCompressFormat, this.mCompressQuality,
                    out);
        } finally {
            if (out != null)
                out.close();
        }
    }
}
