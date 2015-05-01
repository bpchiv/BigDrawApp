package edu.msu.chiversb.bigdrawingapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.util.AttributeSet;
import android.util.Log;
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
    private double centerLong = 0;
    private double centerLat = 0;
    private ArrayList<Location> locations = new ArrayList<Location>();
    private ArrayList<Integer> colors = new ArrayList<Integer>();
    private ArrayList<Float> penWidth = new ArrayList<Float>();

    private boolean firstDraw = true;
    private float y=(float)0.0;

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
        customPaint.setStrokeWidth(y);
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    public void addLocation(Location location){
        locations.add(location);
        colors.add(customPaint.getColor());
        penWidth.add(y);
        Log.i("location size", String.valueOf(locations.size()));
        invalidate();
    }

    /**
     * Set the current pen color
     * @param color pen color integer value
     */
    public void setColor(int color) {
        customPaint.setColor(color);
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

        if(firstDraw) {
            centerLong = locations.get(0).getLongitude();
            centerLat = locations.get(0).getLatitude();
            firstDraw = false;
        }
        double x = (width/360.0) * (180 + centerLong);
        double y = (height/180.0) * (90 - centerLat);

        double scaleX = x;
        double scaleY = y;

        for(int i = 1; i < locations.size(); i++) {

            customPaint.setColor(colors.get(i));
            customPaint.setStrokeWidth(penWidth.get(i));

            double dX = 0.0;
            double dY = 0.0;

            if(locations.get(i).getLongitude() > centerLong){
                dX = scaleX + (locations.get(i).getLongitude() - centerLong) * 1000000;
            }
            else
                dX = scaleX - (centerLong - locations.get(i).getLongitude()) * 1000000;

            if(locations.get(i).getLatitude() > centerLat){
                dY = scaleY + (locations.get(i).getLatitude() - centerLat) * 1000000;
            }
            else
                dY = scaleY - (centerLat - locations.get(i).getLatitude()) * 1000000;


            canvas.drawLine((float) x, (float) y, (float) dX, (float) dY, customPaint);
            x = dX;
            y = dY;
        }

    }

}
