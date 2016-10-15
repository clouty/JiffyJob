package com.jiffyjob.nimblylabs.customui;

/**
 * ImageHelper provides conversion algorithms to convert images.
 * Created by NimblyLabs on 24/1/2015.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;

import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ImageHelper {
    /**
     * Converts an image into a rounded image. Increase bitmap size if bitmap doesn't fit center</br>
     * Radius = 100 standard size
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float radius) {
        //radius = 100 standard size
        int scale = (int) radius * 2;
        Bitmap result = null;
        try {
            Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, scale, scale, true);//resize
            result = Bitmap.createBitmap(scale, scale, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            int color = 0xff424242;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, scale, scale);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(radius, radius, radius, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(resizeBitmap, rect, rect, paint);

        } catch (NullPointerException e) {
        } catch (OutOfMemoryError o) {
        }
        return result;
    }

    public static void setAlphaGrayScaleImage(Context context, String urlString, ImageView imageView) {
        if (urlString == null) return;
/*
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        imageView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
*/
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setAlpha(0.3f);

        /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();*/

        //https://github.com/wasabeef/glide-transformations
        Glide.with(context)
                .load(urlString)
                .fitCenter()
                .bitmapTransform(new GrayscaleTransformation(context))
                .into(imageView);
    }

    public static byte[] BitmapToStream(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
        return stream.toByteArray();
    }
}
