package com.example.linesketch;


import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;

public class Line {

    PointF startPoint, endPoint, midPoint;
    PointF translateX, translateY;

    float length;
    float ratioA, ratioB, ratioC;
    float negThreshholdDistance;
    float posThreshholdDistance;
    double rotation; // rotation in radians
    double scale;

    public Line(PointF startPoint, PointF endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;

        length = distance(startPoint.x, startPoint.y, endPoint.x, endPoint.y);

        midPoint = new PointF((startPoint.x + endPoint.x)/2, (startPoint.y + endPoint.y)/2);



        recalculateRatios(startPoint, endPoint);

        negThreshholdDistance = -20;
        posThreshholdDistance = 20;

        rotation = 0;
        scale = 1.0;

       translateX = startPoint;
       translateY = endPoint;


    }

    public void recalculateRatios(PointF sp, PointF ep) {
        midPoint = new PointF((sp.x + ep.x)/2, (sp.y + ep.y)/2);
        length = distance(sp.x, sp.y, ep.x, ep.y);
        ratioA = (sp.y - ep.y) / length;
        ratioB = (ep.x - sp.x) / length;
        ratioC = -1 * ((sp.y - ep.y) * sp.x + (ep.x - sp.x) * sp.y) / length;
    }

    public float distance(float x1, float y1, float x2, float y2) {
        float ac = Math.abs(y2-y1);
        float cb = Math.abs(x2-x1);
        return (float)Math.hypot(ac, cb);
    }

    public boolean calculateIfLine(ArrayList<PointF> points) {
        float average = 0;

        for(int i = 0; i < points.size()-1; i++) {
            PointF current = points.get(i);
            PointF next = points.get(i+1);
            average = average + distance(current.x, current.y, next.x, next.y);
        }

        float threshold = distance(startPoint.x, startPoint.y, endPoint.x, endPoint.y) + 50;
        if (average < threshold ) {
            return true;
        }
        return false;
    }

    float distanceFromLine(float x, float y) {
        return ratioA * x + ratioB * y + ratioC;
    }

    public boolean checkifCloseToLine(PointF p) {
        float distance = distanceFromLine(p.x, p.y);
        if( distance < posThreshholdDistance && distance > negThreshholdDistance) {
            return true;
        }
        return false;
    }

    public void move(PointF currentPoint, float dx, float dy) {
        if(startPoint.equals(isEndpoints(currentPoint, 20))) {
            startPoint.x += dx;
            startPoint.y += dy;
            recalculateRatios(startPoint, endPoint);
        }
        else if (endPoint.equals(isEndpoints(currentPoint, 20))) {
            endPoint.x += dx;
            endPoint.y += dy;
            recalculateRatios(startPoint, endPoint);
        }
        Log.d("line class startpoint", String.valueOf(startPoint));
        Log.d("line class endoint", String.valueOf(endPoint));
    }

    public void changeEndpoints(PointF currentPoint, PointF newPoint) {
        if(startPoint.equals(isEndpoints(currentPoint, 20))) {
            startPoint.x  = newPoint.x;
            startPoint.y  = newPoint.y;
            recalculateRatios(startPoint, endPoint);
        }
        else if (endPoint.equals(isEndpoints(currentPoint, 20))) {
            endPoint.x  = newPoint.x;
            endPoint.y  = newPoint.y;
            recalculateRatios(startPoint, endPoint);
        }
    }

    public PointF isEndpoints(PointF p, float radius) {
        PointF found = null;
        if(p.x >= startPoint.x-radius && p.x <= startPoint.x+radius && p.y >= startPoint.y-radius && p.y <= startPoint.y+radius) {
            found = startPoint;
            return found;
        }
        else if(p.x >= endPoint.x-radius && p.x <= endPoint.x+radius && p.y >= endPoint.y-radius && p.y <= endPoint.y+radius) {
            found = endPoint;
            return found;
        }
        return found;
    }

    public void transForm() {

        //Scaling
        startPoint.x  = (float)(((startPoint.x - endPoint.x)/2) - ((length/2) * (scale/2)));
        startPoint.y = (float)(((startPoint.y - endPoint.y)/2) - ((length/2) * (scale/2)));;
        endPoint.x =  (float)(((startPoint.x - endPoint.x)/2) + ((length/2) * (scale/2)));
        endPoint.y =  (float)(((startPoint.y - endPoint.y)/2) + ((length/2) * (scale/2)));

//        // rotate
//
//        x1 = startPoint.x;
//        y1 = startPoint.y;
//        x2 = endPoint.x;
//        y2 = endPoint.y;
//
//        startPoint.x = (float)(Math.cos(rotation) * x1 - Math.sin(rotation) * y1);
//        startPoint.y = (float)(Math.sin(rotation) * x1 + Math.cos(rotation) * y1);
//        endPoint.x = (float)(Math.cos(rotation) * x2 - Math.sin(rotation) * y2);
//        endPoint.y = (float)(Math.sin(rotation) * x2 + Math.cos(rotation) * y2);
//
//        startPoint.x += translateX.x;
//        startPoint.y += translateX.y;
//        endPoint.x += translateY.x;
//        endPoint.y += translateY.y;


    }

    public void setRotation(double rot) {
        rotation = rot;
    }

    public void setScale(double scale) {

//        Log.d("BEFORE SCALE START POINT", String.valueOf(startPoint));
//        Log.d("BEFORE SCALE END POINT", String.valueOf(endPoint));
//
//        Log.d("BEFORE LENGTH", String.valueOf(length));

        if( scale < 1.0) {
            scale = -scale;
            Log.d("SCALE", String.valueOf(scale));
        }

        startPoint.x  = (float)(midPoint.x - (length/2 * scale));
        startPoint.y = (float)(midPoint.y - (length/2 * scale));
        endPoint.x =  (float)(midPoint.x + (length/2 * scale));
        endPoint.y =  (float)(midPoint.y + (length/2 * scale));
//        recalculateRatios(startPoint, endPoint);

//        Log.d("AFTER SCALE START POINT", String.valueOf(startPoint));
//        Log.d("AFTER SCALE END POINT", String.valueOf(endPoint));
//
//        float newLength = distance(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
//
//        Log.d("AFTER LENGTH", String.valueOf(newLength));
    }
}
