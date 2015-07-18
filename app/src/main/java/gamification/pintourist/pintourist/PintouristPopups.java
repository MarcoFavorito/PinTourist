package gamification.pintourist.pintourist;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Map;

import gamification.pintourist.pintourist.meccanica.Domanda;
import gamification.pintourist.pintourist.meccanica.GamePhase;
import gamification.pintourist.pintourist.meccanica.Indizio;
import gamification.pintourist.pintourist.meccanica.Pin;
import gamification.pintourist.pintourist.meccanica.Sfida;
import gamification.pintourist.pintourist.meccanica.Utility;

/**
 * Created by Marco on 11/06/2015.
 */
public class PintouristPopups {
    public static Dialog mainDialog;

    public static void funzioneBase(Dialog d) {
        d.getWindow();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setCancelable(false);
        d.setCanceledOnTouchOutside(false);
    }

    public interface MyPopup {
        public void setUp();
    }

    public class PopupIndizi extends Dialog implements MyPopup {

        public Pin pin;
        public Indizio indizio;

        public PopupIndizi(Context context, Pin pin) {
            super(context);
            this.pin = pin;
            this.indizio = pin.getIndizi();
            setUp();
        }


        public void setUp() {
            funzioneBase(this);
            this.setContentView(R.layout.popup_indizi);

            TextView titoloIndizio = (TextView) this.findViewById(R.id.popupIndiziTitolo);
            titoloIndizio.setText("Indizio zona San Lorenzo, Pin id: " + (pin.getPinId() + 1));
            TextView descrizioneIndizio = (TextView) this.findViewById(R.id.popupIndizioDescrizione);
            descrizioneIndizio.setText(pin.getIndizi().getNextIndizio());

            Button btnOk = (Button) this.findViewById(R.id.popupIndiziBtnOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

            Button btnAltroIndizio = (Button) this.findViewById(R.id.popupIndiziBtnAltroIndizio);
            btnAltroIndizio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (pin.getIndizi().hasNextIndizio()) {
                        MapsActivity.pintouristDialog = MapsActivity.pintouristPopups.new PopupIndizi(MapsActivity.getAppContext(), pin);
                        MapsActivity.pintouristDialog.show();
                    }
                    else{
                        MapsActivity.pintouristDialog = MapsActivity.pintouristPopups.new PopupIndiziFineIndizi(MapsActivity.getAppContext(), pin);
                        MapsActivity.pintouristDialog.show();
                    }
                }
            });
        }

    }

    public class PopupSfida extends Dialog implements MyPopup {
        public Pin pin;
        public Sfida sfida;

        public PopupSfida(Context context, Pin pin) {
            super(context);
            this.pin = pin;
            this.sfida = pin.getSfida();
            setUp();
        }

        public void setUp() {
            funzioneBase(this);
            this.setContentView(R.layout.popup_sfida);
            TextView domandaSfida = (TextView) this.findViewById(R.id.popupSfidaDomanda);
            domandaSfida.setText(this.sfida.getNextDomanda().getQuestion()); //ATTENZIONE! CHIAMATA A getNextDomanda IMPORTANTE! VA FATTA UNA SOLA VOLTA
            ImageView immagineSfida = (ImageView) this.findViewById(R.id.popupSfidaImmagine);
            immagineSfida.setImageResource(this.sfida.getCurrentDomanda().getImage());
            setupBottoniRisposta(this.sfida.getCurrentDomanda(), this, this.sfida);
        }

        public void setupBottoniRisposta(final Domanda q, final Dialog d, Sfida s) {
            Button btn1 = (Button) d.findViewById(R.id.risposta1);
            Button btn2 = (Button) d.findViewById(R.id.risposta2);
            Button btn3 = (Button) d.findViewById(R.id.risposta3);
            Button btn4 = (Button) d.findViewById(R.id.risposta4);
            Button[] array = new Button[]{
                    btn1, btn2, btn3, btn4
            };
            for (int i = 0; i < array.length; i++) {
                final int aux = i;
                array[i].setText(q.getPossibleAnswers()[i]);
                array[i].setTextSize(15);
                array[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (aux == q.getCorrectAnswerIndex()) {
                            //Hai risposto correttamente! feedback positivo
                            d.dismiss();
                            MapsActivity.setupPopupFeedbackPositivo(pin);

                        } else {
                            //feedback negativo --> next sfida
                            d.dismiss();
                            MapsActivity.setupPopupFeedbackNegativo(pin);
                        }
                    }
                });
            }
        }
    }

    public class PopupIndiziFineIndizi extends Dialog implements MyPopup {

        public Pin pin;

        public PopupIndiziFineIndizi(Context context, Pin pin) {
            super(context);
            this.pin = pin;
            setUp();
        }


        public void setUp() {
            funzioneBase(this);
            this.setContentView(R.layout.popup_indizi_fine_indizi);
            TextView titoloIndizio = (TextView) this.findViewById(R.id.popupIndiziFineIndiziTitolo);
            titoloIndizio.setText("Indizio zona San Lorenzo, Pin id: " + (pin.getPinId() + 1));
            Button btnOk = (Button) this.findViewById(R.id.popupIndiziFineIndiziBtnOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    public class PopupSfidaPrimaSchermata extends Dialog implements MyPopup {

        public Pin pin;

        public PopupSfidaPrimaSchermata(Context context, Pin pin) {
            super(context);
            this.pin = pin;
            setUp();
        }


        public void setUp() {
            funzioneBase(this);
            this.setContentView(R.layout.popup_sfida_prima_schermata);
            TextView titoloSfida = (TextView) this.findViewById(R.id.popupSfidaPrimaSchermataTitolo);
            titoloSfida.setText("Sfida zona San Lorenzo, Pin id: " + (pin.getPinId() + 1) + "\n" + pin.getNome());

            this.setCancelable(false);
            this.setCanceledOnTouchOutside(false);
            Button btnOk = (Button) this.findViewById(R.id.popupSfidaPrimaSchermataBtnAvanti);
            btnOk.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                    MapsActivity.setupPopupSfida(pin);
                }
            });
        }
    }

    public class PopupGestioneIndizi extends Dialog implements MyPopup {

        public Pin pin;
        public Indizio indizio;

        public PopupGestioneIndizi(Context context, Pin pin) {
            super(context);
            this.pin = pin;
            this.indizio = pin.getIndizi();
            setUp();
        }


        public void setUp() {
            funzioneBase(this);
            this.setContentView(R.layout.gestione_indizi);
            final Button btnAltroIndizio = (Button) this.findViewById(R.id.gestioneIndiziBtnAltroIndizio);
            Button btnOk = (Button) this.findViewById(R.id.gestioneIndiziBtnOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

            TableLayout tabellaListaPin = (TableLayout) this.findViewById(R.id.gestioneIndizi_tabellaListaPin);
            TableRow rigaPin;
            TextView nomePin;
            for (final Pin p : Utility.ZonaSanLorenzo.getPins_CurrentZone()) {
                if (p.getIndizi() == null) continue;
                if (p.getIndizi().getLevel() != -1) {
                    rigaPin = new TableRow(MapsActivity.getAppContext());
                    nomePin = new TextView(MapsActivity.getAppContext());
                    nomePin.setText("Zona San Lorenzo Pin " + p.getPinId());
                    nomePin.setTextSize(30);
                    nomePin.setTextAppearance(MapsActivity.getAppContext(), android.R.style.TextAppearance_Large);
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
                                    dismiss();
                                    MapsActivity.setupPopupIndizi(p);
                                }
                            });
                            TextView testoIndizio;
                            TableRow rigaIndizio;
                            TableLayout tabellaListaIndizi = (TableLayout) MapsActivity.pintouristDialog.findViewById(R.id.gestioneIndizi_tabellaListaIndizi);
                            if (p.getIndizi().getStringheIndizi() != null)
                                for (int index = 0; index <= p.getIndizi().getLevel(); index++) {
                                    final int indice_ausiliario = index;
                                    p.getIndizi().getStringheIndizi();
                                    rigaIndizio = new TableRow(MapsActivity.getAppContext());
                                    testoIndizio = new TextView(MapsActivity.getAppContext());
                                    //testoIndizio.setText(p.getIndizi().getStringaIndizio(index));
                                    testoIndizio.setText("Indizio " + (index + 1));
                                    testoIndizio.setTextSize(30);
                                    testoIndizio.setTextAppearance(MapsActivity.getAppContext(), android.R.style.TextAppearance_Large);
                                    testoIndizio.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    rigaIndizio.addView(testoIndizio);
                                    rigaIndizio.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    rigaIndizio.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dismiss();
                                            MapsActivity.setupPopupIndiziFromGestione(p, indice_ausiliario);
                                        }
                                    });
                                    tabellaListaIndizi.addView(rigaIndizio);
                                }
                        }

                    });
                    tabellaListaPin.addView(rigaPin);
                }
            }
        }

    }

    public class PopupRiepilogoIndizi extends Dialog implements MyPopup {

        public Pin pin;
        public Indizio indizio;

        public PopupRiepilogoIndizi(Context context, Pin pin) {
            super(context);
            if (pin!=null && pin.getIndizi()!=null) {
                this.pin = pin;
                this.indizio = pin.getIndizi();
            }
            setUp();
        }


        public void setUp() {
            funzioneBase(this);
            this.setContentView(R.layout.riepilogo_indizi);
            final Button btnAltroIndizio = (Button) this.findViewById(R.id.riepilogoIndiziBottoneAltroIndizio);
            Button btnOk = (Button) this.findViewById(R.id.riepilogoIndiziBottoneOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });
            btnAltroIndizio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    MapsActivity.setupPopupIndizi(pin);
                }
            });

            TextView testoIndizio;
            TableRow rigaIndizio;
            TableLayout tabellaListaIndizi = (TableLayout) this.findViewById(R.id.riepilogoIndiziTabellaListaIndizi);
            if (pin!=null && pin.getIndizi().getStringheIndizi() != null) {
                TextView titoloRiepilogoIndizi= (TextView)this.findViewById(R.id.riepilogoIndiziPinIndizio);
                titoloRiepilogoIndizi.setText("Indizi del pin id: "+pin.getPinId()+", zona" + pin.getBelongingZone().getNome());
                for (int index = 0; index <= pin.getIndizi().getLevel(); index++) {
                    final int indice_ausiliario = index;
                    pin.getIndizi().getStringheIndizi();
                    rigaIndizio = new TableRow(MapsActivity.getAppContext());
                    testoIndizio = new TextView(MapsActivity.getAppContext());
                    //testoIndizio.setText(p.getIndizi().getStringaIndizio(index));
                    testoIndizio.setText("Indizio " + (index + 1));
                    testoIndizio.setTextSize(30);
                    testoIndizio.setTextAppearance(MapsActivity.getAppContext(), android.R.style.TextAppearance_Large);
                    testoIndizio.setGravity(Gravity.CENTER_HORIZONTAL);
                    testoIndizio.setPadding(20, 20, 20, 20);
                    rigaIndizio.addView(testoIndizio);
                    rigaIndizio.setLayoutParams(new ViewGroup.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    rigaIndizio.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            MapsActivity.setupPopupIndiziFromGestione(pin, indice_ausiliario);
                        }
                    });
                    tabellaListaIndizi.addView(rigaIndizio);
                    btnAltroIndizio.setEnabled(true);
                }
            }
            else{
                rigaIndizio = new TableRow(MapsActivity.getAppContext());
                testoIndizio = new TextView(MapsActivity.getAppContext());
                //testoIndizio.setText(p.getIndizi().getStringaIndizio(index));
                testoIndizio.setText("Non hai selezionato nessun Pin obiettivo");
                testoIndizio.setTextSize(30);
                testoIndizio.setTextAppearance(MapsActivity.getAppContext(), android.R.style.TextAppearance_Large);
                testoIndizio.setGravity(Gravity.CENTER_HORIZONTAL);
                testoIndizio.setPadding(20, 20, 20, 20);
                rigaIndizio.addView(testoIndizio);
                tabellaListaIndizi.addView(rigaIndizio);
            }
        }
    }

    public class PopupIndiziFromGestione extends Dialog implements MyPopup{

        public Pin pin;
        public Indizio indizio;
        public int numIndizio;

        public PopupIndiziFromGestione(Context context, Pin pin, int numIndizio){
            super(context);
            this.pin=pin;
            this.indizio=pin.getIndizi();
            this.numIndizio=numIndizio;
            setUp();
        }


        public void setUp(){
            funzioneBase(this);
            this.setContentView(R.layout.popup_indizi_from_gestione);
            TextView titoloIndizio= (TextView) this.findViewById(R.id.popupIndiziTitolo);
            titoloIndizio.setText("Indizio zona San Lorenzo, Pin id: " + (pin.getPinId() + 1));
            TextView descrizioneIndizio = (TextView)this.findViewById(R.id.popupIndizioDescrizione);
            descrizioneIndizio.setText(pin.getIndizi().getStringaIndizio(numIndizio));

            this.setCancelable(false);
            this.setCanceledOnTouchOutside(false);
            Button btnOk = (Button) this.findViewById(R.id.popupIndiziBtnOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                    MapsActivity.setupPopupRiepilogoIndizi(pin);
                }
            });
        }
    }

    public class PopupFeedbackPositivo extends Dialog implements MyPopup {
        public Pin pin;

        public PopupFeedbackPositivo(Context context, Pin pin) {
            super(context);
            this.pin = pin;
            setUp();
        }

        public void setUp() {
            funzioneBase(this);
            this.setContentView(R.layout.feedback_positivo);
            pin.setConquistato(true);
            MapsActivity.gamePhase= GamePhase.PIN_CHOICE;
            MapsActivity.mPinTarget=null;
            MapsActivity.suggeritore.setText(R.string.scegliPinPartenza);
            Button btnOk = (Button) this.findViewById(R.id.popupFeedbackBtnAvanti);
           btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }

    public class PopupFeedbackNegativo extends Dialog implements MyPopup {
        public Pin pin;

        public PopupFeedbackNegativo(Context context, Pin pin) {
            super(context);
            this.pin = pin;
            setUp();
        }

        public void setUp() {
            funzioneBase(this);
            this.setContentView(R.layout.feedback_negativo);

            if (pin.getSfida().hasNextDomanda()){
                TextView popupFeedbackNegativoMessaggio = (TextView) this.findViewById(R.id.popupFeedbackMessaggio);
                popupFeedbackNegativoMessaggio.setText("Risposta Errata! Hai ancora "+ pin.getSfida().tentativiRimasti()+" tentativi. Premi sul tasto Ok per continuare");

                Button btnOk = (Button) this.findViewById(R.id.popupFeedbackBtnAvanti);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        MapsActivity.setupPopupSfida(pin);
                    }
                });
            }
            else{
                TextView popupFeedbackNegativoMessaggio = (TextView) this.findViewById(R.id.popupFeedbackMessaggio);
                popupFeedbackNegativoMessaggio.setText("Risposta Errata! Hai esaurito tutti i tentativi rimasti.\nHai conquistato il Pin senza ottenere nessun punteggio. Premi sul tasto Ok per tornare sulla mappa");

                Button btnOk = (Button) this.findViewById(R.id.popupFeedbackBtnAvanti);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();

                    }
                });
                pin.setConquistato(true);
                MapsActivity.gamePhase=GamePhase.PIN_CHOICE;
                pin.getPinMarker().setTitle(pin.getNome());
                MapsActivity.mPinTarget=null;
                MapsActivity.suggeritore.setText(R.string.scegliPinPartenza);
            }

        }
    }

}