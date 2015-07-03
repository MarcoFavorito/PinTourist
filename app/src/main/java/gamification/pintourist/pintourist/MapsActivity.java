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
                setupPopupGestioneIndizi();
            }
        });

        bottoneMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mDrawer.openDrawer(GravityCompat.START);
                //dovrebbe aprire il menu laterale, ma non ci riesco...
            }
        });

        startGame();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawer.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_save).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
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

    @Override
    protected void onResume() {
        super.onResume();
        //mLocationListener.update();
        //setUpMapIfNeeded();
        mMapViewer.setUpMapIfNeeded();
    }

// FINE METODI ACTIVITY
// _______________________________________________________________________________________________
//INIZIO ELEMENTI PER IL MENU LATERALE

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

//FINE ELEMENTI PER IL MENU LATERALE
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
                if (!marker.equals(mAvatar.getAvatarMarker())) {
                    if (gamePhase == GamePhase.PIN_CHOICE) {
                        for (Pin p : Utility.ZonaSanLorenzo.getPins_CurrentZone()) {
                            if (p.getPinMarker().equals(marker) && !p.getIsConquistato()) {
                                MapsActivity.mPinTarget = p;
                            }
                        }
                        if (mPinTarget!=null) {
                            MapsActivity.mPinTarget.getPinMarker().setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            //Utility.markers.get(markerId);
                            Toast.makeText(MapsActivity.this, "You have selected the Pin with id: ", Toast.LENGTH_LONG).show();
                            suggeritore.setText(R.string.arrivaAlPin);
                            gamePhase = GamePhase.PIN_DISCOVERING;
                            setupPopupIndizi(mPinTarget);
                        }
                    } else if (gamePhase == GamePhase.PIN_DISCOVERING) {
                        if (!MapsActivity.mPinTarget.getPinMarker().equals(marker)) {
                            //TODO: Immagine dialogo per confermare il cambio Pin Obiettivo: per ora non lo implementiamo
                            Toast.makeText(MapsActivity.this, "You have selected the Pin with id: " + " but the target Pin was already selected", Toast.LENGTH_LONG).show();
                        } else { //Pin Target == Pin premuto:
                            if (MapsActivity.mPinTarget.isIlluminato()) { //Sei vicino?
                                setupPopupSfidaPrimaSchermata(mPinTarget);
                            }
                        }
                    }
                    //Toast.makeText(MapsActivity.this, "", Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });
    }




    //_________________________________________________________
    //setup dei popup (che fa pure rima :D )
    public void setupPopupIndizi(final Pin pin){

        MapsActivity.dialogIndizi= new Dialog(MapsActivity.this);

        // Evito la presenza della barra del titolo nella mia dialog
        MapsActivity.dialogIndizi.getWindow();
        MapsActivity.dialogIndizi.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MapsActivity.dialogIndizi.setCancelable(false);
        dialogIndizi.setCanceledOnTouchOutside(false);
        if (pin.getIndizi()==null){
            MapsActivity.dialogIndizi.setContentView(R.layout.popup_indizi);
            TextView titoloIndizio = (TextView) dialogIndizi.findViewById(R.id.popupIndiziTitolo);
            Button btnOk = (Button) MapsActivity.dialogIndizi.findViewById(R.id.popupIndiziBtnOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MapsActivity.dialogIndizi.dismiss();

                }
            });
            MapsActivity.dialogIndizi.show();
            return;

        }
        if (pin.getIndizi().getLevel()==2){
            MapsActivity.dialogIndizi.setContentView(R.layout.popup_indizi_fine_indizi);
            TextView titoloIndizio = (TextView) dialogIndizi.findViewById(R.id.popupIndiziTitolo);
            titoloIndizio.setText("Indizio zona San Lorenzo, Pin id: " + (pin.getPinId() + 1));
            Button btnOk = (Button) MapsActivity.dialogIndizi.findViewById(R.id.popupIndiziFineIndiziBtnOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MapsActivity.dialogIndizi.dismiss();
                }
            });
        }
        // Carico il layout della dialog al suo intenro
        else {
            MapsActivity.dialogIndizi.setContentView(R.layout.popup_indizi);

            TextView titoloIndizio = (TextView) dialogIndizi.findViewById(R.id.popupIndiziTitolo);
            titoloIndizio.setText("Indizio zona San Lorenzo, Pin id: " + (pin.getPinId() + 1));
            TextView descrizioneIndizio = (TextView) dialogIndizi.findViewById(R.id.popupIndizioDescrizione);
            descrizioneIndizio.setText(pin.getIndizi().getNextIndizio());

            // Nel caso fosse previsto un titolo questo sarebbe il codice da
            // utilizzare eliminando quello visto poco sopra per evitarlo
            //dialog.setTitle("Testo per il titolo");



            // Qui potrei aggiungere eventuali altre impostazioni per la dialog
            Button btnOk = (Button) MapsActivity.dialogIndizi.findViewById(R.id.popupIndiziBtnOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MapsActivity.dialogIndizi.dismiss();

                }
            });

            //Gestisco il bottone di chiusura della dialog (quello in alto a destra)


            Button btnAltroIndizio = (Button) dialogIndizi.findViewById(R.id.popupIndiziBtnAltroIndizio);
            btnAltroIndizio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogIndizi.dismiss();
                    setupPopupIndizi(pin);
                }
            });
        }
        // Faccio comparire la dialog
        MapsActivity.dialogIndizi.show();
    }

    public void setupPopupSfidaPrimaSchermata(final Pin pin){
        MapsActivity.dialogSfida= new Dialog(MapsActivity.this);


        MapsActivity.dialogSfida.getWindow();
        MapsActivity.dialogSfida.requestWindowFeature(Window.FEATURE_NO_TITLE);

        MapsActivity.dialogSfida.setContentView(R.layout.popup_sfida_prima_schermata);

        //dialogSfida.setTitle("Testo per il titolo");
        TextView titoloSfida = (TextView) dialogSfida.findViewById(R.id.popupSfidaPrimaSchermataTitolo);
        titoloSfida.setText("Sfida zona San Lorenzo, Pin id: " + (pin.getPinId() + 1)+"\n"+pin.getNome());

        dialogSfida.setCancelable(false);
        dialogSfida.setCanceledOnTouchOutside(false);
        // Qui potrei aggiungere eventuali altre impostazioni per la dialog
        // ...

        //Gestisco il bottone di chiusura della dialog (quello in alto a destra)
        Button btnOk = (Button) MapsActivity.dialogSfida.findViewById(R.id.popupSfidaPrimaSchermataBtnAvanti);
        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MapsActivity.dialogSfida.dismiss();
                setupPopupSfida(pin);
            }
        });
        // Faccio comparire la dialog
        MapsActivity.dialogSfida.show();
    }

    public void setupPopupSfida(final Pin pin){
        MapsActivity.dialogSfida= new Dialog(MapsActivity.this);
        MapsActivity.dialogSfida.getWindow();
        MapsActivity.dialogSfida.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MapsActivity.dialogSfida.setContentView(R.layout.popup_sfida);
        MapsActivity.dialogSfida.setCancelable(false);
        dialogSfida.setCanceledOnTouchOutside(false);

        TextView domandaSfida= (TextView) dialogSfida.findViewById(R.id.popupSfidaDomanda);
        domandaSfida.setText(pin.getSfida().getNextDomanda().getQuestion()); //ATTENZIONE! CHIAMATA A getNextDomanda IMPORTANTE! VA FATTA UNA SOLA VOLTA
        ImageView immagineSfida = (ImageView) dialogSfida.findViewById(R.id.popupSfidaImmagine);
        immagineSfida.setImageResource(pin.getSfida().getCurrentDomanda().getImage());

        setupBottoniRisposta(pin.getSfida().getCurrentDomanda(), dialogSfida, pin.getSfida(), pin);


        MapsActivity.dialogSfida.show();
    }

    public void setupPopupGestioneIndizi(){
        MapsActivity.dialogGestioneIndizi= new Dialog(MapsActivity.this);
        MapsActivity.dialogGestioneIndizi.getWindow();
        MapsActivity.dialogGestioneIndizi.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MapsActivity.dialogGestioneIndizi.setCancelable(false);
        dialogGestioneIndizi.setCanceledOnTouchOutside(false);
        MapsActivity.dialogGestioneIndizi.setContentView(R.layout.gestione_indizi);


        final Button btnAltroIndizio=(Button) MapsActivity.dialogGestioneIndizi.findViewById(R.id.gestioneIndiziBtnAltroIndizio);
        Button btnOk = (Button) MapsActivity.dialogGestioneIndizi.findViewById(R.id.gestioneIndiziBtnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MapsActivity.dialogGestioneIndizi.dismiss();
            }
        });

        TableLayout tabellaListaPin=(TableLayout) dialogGestioneIndizi.findViewById(R.id.gestioneIndizi_tabellaListaPin);
        TableRow rigaPin;
        TextView nomePin;
        for (final Pin p: Utility.ZonaSanLorenzo.getPins_CurrentZone()){
            if (p.getIndizi()==null) continue;
            if (p.getIndizi().getLevel()!=-1){
                rigaPin=new TableRow(this);
                nomePin=new TextView(this);
                nomePin.setText("Zona San Lorenzo Pin " + p.getPinId());
                nomePin.setTextSize(30);
                nomePin.setTextAppearance(this, android.R.style.TextAppearance_Large);
                nomePin.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                rigaPin.addView(nomePin);
                rigaPin.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                rigaPin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        btnAltroIndizio.setEnabled(true);
                        btnAltroIndizio.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGestioneIndizi.dismiss();
                                setupPopupIndizi(p);
                            }
                        });
                        TextView testoIndizio;
                        TableRow rigaIndizio;
                        TableLayout tabellaListaIndizi = (TableLayout) dialogGestioneIndizi.findViewById(R.id.gestioneIndizi_tabellaListaIndizi);
                        if (p.getIndizi().getStringheIndizi() != null)
                            for (int index = 0; index <= p.getIndizi().getLevel(); index++) {
                                final int indice_ausiliario = index;
                                p.getIndizi().getStringheIndizi();
                                rigaIndizio = new TableRow(MapsActivity.getAppContext());
                                testoIndizio = new TextView(MapsActivity.getAppContext());
                                //testoIndizio.setText(p.getIndizi().getStringaIndizio(index));
                                testoIndizio.setText("Indizio "+(index+1));
                                testoIndizio.setTextSize(30);
                                testoIndizio.setTextAppearance(MapsActivity.getAppContext(), android.R.style.TextAppearance_Large);
                                testoIndizio.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                rigaIndizio.addView(testoIndizio);
                                rigaIndizio.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                rigaIndizio.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogGestioneIndizi.dismiss();
                                        setupPopupIndiziFromGestione(p, indice_ausiliario);
                                    }
                                });
                                tabellaListaIndizi.addView(rigaIndizio);
                            }
                    }

                });
                tabellaListaPin.addView(rigaPin);
            }
        }
        dialogGestioneIndizi.show();
    }

    public void setupPopupIndiziFromGestione(final Pin pin, int numIndizio){
        MapsActivity.dialogIndizi= new Dialog(MapsActivity.this);

        // Evito la presenza della barra del titolo nella mia dialog
        MapsActivity.dialogIndizi.getWindow();
        MapsActivity.dialogIndizi.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Carico il layout della dialog al suo intenro

        MapsActivity.dialogIndizi.setContentView(R.layout.popup_indizi_from_gestione);

        TextView titoloIndizio= (TextView) dialogIndizi.findViewById(R.id.popupIndiziTitolo);
        titoloIndizio.setText("Indizio zona San Lorenzo, Pin id: " + (pin.getPinId() + 1));
        TextView descrizioneIndizio = (TextView)dialogIndizi.findViewById(R.id.popupIndizioDescrizione);
        descrizioneIndizio.setText(pin.getIndizi().getStringaIndizio(numIndizio));

        // Nel caso fosse previsto un titolo questo sarebbe il codice da
        // utilizzare eliminando quello visto poco sopra per evitarlo
        //dialog.setTitle("Testo per il titolo");

        MapsActivity.dialogIndizi.setCancelable(false);
        dialogIndizi.setCanceledOnTouchOutside(false);

        // Qui potrei aggiungere eventuali altre impostazioni per la dialog
        // ...

        //Gestisco il bottone di chiusura della dialog (quello in alto a destra)
        Button btnOk = (Button) MapsActivity.dialogIndizi.findViewById(R.id.popupIndiziBtnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MapsActivity.dialogIndizi.dismiss();
                setupPopupGestioneIndizi();
            }
        });
        // Faccio comparire la dialog
        MapsActivity.dialogIndizi.show();
    }

    public void setupBottoniRisposta(final Domanda q,final Dialog d, Sfida s, final Pin pin){
        Button btn1= (Button) d.findViewById(R.id.risposta1);
        Button btn2= (Button) d.findViewById(R.id.risposta2);
        Button btn3= (Button) d.findViewById(R.id.risposta3);
        Button btn4= (Button) d.findViewById(R.id.risposta4);
        Button[] array= new Button[]{
                btn1,btn2,btn3,btn4
        };
        for (int i=0;i<array.length;i++){
            final int aux=i;
            array[i].setText(q.getPossibleAnswers()[i]);
            array[i].setTextSize(15);
            array[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (aux == q.getCorrectAnswerIndex()) {
                        //Hai risposto correttamente! feedback positivo
                        d.dismiss();
                        setupPopupFeedbackPositivo(pin);

                    } else {
                        //feedback negativo --> next sfida
                        d.dismiss();
                        setupPopupFeedbackNegativo(pin);
                    }
                }
            });
        }
    }

    public void setupPopupFeedbackPositivo(Pin pin){
        dialogFeedback= new Dialog(MapsActivity.this);
        dialogFeedback.getWindow();
        dialogFeedback.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogFeedback.setContentView(R.layout.feedback_positivo);
        dialogFeedback.setCancelable(false);
        dialogFeedback.setCanceledOnTouchOutside(false);

        pin.setConquistato(true);
        MapsActivity.gamePhase=GamePhase.PIN_CHOICE;
        MapsActivity.mPinTarget=null;
        suggeritore.setText(R.string.scegliPinPartenza);

        Button btnOk = (Button) dialogFeedback.findViewById(R.id.popupFeedbackBtnAvanti);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFeedback.dismiss();
            }
        });

        dialogFeedback.show();
    }

    public void setupPopupFeedbackNegativo(final Pin pin){
        dialogFeedback= new Dialog(MapsActivity.this);
        dialogFeedback.getWindow();
        dialogFeedback.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogFeedback.setContentView(R.layout.feedback_negativo);
        dialogFeedback.setCancelable(false);
        dialogFeedback.setCanceledOnTouchOutside(false);

        if (pin.getSfida().hasNextDomanda()){
            TextView popupFeedbackNegativoMessaggio = (TextView) dialogFeedback.findViewById(R.id.popupFeedbackMessaggio);
            popupFeedbackNegativoMessaggio.setText("Risposta Errata! Hai ancora "+ pin.getSfida().tentativiRimasti()+" tentativi. Premi sul tasto Ok per continuare");

            Button btnOk = (Button) dialogFeedback.findViewById(R.id.popupFeedbackBtnAvanti);
            btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFeedback.dismiss();
                setupPopupSfida(pin);
                }
            });
        }
        else{
            TextView popupFeedbackNegativoMessaggio = (TextView) dialogFeedback.findViewById(R.id.popupFeedbackMessaggio);
            popupFeedbackNegativoMessaggio.setText("Risposta Errata! Hai esaurito tutti i tentativi rimasti.\nHai comunque conquistato il Pin. Premi sul tasto Ok per tornare sulla mappa");

            Button btnOk = (Button) dialogFeedback.findViewById(R.id.popupFeedbackBtnAvanti);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogFeedback.dismiss();

                }
            });
        }

        pin.setConquistato(true);
        MapsActivity.gamePhase=GamePhase.PIN_CHOICE;
        MapsActivity.mPinTarget=null;
        suggeritore.setText(R.string.scegliPinPartenza);

        dialogFeedback.show();
    }
}