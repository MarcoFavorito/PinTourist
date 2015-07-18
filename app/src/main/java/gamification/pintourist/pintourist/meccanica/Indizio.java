package gamification.pintourist.pintourist.meccanica;

import android.media.Image;

/**
 * Created by Marco on 11/06/2015.
 */
public class Indizio {

    private String [] stringheIndizi;
    private int level;
    private Image[] images;

    public Indizio(String [] arrayIndizi){
        stringheIndizi = arrayIndizi;
        level=-1;
    }

    public Indizio(String [] arrayIndizi, Image[] images){
        stringheIndizi = arrayIndizi;
        this.level=-1;
        this.images=images;
    }
    public String getStringaIndizio(int numeroIndizio) {
        return stringheIndizi[numeroIndizio];
    }
    public int getLevel() {
        return level;
    }
    public String getNextIndizio(){
        if (this.level<2){
            level++;
            return stringheIndizi[level];
        }
        return null;
    }
    public String[] getStringheIndizi() {
        return stringheIndizi;
    }
    public boolean hasNextIndizio(){
        return this.level<this.stringheIndizi.length-1;
    }
}
