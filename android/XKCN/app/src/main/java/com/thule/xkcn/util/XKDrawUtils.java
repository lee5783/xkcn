package com.thule.xkcn.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created by thule on 10/10/14.
 */
public class XKDrawUtils {

    public static Bitmap getRoundedCornerBitmap(Context context, Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        if (context == null)
        {
            Log.i("", "Context is null");
        }

        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;

        float ratio = Math.max(1.0f, Float.valueOf(bitmap.getWidth())/screenWidth);

        float BORDER_WIDTH_PIXELS = XKUtils.convertDpToPixel(context, 2) * ratio;
        float ROUNDED_PIXELS = XKUtils.convertDpToPixel(context, 8) * ratio;

        final int color = 0xff424242;
        final Paint paint = new Paint();

        final RectF rectF = new RectF(BORDER_WIDTH_PIXELS/2, BORDER_WIDTH_PIXELS/2, bitmap.getWidth()-BORDER_WIDTH_PIXELS/2, bitmap.getHeight()-BORDER_WIDTH_PIXELS/2);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, ROUNDED_PIXELS, ROUNDED_PIXELS, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawBitmap(bitmap, rect, rect, paint);

        Paint borderPaint = new Paint();
        borderPaint.setStrokeWidth(BORDER_WIDTH_PIXELS);
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(Color.WHITE);
        borderPaint.setStyle(Paint.Style.STROKE);

        canvas.drawRoundRect(rectF, ROUNDED_PIXELS, ROUNDED_PIXELS, borderPaint);

        return output;
    }
}
