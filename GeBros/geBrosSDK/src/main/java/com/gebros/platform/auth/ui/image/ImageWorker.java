package com.gebros.platform.auth.ui.image;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.gebros.platform.log.GBLog;

import java.lang.ref.WeakReference;

/**
 * Created by jce_platform on 2016. 5. 31..
 */
public abstract class ImageWorker {

    public static final String TAG = "ImageWorker";
    public static final int FADE_IN_TIME = 200;

    private ImageCache mImageCache;
    private Bitmap mLoadingBitmap;
    private boolean mFadeInBitmap = true;
    private boolean mExitTasksEarly = false;
    protected Context mContext;

    public interface ImageLoadingListener {
        public void onLoadingComplete(Bitmap bitmap);
    }

    protected ImageWorker(Context context) {
        this.mContext = context;
    }

    public void loadImage(Object data, ImageView imageView) {
        Bitmap bitmap = null;
        if (this.mImageCache != null) {
            bitmap = this.mImageCache.getBitmapFromMemCache(String.valueOf(data));
        }
        if (bitmap != null) {
            Bitmap r_bitmap = ImageUtils.getRoundCornerBitmap(bitmap, this.mContext);
            imageView.setImageBitmap(r_bitmap);
        } else if (cancelPotentialWork(data, imageView)) {
            BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            AsyncDrawable asyncDrawable = new AsyncDrawable(this.mContext.getResources(), this.mLoadingBitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            try {
                task.execute(new Object[] { data });
            } catch (Exception e) {
                GBLog.d("[ImageWorker] bitmap asyncDrawable catch!!!!!!!!:::::::::");
            }
        }
    }

    public void loadImage(Object data, ImageLoadingListener imageLoadingListener) {

        Bitmap bitmap = null;

        if (this.mImageCache != null) {
            bitmap = this.mImageCache.getBitmapFromMemCache(String.valueOf(data));
        }

        if (bitmap != null) {
            imageLoadingListener.onLoadingComplete(bitmap);
        } else  {

            SimpleBitmapWorkTask task = new SimpleBitmapWorkTask(imageLoadingListener);
            try {
                task.execute(new Object[] { data });
            } catch (Exception e) {

            }
        }
    }

    public void setLoadingImage(Bitmap bitmap) {
        this.mLoadingBitmap = bitmap;
    }

    public void setLoadingImage(int resId) {
        this.mLoadingBitmap = BitmapFactory.decodeResource(
                this.mContext.getResources(), resId);
    }

    public void setImageCache(ImageCache cacheCallback) {
        this.mImageCache = cacheCallback;
    }

    public ImageCache getImageCache() {
        return this.mImageCache;
    }

    public void setImageFadeIn(boolean fadeIn) {
        this.mFadeInBitmap = fadeIn;
    }

    public void setExitTasksEarly(boolean exitTasksEarly) {
        this.mExitTasksEarly = exitTasksEarly;
    }

    protected abstract Bitmap processBitmap(Object paramObject);

    public static void cancelWork(ImageView imageView) {
        BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null) {
            bitmapWorkerTask.cancel(true);
        }
    }

    public static boolean cancelPotentialWork(Object data, ImageView imageView) {
        BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            Object bitmapData = bitmapWorkerTask.data;
            if ((bitmapData == null) || (!(bitmapData.equals(data)))) {
                bitmapWorkerTask.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    private void setImageBitmap(ImageView imageView, Bitmap bitmap) {
        bitmap = ImageUtils.getRoundCornerBitmap(bitmap, this.mContext);
        if (this.mFadeInBitmap) {

            TransitionDrawable td = new TransitionDrawable(new Drawable[] {
                    new ColorDrawable(17170445),
                    new BitmapDrawable(this.mContext.getResources(), bitmap) });

            imageView.setBackgroundDrawable(new BitmapDrawable(this.mContext
                    .getResources(), this.mLoadingBitmap));

            imageView.setImageDrawable(td);
            td.startTransition(200);

        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    private static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                             ImageWorker.BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);

            this.bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public ImageWorker.BitmapWorkerTask getBitmapWorkerTask() {
            return ((ImageWorker.BitmapWorkerTask) this.bitmapWorkerTaskReference
                    .get());
        }
    }

    private class SimpleBitmapWorkTask extends AsyncTask<Object, Void, Bitmap> {

        private Object data;
        private ImageLoadingListener imageLoadingListner;

        public SimpleBitmapWorkTask(ImageLoadingListener imageLoadingListener) {
            this.imageLoadingListner = imageLoadingListener;
        }

        @Override
        protected Bitmap doInBackground(Object... params) {

            this.data = params[0];
            String dataString = String.valueOf(this.data);
            Bitmap bitmap = null;

            if ((ImageWorker.this.mImageCache != null) && (!(isCancelled())) && (!(ImageWorker.this.mExitTasksEarly))) {
                bitmap = ImageWorker.this.mImageCache.getBitmapFromDiskCache(dataString);
            }

            if ((bitmap == null) && (!(isCancelled())) && (!(ImageWorker.this.mExitTasksEarly))) {
                bitmap = ImageWorker.this.processBitmap(params[0]);
            }

            if ((bitmap != null) && (ImageWorker.this.mImageCache != null)) {
                ImageWorker.this.mImageCache.addBitmapToCache(dataString, bitmap);
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if ((isCancelled()) || (ImageWorker.this.mExitTasksEarly)) {
                bitmap = null;
            }

            imageLoadingListner.onLoadingComplete(bitmap);
        }
    }

    private class BitmapWorkerTask extends AsyncTask<Object, Void, Bitmap> {

        private Object data;
        private final WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask(ImageView paramImageView) {
            this.imageViewReference = new WeakReference<ImageView>(paramImageView);
        }

        protected Bitmap doInBackground(Object... params) {
            this.data = params[0];
            String dataString = String.valueOf(this.data);

            Bitmap bitmap = null;

            if ((ImageWorker.this.mImageCache != null) && (!(isCancelled()))
                    && (getAttachedImageView() != null)
                    && (!(ImageWorker.this.mExitTasksEarly))) {
                bitmap = ImageWorker.this.mImageCache
                        .getBitmapFromDiskCache(dataString);
            }

            if ((bitmap == null) && (!(isCancelled()))
                    && (getAttachedImageView() != null)
                    && (!(ImageWorker.this.mExitTasksEarly))) {
                bitmap = ImageWorker.this.processBitmap(params[0]);
            }

            if ((bitmap != null) && (ImageWorker.this.mImageCache != null)) {
                ImageWorker.this.mImageCache.addBitmapToCache(dataString, bitmap);
            }

            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            if ((isCancelled()) || (ImageWorker.this.mExitTasksEarly)) {
                bitmap = null;
            }

            ImageView imageView = getAttachedImageView();
            if ((bitmap != null) && (imageView != null))
                ImageWorker.this.setImageBitmap(imageView, bitmap);
        }

        private ImageView getAttachedImageView() {
            ImageView imageView = (ImageView) this.imageViewReference.get();
            BitmapWorkerTask bitmapWorkerTask = ImageWorker.getBitmapWorkerTask(imageView);

            if (this == bitmapWorkerTask) {
                return imageView;
            }

            return null;
        }
    }
}
