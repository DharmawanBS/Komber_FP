package dharmawan.fp;

import android.graphics.Point;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by gdwyn on 22-May-17.
 */

public class CariTabrakan {
    private ArrayList<Koordinat> point;
    public ArrayList<Point> tabrakan = new ArrayList<>();

    public CariTabrakan(ArrayList<Koordinat> point) {
        this.point = point;
        tabrakan.clear();
        for(int i=0;i<point.size();i++) {
            for(int j=i+1;j<point.size();j++) {
                if(berpotongan(point.get(i).x,point.get(i).y,point.get(j).x,point.get(j).y,point.get(i).radius,point.get(j).radius)) {
                    tabrakan.add(new Point(i,j));
                    tabrakan.add(new Point(j,i));
                }
            }
        }
    }

    private boolean berpotongan(float x1,float y1,float x2,float y2,int radius1, int radius2) {
        if((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2) <= (radius1+radius2)*(radius1+radius2)) return true;
        return false;
    }

    public ArrayList<String> output () {
        ArrayList<String> hasil = new ArrayList();
        ArrayList<Point> x = tabrakan;
        if(point != null) {
            for(int i=0;i<point.size();i++) {
                String a = i+":";
                for(int j=0;j<x.size();j++) {
                    if(x.get(j).x == i) {
                        a+="-"+x.get(j).y;
                    }
                }
                hasil.add(a);
            }
        }
        return hasil;
    }
}