package gamification.pintourist.pintourist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;


import gamification.pintourist.pintourist.meccanica.*;

public class MapsActivity extends FragmentActivity {

    //Map Viewer & gestire fragment della mappa
    private static MapViewer mMapViewer;
    public static FragmentManager fragmentManager;
    private static Context context;
    public static TextView suggeritore;
    //Elementi interfaccia
    //Menu laterale
    /*
    private ListView mDrawerList;
    private DrawerLayout mDrawer;
    private CustomActionBarDrawerToggle mDrawerToggle;
    private String[] menuItems;
    */
    //Dialog per i popup
    public static Dialog dialogIndizi;
    public static Dialog dialogSfida;
    public static Dialog dialogGestioneIndizi;
    public static Dialog dialogFeedback;

    //___________________________________
    //Meccanica
    private static Pin mPinTarget;
    private static Avatar mAvatar;


    public static GamePhase gamePhase=GamePhase.PIN_CHOICE;





    //Elementi interfaccia
    //Menu laterale
    private ListView mDrawerList;
    private DrawerLayout mDrawer;
    private CustomActionBarDrawerToggle mDrawerToggle;
    private String[] menuItems;
    //Suggeritore

//INIZIO METODI ACTIVITY
// _______________________________________________________________________________________________

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map);

        this.context=getApplicationContext();
        this.suggeritore=(TextView) findViewById(R.id.suggeritore);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // set a custom shadow that overlays the main content when the drawer
        // opens
        mDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        _initMenu();
        mDrawerToggle = new CustomActionBarDrawerToggle(this, mDrawer);
        mDrawer.setDrawerListener(mDrawerToggle);
        /*
        // Set a toolbar to replace the action bar.
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        */

        // initialising the object of the FragmentManager. Here I'm passing getSupportFragmentManager().
        // You can pass getFragmentManager() if you are coding for Android 3.0 or above.
        fragmentManager = getSupportFragmentManager();

        mMapViewer = new MapViewer();
        mAvatar = new Avatar();

        mMapViewer.setUpMapIfNeeded();
        mAvatar.addAvatar();
        mMapViewer.moveCameraTo(mAvatar.getAvatarMarkerOptions().getPosition(), 500);

        Utility.ZonaRioneMonti.draw();
        Utility.ZonaSanLorenzo.draw();

        startGame();
        ImageButton bottoneIndizi=(ImageButton) findViewById(R.id.bottoneIndizi);
        ImageButton bottoneMenu=(ImageButton) findViewById(R.id.bottoneMenuPrincipale);

        bottoneIndizi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setupPopupGestioneIndizi();
            }
        });

        bottoneMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mDrawer.openDrawer(GravityCompat.START);
                //dovrebbe aprire il menu laterale, ma non ci riesco...
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * The action bar home/up should open or close the drawer.
		 * ActionBarDrawerToggle will take care of this.
		 */
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }
    private class CustomActionBarDrawerToggle extends ActionBarDrawerToggle {

        public CustomActionBarDrawerToggle(Activity mActivity,DrawerLayout mDrawerLayout){

            super(
                    mActivity,
                    mDrawerLayout,
                    R.string.ns_menu_open,
                    R.string.ns_menu_close);
            //                    R.drawable.ic_drawer,

        }

        @Override
        public void onDrawerClosed(View view) {
            //getActionBar().setTitle(getString(R.string.ns_menu_close));
            invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            //getActionBar().setTitle(getString(R.string.ns_menu_open)); Non funziona? problemi con l'interazione con le risorse?
            //getActionBar().setTitle("Navigation Drawer Open Menu"); //aggiunta manualmente da me

            invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // Highlight the selected item, update the title, and close the drawer
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            String text= "menu click... should be implemented";
            Toast.makeText(MapsActivity.this, text, Toast.LENGTH_LONG).show();
            //You should reset item counter
            mDrawer.closeDrawer(mDrawerList);

        }


    }


    private void _initMenu() {
        NsMenuAdapter mAdapter = new NsMenuAdapter(this);

        // Add Header
        mAdapter.addHeader(R.string.ns_menu_main_header);

        // Add first block

        menuItems = getResources().getStringArray(
                R.array.ns_menu_items);
        String[] menuItemsIcon = getResources().getStringArray(
                R.array.ns_menu_items_icon);

        int res = 0;
        for (String item : menuItems) {

            int id_title = getResources().getIdentifier(item, "string",
                    this.getPackageName());
            int id_icon = getResources().getIdentifier(menuItemsIcon[res],
                    "drawable", this.getPackageName());

            NsMenuItemModel mItem = new NsMenuItemModel(id_title, id_icon);
            if (res == 1) mItem.counter = 12; //it is just an example...
            if (res == 3) mItem.counter = 3; //it is just an example...
            mAdapter.addItem(mItem);
            res++;
        }

        mAdapter.addHeader(R.string.ns_menu_main_header2);

        this.mDrawerList = (ListView) findViewById(R.id.drawer);
        if (this.mDrawerList != null)
            this.mDrawerList.setAdapter(mAdapter);

        this.mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    }
    // _______________________________________________________________________________________________
//NOSTRI METODI

    public static Context getAppContext(){
        return  context;
    }
    public static MapViewer getmMapViewer() {return mMapViewer;}
    public static Pin getPinTarget(){
        return mPinTarget;
    }
    public static Avatar getmAvatar() {
        return mAvatar;
    }

    public void startGame(){
        suggeritore.setText(R.string.scegliPinPartenza);
        //for (final Pin p: Utility.ZonaSanLorenzo.getPins_CurrentZone()){
        mMapViewer.getmMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (gamePhase== GamePhase.PIN_CHOICE) {
                    for (Pin p: Utility.ZonaSanLorenzo.getPins_CurrentZone()){
                        if (p.getPinMarker().equals(marker)){
                            MapsActivity.mPinTarget=p;
                        }
                    }
                    MapsActivity.mPinTarget.getPinMarker().setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    //Utility.markers.get(markerId);
                    Toast.makeText(MapsActivity.this, "You have selected the Pin with id: ", Toast.LENGTH_LONG).show();
                    suggeritore.setText(R.string.arrivaAlPin);
                    gamePhase=GamePhase.PIN_DISCOVERING;
                    //setupPopupIndizi();
                }
                else if (gamePhase==GamePhase.PIN_DISCOVERING){
                    if (MapsActivity.mPinTarget.getPinMarker() != marker ){
                        //TODO: Immagine dialogo per confermare il cambio Pin Obiettivo: per ora non lo implementiamo
                        Toast.makeText(MapsActivity.this, "You have selected the Pin with id: " + " but the target Pin was already selected", Toast.LENGTH_LONG).show();
                    }
                    else{ //Pin Target == Pin premuto:
                        if (MapsActivity.mPinTarget.isIlluminato()){ //Sei vicino?
                            //setupPopupSfidaPrimaSchermata();
                        }
                    }
                }
                //Toast.makeText(MapsActivity.this, "", Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }
}