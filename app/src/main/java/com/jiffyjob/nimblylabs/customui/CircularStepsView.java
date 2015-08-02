package com.jiffyjob.nimblylabs.customui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.jiffyjob.nimblylabs.app.R;

/**
 * Created by NimblyLabs on 2/4/2015.
 */
public class CircularStepsView extends View {
    public CircularStepsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //paint object for drawing in onDraw
        circlePaint = new Paint();
        //get the attributes specified in attrs.xml using the name we included
        TypedArray styleTypedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleView, 0, 0);

        try {
            //get the text and colors specified using the names in attrs.xml
            circleText = styleTypedArray.getString(R.styleable.CircleView_circleLabel);
            circleColor = styleTypedArray.getInteger(R.styleable.CircleView_circleColor, 0);//0 is default
            labelColor = styleTypedArray.getInteger(R.styleable.CircleView_labelColor, 0);
        } finally {
            styleTypedArray.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        //get 15th of the width and height as we are working with a circle
        viewWidth = size.x / 15;
        viewHeight = size.y / 15;

        //get the radius as minimum of the width or height, whichever is smaller
        //subtract 5 so that it has some space around it
        int radius = Math.min(viewWidth, viewHeight) - 5;

        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setAntiAlias(true);

        //set the paint color using the circle color specified
        circlePaint.setColor(circleColor);

        if (widthMode == MeasureSpec.AT_MOST) {
            canvas.drawCircle(radius, radius, radius, circlePaint);
        } else {
            canvas.drawCircle(widthSize / 2, widthSize / 2, radius, circlePaint);
        }
        //set the text color using the color specified
        circlePaint.setColor(labelColor);
        //set text properties
        circlePaint.setTextAlign(Paint.Align.CENTER);
        circlePaint.setTextSize(40);
        if (widthMode == MeasureSpec.AT_MOST) {
            canvas.drawText(circleText, radius, radius + 12, circlePaint);
        } else {
            //draw the text using the string attribute and chosen properties
            canvas.drawText(circleText, widthSize / 2, (widthSize / 2) + 12, circlePaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        //get 15th of the width and height as we are working with a circle
        int desiredWidth = size.x / 15;
        int desiredHeight = size.y / 15;

        //minimum of (desiredWidth, desiredHeight) is radius of circle
        //multiple of 2 to get desiredWidth
        desiredWidth = (Math.min(desiredWidth, desiredHeight) - 5) * 2;

        widthMode = MeasureSpec.getMode(widthMeasureSpec);
        widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int width = 100;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, width);
    }

    public String getCircleText() {
        return circleText;
    }

    public void setCircleText(String circleText) {
        this.circleText = circleText;
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        String colorStr = getResources().getString(circleColor);
        this.circleColor = Color.parseColor(colorStr);
        this.postInvalidate();
    }

    public int getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
    }

    //determine the mode of the view, wrap_content or match_parent
    private int widthMode;
    //width and height of view
    private int viewWidth;
    private int viewHeight;
    private int widthSize;
    //circle and text colors
    private int circleColor, labelColor;
    //label text
    private String circleText;
    //paint for drawing custom view
    private Paint circlePaint;
}
