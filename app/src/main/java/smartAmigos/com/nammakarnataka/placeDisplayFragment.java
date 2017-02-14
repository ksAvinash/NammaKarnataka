package smartAmigos.com.nammakarnataka;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import smartAmigos.com.nammakarnataka.adapter.DatabaseHelper;
import smartAmigos.com.nammakarnataka.adapter.nearby_places_adapter;


@SuppressLint("ValidFragment")
public class placeDisplayFragment extends Fragment {

    int img_id;
    private Button gmapButton;
    private double latitude, longitude;
    SliderLayout mDemoSlider;
    private InterstitialAd interstitial;
    DatabaseHelper myDBHelper;
    TextView t;
    ListView list;
    Context context;

    String places[];
    public placeDisplayFragment(){

    }

    String nearPlaces;


    public placeDisplayFragment(int img_id) {
        this.img_id = img_id;
    }


    private List<nearby_places_adapter> nearby_adapterList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_general, container, false);

        list = (ListView) view.findViewById(R.id.nearbyPlaceList);


        View header = getActivity().getLayoutInflater().inflate(R.layout.header, null);
        View footer = getActivity().getLayoutInflater().inflate(R.layout.footer, null);

        list.addHeaderView(header);
        list.addFooterView(footer);



        final TextView place_textView = (TextView) view.findViewById(R.id.place_textView);
        TextView description_textView = (TextView) view.findViewById(R.id.description_textView);
        TextView location_textView = (TextView) view.findViewById(R.id.location_textView);
        TextView season_textView = (TextView) view.findViewById(R.id.season_textView);
        TextView additionalInformation = (TextView) view.findViewById(R.id.additionalInformation);




        Typeface myFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/placenames.otf" );
        place_textView.setTypeface(myFont);
        context = getActivity().getApplicationContext();


        gmapButton = (Button) view.findViewById(R.id.gmapButton);
        mDemoSlider = (SliderLayout) view.findViewById(R.id.layout_images);

        //Call ads
//        AdRequest adRequest = new AdRequest.Builder().build();
//        interstitial = new InterstitialAd(getContext());
//        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
//        interstitial.loadAd(adRequest);
//        interstitial.setAdListener(new AdListener() {
//            public void onAdLoaded() {
//                if (interstitial.isLoaded()&&Math.random()>0.85) {
//                    interstitial.show();
//                }
//            }
//        });
        //Finish calling ads



        nearby_adapterList.clear();


        myDBHelper = new DatabaseHelper(getContext());
        Cursor cursor = myDBHelper.getPlaceById(img_id);

        while (cursor.moveToNext()){
            place_textView.setText(cursor.getString(1));
            description_textView.setText(cursor.getString(2));
            location_textView.setText(cursor.getString(3));
            season_textView.setText(cursor.getString(4));
            additionalInformation.setText(cursor.getString(5));
            nearPlaces = cursor.getString(6);
            latitude = cursor.getDouble(7);
            longitude = cursor.getDouble(8);
        }


        gmapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(
                            new Intent(
                                    android.content.Intent.ACTION_VIEW,
                                    Uri.parse("geo:" + latitude + "," + longitude + "?q=(" + place_textView.getText() + ")@" + latitude + "," + longitude)));

            }
        });


    //code for multiple images loading starts
        TextSliderView textSliderView;
        String[] imagesArray = new String[25];
        Cursor imageURLCursor = myDBHelper.getAllImagesArrayByID(img_id);
        for (int i=0;imageURLCursor.moveToNext();i++){
            imagesArray[i] = imageURLCursor.getString(1);
        }

        for(int i = 0; i < imageURLCursor.getCount(); i++) {
            textSliderView = new TextSliderView(getContext());
            textSliderView
                    .image(imagesArray[i])
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(7000);
    //Code for multiple images loading ends




        places = nearPlaces.split(",");


        for (String place : places) {
            nearby_adapterList.add(new nearby_places_adapter(place));
        }
        displayList();


        return view;
    }



    private void displayList() {
        ArrayAdapter<nearby_places_adapter> adapter = new nearbyPlaceAdapterClass();

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myDBHelper = new DatabaseHelper(context);

                String name[] = places[position-1].split(" ");
                Cursor cursor = myDBHelper.getPlaceByString(name[0]);

                Fragment fragment = new SearchResults(cursor);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.content_main, fragment);
                ft.commit();
            }
        });
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
