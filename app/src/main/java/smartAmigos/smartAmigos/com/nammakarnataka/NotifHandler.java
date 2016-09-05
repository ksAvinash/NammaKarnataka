package smartAmigos.smartAmigos.com.nammakarnataka;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by CHARAN on 9/6/2016.
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

//        Bundle extras = getIntent().getExtras();
//        if(extras.containsKey("useDB")){
//            String useDB = extras.getString("useDB");
//            Log.i("useDB",useDB);
//            if(useDB.equals("true")){
//
//            } else {
//                if(extras.containsKey("place")&&extras.containsKey("description")&&extras.containsKey("location")&&extras.containsKey("lat")&&extras.containsKey("long")
//                        &&extras.containsKey("season")&&extras.containsKey("nearby")&&extras.containsKey("addinfo")){
//                    place_textView.setText(extras.getString("place"));
//                    description_textView.setText(extras.getString("description"));
//                    location_textView.setText(extras.getString("location")+" ( "+extras.getString("lat")+","+extras.getString("long")+" ) ");
//                    season_textView.setText(extras.getString("season"));
//                    nearby_textView.setText(extras.getString("nearby_textView"));
//                    additionalInformation.setText(extras.getString("addinfo"));
//                }
//            }
//
//        }

        try {
            new GetDataTask().execute(new URL("http://charan.net23.net/getdata.php"));
        }catch (Exception e){

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
        protected void onPreExecute() {
            // super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONArray jArray = new JSONArray(result);

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    String place = json.getString("Place");
                    String description = json.getString("Description");
                    String location = json.getString("Location");
                    String lat = json.getString("Lat");
                    String longitude = json.getString("Long");
                    String season = json.getString("Season");
                    String nearby = json.getString("Nearby");
                    String addinfo = json.getString("Addinfo");

                    place_textView.setText(place);
                    description_textView.setText(description);
                    location_textView.setText(location+" ( "+lat+","+longitude+" ) ");
                    season_textView.setText(season);
                    nearby_textView.setText(nearby);
                    additionalInformation.setText(addinfo);

                }
            } catch (Exception e) {

            }
        }
    }
}
