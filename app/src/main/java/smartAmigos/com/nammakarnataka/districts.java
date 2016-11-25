package smartAmigos.com.nammakarnataka;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

import smartAmigos.com.nammakarnataka.adapter.districts_adapter;


public class districts extends AppCompatActivity {
    private List<districts_adapter> dist_adapterList = new ArrayList<>();

    MaterialRefreshLayout materialRefreshLayout;
    ListView list;
    FloatingActionButton fab;
    static int serverVersion, localVersion;
    Context context;

    InterstitialAd mInterstitialAd;
    private InterstitialAd interstitial;

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
        AdRequest adRequest = new AdRequest.Builder().build();

        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(districts.this);
        // Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));

        interstitial.loadAd(adRequest);
        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                // Call displayInterstitial() function
                if (interstitial.isLoaded()&&Math.random()>0.3) {
                    interstitial.show();
                }
            }
        });
        //Finish calling ads

        context = getApplicationContext();
        materialRefreshLayout = (MaterialRefreshLayout)findViewById(R.id.refresh);
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

        awesomeAlgorithm();

        if(!loadJsonFile()){
            if (isNetworkConnected()) {
                Toast.makeText(this, "please wait for a moment!", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences = getSharedPreferences("districts_version", Context.MODE_PRIVATE);
                localVersion = preferences.getInt("version", 0);
                new DistrictVersion().execute("http://nammakarnataka.net23.net/districts/districts_version.json");
            } else {
                Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        }


        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {

                if (isNetworkConnected()) {
                    SharedPreferences preferences = getSharedPreferences("districts_version", Context.MODE_PRIVATE);
                    localVersion = preferences.getInt("version", 0);
                    new DistrictVersion().execute("http://nammakarnataka.net23.net/districts/districts_version.json");
                } else {
                    Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                    materialRefreshLayout.finishRefresh();
                }
            }

        });

    }


    private void awesomeAlgorithm() {

        SharedPreferences preferences = getSharedPreferences("golmal", Context.MODE_PRIVATE);
        int b = preferences.getInt("district",0);
        if(b == 4){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("district",1);
            editor.commit();

            if(isNetworkConnected())
            new DistrictVersion().execute("http://nammakarnataka.net23.net/districts/districts_version.json");

        }else{
            if(b%3 == 0){
                Toast.makeText(this, "Swipe down to refresh Contents!", Toast.LENGTH_SHORT).show();
            }
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("district",++b);
            editor.commit();
        }
    }


    private boolean loadJsonFile() {
        dist_adapterList.clear();
        String ret = null;
        BufferedReader reader = null;
        File file = new File("/data/data/smartAmigos.com.nammakarnataka/districts.json");
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                reader = new BufferedReader(new InputStreamReader(fis));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                ret = builder.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null)
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

            try{
                JSONObject parent = new JSONObject(ret);
                JSONArray items = parent.getJSONArray("list");
                JSONObject child = null;
                for(int i=0;i<items.length();i++){
                     child = items.getJSONObject(i);
                    dist_adapterList.add(new districts_adapter(child.getString("name")));
                }
                displayList(items);
            }catch (JSONException e){
                e.printStackTrace();
            }

            return true;
        }
        return false;
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }



    private void saveJsonFile(String data) {
        FileOutputStream stream = null;
        try {
            File path = new File("/data/data/smartAmigos.com.nammakarnataka/districts.json");
            stream = new FileOutputStream(path);
            stream.write(data.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null)
                    stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class DistrictVersion extends AsyncTask<String, String, String> {
        HttpURLConnection connection;
        BufferedReader reader;

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                return builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject parent = new JSONObject(s);
                JSONObject news_version = parent.getJSONObject("districts_version");

                serverVersion = news_version.getInt("version");


            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (localVersion != serverVersion) {
                new districtFile().execute("http://nammakarnataka.net23.net/districts/districts.json");
            } else {
                Toast.makeText(context, "Districts List is up to date!", Toast.LENGTH_SHORT).show();
                materialRefreshLayout.finishRefresh();
            }

        }
    }


    public class districtFile extends AsyncTask<String, String, String> {

        HttpURLConnection connection;
        BufferedReader reader;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                String str = builder.toString();
                saveJsonFile(str);
                return str;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dist_adapterList.clear();

            SharedPreferences preferences = getSharedPreferences("districts_version", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("version", serverVersion);
            editor.apply();

            Toast.makeText(context, "Districts List updated!", Toast.LENGTH_SHORT).show();
            try{
                JSONObject parent = new JSONObject(s);
                JSONArray items = parent.getJSONArray("list");
                JSONObject child = null;
                for(int i=0;i<items.length();i++){
                    child = items.getJSONObject(i);
                    dist_adapterList.add(new districts_adapter(child.getString("name")));
                }
                materialRefreshLayout.finishRefresh();
                displayList(items);
            }catch (JSONException e){
                e.printStackTrace();
            }


        }
    }

    private void displayList(final JSONArray par) {
        ArrayAdapter<districts_adapter> adapter = new myDistListAdapterClass();

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                JSONObject child;
                try {
                    child = par.getJSONObject(position);
                    Fragment fragment = new distDisplayFragment(child);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_my_location, fragment);
                    ft.addToBackStack("districts");
                    ft.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }



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
