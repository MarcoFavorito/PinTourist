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
        level=0;
    }

    public Indizio(String [] arrayIndizi, Image[] images){
        indizi = arrayIndizi;
        this.level=0;
        this.images=images;
    }
    public String getIndizi(int numeroIndizio) {
        return indizi[numeroIndizio];
    }

    public String getNextIndizio(){
        if (this.level<3){
            level++;
            return indizi[level-1];
        }
        return null;
    }
}
