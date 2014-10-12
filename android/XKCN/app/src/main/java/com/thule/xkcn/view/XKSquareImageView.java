package com.thule.xkcn.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.thule.xkcn.util.XKUtils;

public class XKSquareImageView extends ImageView
{

	private int ROUNDED_RADIUS = 0;
	private int BODER_WIDTH = 3;
	private Paint _bitmapPaint;
	private Paint _borderPaint;

    private Bitmap _bitmap;

	public XKSquareImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	public XKSquareImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public XKSquareImageView(Context context)
	{
		super(context);
		init();
	}

	void init()
	{
		ROUNDED_RADIUS = XKUtils.convertDpToPixel(getContext(), 8);
		BODER_WIDTH = XKUtils.convertDpToPixel(getContext(), 2);

		_bitmapPaint = new Paint();
		_bitmapPaint.setAntiAlias(true);

		_borderPaint = new Paint();
		_borderPaint.setAntiAlias(true);
		_borderPaint.setStyle(Style.STROKE);
		_borderPaint.setStrokeWidth(BODER_WIDTH);
		_borderPaint.setColor(Color.WHITE);
	}

	@Override
	protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec)
	{
		final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		setMeasuredDimension(width, width);
	}

	@Override
	protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh)
	{
		super.onSizeChanged(w, w, oldw, oldh);
	}

    @Override
    public void setImageBitmap(Bitmap bm) {
        _bitmap = null;
        super.setImageBitmap(bm);
    }

    @SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas)
	{
        if(_bitmap==null)
        {
            Drawable drawable = getDrawable();
            if (drawable != null && drawable instanceof BitmapDrawable)
            {
                Bitmap origialBitmap = Bitmap.createBitmap(((BitmapDrawable) drawable).getBitmap());
                if (origialBitmap != null)
                {
                    _bitmap = getCroppedBitmap(origialBitmap);
                    origialBitmap.recycle();
                    origialBitmap = null;
                }
            }
            else
            {
                super.onDraw(canvas);
            }
        }

        if (_bitmap!=null)
        {
            canvas.drawBitmap(_bitmap, getMatrix(), _bitmapPaint);

            canvas.drawRoundRect(new RectF(BODER_WIDTH / 2, BODER_WIDTH / 2, getWidth() - BODER_WIDTH / 2, getHeight()
                    - BODER_WIDTH / 2), ROUNDED_RADIUS, ROUNDED_RADIUS, _borderPaint);
        }
        else
        {
            super.onDraw(canvas);
        }
	}

	/**
	 * Crops a circle out of the thumbnail photo.
	 */
	public Bitmap getCroppedBitmap(Bitmap bitmap)
	{
		Bitmap output = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);

		final Rect rect = new Rect(BODER_WIDTH / 2, BODER_WIDTH / 2, getWidth() - BODER_WIDTH / 2, getHeight() - BODER_WIDTH
				/ 2);

		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		paint.setAntiAlias(true);

		canvas.drawRoundRect(new RectF(rect), ROUNDED_RADIUS, ROUNDED_RADIUS, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, getImageMatrix(), paint);

		return output;
	}
}
