package com.example.linesketch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Shader;
import android.view.View;


public class SketchView extends View implements SketchListener {

    Paint paint;
    SketchController controller;
    InteractionModel iModel;
    SketchModel model;

    public SketchView(Context aContext) {
        super(aContext);
        paint = new Paint();
        setBackgroundColor(Color.BLACK);
    }

    public void setModel(SketchModel newModel) {
        model = newModel;
    }

    public void setController(SketchController newController) {
        controller = newController;
        this.setOnTouchListener(newController);
    }

    public void setInteractionModel(InteractionModel newIModel){
        iModel = newIModel;
    }

    @Override
    protected void onDraw(Canvas c) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10.0f);
        paint.setColor(Color.GRAY);

        if (!iModel.points.isEmpty()) {
            boolean first = true;
            Path path = new Path();
            for (PointF p : iModel.points) {
                if (first) {
                    first = false;
                    path.moveTo(p.x, p.y);
                } else {
                    path.lineTo(p.x, p.y);
                }
            }
            c.drawPath(path, paint);
        }

        if(!model.lines.isEmpty()) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(40.0f);
            paint.setColor(Color.GREEN);
            paint.setAlpha(30);

            for(Line l: model.lines) {
                c.drawLine(l.startPoint.x, l.startPoint.y, l.endPoint.x, l.endPoint.y, paint);
            }

            paint.setStrokeWidth(8.0f);
            paint.setAlpha(150);

            for(Line l: model.lines) {
                c.drawLine(l.startPoint.x, l.startPoint.y, l.endPoint.x, l.endPoint.y, paint);
            }

        }

        if(!iModel.selectedLine.isEmpty()) {
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setStrokeWidth(8.0f);
            paint.setColor(Color.YELLOW);

            if(iModel.selectedLine.size() > 1) {
                for(Line l: iModel.selectedLine) {
                    c.drawLine(l.startPoint.x, l.startPoint.y, l.endPoint.x, l.endPoint.y, paint);
                }
            } else {
                for(Line l: iModel.selectedLine) {
                    c.drawLine(l.startPoint.x, l.startPoint.y, l.endPoint.x, l.endPoint.y, paint);
                    c.drawCircle(l.startPoint.x, l.startPoint.y ,10, paint);

                    c.drawCircle(l.endPoint.x, l.endPoint.y,10, paint);
                }
            }

        }
    }

    public void modelChanged(){
        this.invalidate();
    }

}
