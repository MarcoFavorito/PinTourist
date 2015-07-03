package gamification.pintourist.pintourist.meccanica;

import android.media.Image;
import gamification.pintourist.pintourist.meccanica.*;

/**
 * Created by Marco on 11/06/2015.
*/
public class Sfida {

    private Image immagineSfida;
    private int level;
    private Domanda domande [];

    public Sfida(Domanda [] domande, Image immagine) {

        this.domande = domande;
        this.immagineSfida = immagine;
        this.level=-1;
    }

    public Domanda[] getDomande() {
        return domande;
    }
    public Image getImmagineSfida() {
        return immagineSfida;
    }
    public int getLevel() {
        return level;
    }
    public Domanda getCurrentDomanda(){
        if (this.level>=0 && this.level<=domande.length){
            return this.domande[level];
        }
        return null;
    }

    public void setImmagineSfida(Image immagineSfida) {
        this.immagineSfida = immagineSfida;
    }
    public void setDomande(Domanda[] domande) {
        this.domande = domande;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    
    public Domanda getNextDomanda(){
        if (this.level<this.domande.length){
            level++;
            return domande[level];
        }
        return null;
    }
    public Domanda getDomandaIndex(int index){
        return domande[index];
    }
    /*
    public void setDomandeLivello(int livello, Domanda a){
        int i = getlevel(this.domande[livello]);
        if (i == -1) throw new domandePieneException("Già sono state inserite 3 domande di livello "+livello+" per questo pin");
        else this.domande[livello][i] = a;
    }



    static class domandePieneException extends RuntimeException{
        public domandePieneException(String msg){
            super(msg);
        }
    }

    private int getlevel(Domanda[] a){
    int i = 0;
    while (a[i]!= null) i++;
    if (i == a.length -1) return -1;
    return i;
}
*/

}
