package dharmawan.fp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {
    public ArrayList<Koordinat> point = null;
    private CustomView map;
    private Button show;
    private LinearLayout layout;
    private String URL = "";
    private ProgressDialog pDialog;
    //http://192.168.0.18/mobile_computing/index.php
    private Socket client;
    private String hasil = "";
    ArrayList<String> tabrakan;
    private static final String[] color = new String[] {
            "#000000","#FFFF00","#1CE6FF","#FF34FF","#FF4A46","#008941","#006FA6","#A30059",
            "#FFDBE5","#7A4900","#0000A6","#63FFAC","#B79762","#004D43","#8FB0FF","#997D87",
            "#5A0007","#809693","#FEFFE6","#1B4400","#4FC601","#3B5DFF","#4A3B53","#FF2F80",
            "#61615A","#BA0900","#6B7900","#00C2A0","#FFAA92","#FF90C9","#B903AA","#D16100",
            "#DDEFFF","#000035","#7B4F4B","#A1C299","#300018","#0AA6D8","#013349","#00846F",
            "#372101","#FFB500","#C2FFED","#A079BF","#CC0744","#C0B9B2","#C2FF99","#001E09",
            "#00489C","#6F0062","#0CBD66","#EEC3FF","#456D75","#B77B68","#7A8741","#788D66",
            "#885578","#FAD09F","#FF8A9A","#D157A0","#BEC459","#456648","#0086ED","#886F4C",
            "#34362D","#B4A8BD","#00A6AA","#452C2C","#636375","#A3C8C9","#FF913F","#938A81",
            "#575329","#00FECF","#B05B6F","#8CD0FF","#3B9700","#04F757","#C8A1A1","#1E6E00",
            "#7900D7","#A77500","#6367A9","#A05837","#6B002C","#772600","#D790FF","#9B9700",
            "#549E79","#FFF69F","#201625","#72418F","#BC23FF","#99ADC0","#3A2465","#922329",
            "#5B4534","#FDE8DC","#404E55","#0089A3","#CB7E98","#A4E804","#324E72","#6A3A4C",
            "#83AB58","#001C1E","#D1F7CE","#004B28","#C8D0F6","#A3A489","#806C66","#222800",
            "#BF5650","#E83000","#66796D","#DA007C","#FF1A59","#8ADBB4","#1E0200","#5B4E51",
            "#C895C5","#320033","#FF6832","#66E1D3","#CFCDAC","#D0AC94","#7ED379","#012C58"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        point = (ArrayList<Koordinat>) getIntent().getSerializableExtra("koordinat");
        URL = getIntent().getExtras().getString("url");
        map = new CustomView(this,false);
        findViewById();

        tabrakan = new CariTabrakan(point).output();
        if(URL.equals("")) {
            show.setText("Dikerjakan di local");
            Graph graph = new Graph(tabrakan);
            graph.colourVertices();
            int[] warna = graph.color;
            show(warna);
        }
        else {
            //dariserver(tabrakan);
            //show.setText(URL);
            new ClientTCP().execute();
        }
    }

    private void findViewById() {
        show = (Button) findViewById(R.id.btn_show);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        layout = (LinearLayout) findViewById(R.id.layout1);
        layout.addView(map);
    }

    private void show(int[] colornya) {
        for(int i=0;i<point.size();i++) {
            int ke = colornya[i]-1;
            if(ke > color.length) ke %= color.length;
            point.get(i).warna = color[ke];
        }
        map.point = point;
        map.invalidate();
    }

    private void dariserver(final ArrayList<String> tabrakan) {
        pDialog = new ProgressDialog(ResultActivity.this);
        pDialog.setMessage("Loading. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ResultActivity.this,response,Toast.LENGTH_LONG).show();
                        if(response.length() > 2) {
                            int[] warna = new int[(response.length()-2)/2+1];
                            int j=-1;
                            for(int i=1;i<response.length()-1;i+=2) {
                                warna[++j] = Integer.parseInt(String.valueOf(response.charAt(i)));
                            }
                            show.setText("Dikerjakan di "+URL);
                            show(warna);
                        }
                        pDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ResultActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        //finish();
                        pDialog.dismiss();
                        show.setText("Dikerjakan di local");
                        Graph graph = new Graph(tabrakan);
                        graph.colourVertices();
                        int[] warna = graph.color;
                        show(warna);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("jumlah",String.valueOf(point.size()));
                int i = 0;
                while(i < tabrakan.size()) {
                    params.put("data_"+i,tabrakan.get(i));
                    i++;
                }
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public class ClientTCP extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ResultActivity.this);
            pDialog.setMessage("Loading data. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(final String hasil) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    show.setText("Dikerjakan di "+URL);
                    String[] jawaban = hasil.split(",");
                    int[] warna = new int[jawaban.length];
                    int ke = -1;
                    for(int i=0;i<jawaban.length;i++) {
                        warna[++ke] = Integer.parseInt(jawaban[i]);
                    }
                    show(warna);
                }
            });
            super.onPostExecute(hasil);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                client = new Socket(URL, 4444);  //connect to server

                OutputStream outToServer = client.getOutputStream();
                DataOutputStream out = new DataOutputStream(outToServer);
                //out.writeUTF(s1 + "nn" + s2);
                int jumlah = tabrakan.size();
                out.writeUTF(String.valueOf(jumlah));
                for(int i=0;i<jumlah;i++) {
                    out.writeUTF(tabrakan.get(i));
                }
                InputStream inFromServer = client.getInputStream();
                DataInputStream in = new DataInputStream(inFromServer);
                hasil = in.readUTF();

                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return hasil;
        }
    }
}