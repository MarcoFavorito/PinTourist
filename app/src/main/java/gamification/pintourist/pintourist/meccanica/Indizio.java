package gamification.pintourist.pintourist.meccanica;

import android.media.Image;

/**
 * Created by Marco on 11/06/2015.
 */
public class Indizio {

    private String [] indizi;
    private int level;
    private Image[] images;
    public Indizio(String [] arrayIndizi){
        indizi = arrayIndizi;
        level=-1;
    }

    public Indizio(String [] arrayIndizi, Image[] images){
        indizi = arrayIndizi;
        this.level=-1;
        this.images=images;
    }
    public String getIndizi(int numeroIndizio) {
        return indizi[numeroIndizio];
    }

    public int getLevel() {
        return level;
    }

    public String getNextIndizio(){
        if (this.level<2){
            level++;
            return indizi[level];
        }
        return null;
    }
}
