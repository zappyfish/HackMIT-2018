package com.example.liamkelly.drawingbuddy;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class CanvasView extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private Context mContext;
    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    float mScaleFactorX, mScaleFactorY;

    private double mCurEnergy = 0;
    private boolean isDrawing = true;

    private int mStepSize;

    public CanvasView(Context context, int stepSize) {
        super(context);
        mContext = context;
        mStepSize = stepSize;
        init(null, 0);
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs, 0);
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(attrs, defStyle);
    }

    private void drawPoints(Canvas canvas, int stepSize) {
        List<int[]> pts = ImageStateManager.getInstance(mContext).getPoints(stepSize);
        for (int[] pt : pts) {
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            canvas.drawCircle(pt[0], pt[1], 6, paint);
        }
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        canvas.drawCircle(pts.get(0)[0], pts.get(0)[1], 12, paint);
    }

    private void drawUserPoints(Canvas canvas) {
        List<int[]> pts = ImageStateManager.getInstance(mContext).getUserPoints();
        int avg = energyToColor(ImageStateManager.getInstance(mContext).getAverageEnergy());
        Paint aP = new Paint();
        aP.setColor(avg);
        for (int i = 0; i < pts.size(); i++) {
            int[] pt = pts.get(i);
            canvas.drawCircle(pt[0], pt[1], 6, aP);
            if (i > 0) {
                int[] nxtpt = pts.get(i-1);
                canvas.drawLine(nxtpt[0], nxtpt[1], pt[0], pt[1], aP);
            }
        }
        if (pts.size() > 0) {
            Paint curPaint = new Paint();
            curPaint.setColor(energyToColor(mCurEnergy));
            int len = pts.size() - 1;
            canvas.drawCircle(pts.get(len)[0], pts.get(len)[1], 20, curPaint);
        }
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CanvasView, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.CanvasView_exampleString);
        mExampleColor = a.getColor(
                R.styleable.CanvasView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.CanvasView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.CanvasView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.CanvasView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawPoints(canvas, mStepSize);
        drawUserPoints(canvas);
        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;


        // Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }

    @Override
    public boolean onTouchEvent (MotionEvent e) {
        int x, y;
        if (isDrawing) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int)e.getX();
                    y = (int)e.getY();
                    mCurEnergy = ImageStateManager.getInstance(mContext).getEnergy(x, y);
                    invalidate(); // add it here
                    break;
                case MotionEvent.ACTION_MOVE:
                    x = (int)e.getX();
                    y = (int)e.getY();
                    mCurEnergy = ImageStateManager.getInstance(mContext).getEnergy(x, y);
                    invalidate(); // add it here
                    break;
                case MotionEvent.ACTION_UP:
                    isDrawing = false;
                    sendToDatabaseWhenDone();
                    invalidate(); // add it here
                    break;
            }
        }
        return true;
    }

    private int energyToColor(double energy) {
        int blue = 0, green = 255, red = 0;
        green -= (energy * 10);
        red += (energy * 10);
        if (green < 0) green = 0;
        if (red > 255) red = 255;
        return 0xFF000000 | (((red & 0xff) << 16) | ((green & 0xff) << 8) | (blue & 0xff));
    }

    private void sendToDatabaseWhenDone() {
        //
        // img_name:
        // { 1:
        //  {   avg_energy: l23,
        //      time_to_draw: 123
        //      img : hexstring
        //      difficulty_score: 0.5
        //  }
        // }
    }
}
