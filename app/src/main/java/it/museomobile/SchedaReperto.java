package it.museomobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Giovanni on 30/10/2017.
 */

public class SchedaReperto extends Activity implements View.OnClickListener{

    private final String TAG = MainActivity.class.getSimpleName();

    private TextView statusMessage;
    private static final int RC_BARCODE_CAPTURE = 9001;
    private String operaId;

    private String ip= "192.168.1.101";
    //private String ip="192.168.1.74";
    private TextView descrizShort;//modificare prova
    private TextView idView;

    private HashMap<String, String> operaDarte;
    //private JSONArray opera;





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheda_reperto);
        Intent intent = getIntent();
        String pkg = getPackageName();

        operaDarte =  new HashMap<>();
        statusMessage = (TextView)findViewById(R.id.status_message);
        idView = (TextView) findViewById(R.id.operaId);
        descrizShort = (TextView) findViewById(R.id.descrizione);
        operaId = (String)intent.getSerializableExtra(pkg + ".operaId");

        new GetOpera().execute();
    }

    private class GetOpera extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://"+ip+":8000/api/OperaDarte/";//http://10.0.2.2:8000/api/Rilevazione/
            String jsonStr = sh.makeServiceCall(url);


            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    String length = jsonObj.getString("length");
                    JSONArray opere = jsonObj.getJSONArray("json");
                    for(int i = 0;i<opere.length();i++) {
                        JSONObject c = opere.getJSONObject(i);
                            if(c.getString("id").equals( operaId)){

                                String idOpera = c.getString("id");
                                String AnnoPubblicazione = c.getString("AnnoPubblicazione");
                                String titolo = c.getString("Titolo");
                                String descrizioneShort = c.getString("DescrizioneShort");
                                String descrizioneEstesa = c.getString("DescrizioneEstesa");
                                String vsMuseo = c.getString("vsMuseo");
                                String vsTipologia = c.getString("vsTipologia");  // tipologia get
                                String vsArtista = c.getString("vsArtista"); // artista get

                                String audio = c.getString("Audio");
                                String video = c.getString("Video");       // da completare

                                operaDarte.put("idOperaDarte",idOpera);
                                operaDarte.put("Titolo",titolo);
                                operaDarte.put("DescrizioneShort",descrizioneShort);
                                operaDarte.put("","");
                                operaDarte.put("","");
                                operaDarte.put("","");

                                System.out.println(operaDarte.get("Titolo"));

                        }
                        //id.setText(c.getString("Titolo"));
                        /*String time = c.getString("Timestamp");
                        String[] timeSplit = time.replace("T"," ").replace("Z","").split("[- :]");
                        String day = timeSplit[2];
                        String month = (Integer.parseInt(timeSplit[1]))+"";
                        String year = timeSplit[0].substring(2,4);
                        String hours = (Integer.parseInt(timeSplit[3])+2)+"";
                        String minutes = timeSplit[4];
                        String seconds = timeSplit[5].substring(0,2);
                        HashMap<String, String> detec = new HashMap<>();
                        detec.put("timeStamp",day+" "+month+" "+year+"  "+hours+":"+minutes+":"+seconds);
                        detec.put("detection",c.getInt("IdRilevazione")+"");
                        opereDarte.add(detec);*/
                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            idView.setText(operaDarte.get("Titolo"));
            descrizShort.setText(operaDarte.get("DescrizioneShort"));
        }
    }



    public void onClick(View v) {


    }

    public void showToast(String string){
        Context context = getApplicationContext();
        CharSequence text = string;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, -500);
        toast.show();
    }
    @Override
    protected void onStart() {
        Log.i(TAG, "onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }


}
