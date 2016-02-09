package ramola.techsearch;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class LocationDetector extends Service implements LocationListener {
    private final long minTime=1000*60;
    private final float minDistance=10;

    public LocationDetector(Context context) {
        this.context = context;
        location=getLocation();
    }

    private Context context;

    public double getLongitude() {
        if(location!=null)
        return location.getLongitude();
        return 0.00;
    }

    public double getLatitude() {
        if (location != null)
            return location.getLatitude();
        return 0.00;
    }
    private Location location;
    private boolean isGPSEnabled=false;
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private Location getLocation(){
        Location location=null;
        LocationManager locationManager= (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnabled){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,minTime,minDistance,this);
            if(locationManager!=null){
                location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location!=null)
                    return location;

            }
        }
        return null;
    }

}
