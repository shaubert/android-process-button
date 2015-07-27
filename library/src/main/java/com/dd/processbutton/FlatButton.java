package com.dd.processbutton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.Button;

public class FlatButton extends Button {

    private Drawable mDrawable;
    private float cornerRadius;
    private BackgroundBuilder backgroundBuilder;

    private boolean hasSavedText;
    private String mSavedText;

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
        backgroundBuilder = new BackgroundBuilder(context);
        if (attrs != null) {
            initAttributes(attrs);
        } else {
            mDrawable = getBackground();
        }

        mSavedText = getText().toString();
        setBackgroundCompat(mDrawable);
    }

    private void initAttributes(AttributeSet attributeSet) {
        TypedArray attr = backgroundBuilder.getTypedArray(attributeSet, R.styleable.FlatButton);
        if (attr == null) {
            return;
        }

        try {
            float defValue = backgroundBuilder.getDimension(R.dimen.corner_radius);
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

    protected void saveTextIfNotYet() {
        if (!hasSavedText) {
            hasSavedText = true;
            mSavedText = getText().toString();
        }
    }

    protected void restoreText() {
        if (hasSavedText) {
            setText(mSavedText);

            hasSavedText = false;
            mSavedText = null;
        }
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
        BackgroundBuilder.setBackgroundCompat(this, drawable);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.savedText = mSavedText;
        savedState.hasSavedText = hasSavedText;

        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mSavedText = savedState.savedText;
        hasSavedText = savedState.hasSavedText;
    }

    static class SavedState implements Parcelable {
        public static final SavedState EMPTY_STATE = new SavedState() {
        };

        private String savedText;
        private boolean hasSavedText;

        // This keeps the parent(RecyclerView)'s state
        Parcelable superState;

        SavedState() {
            superState = null;
        }

        protected SavedState(Parcelable superState) {
            this.superState = superState != EMPTY_STATE ? superState : null;
        }

        protected SavedState(Parcel in) {
            // Parcel 'in' has its parent(RecyclerView)'s saved state.
            // To restore it, class loader that loaded RecyclerView is required.
            Parcelable superState = in.readParcelable(getClass().getClassLoader());
            this.superState = superState != null ? superState : EMPTY_STATE;

            savedText = in.readString();
            hasSavedText = in.readInt() > 0;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeParcelable(superState, flags);

            out.writeString(savedText);
            out.writeInt(hasSavedText ? 1 : 0);
        }

        public Parcelable getSuperState() {
            return superState;
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
