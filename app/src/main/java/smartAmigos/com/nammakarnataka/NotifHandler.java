package smartAmigos.com.nammakarnataka;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
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

import smartAmigos.com.nammakarnataka.adapter.DatabaseHelper;




public class NotifHandler extends Activity {

    Double latitude,longitude;
    TextView placename, description, district, bestseason, additionalInformation, gmapButton, nearby;
    SliderLayout image;
    TextSliderView textSliderView;
    DatabaseHelper myDBHelper;
    Context context;
    int id;
    InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);
        context = getApplicationContext();


//        AdRequest adRequest = new AdRequest.Builder().build();
//        interstitial = new InterstitialAd(NotifHandler.this);
//        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
//        interstitial.loadAd(adRequest);
//        interstitial.setAdListener(new AdListener() {
//            public void onAdLoaded() {
//                if (interstitial.isLoaded()) {
//                    interstitial.show();
//                }
//            }
//         });







        placename = (TextView) findViewById(R.id.noti_placename);
        description = (TextView) findViewById(R.id.noti_description);
        district = (TextView) findViewById(R.id.noti_district);
        bestseason = (TextView) findViewById(R.id.noti_beastseason);
        additionalInformation = (TextView) findViewById(R.id.noti_additionalInformation);
        gmapButton = (Button) findViewById(R.id.noti_gmapButton);
        image = (SliderLayout) findViewById(R.id.noti_images);
        nearby = (TextView) findViewById(R.id.noti_nearby);

        try {

            Bundle extras = getIntent().getExtras();
            String value = extras.getString("data");


            Log.d("NOTIFICATION DATA",value);

            JSONObject item = new JSONObject(value);
            JSONArray images = item.getJSONArray("image");


            Typeface myFont = Typeface.createFromAsset(getAssets(), "fonts/placenames.otf" );
            placename.setTypeface(myFont);

            placename.setText(item.getString("name"));


            description.setText(item.getString("description"));
            district.setText(item.getString("district"));
            bestseason.setText(item.getString("bestSeason"));
            additionalInformation.setText(item.getString("additionalInformation"));
            nearby.setText(item.getString("nearByPlaces"));

            latitude = item.getDouble("latitude");
            longitude = item.getDouble("longitude");

            id = item.getInt("id");

            int i=0;
            while(images.getString(i) != null){
                textSliderView = new TextSliderView(getApplicationContext());
                textSliderView
                        .image(images.getString(i))
                        .setScaleType(BaseSliderView.ScaleType.Fit);
                image.addSlider(textSliderView);
                i++;
            }

            image.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
            image.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            image.setCustomAnimation(new DescriptionAnimation());
            image.setDuration(60000);


        }catch (Exception e){

        }

        gmapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(
                        new Intent(
                                android.content.Intent.ACTION_VIEW,
                                Uri.parse("geo:" + latitude + "," + longitude + "?q=(" + placename.getText() + ")@" + latitude + "," + longitude)
                        )
                );

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
