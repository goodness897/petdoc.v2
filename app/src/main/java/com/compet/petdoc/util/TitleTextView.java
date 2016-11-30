package com.compet.petdoc.util;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Mu on 2016-11-28.
 */

public class TitleTextView extends TextView {

    public TitleTextView(Context context) {
        super(context);
        setType(context);
    }

    public TitleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context);

    }

    public TitleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setType(context);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TitleTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setType(context);

    }

    private void setType(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "Billabong.ttf"));
    }

}
