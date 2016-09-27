package com.planktonspace.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Mercury on 9/21/2016 AD.
 */

public class SignatureView extends View {


    private boolean mIsReadOnly = false;
    private float mStrokeWidth = 5;
    private int mPaintColor = Color.BLACK;
    private int mBackgroundColor = Color.WHITE;
    private int mLastHeight;
    private int mLastWidth;

    private Bitmap mCanvasBitmap;


    private Path mDrawPath;
    private Paint mDrawPaint, mCanvasPaint;

    private Canvas mDrawCanvas;

    public SignatureView(Context context) {
        super(context);
        setupDrawing();
    }

    public SignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }


    public SignatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupDrawing();
    }

    @TargetApi(21)
    public SignatureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupDrawing();
    }

    public Bitmap getCanvasBitmap() {
        return mCanvasBitmap;
    }

    public void setCanvasBitmap(Bitmap mCanvasBitmap) {
        this.mCanvasBitmap = mCanvasBitmap;
    }

    public void setStrokeWidth(float mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
    }

    public void setPaintColor(int mPaintColor) {
        this.mPaintColor = mPaintColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mCanvasBitmap, 0, 0, mCanvasPaint);
        canvas.drawPath(mDrawPath, mDrawPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLastHeight = h;
        mLastWidth = w;

        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvasBitmap.eraseColor(mBackgroundColor);
        mDrawCanvas = new Canvas();

        mDrawCanvas.drawColor(mBackgroundColor, PorterDuff.Mode.CLEAR);
        mDrawCanvas.setBitmap(mCanvasBitmap);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mIsReadOnly) {
            return false;
        }

        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDrawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                mDrawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                mDrawCanvas.drawPath(mDrawPath, mDrawPaint);
                mDrawPath.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;

    }


    private void setupDrawing() {
        mDrawPath = new Path();
        mDrawPaint = new Paint();

        mDrawPaint.setColor(mPaintColor);


        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setStrokeWidth(mStrokeWidth);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);

        mCanvasPaint = new Paint(Paint.DITHER_FLAG);



    }

    public void clearCanvas() {

        mDrawCanvas.drawColor(mBackgroundColor, PorterDuff.Mode.CLEAR);
        mCanvasBitmap.eraseColor(mBackgroundColor);
        invalidate();
    }


}
