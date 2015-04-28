package edu.msu.chiversb.bigdrawingapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by bpchiv on 4/28/15.
 */
public class DrawView extends View{
    /**
     * Paint to use when drawing the custom color hat
     */
    private Paint customPaint;
    private double transLat = 0;
    private double transLong = 0;
    private ArrayList<Location> locations = new ArrayList<Location>();
    private int color = 0;
    private boolean firstDraw = true;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DrawView(Context context) {
        super(context);
        init(context);
    }

    /**
     * Initialize the view
     * @param context
     */
    private void init(Context context) {
        customPaint = new Paint();
        customPaint.setStyle(Paint.Style.STROKE);
        customPaint.setColor(Color.BLACK);
    }

    public void addLocation(Location location){
        locations.add(location);
        invalidate();
    }

    /**
     * Set the current pen color
     * @param color pen color integer value
     */
    public void setColor(int color) {
        this.color = color;
        customPaint.setColor(color);
        invalidate();
    }

    /**
     * Handle a draw event
     * @param canvas canvas to draw on.
     */
    @Override
    protected void onDraw(Canvas canvas){
        if(locations.isEmpty())
            return;
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        double x = (width/360.0) * (180 + locations.get(0).getLongitude());
        double y = (height/180.0) * (90 - locations.get(0).getLatitude());

        for(int i = 1; i < locations.size(); i++){
            double x2 = (width/360.0) * (180 + locations.get(i).getLongitude());
            double y2 = (height/180.0) * (90 - locations.get(i).getLatitude());
            canvas.drawLine((float)x, (float)y, (float)x2, (float)y2, customPaint);
            x = x2;
            y = y2;
        }
    }



}
