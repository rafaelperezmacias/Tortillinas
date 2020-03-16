package com.zamnadev.tortillinas.Firma;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class Canvas extends View {
    private Paint paint;

    private Path path;

    private boolean clearCanvas = false;

    public Canvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        path = new Path();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
        if(clearCanvas) {
            path = new Path();
            Paint paint2 = new Paint();
            paint2.setAlpha(0);
            canvas.drawPath(path, paint2);
            clearCanvas = false;
        } else {
            canvas.drawPath(path, paint);
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
}