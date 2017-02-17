package smartAmigos.com.nammakarnataka;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import smartAmigos.com.nammakarnataka.adapter.DatabaseHelper;
import smartAmigos.com.nammakarnataka.adapter.generic_adapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResults extends Fragment {

    public SearchResults(){}

    Cursor PlaceCursor;
    InterstitialAd mInterstitialAd;
    private InterstitialAd interstitial;
    private List<generic_adapter> search_adapterList = new ArrayList<>();

    static SimpleDraweeView draweeView;
    View view;
    Context context;
    ListView list;
    TextView t;
    DatabaseHelper myDBHelper;

    @SuppressLint("ValidFragment")
    public SearchResults(Cursor PlaceCursor) {
        this.PlaceCursor = PlaceCursor;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search, container, false);
        context = getActivity().getApplicationContext();


        t = (TextView) view.findViewById(R.id.tr1);
        Typeface myFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Kaushan.otf" );
        t.setTypeface(myFont);


        //Call ads
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitial = new InterstitialAd(context);
        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
        interstitial.loadAd(adRequest);
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                if (interstitial.isLoaded()&&Math.random()>0.6) {
                    interstitial.show();
                }
            }
        });
        // Finish calling ads



        list = (ListView) view.findViewById(R.id.searchList);
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        Fresco.initialize(getActivity());

        myDBHelper = new DatabaseHelper(context);
        while(PlaceCursor.moveToNext()){

            String [] imagesArray = new String[25];
            Cursor imageURLCursor = myDBHelper.getAllImagesArrayByID(PlaceCursor.getInt(0));
            for (int i=0;imageURLCursor.moveToNext();i++){
                imagesArray[i] = imageURLCursor.getString(1);
            }

            search_adapterList.add(
                    new generic_adapter(
                            imagesArray,        //id
                            PlaceCursor.getString(1),//name
                            PlaceCursor.getString(2),//description
                            PlaceCursor.getString(3),//district
                            PlaceCursor.getString(4),//best season
                            PlaceCursor.getString(5),//additional info
                            PlaceCursor.getString(6),//nearby place
                            PlaceCursor.getDouble(7),//latitude
                            PlaceCursor.getDouble(8) //longitude
                    ));
        }

        displayList();


        return view;

    }


    private void displayList() {
        ArrayAdapter<generic_adapter> adapter = new mySearchListAdapterClass();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PlaceCursor.moveToPosition(position);
                int img_id = PlaceCursor.getInt(0);

                Fragment fragment = new placeDisplayFragment(img_id);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();

            }
        });
    }


    public class mySearchListAdapterClass extends ArrayAdapter<generic_adapter> {

        mySearchListAdapterClass() {
            super(context, R.layout.item, search_adapterList);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                itemView = inflater.inflate(R.layout.item, parent, false);

            }
            generic_adapter current = search_adapterList.get(position);

            //Code to download image from url and paste.
            Uri uri = Uri.parse(current.getImage()[0]);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.item_Image);
            draweeView.setImageURI(uri);
            //Code ends here.
            TextView t_name = (TextView) itemView.findViewById(R.id.item_Title);
            t_name.setText(current.getTitle());

            TextView t_dist = (TextView) itemView.findViewById(R.id.item_Dist);
            t_dist.setText(current.getDistrict());

            return itemView;
        }


    }
}
