package com.abs2432gmail.daejeoninfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import net.daum.android.map.MapViewEventListener;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class MapViewActivity extends AppCompatActivity implements MapViewEventListener, net.daum.mf.map.api.MapView.POIItemEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        MapView mapView = new MapView(this);

        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
    }

    @Override
    public void onLoadMapView() {

    }

    @Override
    public void onPOIItemSelected(net.daum.mf.map.api.MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(net.daum.mf.map.api.MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(net.daum.mf.map.api.MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(net.daum.mf.map.api.MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}
