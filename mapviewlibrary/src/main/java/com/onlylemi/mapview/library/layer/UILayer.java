package com.onlylemi.mapview.library.layer;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.MotionEvent;

import com.onlylemi.mapview.library.MapView;

/**
 * Created by hust on 2016/12/10.
 */

public class UILayer extends MapBaseLayer {
    public UILayer(MapView mapView) {
        super(mapView);
    }

    //private rect uirect
    //private string info

    @Override
    public void onTouch(MotionEvent event) {

    }

    @Override
    public void draw(Canvas canvas, Matrix currentMatrix, float currentZoom, float currentRotateDegrees) {

    }
}
