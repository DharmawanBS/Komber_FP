package dharmawan.fp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by gdwyn on 21-May-17.
 */

public class CustomView extends View {
    Bitmap mBitmap;
    Paint paint,paint2;
    public ArrayList<Koordinat> point;
    public int r = 30;
    private boolean edit = true;

    public CustomView(Context context,boolean edit) {
        super(context);
        this.edit = edit;
        point = new ArrayList();
        mBitmap = Bitmap.createBitmap(400, 800, Bitmap.Config.ARGB_8888);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint2 = new Paint();
        paint2.setColor(Color.BLACK);
        paint2.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int k = 0;
        while(k < point.size()) {
            if(!edit) {
                paint.setColor(Color.parseColor(point.get(k).warna));
                paint2.setColor(Color.parseColor(point.get(k).warna));
            }
            canvas.drawCircle(point.get(k).x, point.get(k).y, 5, paint);
            canvas.drawCircle(point.get(k).x, point.get(k).y, point.get(k).radius, paint2);
            k++;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(edit) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                float x = event.getX();
                float y = event.getY();
                point.add(new Koordinat(x, y, r));
                invalidate();
            }
            return false;
        }
        return false;
    }
}