package gamification.pintourist.pintourist.meccanica;


import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import gamification.pintourist.pintourist.MapsActivity;


/**
 * Created by Daniel on 04/06/2015.
 */
public class Pin {


    private Zona belongingZone;
    private Marker pinMarker;
    private MarkerOptions pinMarkerOptions;
    private boolean conquistato;
    private boolean isObbiettivo;
    private String nome;
    private Indizio indizi;
    private Sfida sfida;
    private Location pinMarkerLocation;



    public Pin(String nome, double Lat, double Long, Indizio lista_indizi, Sfida sfida) {
        pinMarkerOptions = new MarkerOptions().position(new LatLng(Lat, Long)).title("Scoprimi");
        conquistato = false;
        isObbiettivo=false;
        this.nome = nome;
        this.indizi = lista_indizi;
        this.sfida=sfida;
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
        //___

    }



    // costruttore temporaneo
    public Pin(String nome, double Lat, double Long) {
        pinMarkerOptions = new MarkerOptions().position(new LatLng(Lat, Long)).title("Scoprimi");
        conquistato = false;
        this.nome = nome;
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
    //END get methods
    //__________________________________________________________________________
    //set methods
    public void setConquistato() {
        this.conquistato = true;
    }
    public void setObbiettivo() {
        this.isObbiettivo = true;
    }
    public void setBelongingZone(Zona belongingZone) {
        this.belongingZone = belongingZone;
    }
    public void setConquistato(boolean conquistato) {
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

    /*
        private static int generatoreRandom() {
            Random r = new Random();
            return r.nextInt(NUMERO_DOMANDE);
        }
    */
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
                return true;
            }
            else return false;
        }
        return false;
    }
}