package gamification.pintourist.pintourist.meccanica;


import android.location.Location;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.TimerTask;

import gamification.pintourist.pintourist.MapsActivity;
import gamification.pintourist.pintourist.R;


/**
 * Created by Daniel on 04/06/2015.
 */
public class Pin {
    public static int indexColorAnimation=0;
    public boolean isAnimationSet;

    private Zona belongingZone;
    private Marker pinMarker;
    private MarkerOptions pinMarkerOptions;
    private boolean conquistato;
    private boolean isObbiettivo;
    private boolean isIlluminato;
    private String nome;
    private Indizio indizi;
    private Sfida sfida;
    private Location pinMarkerLocation;
    private int pinId;

    private static int autoincrementalId=0;



    public Pin(String nome, double Lat, double Long, Indizio lista_indizi, Sfida sfida) {
        pinMarkerOptions = new MarkerOptions().position(new LatLng(Lat, Long));
        conquistato = false;
        isObbiettivo = false;
        this.nome = nome;
        this.indizi = lista_indizi;
        this.sfida =sfida;
        this.pinId = autoincrementalId++;
        this.isIlluminato = false;
        isAnimationSet = false;
    }

    public Pin(String nome,MarkerOptions markerOptions, Indizio lista_indizi, Sfida sfida) {
        pinMarkerOptions = markerOptions;
        conquistato = false;
        isObbiettivo=false;
        this.nome = nome;
        this.indizi = lista_indizi;
        this.sfida=sfida;
        //set della Location, ci serve per la distanza
        this.pinMarkerLocation=new Location("gps");
        this.pinMarkerLocation.setLongitude(markerOptions.getPosition().longitude);
        this.pinMarkerLocation.setLatitude(markerOptions.getPosition().latitude);
        this.pinId= autoincrementalId++;
        this.isIlluminato=false;
        isAnimationSet=false;
    }



    // costruttore temporaneo
    public Pin(String nome, double Lat, double Long) {
        pinMarkerOptions = new MarkerOptions().position(new LatLng(Lat, Long)).title("Scoprimi");
        conquistato = false;
        this.nome = nome;
        this.pinId= autoincrementalId++;
        this.isIlluminato=false;
        isAnimationSet=false;
    }

    //costruttore Pin poste
    public Pin(String nome, double Lat, double Long, int res) {
        pinMarkerOptions = new MarkerOptions().position(new LatLng(Lat, Long)).icon(BitmapDescriptorFactory.fromResource(res));
        conquistato = false;
        isObbiettivo = false;
        this.nome = nome;
        this.pinId = autoincrementalId++;
        this.isIlluminato = false;
        isAnimationSet = false;
    }

    public String getNome() {
        return nome;
    }


    // get methods
    //_______________________________________________________
    public MarkerOptions getPinMarkerOptions() {
        return pinMarkerOptions;
    }
    public Marker getPinMarker() {
        return pinMarker;
    }
    //return lat and long of the pin
    public LatLng getLatLng() {
        return getPinMarker().getPosition();
    }
    public Indizio getIndizi() {
        return indizi;
    }
    public Sfida getSfida(){
        return sfida;
    }
    public boolean getIsConquistato() {
        return conquistato;
    }
    public Zona getBelongingZone() {
        return belongingZone;
    }
    public Location getPinMarkerLocation() {
        return pinMarkerLocation;
    }
    public int getPinId() {
        return pinId;
    }

    //END get methods
    //__________________________________________________________________________
    //set methods
    public void setObbiettivo(boolean b) {
        this.isObbiettivo = b;
    }
    public void setBelongingZone(Zona belongingZone) {
        this.belongingZone = belongingZone;
    }
    public void setConquistato(boolean conquistato) {
        this.getPinMarker().setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
   /*     LatLng temp = this.getLatLng();
        this.pinMarkerOptions = null;
        pinMarkerOptions = new MarkerOptions().position(temp).icon(BitmapDescriptorFactory.fromResource(R.drawable.conquista_pin));
    */
        this.conquistato = conquistato;
    }
    public void setIndizi(Indizio indizi) {
        this.indizi = indizi;
    }
    public void setIsObbiettivo(boolean isObbiettivo) {
        this.isObbiettivo = isObbiettivo;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setPinMarker(Marker pinMarker) {
        this.pinMarker = pinMarker;
    }
    public void setPinMarkerOptions(MarkerOptions pinMarkerOptions) {
        this.pinMarkerOptions = pinMarkerOptions;
    }
    public void setSfida(Sfida sfida) {
        this.sfida = sfida;
    }
    //setting the snippet as the name of the monument after the user's answered the question
    public void setName() {
        if (this.getIsConquistato())
            this.pinMarkerOptions.title(this.getNome());
    }
    public void setPinMarkerLocation(Location pinMarkerLocation) {
        this.pinMarkerLocation = pinMarkerLocation;
    }
    //END set methods
    //__________________________________________________________________________

    public void addMarker(){
        this.pinMarker=MapsActivity.getmMapViewer().getmMap().addMarker(this.pinMarkerOptions);
        this.pinMarkerLocation=new Location("gps");
        this.pinMarkerLocation.setLatitude(pinMarkerOptions.getPosition().latitude);
        this.pinMarkerLocation.setLongitude(pinMarkerOptions.getPosition().longitude);
    }

    public boolean isIlluminato() {
        if (MapsActivity.getPinTarget() != null) {
            Location avatarLocation = MapsActivity.getmAvatar().calculateAvatarLocation();
            Location pinTargetLocation = this.getPinMarkerLocation();
            //Toast.makeText(MapsActivity.getAppContext(), "Illuminato", Toast.LENGTH_LONG).show();

            if (avatarLocation.distanceTo(pinTargetLocation) < Utility.MIN_DSTANCE) {
                this.isIlluminato=true;
                //if (!isAnimationSet) setAnimazione(this.pinMarker);
                return true;
            }
            else {
                this.isIlluminato=false;
                return false;
            }
        }
        this.isIlluminato=false;
        return false;
    }

    public void setAnimazione(final Marker marker){
        if (this.isIlluminato){
            TimerTask setIconIndex = new TimerTask() {
                @Override
                public void run() {
                    MapsActivity.getPinTarget().getPinMarker().setIcon(BitmapDescriptorFactory.defaultMarker(Utility.iconArray[indexColorAnimation]));
                    setAnimazione(MapsActivity.getPinTarget().getPinMarker());
                }
            };
            indexColorAnimation=(indexColorAnimation++)%Utility.iconArray.length;
            Timer timer = new Timer();
            timer.schedule(setIconIndex, 1000);
        }
        else{
            isAnimationSet=false;
        }

    }

}