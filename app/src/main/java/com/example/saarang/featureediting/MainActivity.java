package com.example.saarang.featureediting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;

public class MainActivity extends AppCompatActivity {
    private MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = findViewById(R.id.mapView);
        ArcGISMap map = new ArcGISMap(Basemap.Type.TOPOGRAPHIC, 34.056295, -117.195800, 2);
        mMapView.setMap(map);
        editFeatures();
    }


    private void editFeatures() {
        ServiceFeatureTable serviceFeatureTable = new ServiceFeatureTable("http://sampleserver6.arcgisonline.com/arcgis/rest/services/Sync/SaveTheBaySync/FeatureServer/0");
        FeatureLayer featureLayer = new FeatureLayer(serviceFeatureTable);
        mMapView.getMap().getOperationalLayers().add(featureLayer);
        mMapView.setOnTouchListener(new CustomMapListener(this,mMapView,serviceFeatureTable));
    }


}
