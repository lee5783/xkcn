package com.thule.xkcn.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.TextView;

import com.thule.xkcn.R;

public class XKOutlineTextView extends TextView {

    private boolean stroke = false;
    private float strokeWidth = 0.0f;
    private int strokeColor;

    public XKOutlineTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initView(context, attrs);
    }

    public XKOutlineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(context, attrs);
    }

    public XKOutlineTextView(Context context) {
        super(context);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.XKOutlineTextView);
        stroke = a.getBoolean(R.styleable.XKOutlineTextView_textStroke, false);
        strokeWidth = a.getFloat(R.styleable.XKOutlineTextView_textStrokeWidth, 0.0f);
        strokeColor = a.getColor(R.styleable.XKOutlineTextView_textStrokeColor, 0xffffffff);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (stroke) {
            ColorStateList states = getTextColors();
            getPaint().setStyle(Style.STROKE);
            getPaint().setStrokeWidth(strokeWidth);
            setTextColor(strokeColor);
            super.onDraw(canvas);

            getPaint().setStyle(Style.FILL);
            setTextColor(states);
        }

        super.onDraw(canvas);
    }
}  