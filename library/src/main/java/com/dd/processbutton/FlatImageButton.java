package com.dd.processbutton;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class FlatImageButton extends ImageButton {

    private Drawable mDrawable;
    private float cornerRadius;
    private BackgroundBuilder backgroundBuilder;

    public FlatImageButton(Context context) {
        super(context);
        init(context, null);
    }

    public FlatImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FlatImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlatImageButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs) {
        backgroundBuilder = new BackgroundBuilder(context);
        if (attrs != null) {
            initAttributes(attrs);
        } else {
            mDrawable = getBackground();
        }

        setBackgroundCompat(mDrawable);
    }

    private void initAttributes(AttributeSet attributeSet) {
        TypedArray attr = backgroundBuilder.getTypedArray(attributeSet, R.styleable.FlatButton);
        if (attr == null) {
            return;
        }

        try {
            float defValue = backgroundBuilder.getDimension(R.dimen.pb_library_corner_radius);
            cornerRadius = attr.getDimension(R.styleable.FlatButton_pb_cornerRadius, defValue);
            mDrawable = backgroundBuilder.createBackground(attributeSet);
        } finally {
            attr.recycle();
        }
    }


    public float getCornerRadius() {
        return cornerRadius;
    }

    public Drawable getNormalDrawable() {
        return mDrawable;
    }

    protected float getDimension(int id) {
        return backgroundBuilder.getDimension(id);
    }

    @SuppressLint("NewApi")
    protected void setColor(GradientDrawable drawable, TypedArray attr, int index, int defaultColor) {
        BackgroundBuilder.setColor(drawable, attr, index, defaultColor);
    }

    protected ColorStateList getColor(TypedArray attr, int index, int defaultColor) {
        return BackgroundBuilder.getColor(attr, index, defaultColor);
    }

    protected int getColor(int id) {
        return backgroundBuilder.getColor(id);
    }

    protected TypedArray getTypedArray(AttributeSet attributeSet, int[] attr) {
        return backgroundBuilder.getTypedArray(attributeSet, attr);
    }

    protected Drawable getDrawable(int id) {
        return backgroundBuilder.getDrawable(id);
    }

    /**
     * Set the View's background. Masks the API changes made in Jelly Bean.
     *
     * @param drawable
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public void setBackgroundCompat(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

}
