package com.zamnadev.tortillinas.Firma;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class Canvas extends View {
    private Paint paint;

    private Path path;

    private boolean clearCanvas = false;

    private FirmaActivity activity;

    private boolean showBtnGuardar;

    public Canvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        path = new Path();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
        showBtnGuardar = false;
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
        if(clearCanvas) {
            path = new Path();
            Paint clearPaint = new Paint();
            clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawRect(0, 0, 0, 0, clearPaint);
            canvas.drawColor(Color.WHITE);
            clearCanvas = false;
        } else {
            canvas.drawPath(path, paint);
            if (showBtnGuardar) {
                activity.showBtnGuardar();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xPos = event.getX();
        float yPos = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                path.moveTo(xPos, yPos);
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                path.lineTo(xPos, yPos);
                showBtnGuardar = true;
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
            default: {
                return false;
            }
        }
        invalidate();
        return true;
    }

    public void clear() {
        clearCanvas = true;
        invalidate();
    }

    public void setFirmaActivity(FirmaActivity activity) {
        this.activity = activity;
    }
}