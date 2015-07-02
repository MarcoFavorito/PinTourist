package gamification.pintourist.pintourist.meccanica;

import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import gamification.pintourist.pintourist.MapsActivity;
import gamification.pintourist.pintourist.R;

/**
 * Created by Daniel on 04/06/2015.
 */

public class Avatar {
    // troppo piccola!
    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.avatar_image2)) //troppo grande!
    //.icon(R.drawable.uomino)
    //.position(new LatLng(41.891232, 12.492266))
    private MarkerOptions avatarMarkerOptions;
    private Marker avatarMarker;
    private Location avatarLocation;

    public Avatar() {
        avatarMarkerOptions = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.avatar_image))
                .position(Utility.avatarLatLng)
                .draggable(true)
                .title(Utility.avatarTitle)
                .snippet("My Position");
        avatarLocation = new Location("gps");
        avatarLocation.setLatitude(avatarMarkerOptions.getPosition().latitude);
        avatarLocation.setLongitude(avatarMarkerOptions.getPosition().longitude);
        Toast.makeText(MapsActivity.getAppContext(), avatarLocation.toString(), Toast.LENGTH_LONG).show();


        MapsActivity.getmMapViewer().getmMap().setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                }

            @Override
            public void onMarkerDrag(Marker marker) {
                if (marker.getTitle().equals(Utility.avatarTitle) && MapsActivity.getPinTarget() != null) {
                    if (MapsActivity.getPinTarget().isIlluminato()) {
                        if (!MapsActivity.suggeritore.getText().toString().equals(R.string.cliccaSulPinIlluminato))
                            MapsActivity.suggeritore.setText(R.string.cliccaSulPinIlluminato);
                        //avvia animazione
                        //if (!Utility.animazione.isAlive()) Utility.animazione.run();
                    } else {
                        if (!MapsActivity.suggeritore.getText().toString().equals(R.string.arrivaAlPin))
                            MapsActivity.suggeritore.setText(R.string.arrivaAlPin);
                        //if (Utility.animazione.isAlive()) Utility.animazione.interrupt();
                    }
                }
            }


            @Override
            public void onMarkerDragEnd(Marker marker) {
                onMarkerDrag(marker);
                if (MapsActivity.getPinTarget()!=null && MapsActivity.getPinTarget().isIlluminato()) {
                    //Toast.makeText(MapsActivity.getAppContext(), "Illuminato", Toast.LENGTH_LONG).show();
                    //Utility.animazione.start();
                    //Toast.makeText(MapsActivity.getAppContext(), "Illuminato", Toast.LENGTH_LONG).show();

                }
                }

        });
    }


    public MarkerOptions getAvatarMarkerOptions(){
        return avatarMarkerOptions;
    }

    public Marker getAvatarMarker() {
        return avatarMarker;
    }

    public Location getAvatarLocation() {
        return avatarLocation;
    }

    public Location calculateAvatarLocation(){
        if (getAvatarMarker()!=null){
            LatLng avatarLatLng = avatarMarker.getPosition();
            avatarLocation.setLatitude(avatarLatLng.latitude);
            avatarLocation.setLongitude(avatarLatLng.longitude);
            return getAvatarLocation();
        }
        else{
            return null;
        }
    }

    public void addAvatar(){
        avatarMarker= MapsActivity.getmMapViewer().getmMap().addMarker(this.avatarMarkerOptions);
    }
    /*
    private Bitmap scaleImage(Resources res, int id, int lessSideSize) {
        Bitmap b = null;
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(res, id, o);

        float sc = 0.0f;
        int scale = 1;
        // if image height is greater than width
        if (o.outHeight > o.outWidth) {
            sc = o.outHeight / lessSideSize;
            scale = Math.round(sc);
        }
        // if image width is greater than height
        else {
            sc = o.outWidth / lessSideSize;
            scale = Math.round(sc);
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        b = BitmapFactory.decodeResource(res, id, o2);
        return b;
    }
    */

}