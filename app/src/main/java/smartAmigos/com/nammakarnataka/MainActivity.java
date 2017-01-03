package smartAmigos.com.nammakarnataka;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.pushbots.push.Pushbots;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import smartAmigos.com.nammakarnataka.adapter.DatabaseHelper;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView t;
    SliderLayout mDemoSlider;
    FloatingActionButton fab;
    DrawerLayout drawer;
    static int serverVersion, localVersion;
    ProgressDialog pd;


    DatabaseHelper myDBHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pd = new ProgressDialog(this);


        Pushbots.sharedInstance().init(getApplicationContext());
        Pushbots.sharedInstance().setCustomHandler(customHandler.class);

        t = (TextView) findViewById(R.id.listNews);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        Typeface myFont = Typeface.createFromAsset(getAssets(), "fonts/Kaushan.otf");
        t.setTypeface(myFont);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                mDemoSlider = (SliderLayout) findViewById(R.id.mainActivitySlider);
                final HashMap<String, Integer> file_maps = new HashMap<>();
                //Positively do not change any images
                file_maps.put("TB Dam", R.drawable.tb_dam);
                file_maps.put("Jog Falls", R.drawable.jog);
                file_maps.put("Mysore Palace", R.drawable.mysuru);
                file_maps.put("Mullayanagiri", R.drawable.mullayanagiri);
                file_maps.put("Dandeli", R.drawable.dandeli1);
                file_maps.put("Wonder La",R.drawable.wonderla);

                for (String name : file_maps.keySet()) {
                    TextSliderView textSliderView = new TextSliderView(getApplicationContext());
                    // initialize a SliderLayout
                    textSliderView
                            .description(name)
                            .image(file_maps.get(name))
                            .setScaleType(BaseSliderView.ScaleType.Fit);

                    //add your extra information
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle()
                            .putString("extra", name);

                    mDemoSlider.addSlider(textSliderView);
                }

                mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                mDemoSlider.setDuration(7000);

            }
        }).start();


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        if(isNetworkConnected()){
            SharedPreferences preferences = getSharedPreferences("base_version", Context.MODE_PRIVATE);
            localVersion = preferences.getInt("version", 0);
            new baseNewsVersion().execute("http://nammakarnataka.net23.net/general/base_version.json");
        }


    }



    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public class baseNewsVersion extends AsyncTask<String, String, String> {
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
                JSONObject base_version = parent.getJSONObject("base_version");
                serverVersion = base_version.getInt("version");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (localVersion != serverVersion) {
                pd.setMessage("Fetching new places please wait..");
                pd.setCancelable(false);
                pd.show();
                new baseFile().execute("http://nammakarnataka.net23.net/general/base.json");
            }else {
                Toast.makeText(getApplicationContext(), "All places are up to date!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public class baseFile extends AsyncTask<String, String, String> {

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


            if(pd.isShowing())
                pd.dismiss();


            try {
                JSONObject parent = new JSONObject(s);
                JSONArray items = parent.getJSONArray("list");

                if (items != null){

                    myDBHelper = new DatabaseHelper(getApplicationContext());
                    myDBHelper.deleteTables();

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject child = items.getJSONObject(i);
                        JSONArray images = child.getJSONArray("image");

                        for (int j = 0; j < images.length(); j++) {
                            myDBHelper.insertIntoImages(child.getInt("id"),images.getString(j));

                        }
                        myDBHelper.insertIntoPlace(child.getInt("id"), child.getString("name"), child.getString("description"), child.getString("district"), child.getString("bestSeason"), child.getString("additionalInformation"), child.getString("nearByPlaces"), child.getDouble("latitude"), child.getDouble("longitude"), child.getString("category"));

                    }

                    SharedPreferences preferences = getSharedPreferences("base_version", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("version", serverVersion);
                    editor.commit();
                    Toast.makeText(getApplicationContext(),"Update Successful",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        final SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener(){

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        myDBHelper = new DatabaseHelper(getApplicationContext());
                        Cursor cursor = myDBHelper.getPlaceByString(query);

                        Fragment fragment = new SearchResults(cursor);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_main, fragment);
                        ft.commit();

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        myDBHelper = new DatabaseHelper(getApplicationContext());
                        Cursor cursor = myDBHelper.getPlaceByString(newText);

                        Fragment fragment = new SearchResults(cursor);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_main, fragment);
                        ft.commit();

                        return false;
                    }
                }
        );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case R.id.action_dev:
                                    Intent intent = new Intent(MainActivity.this, aboutDev.class);
                                    startActivity(intent);
                                    break;


            case R.id.action_share:
                                    String str = "https://play.google.com/store/apps/details?id=" + getPackageName();
                                    Intent sendIntent = new Intent();
                                    sendIntent.setAction(Intent.ACTION_SEND);
                                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                                            "All you need to know about Karnataka\n\nDownload:\n" + str);
                                    sendIntent.setType("text/plain");
                                    startActivity(sendIntent);
                                    break;


            case R.id.action_refresh:
                                    if (isNetworkConnected()) {
                                        Toast.makeText(getApplicationContext(), "Please wait..", Toast.LENGTH_SHORT).show();
                                        SharedPreferences preferences = getSharedPreferences("base_version", Context.MODE_PRIVATE);
                                        localVersion = preferences.getInt("version", 0);
                                        new baseNewsVersion().execute("http://nammakarnataka.net23.net/general/base_version.json");
                                    }else {
                                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                                    }
                                    break;




        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment;
        FragmentTransaction ft;
        Intent intent;
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_dams:
                fragment = new damsFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.nav_temples:
                fragment = new templesFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.nav_beaches:
                fragment = new beachesFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.nav_hillstations:
                fragment = new hillstationsFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.nav_trekking:
                fragment = new trekkingFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.nav_waterfalls:
                fragment = new waterfallsFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;


            case R.id.new_place:
                fragment = new addNewPlace();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.feedback:
                intent = new Intent(MainActivity.this, feedbacks.class);
                startActivity(intent);
                break;


            case R.id.nav_heritage:
                fragment = new heritageFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();

                break;


            case R.id.nav_home:
                intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;


            case R.id.nav_districts:
                fragment = new districtFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;


            case R.id.nav_otherPlaces:
                fragment = new otherPlaces();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private void saveJsonFile(String data) {
        FileOutputStream stream = null;
        try {
            File path = new File("/data/data/smartAmigos.com.nammakarnataka/general.json");
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



}

