package com.example.linesketch;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class SketchModel {

    ArrayList<SketchListener> subscribers;
    ArrayList<Line> lines;

    public SketchModel() {
        subscribers = new ArrayList<>();
        lines = new ArrayList<>();
    }

    public void addLine(Line l) {
        this.lines.add(l);
        notifySubscribers();
    }

    public Line returnSelectedLine(PointF p) {
        Line found  = null;
        for (Line l : lines) {
            if (l.checkifCloseToLine(p)) {
                found = l;
            }
        }
        return found;
    }

    public Line checkIfHitEndPointsOfLine(PointF p) {
        Line found  = null;
        for (Line l : lines) {
            if (l.isEndpoints(p, 20) != null) {
                found = l;
            }
        }
        return found;
    }

    public PointF checkIfPointCloseToAnotherLineEndpoints(PointF p, Line selectedLine) {
        ArrayList<PointF> found = new ArrayList<>();
        for (Line l : lines) {
            if(!l.equals(selectedLine)) {
                if (l.isEndpoints(p, 30) != null) {
                    found.add(l.isEndpoints(p,30));
                }
            }
        }

        return found.stream().findAny().orElse(null);
    }



    public void translateLine(Line line, PointF currentPoint, float dx, float dy) {
        line.move(currentPoint, dx, dy);
        notifySubscribers();
    }

    public void changeEndpoints(Line l, PointF currentPoint, PointF newPoint ) {
        l.changeEndpoints(currentPoint, newPoint);
        notifySubscribers();

    }

    public void rotateLine(Line l, double theta) {
        l.setRotation(theta);
        notifySubscribers();
    }

    public void scaleLine(Line l, double sx) {
        l.setScale(sx);
        notifySubscribers();
    }


    public void addSubscriber(SketchListener subscriber) {
        subscribers.add(subscriber);
    }

    private void notifySubscribers() {
        for (SketchListener sl : subscribers) {
            sl.modelChanged();
        }
    }
}
