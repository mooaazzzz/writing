package com.moaz.writing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

public class CanvasView extends View {


    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    private Paint mPaint;
    public boolean checkTouch;
    private int penColor;

    public CanvasView(Context context, Boolean checkTouch,int penColor) {
        super(context);

        this.checkTouch = checkTouch;
        this.penColor = penColor;
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        int myColor = context.getResources().getColor(penColor);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(myColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(8);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.TRANSPARENT);


    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (checkTouch) {
            canvas.drawBitmap(mBitmap, 0, 0, mPaint);
            canvas.drawPath(mPath, mPaint);
        }
    }
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }
    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw
        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                checkTouch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                checkTouch = true;
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                checkTouch = true;
                break;
                default:
                    checkTouch = false;
                    break;
        }
        return true;
    }

    public void clear(){
        mBitmap.eraseColor(Color.TRANSPARENT);
        invalidate();
        checkTouch = false;
        System.gc();
    }
}
