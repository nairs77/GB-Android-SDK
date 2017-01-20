package com.gebros.platform.auth.ui.image;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.gebros.platform.internal.JR;
import com.gebros.platform.log.GBLog;

/**
 * Created by jce_platform on 2016. 5. 31..
 */
public class ImageLoader {

    private static ImageLoader instance;
    private static final String DEFAULT_PROFILE_PATH = "img_profile_default";

    /**
     * Directory for cached profile image
     */
    private static final String IMAGE_CACHE_DIR = "thumbnail";

    /**
     * Memory cache size
     */
    private static final int MEM_CACHE_SIZE = 1024 * 1024 * 5;

    /**
     * Disk cache size
     */
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10;

    private static ImageCache thumbnailImageCache;
    private ImageResizer thumbnailImageWorker;

    private Context context;
    private int imageSize;

    /**
     * Get ImageLoader instance.
     *
     * @return
     */
    public static ImageLoader getInstance(Context context) {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                instance = new ImageLoader(context);
            }
        }

        return instance;
    }

    private ImageLoader(Context context) {
        super();
        this.context = context;
        initImageWorker();
    }

    private void initImageWorker() {
        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(IMAGE_CACHE_DIR);
        cacheParams.memCacheSize = MEM_CACHE_SIZE;
        cacheParams.diskCacheSize = DISK_CACHE_SIZE;

        thumbnailImageCache = new ImageCache(context, cacheParams);

        thumbnailImageWorker = new ImageFetcher(context, getImageSize(context));
        thumbnailImageWorker.setLoadingImage(JR.drawable(DEFAULT_PROFILE_PATH));
        thumbnailImageWorker.setImageCache(thumbnailImageCache);
    }

    @SuppressWarnings("deprecation")
    private int getImageSize(Context context) {

        if (imageSize == 0) {

            Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

            final int height = display.getHeight();
            final int width = display.getWidth();

            imageSize = height > width ? height : width;
        }
        return imageSize;
    }

    public void loadThumbnailImage(String url, ImageView imageView) {
        GBLog.d("[ImageLoader] IMGAE URL:::::::::::::"+url);
        if (thumbnailImageWorker == null) {
            synchronized (ImageLoader.class) {
                initImageWorker();
            }
        }

        thumbnailImageWorker.loadImage(url, imageView);
    }
}
