package com.example.assignment2;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class MyDrawingArea extends View {
    Path path = new Path();
    private int currentColor = Color.BLACK; // Default color is black

    //This Function Use to Set Color
    public void setCurrentColor(int color) {
        currentColor = color;
    }

    public int getCurrentColor() {
        return currentColor;
    }
    public MyDrawingArea(Context context) {
        super(context);
    }

    public MyDrawingArea(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyDrawingArea(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyDrawingArea(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setColor(currentColor);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5f);
/*
Note: Declare path somewhere outside onDraw
Path path = new Path();
*/
        //path.moveTo(100, 100);
        /*
        path.lineTo(150, 50);
        path.lineTo(200, 150);
        path.lineTo(250, 100);
        path.lineTo(300, 120);
        path.moveTo(120, 400);
        path.lineTo(170, 350);
        path.lineTo(220, 450);
        path.lineTo(270, 400);
        path.lineTo(320, 420);
        */
        canvas.drawPath(path, p);
        invalidate();


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX(), y = event.getY();
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            path.moveTo(x, y); //path is global. Same thing that onDraw uses.
        } else if (action == MotionEvent.ACTION_MOVE) {
            path.lineTo(x, y);
        }
        return true;
    }

    Bitmap bmp;

    public Bitmap getBitmap() {
        bmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        Paint p = new Paint();
        p.setColor(currentColor);
        p.setStyle(Paint.Style.STROKE);
        p.setAntiAlias(true);
        p.setStrokeWidth(5f);
        c.drawPath(path, p); //path is global. The very same thing that onDraw uses.
        return bmp;
    }


    public void clear() {
        path.reset();
    }

    public void setBitmap(Bitmap bitmap) {
        path.reset(); // Clear the existing drawing path
        path.addPath(createPathFromBitmap(bitmap)); // Convert the bitmap to a path
        invalidate(); // Redraw the view
    }
    private Path createPathFromBitmap(Bitmap bitmap) {
        Path path = new Path();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        boolean isDrawing = false; // To track whether we are currently drawing a path

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = bitmap.getPixel(x, y);
                if (pixel != Color.TRANSPARENT) {
                    if (!isDrawing) {
                        path.moveTo(x, y);
                        isDrawing = true;
                    } else {
                        path.lineTo(x, y);
                    }
                } else {
                    isDrawing = false;
                }
            }
        }

        return path;
    }


}
