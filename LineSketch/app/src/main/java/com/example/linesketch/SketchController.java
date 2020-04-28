package com.example.linesketch;

import android.graphics.PointF;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class SketchController implements View.OnTouchListener{

    SketchModel model;
    InteractionModel iModel;
    private Handler handler = new Handler();
    private Runnable longPressCheck;

    private enum State { READY, PENDING, DRAGGING, CREATING}

    private State currentState = State.READY;

    PointF startPoint, endPoint;

    Line currentSelected;

    private float normX, normY;
    private float normDX, normDY;
    private float prevNormX, prevNormY;

    public SketchController() {
        startPoint = new PointF(0,0);
        endPoint = new PointF(0,0);
        prevNormX = 0;
        prevNormY = 0;

        longPressCheck = () -> {
            // call a method to deal with the callback "event"
            checkForLongPress();
        };

    }

    public void setModel(SketchModel aModel) {
        model = aModel;
    }

    public void setIModel(InteractionModel im) {
        iModel = im;
    }

    public boolean onTouch(View v, MotionEvent event) {

        switch(currentState) {

            case READY:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startPoint = new PointF(event.getX(), event.getY());
                    prevNormX = startPoint.x;
                    prevNormY = startPoint.y;
                    iModel.addPoints(startPoint);
                    handler.postDelayed(longPressCheck, 1000);
                    currentState = State.PENDING;
                }
                break;

            case PENDING:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        handler.removeCallbacks(longPressCheck);
                        if (!iModel.selectedLine.isEmpty()
                                && iModel.selectedLine.stream().anyMatch(l -> (l.isEndpoints(startPoint, 20) != null))
                                && iModel.selectedLine.size() == 1 ) {
                            currentSelected = model.checkIfHitEndPointsOfLine(startPoint);
                            currentState = State.DRAGGING;
                        } else {
                            currentState= State.CREATING;
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacks(longPressCheck);
                        PointF p = new PointF(event.getX(), event.getY());
                        if(startPoint.equals(p)) {
                            Line line = model.returnSelectedLine(p);
                            if(line != null) {
                                iModel.removeSelectedLine();
                                iModel.setSelectedLine(line);

                            } else {
                                iModel.removeSelectedLine();
                            }
                            iModel.removePoints();
                        }
                        currentState = State.READY;
                        break;
                }
                break;

            case DRAGGING:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        startPoint = currentSelected.isEndpoints(startPoint, 20);
                        normX = event.getX();
                        normY = event.getY();
                        normDX = normX - prevNormX;
                        normDY = normY - prevNormY;
                        prevNormX = normX;
                        prevNormY = normY;

                        iModel.removePoints();
                        model.translateLine(currentSelected, startPoint, normDX, normDY);
                        currentState = State.DRAGGING;
                        break;

                    case MotionEvent.ACTION_UP:

                        PointF foundByStartPoint = model.checkIfPointCloseToAnotherLineEndpoints(iModel.getSelectedLine().startPoint, iModel.getSelectedLine());
                        PointF foundByEndPoint = model.checkIfPointCloseToAnotherLineEndpoints(iModel.getSelectedLine().endPoint, iModel.getSelectedLine());

                        if(foundByStartPoint != null && !foundByStartPoint.equals(iModel.getSelectedLine().startPoint)) {
                            model.changeEndpoints(iModel.getSelectedLine(), iModel.getSelectedLine().startPoint, foundByStartPoint);
                        }
                        else if(foundByEndPoint !=  null && !foundByEndPoint.equals(iModel.getSelectedLine().length)) {
                            model.changeEndpoints(iModel.getSelectedLine(), iModel.getSelectedLine().endPoint, foundByEndPoint);
                        }
                        currentState = State.READY;
                }
                break;

            case CREATING:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        PointF p = new PointF(event.getX(), event.getY());
                        iModel.addPoints(p);
                        currentState = State.CREATING;
                        break;

                    case MotionEvent.ACTION_UP:
                        endPoint = new PointF(event.getX(), event.getY());
                        iModel.addPoints(endPoint);
                        Line line = new Line(startPoint, endPoint);

                        if (!line.calculateIfLine(iModel.getPoints())) {
                            iModel.removePoints();
                            currentState = State.READY;
                            break;
                        }
                        iModel.removePoints();
                        iModel.removeSelectedLine();
                        model.addLine(line);
                        currentState = State.READY;
                        break;
                }
                break;

        }
        return true;
    }

    private void checkForLongPress() {
        switch (currentState) {
            case PENDING:
                    Line line = model.returnSelectedLine(startPoint);
                    if(line != null) {
                        iModel.setSelectedLine(line);
                    }
                    iModel.removePoints();
                }
                currentState = State.READY;
        }

    public void changeScale(double newSx) {

        double value = 0.5 + (1.5*newSx)/100;
        Log.d("SCALE", String.valueOf(value));
        if(!iModel.selectedLine.isEmpty()) {
            model.scaleLine(iModel.getSelectedLine(), value);
        }

    }

    public void  changeRotation(int value) {
        double theta = (4*Math.PI*value)/100;
        Log.d("ROT", String.valueOf(theta));
        if(!iModel.selectedLine.isEmpty()) {
            model.rotateLine(iModel.getSelectedLine(), theta);
        }
    }

}
