package smartAmigos.com.nammakarnataka;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import smartAmigos.com.nammakarnataka.adapter.DatabaseHelper;
import smartAmigos.com.nammakarnataka.adapter.districts_adapter;
import smartAmigos.com.nammakarnataka.adapter.generic_adapter;


public class districts extends AppCompatActivity {
    private List<districts_adapter> dist_adapterList = new ArrayList<>();

    ListView list;
    FloatingActionButton fab;
    Context context;

    InterstitialAd mInterstitialAd;
    private InterstitialAd interstitial;

    DatabaseHelper myDBHelper;
    Cursor PlaceCursor;

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Call ads
//        AdRequest adRequest = new AdRequest.Builder().build();
//        interstitial = new InterstitialAd(districts.this);
//        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
//        interstitial.loadAd(adRequest);
//        interstitial.setAdListener(new AdListener() {
//            public void onAdLoaded() {
//                if (interstitial.isLoaded()&&Math.random()>0.3) {
//                    interstitial.show();
//                }
//            }
//        });
        //Finish calling ads

        context = getApplicationContext();
        list = (ListView)findViewById(R.id.districtList);

        TextView xt = (TextView)findViewById(R.id.xt);
        Typeface myFont = Typeface.createFromAsset(this.getAssets(), "fonts/Kaushan.otf" );
        xt.setTypeface(myFont);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }


        myDBHelper = new DatabaseHelper(context);
        PlaceCursor = myDBHelper.getAllDistricts();

        while(PlaceCursor.moveToNext()){
            dist_adapterList.add(new districts_adapter( PlaceCursor.getString(0)));
        }

        displayList();


    }


    private void displayList() {
        ArrayAdapter<districts_adapter> adapter = new myDistListAdapterClass();

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                PlaceCursor.moveToPosition(position);
                String dist = PlaceCursor.getString(0);


                    Fragment fragment = new distDisplayFragment(dist);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_my_location, fragment);
                    ft.addToBackStack("districts");
                    ft.commit();


            }
        });
    }


    public class myDistListAdapterClass extends ArrayAdapter<districts_adapter> {

        myDistListAdapterClass() {
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
