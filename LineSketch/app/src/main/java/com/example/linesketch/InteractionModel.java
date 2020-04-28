package com.example.linesketch;

import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;

public class InteractionModel {

    ArrayList<PointF> points;
    ArrayList<SketchListener> subscribers;
    ArrayList<Line> selectedLine;

    public InteractionModel() {
        points = new ArrayList<>();
        subscribers = new ArrayList<>();
        selectedLine = new ArrayList<>();
    }

    public void addPoints(PointF p) {
        this.points.add(p);
        notifySubscribers();

    }

    public void setSelectedLine(Line l) {
        this.selectedLine.add(l);
        Log.d("StartPoint", String.valueOf(l.startPoint));
        Log.d("Midpoint", String.valueOf(l.midPoint));
        Log.d("Endpoint", String.valueOf(l.endPoint));
        notifySubscribers();
    }

    public Line getSelectedLine() {
        return selectedLine.stream().findFirst().get();
    }

    public void removeSelectedLine() {
        this.selectedLine = new ArrayList<>();
        notifySubscribers();
    }

    public ArrayList<PointF> getPoints() {
        return this.points;
    }

    public void removePoints() {
        this.points = new ArrayList<>();
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
