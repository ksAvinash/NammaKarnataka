package smartAmigos.com.nammakarnataka;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import smartAmigos.com.nammakarnataka.adapter.DatabaseHelper;
import smartAmigos.com.nammakarnataka.adapter.nearby_places_adapter;


@SuppressLint("ValidFragment")
public class placeDisplayFragment extends Fragment {

    int img_id;
    private Button gmapButton;
    private double latitude, longitude;
    private InterstitialAd interstitial;
    DatabaseHelper myDBHelper;
    TextView t;
    ListView list;
    Context context;

    String places[];
    public placeDisplayFragment(){

    }

    String nearPlaces,placename;


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

        SimpleDraweeView gallery_icon = (SimpleDraweeView) view.findViewById(R.id.gallery_icon);
        SimpleDraweeView favourite_icon = (SimpleDraweeView) view.findViewById(R.id.fav_icon);
        SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.layout_images);
        SimpleDraweeView visited_icon = (SimpleDraweeView) view.findViewById(R.id.visited_icon);



        Typeface myFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/placenames.otf" );
        place_textView.setTypeface(myFont);
        context = getActivity().getApplicationContext();


        gmapButton = (Button) view.findViewById(R.id.gmapButton);

        //Call ads
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitial = new InterstitialAd(getContext());
        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
        interstitial.loadAd(adRequest);
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                if (interstitial.isLoaded()&&Math.random()>0.7) {
                    interstitial.show();
                }
            }
        });
        //Finish calling ads



        nearby_adapterList.clear();


        myDBHelper = new DatabaseHelper(getContext());
        Cursor cursor = myDBHelper.getPlaceById(img_id);

        while (cursor.moveToNext()){
            placename = cursor.getString(1);
            place_textView.setText(placename);
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







        String[] imagesArray = new String[25];
        Cursor imageURLCursor = myDBHelper.getAllImagesArrayByID(img_id);
        for (int i=0;imageURLCursor.moveToNext();i++){
            imagesArray[i] = imageURLCursor.getString(1);
        }
        Uri uri = Uri.parse(imagesArray[1]);
        draweeView.getHierarchy().setProgressBarImage(new CircleProgressBarDrawable(2));
        draweeView.setImageURI(uri);



        places = nearPlaces.split(",");


        for (String place : places) {
            nearby_adapterList.add(new nearby_places_adapter(place));
        }
        displayList();



        gallery_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new GalleryFragment(img_id,placename);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });


        visited_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"Nice, you have visited "+placename, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                myDBHelper = new DatabaseHelper(context);
                myDBHelper.insertIntoVisited(img_id);

            }
        });


        favourite_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, placename+" Added to Favourites list", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                myDBHelper = new DatabaseHelper(context);
                myDBHelper.insertIntoFavourites(img_id);

            }

        });


        return view;
    }



    private void displayList() {
        ArrayAdapter<nearby_places_adapter> adapter = new nearbyPlaceAdapterClass();

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try{
                    myDBHelper = new DatabaseHelper(context);

                    String name[] = places[position-1].split(" ");
                    Cursor cursor = myDBHelper.getPlaceByString(name[0]);

                    Fragment fragment = new SearchResults(cursor);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    ft.replace(R.id.content_main, fragment);
                    ft.commit();

                }catch (Exception e){

                }


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
