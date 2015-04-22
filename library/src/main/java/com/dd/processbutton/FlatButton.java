package com.dd.processbutton;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.*;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

public class FlatButton extends Button {

    private Drawable mDrawable;
    private CharSequence mNormalText;
    private float cornerRadius;

    public FlatButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public FlatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FlatButton(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            initAttributes(context, attrs);
        } else {
            mDrawable = getBackground();
        }

        mNormalText = getText().toString();
        setBackgroundCompat(mDrawable);
    }

    private void initAttributes(Context context, AttributeSet attributeSet) {
        TypedArray attr = getTypedArray(context, attributeSet, R.styleable.FlatButton);
        if (attr == null) {
            return;
        }

        try {
            float defValue = getDimension(R.dimen.corner_radius);
            cornerRadius = attr.getDimension(R.styleable.FlatButton_pb_cornerRadius, defValue);

            if (Build.VERSION.SDK_INT >= 21) {
                setupDrawableV21(attr);
            } else {
                setupDrawable(attr);
            }
        } finally {
            attr.recycle();
        }
    }

    private void setupDrawable(TypedArray attr) {
        StateListDrawable stateListDrawable = new StateListDrawable();

        stateListDrawable.addState(new int[]{-android.R.attr.state_enabled},
                createDisabledDrawable(attr));
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed},
                createPressedDrawable(attr));
        stateListDrawable.addState(new int[]{android.R.attr.state_focused},
                createPressedDrawable(attr));
        stateListDrawable.addState(new int[]{android.R.attr.state_selected},
                createPressedDrawable(attr));
        stateListDrawable.addState(new int[]{},
                createNormalDrawable(attr));

        mDrawable = stateListDrawable;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupDrawableV21(TypedArray attr) {
        StateListDrawable stateListDrawable = new StateListDrawable();

        stateListDrawable.addState(new int[]{-android.R.attr.state_enabled},
                createDisabledDrawable(attr));
        stateListDrawable.addState(new int[]{},
                createNormalDrawable(attr));

        int blueDark = getColor(R.color.blue_pressed);
        ColorStateList color = getColor(attr, R.styleable.FlatButton_pb_colorPressed, blueDark);

        mDrawable = new RippleDrawable(color, stateListDrawable, new ColorDrawable(Color.WHITE));
    }

    private Drawable createDisabledDrawable(TypedArray attr) {
        GradientDrawable drawableDisabled =
                (GradientDrawable) getDrawable(R.drawable.rect_disabled).mutate();
        drawableDisabled.setCornerRadius(getCornerRadius());

        int blueDisabled = getColor(R.color.blue_disabled);
        setColor(drawableDisabled, attr, R.styleable.FlatButton_pb_colorPressed, blueDisabled);

        return drawableDisabled;
    }

    protected void setColor(GradientDrawable drawable, TypedArray attr, int index, int defaultColor) {
        ColorStateList stateList = getColor(attr, index, defaultColor);
        if (Build.VERSION.SDK_INT >= 21) {
            drawable.setColor(stateList);
        } else {
            drawable.setColor(stateList.getDefaultColor());
        }
    }

    protected ColorStateList getColor(TypedArray attr, int index, int defaultColor) {
        ColorStateList result = null;
        if (attr.hasValue(index)) {
            result = attr.getColorStateList(index);
        }

        if (result == null) {
            result = ColorStateList.valueOf(defaultColor);
        }

        return result;
    }

    private Drawable createNormalDrawableV21(TypedArray attr) {
        GradientDrawable drawableNormal =
                (GradientDrawable) getDrawable(R.drawable.rect_normal).mutate();
        drawableNormal.setCornerRadius(getCornerRadius());
        int blueNormal = getColor(R.color.blue_normal);
        setColor(drawableNormal, attr, R.styleable.FlatButton_pb_colorNormal, blueNormal);
        return drawableNormal;
    }

    private Drawable createNormalDrawable(TypedArray attr) {
        if (Build.VERSION.SDK_INT >= 21) {
            return createNormalDrawableV21(attr);
        }

        LayerDrawable drawableNormal =
                (LayerDrawable) getDrawable(R.drawable.rect_normal).mutate();

        GradientDrawable drawableTop =
                (GradientDrawable) drawableNormal.getDrawable(0).mutate();
        drawableTop.setCornerRadius(getCornerRadius());

        int blueDark = getColor(R.color.blue_pressed);
        setColor(drawableTop, attr, R.styleable.FlatButton_pb_colorPressed, blueDark);

        GradientDrawable drawableBottom =
                (GradientDrawable) drawableNormal.getDrawable(1).mutate();
        drawableBottom.setCornerRadius(getCornerRadius());

        int blueNormal = getColor(R.color.blue_normal);
        setColor(drawableBottom, attr, R.styleable.FlatButton_pb_colorNormal, blueNormal);
        return drawableNormal;
    }

    private Drawable createPressedDrawable(TypedArray attr) {
        GradientDrawable drawablePressed =
                (GradientDrawable) getDrawable(R.drawable.rect_pressed).mutate();
        drawablePressed.setCornerRadius(getCornerRadius());

        int blueDark = getColor(R.color.blue_pressed);
        setColor(drawablePressed, attr, R.styleable.FlatButton_pb_colorPressed, blueDark);

        return drawablePressed;
    }

    protected Drawable getDrawable(int id) {
        return getResources().getDrawable(id);
    }

    protected float getDimension(int id) {
        return getResources().getDimension(id);
    }

    protected int getColor(int id) {
        return getResources().getColor(id);
    }

    protected TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    public Drawable getNormalDrawable() {
        return mDrawable;
    }

    public CharSequence getNormalText() {
        return mNormalText;
    }

    /**
     * Set the View's background. Masks the API changes made in Jelly Bean.
     *
     * @param drawable
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public void setBackgroundCompat(Drawable drawable) {
        int pL = getPaddingLeft();
        int pT = getPaddingTop();
        int pR = getPaddingRight();
        int pB = getPaddingBottom();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
        setPadding(pL, pT, pR, pB);
    }
}
