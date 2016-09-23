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

        AdRequest adRequest = new AdRequest.Builder().build();

        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(NotifHandler.this);
        // Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));

        interstitial.loadAd(adRequest);
        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                // Call displayInterstitial() function
                if (interstitial.isLoaded()) {
                    interstitial.show();
                }
            }
        });

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
            place_name = extras.getString("placename");
            String description = extras.getString("description");
            String location = extras.getString("location");
            latitude = extras.getString("latitude");
            longitude = extras.getString("longitude");
            String best_season = extras.getString("bestseason");
            String nearby_places = extras.getString("nearby");
            String additional_info = extras.getString("addinfo");
            String image_url = extras.getString("imgUrl");
            place_textView.setText(place_name);
            description_textView.setText(description);
            location_textView.setText(location+"( "+latitude+", "+longitude+" )");
            season_textView.setText(best_season);
            nearby_textView.setText(nearby_places);
            additionalInformation.setText(additional_info);

            textSliderView = new TextSliderView(getApplicationContext());
            textSliderView
                    .image(image_url)
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            layout_images.addSlider(textSliderView);


            layout_images.setPresetTransformer(SliderLayout.Transformer.RotateDown);
            layout_images.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            layout_images.setCustomAnimation(new DescriptionAnimation());
            layout_images.setDuration(60000);
        }catch (Exception e){

        }

        gmapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(
                            new Intent(
                                    android.content.Intent.ACTION_VIEW,
                                    Uri.parse("geo:" + latitude + "," + longitude + "?q=(" + place_name + ")@" + latitude + "," + longitude)));
                } catch (Exception e) {

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }
}
