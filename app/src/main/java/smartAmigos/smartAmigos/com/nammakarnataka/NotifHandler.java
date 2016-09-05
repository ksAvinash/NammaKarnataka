package smartAmigos.smartAmigos.com.nammakarnataka;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by CHARAN on 9/5/2016.
 */
public class NotifHandler extends Activity {
    private TextView place_textView, description_textView, location_textView, season_textView, additionalInformation, nearby_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_general);

        place_textView = (TextView) findViewById(R.id.place_textView);
        description_textView = (TextView) findViewById(R.id.description_textView);
        location_textView = (TextView) findViewById(R.id.location_textView);
        season_textView = (TextView) findViewById(R.id.season_textView);
        additionalInformation = (TextView) findViewById(R.id.additionalInformation);
        nearby_textView = (TextView) findViewById(R.id.nearby_textView);

        Bundle extras = getIntent().getExtras();
        String defNotif = extras.getString("defNotif");

        if(defNotif.equals(true)){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }else {

            try {
                new GetDataTask().execute(new URL("http://charan.net23.net/getdata.php"));
            } catch (Exception e) {

            }
        }

    }

    public class GetDataTask extends AsyncTask<URL, Void, String> {
        private HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(URL... params) {

            StringBuilder builder = new StringBuilder();
            String result = "";
            try {
                URL url = params[0];
                Log.i("URL :", url.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(20 * 1000);
                urlConnection.setReadTimeout(20 * 1000);
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String response;
                while ((response = reader.readLine()) != null) {
                    builder.append(response + "\n");
//                    Log.i("Response",response.toString());
                    result = builder.toString();
                }
                in.close();
            } catch (Exception e) {

            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONArray jArray = new JSONArray(result);

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    String Place = json.getString("Place");
                    String Description = json.getString("Description");
                    String Location = json.getString("Location");
                    String Lat = json.getString("Lat");
                    String Long = json.getString("Long");
                    String Season = json.getString("Season");
                    String Nearby = json.getString("Nearby");
                    String Addinfo = json.getString("Addinfo");

                    place_textView.setText(Place);
                    description_textView.setText(Description);
                    location_textView.setText(Location + " ( " + Lat + "," + Long + " )");
                    season_textView.setText(Season);
                    nearby_textView.setText(Nearby);
                    additionalInformation.setText(Addinfo);
                }
            } catch (Exception e) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
