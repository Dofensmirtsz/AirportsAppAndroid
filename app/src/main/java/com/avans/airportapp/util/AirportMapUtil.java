package com.avans.airportapp.util;

import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.avans.airportapp.ui.detail.LatLngInterpolator;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

public final class AirportMapUtil {

    public static void animateMarker(final Marker marker, final LatLng finalPosition, final List<LatLng> latLngList, final LatLngInterpolator latLngInterpolator){
        final LatLng startPosition = marker.getPosition();
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 3000;

        handler.post(new Runnable() {
            long elapsed;
            int index = 0;
            float t;
            float v;

            @Override
            public void run(){
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);

                //marker.setPosition(latLngList.get(index));
                marker.setPosition(latLngInterpolator.interpolate(v, startPosition, finalPosition));

                index++;
                index%=latLngList.size();

                handler.postDelayed(this, 16);
            }
        });
    }

}
