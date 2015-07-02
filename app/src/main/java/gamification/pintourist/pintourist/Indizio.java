package gamification.pintourist.pintourist;

import android.media.Image;

/**
 * Created by Marco on 11/06/2015.
 */
public class Indizio {

    private String [] indizi;
    private int level;
    private Image[] images;
    public Indizio(String [] arrayIndizi){
        indizi = new String [3];
    }

    public String getIndizi(int numeroIndizio) {
        return indizi[numeroIndizio];
    }
}
