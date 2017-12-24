package dharmawan.fp;

import java.io.Serializable;

/**
 * Created by gdwyn on 21-May-17.
 */

public class Koordinat implements Serializable {
    float x,y;
    int radius;
    String warna;

    public Koordinat(float x, float y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.warna = "";
    }
}
