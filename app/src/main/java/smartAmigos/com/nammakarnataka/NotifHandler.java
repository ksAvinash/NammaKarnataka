package smartAmigos.com.nammakarnataka;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

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
    private Button gmapButton;
    private String latitude, longitude, place_name;
    private SliderLayout layout_images;
    private TextSliderView textSliderView;
    private CardView comment_CardView;
    InterstitialAd mInterstitialAd;
    private InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_general);

//        AdRequest adRequest = new AdRequest.Builder().build();
//
//        // Prepare the Interstitial Ad
//        interstitial = new InterstitialAd(NotifHandler.this);
//        // Insert the Ad Unit ID
//        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
//
//        interstitial.loadAd(adRequest);
//        // Prepare an Interstitial Ad Listener
//        interstitial.setAdListener(new AdListener() {
//            public void onAdLoaded() {
//                // Call displayInterstitial() function
//                if (interstitial.isLoaded()) {
//                    interstitial.show();
//                }
//            }
//        });

        place_textView = (TextView) findViewById(R.id.place_textView);
        description_textView = (TextView) findViewById(R.id.description_textView);
        location_textView = (TextView) findViewById(R.id.location_textView);
        season_textView = (TextView) findViewById(R.id.season_textView);
        additionalInformation = (TextView) findViewById(R.id.additionalInformation);
        nearby_textView = (TextView) findViewById(R.id.nearby_textView);

        gmapButton = (Button) findViewById(R.id.gmapButton);

        layout_images = (SliderLayout) findViewById(R.id.layout_images);

        comment_CardView = (CardView) findViewById(R.id.comment_CardView);
        comment_CardView.setVisibility(View.GONE);


        Bundle extras = getIntent().getExtras();
        try {
            String place_name = extras.getString("placename");
            String description = extras.getString("description");
            String location = extras.getString("location");
            String best_season = extras.getString("bestseason");
            String nearby_places = extras.getString("nearby");
            String additional_info = extras.getString("addinfo");
            place_textView.setText(place_name);
            description_textView.setText(description);
            location_textView.setText(location);
            season_textView.setText(best_season);
            nearby_textView.setText(nearby_places);
            additionalInformation.setText(additional_info);
        }catch (Exception e){

        }

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
//            new GetDataTask().execute(new URL("http://charan.net23.net/getdata.php"));
        } catch (Exception e) {

        }

//        gmapButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    startActivity(
//                            new Intent(
//                                    android.content.Intent.ACTION_VIEW,
//                                    Uri.parse("geo:" + latitude + "," + longitude + "?q=(" + place_name + ")@" + latitude + "," + longitude)));
//                } catch (Exception e) {
//
//                }
//            }
//        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
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
                    String longit = json.getString("Long");
                    String season = json.getString("Season");
                    String nearby = json.getString("Nearby");
                    String addinfo = json.getString("Addinfo");
                    String imgUrl = json.getString("imgUrl");

                    latitude = lat;
                    longitude = longit;
                    place_name = place;

                    place_textView.setText(place);
                    description_textView.setText(description);
                    location_textView.setText(location + " ( " + lat + "," + longit + " ) ");
                    season_textView.setText(season);
                    nearby_textView.setText(nearby);
                    additionalInformation.setText(addinfo);

                    textSliderView = new TextSliderView(getApplicationContext());
                    textSliderView
                            .image(imgUrl)
                            .setScaleType(BaseSliderView.ScaleType.Fit);
                    layout_images.addSlider(textSliderView);


                    layout_images.setPresetTransformer(SliderLayout.Transformer.RotateDown);
                    layout_images.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    layout_images.setCustomAnimation(new DescriptionAnimation());
                    layout_images.setDuration(60000);

                }
            } catch (Exception e) {

            }
        }
    }
}
