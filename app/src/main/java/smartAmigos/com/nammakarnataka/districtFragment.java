package smartAmigos.com.nammakarnataka;


import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
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
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import smartAmigos.com.nammakarnataka.adapter.DatabaseHelper;
import smartAmigos.com.nammakarnataka.adapter.districts_adapter;


public class districtFragment extends Fragment {





    Context context;
    ListView list;
    TextView t;
    DatabaseHelper myDBHelper;
    Cursor PlaceCursor;

    static SimpleDraweeView draweeView;
    private List<districts_adapter> dist_adapterList = new ArrayList<>();
    InterstitialAd interstitial;
    AdView NKBannerAds;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_district, container, false);
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



        list = (ListView) view.findViewById(R.id.districtsList);

        dist_adapterList.clear();

        myDBHelper = new DatabaseHelper(context);
        PlaceCursor = myDBHelper.getAllDistricts();

        while(PlaceCursor.moveToNext()){
            dist_adapterList.add(new districts_adapter( PlaceCursor.getString(0)));
        }

        displayList();


        return view;
    }

    private void displayList() {
        ArrayAdapter<districts_adapter> adapter = new districtAdapterClass();

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                PlaceCursor.moveToPosition(position);
                String dist = PlaceCursor.getString(0);


                Fragment fragment = new distDisplayFragment(dist);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack("districts");
                ft.commit();


            }
        });
    }

    public class districtAdapterClass extends ArrayAdapter<districts_adapter> {

        districtAdapterClass() {
            super(context, R.layout.district_item, dist_adapterList);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                itemView = inflater.inflate(R.layout.district_item, parent, false);

            }
            districts_adapter current = dist_adapterList.get(position);

            TextView t_name = (TextView) itemView.findViewById(R.id.item_distTitle);
            t_name.setText(current.getDistrict());

            return itemView;
        }

    }


}
