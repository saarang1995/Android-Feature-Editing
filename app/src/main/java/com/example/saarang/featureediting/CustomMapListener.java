package com.example.saarang.featureediting;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.Toast;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureEditResult;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CustomMapListener extends DefaultMapViewOnTouchListener {
    Context context;
    ServiceFeatureTable serviceFeatureTable;
    MapView mapView;
    public CustomMapListener(Context context, MapView mapView, ServiceFeatureTable serviceFeatureTable) {
        super(context, mapView);
        this.context = context;
        this.mapView=mapView;
        this.serviceFeatureTable = serviceFeatureTable;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        //Geometry
        android.graphics.Point screenPoint = new android.graphics.Point((int) e.getX(), (int) e.getY());
        Point mapPoint = mapView.screenToLocation(screenPoint);
        addFeature(mapPoint);
        return true;
    }

    private void addFeature(Point mapPoint) {
        //Attributes:
        HashMap<String, Object> attribute = new HashMap<>();
        attribute.put("type", 4);
        attribute.put("comments", "Edited By Saarang");

        //Feature:

        Feature addFeature = serviceFeatureTable.createFeature(attribute, mapPoint);


        final ListenableFuture<Void> listenableFuture = serviceFeatureTable.addFeatureAsync(addFeature);

        listenableFuture.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {

                    listenableFuture.get();

                    final ListenableFuture<List<FeatureEditResult>> applyEditsFuture = serviceFeatureTable.applyEditsAsync();
                    applyEditsFuture.addDoneListener(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final List<FeatureEditResult> featureEditResults = applyEditsFuture.get();
                                Toast.makeText(context, "Feature Added Successfully" + featureEditResults.size(), Toast.LENGTH_SHORT).show();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception ex) {
                }
            }
        });

    }
}
