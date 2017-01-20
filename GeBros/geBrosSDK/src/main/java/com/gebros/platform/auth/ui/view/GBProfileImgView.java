package com.gebros.platform.auth.ui.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.gebros.platform.internal.JR;

/**
 * Created by jce_platform on 2016. 5. 31..
 */
public class GBProfileImgView extends View {

    public GBProfileImgView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public GBProfileImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public GBProfileImgView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(getResources().getColor(JR.color("BlackEdit")));
        paint.setAntiAlias(true);
        float fr = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
        RectF rectf = new RectF(0, 0, fr, fr);
        canvas.drawArc(rectf, 20, 140, false, paint);

        super.onDraw(canvas);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
