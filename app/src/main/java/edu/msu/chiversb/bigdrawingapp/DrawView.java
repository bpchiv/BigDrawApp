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
        customPaint.setStrokeWidth((float)3.0);
    }

    public void addLocation(Location location){
        locations.add(location);
        Log.i("location size", String.valueOf(locations.size()));
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

        double centerLong = locations.get(0).getLongitude();
        double centerLat = locations.get(0).getLatitude();

        double x = (width/360.0) * (180 + locations.get(0).getLongitude());
        double y = (height/180.0) * (90 - locations.get(0).getLatitude());

        double scaleX = x;
        double scaleY = y;

        for(int i = 1; i < locations.size(); i++) {
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


        // old version; just in case

        /*if(locations.isEmpty())
            return;
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        double x = (width/360.0) * (180 + locations.get(0).getLongitude());
        double y = (height/180.0) * (90 - locations.get(0).getLatitude());

        double scaleX = x;
        double scaleY = y;

        for(int i = 1; i < locations.size(); i++){

            double x2 = (width/360.0) * (180 + locations.get(i).getLongitude());
            double y2 = (height/180.0) * (90 - locations.get(i).getLatitude());

            double saveX2 = x2;
            double saveY2 = y2;


            if (locations.get(i).getLongitude() > locations.get(i-1).getLongitude())
                x2 = x2 - ((x2 - scaleX) * 1000000);
            else
                x2 = x2 + ((scaleX - x2) * 1000000);

            if (locations.get(i).getLatitude() > locations.get(i-1).getLatitude())
                y2 = y2 - ((y2 - scaleY) * 1000000);
            else
                y2 = y2 + ((scaleY - y2) * 1000000);

            canvas.drawLine((float)x, (float)y, (float)x2, (float)y2, customPaint);
            x = x2;
            y = y2;

            scaleX = saveX2;
            scaleY = saveY2;
        }*/
    }



}
