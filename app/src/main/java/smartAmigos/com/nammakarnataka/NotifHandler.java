package smartAmigos.com.nammakarnataka;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import smartAmigos.com.nammakarnataka.adapter.DatabaseHelper;
import smartAmigos.com.nammakarnataka.adapter.nearby_places_adapter;




public class NotifHandler extends Activity {

    Double latitude,longitude;
    private String place_name, description,district,best_season,additional_info,nearby_places;
    private InterstitialAd interstitial;
    ListView list;


    private List<nearby_places_adapter> nearby_adapterList = new ArrayList<>();
    Context context;

    String imagesList[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();


        Log.d("Notification","Tapped the notification");

        setContentView(R.layout.layout_general);

        //popluate the list view
        list = (ListView) findViewById(R.id.nearbyPlaceList);
        View header = getLayoutInflater().inflate(R.layout.header, null);
        View footer = getLayoutInflater().inflate(R.layout.footer, null);
        list.addHeaderView(header);
        list.addFooterView(footer);




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
//        });



        TextView place_textView = (TextView) findViewById(R.id.place_textView);
        TextView description_textView = (TextView) findViewById(R.id.description_textView);
        TextView location_textView = (TextView) findViewById(R.id.location_textView);
        TextView season_textView = (TextView) findViewById(R.id.season_textView);
        TextView additionalInformation = (TextView) findViewById(R.id.additionalInformation);
        Button gmapButton = (Button) findViewById(R.id.gmapButton);
        SliderLayout layout_images = (SliderLayout) findViewById(R.id.layout_images);



        Bundle extras = getIntent().getExtras();
        try {

            String value = extras.getString("data");
            JSONObject parent = new JSONObject(value);
            JSONArray images = parent.getJSONArray("image");

            for (int j = 0; j < images.length(); j++) {
                imagesList[j] = images.getString(j);
            }

            place_name = parent.getString("name");
            description = parent.getString("description");
            district = parent.getString("district");
            latitude = parent.getDouble("latitude");
            longitude = parent.getDouble("longitude");
            best_season = parent.getString("bestSeason");
            additional_info = parent.getString("additionalInformation");
            nearby_places = parent.getString("nearByPlaces");

            Toast.makeText(getApplicationContext(),place_name,Toast.LENGTH_SHORT).show();


            place_textView.setText(place_name);
            description_textView.setText(description);
            location_textView.setText(district);
            season_textView.setText(best_season);
            additionalInformation.setText(additional_info);



            //Code for multiple images downloading
            for (String image: imagesList){
                TextSliderView textSliderView = new TextSliderView(getApplicationContext());
                textSliderView
                        .image(image)
                        .setScaleType(BaseSliderView.ScaleType.Fit);

                layout_images.addSlider(textSliderView);
            }
            layout_images.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
            layout_images.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            layout_images.setCustomAnimation(new DescriptionAnimation());
            layout_images.setDuration(7000);



            //To populate listview of multiple images
            String places[] = nearby_places.split(",");
            for (String place : places) {
                nearby_adapterList.add(new nearby_places_adapter(place));
            }
            displayList();


        }catch (Exception ignored){

        }

        gmapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(
                            new Intent(
                                    android.content.Intent.ACTION_VIEW,
                                    Uri.parse("geo:" + latitude + "," + longitude + "?q=(" + place_name + ")@" + latitude + "," + longitude)));
                } catch (Exception ignored) {

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }




    private void displayList() {
        ArrayAdapter<nearby_places_adapter> adapter = new nearbyPlaceAdapterClass();
        list.setAdapter(adapter);

    }



    public class nearbyPlaceAdapterClass extends ArrayAdapter<nearby_places_adapter> {

        nearbyPlaceAdapterClass() {
            super(context, R.layout.nearby_place_item, nearby_adapterList);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                itemView = inflater.inflate(R.layout.nearby_place_item, parent, false);

            }
            nearby_places_adapter current = nearby_adapterList.get(position);

            TextView t_name = (TextView) itemView.findViewById(R.id.item_nearbyPlace);
            t_name.setText(current.getNearPlace());

            return itemView;
        }

    }

}
