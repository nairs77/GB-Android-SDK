package com.gebros.platform.auth.ui.image;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.view.Display;

/**
 * Created by jce_platform on 2016. 5. 31..
 */
public class ImageResizer extends ImageWorker {

    public static final String TAG = "ImageWorker";
    protected int mImageWidth;
    protected int mImageHeight;

    public ImageResizer(Context context, int imageWidth, int imageHeight) {
        super(context);
        setImageSize(imageWidth, imageHeight);
    }

    public ImageResizer(Context context, int imageSize) {
        super(context);
        setImageSize(imageSize);
    }

    public void setImageSize(int width, int height) {
        this.mImageWidth = width;
        this.mImageHeight = height;
    }

    public void setImageSize(int size) {
        setImageSize(size, size);
    }

    private Bitmap processBitmap(int resId) {

        return decodeSampledBitmapFromResource(this.mContext.getResources(),
                resId, this.mImageWidth, this.mImageHeight);
    }

    protected Bitmap processBitmap(Object data) {
        return processBitmap(Integer.parseInt(String.valueOf(data)));
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static synchronized Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filename, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if ((height > reqHeight) || (width > reqWidth)) {
            if (width > height)
                inSampleSize = Math.round(height / reqHeight);
            else {
                inSampleSize = Math.round(width / reqWidth);
            }

            float totalPixels = width * height;

            float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / inSampleSize * inSampleSize > totalReqPixelsCap) {
                ++inSampleSize;
            }
        }
        return inSampleSize;
    }

    public static Bitmap createBitmapWithDisplaySampleSize(String filePath, int width, int height, int IMAGE_MAX) {

        BitmapFactory.Options inJustOptions = new BitmapFactory.Options();
        inJustOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, inJustOptions);

        int maxValue = 0;
        boolean isWidth = false;

        BitmapFactory.Options options = new BitmapFactory.Options();
        if (inJustOptions.outWidth > inJustOptions.outHeight) {
            maxValue = inJustOptions.outWidth;
            isWidth = true;
        } else {
            maxValue = inJustOptions.outHeight;
            isWidth = false;
        }

        if (maxValue > IMAGE_MAX) {
            if (isWidth) {
                width = IMAGE_MAX;
                height = IMAGE_MAX * inJustOptions.outHeight
                        / inJustOptions.outWidth;
            } else {
                width = IMAGE_MAX * inJustOptions.outWidth
                        / inJustOptions.outHeight;
                height = IMAGE_MAX;
            }
        } else {
            width = inJustOptions.outWidth;
            height = inJustOptions.outHeight;
        }

        options.inSampleSize = calculateInSampleSize(inJustOptions, width,
                height);
        options.inPurgeable = true;
        options.inDither = true;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap createFitThumbnail(String filePath, int width, int height, Display defaultDisplay) {

        if (filePath == null) {
            return null;
        }

        BitmapFactory.Options inJustOptions = new BitmapFactory.Options();
        inJustOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, inJustOptions);

        @SuppressWarnings("deprecation")
        int max = Math.max(defaultDisplay.getWidth(), defaultDisplay.getHeight());

        if (width <= 0) {
            width = Math.min(inJustOptions.outWidth, max);
        }

        if (height <= 0) {
            height = Math.min(inJustOptions.outHeight, max);
        }

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = calculateInSampleSize(inJustOptions, width,
                height);
        options.inPurgeable = true;
        options.inDither = true;
        Bitmap orgImage = BitmapFactory.decodeFile(filePath, options);

        if (orgImage == null) {
            return null;
        }

        int originWidth = orgImage.getWidth();
        int originHeight = orgImage.getHeight();

        Matrix matrix = new Matrix();

        Point resized = calculateFit(originWidth, originHeight, width, height);

        float w = resized.x / originWidth;
        float h = resized.y / originHeight;

        matrix.postScale(w, h);

        Bitmap resize = Bitmap.createBitmap(orgImage, 0, 0,	orgImage.getWidth(), orgImage.getHeight(), matrix, true);
        if (orgImage != resize) {
            orgImage.recycle();
        }

        return resize;
    }

    public static Point calculateFit(int originWidth, int originHeight, int width, int height) {

        int dw = width;
        int dh = height;

        Point point = new Point();

        double waspect = dw / originWidth;
        double haspect = dh / originHeight;
        if (waspect < haspect)
            dh = (int) (originHeight * waspect);
        else {
            dw = (int) (originWidth * haspect);
        }

        if ((dw > originWidth) || (dh > originHeight)) {
            dw = originWidth;
            dh = originHeight;
        }
        point.x = dw;
        point.y = dh;
        return point;
    }

}
