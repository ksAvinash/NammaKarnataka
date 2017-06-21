package smartAmigos.com.nammakarnataka;


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
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import smartAmigos.com.nammakarnataka.adapter.DatabaseHelper;
import smartAmigos.com.nammakarnataka.adapter.generic_adapter;


public class templesFragment extends Fragment {

    InterstitialAd mInterstitialAd;
    private InterstitialAd interstitial;
    private List<generic_adapter> temples_adapterList = new ArrayList<>();

    static SimpleDraweeView draweeView;

    View view;
    Context context;
    ListView list;
    TextView t;
    DatabaseHelper myDBHelper;
    Cursor PlaceCursor;
    AdView NKBannerAds;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_temples, container, false);
        context = getActivity().getApplicationContext();
        if(Math.random() > 0.95){
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitial = new InterstitialAd(context);
            interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
            interstitial.loadAd(adRequest);
            interstitial.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    if (interstitial.isLoaded()) {
                        interstitial.show();
                    }
                }
            });
        }



        list = (ListView) view.findViewById(R.id.templeList);

        temples_adapterList.clear();

        Fresco.initialize(getActivity());

        myDBHelper = new DatabaseHelper(context);
        PlaceCursor = myDBHelper.getAllTemples();

        while(PlaceCursor.moveToNext()){

            String [] imagesArray = new String[25];
            Cursor imageURLCursor = myDBHelper.getAllImagesArrayByID(PlaceCursor.getInt(0));
                for (int i=0;imageURLCursor.moveToNext();i++){
                    imagesArray[i] = imageURLCursor.getString(1);
                }

            temples_adapterList.add(
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
        ArrayAdapter<generic_adapter> adapter = new myTempleListAdapterClass();
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

    public class myTempleListAdapterClass extends ArrayAdapter<generic_adapter> {

        myTempleListAdapterClass() {
            super(context, R.layout.item, temples_adapterList);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                itemView = inflater.inflate(R.layout.item, parent, false);

            }
            generic_adapter current = temples_adapterList.get(position);

            //Code to download image from url and paste.
            Uri uri = Uri.parse(current.getImage()[0]);

            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.item_Image);
            draweeView.getHierarchy().setProgressBarImage(new CircleProgressBarDrawable(1));
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