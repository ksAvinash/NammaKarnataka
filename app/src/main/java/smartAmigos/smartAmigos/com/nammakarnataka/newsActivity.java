package smartAmigos.smartAmigos.com.nammakarnataka;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
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
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

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

import smartAmigos.smartAmigos.com.nammakarnataka.adapter.generic_adapter;


public class newsActivity extends AppCompatActivity {
    Toolbar toolbar;
    MaterialRefreshLayout materialRefreshLayout;
    ListView list;
    static int serverVersion, localVersion;
    static SimpleDraweeView draweeView;
    Context context;
    private List<generic_adapter> news_adapterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        context = getApplicationContext();


        materialRefreshLayout = (MaterialRefreshLayout)findViewById(R.id.refresh);
        list = (ListView) findViewById(R.id.newsList);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        TextView t = (TextView)findViewById(R.id.it);
        Typeface myFont = Typeface.createFromAsset(this.getAssets(), "fonts/Kaushan.otf" );
        t.setTypeface(myFont);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        Fresco.initialize(this);

        if(!loadJsonFile()){
            if (isNetworkConnected()) {
                SharedPreferences preferences = getSharedPreferences("news_version", Context.MODE_PRIVATE);
                localVersion = preferences.getInt("version", 0);
                new NewsVersion().execute("http://nammakarnataka.net23.net/news/news_version.json");
            } else {
                Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        }else if (isNetworkConnected()) {
            Toast.makeText(this, "Swipe down to refresh Contents!", Toast.LENGTH_SHORT).show();
        }
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {

                if (isNetworkConnected()) {
                    SharedPreferences preferences = getSharedPreferences("news_version", Context.MODE_PRIVATE);
                    localVersion = preferences.getInt("version", 0);
                    new NewsVersion().execute("http://nammakarnataka.net23.net/news/news_version.json");
                } else {
                    Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                    materialRefreshLayout.finishRefresh();
                }
            }

        });


    }


    private boolean loadJsonFile() {
        news_adapterList.clear();
        String ret = null;
        BufferedReader reader = null;
        File file = new File("/data/data/smartAmigos.com.nammakarnataka/news.json");
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


            try {
                JSONObject parent = new JSONObject(ret);
                JSONArray items = parent.getJSONArray("list");
                for (int i=0;i<items.length();i++){
                    JSONObject child = items.getJSONObject(i);
                    JSONArray images = child.getJSONArray("image");
                    String [] imagesArray = new String[25];
                    for(int j=0;j<images.length();j++){
                        imagesArray[j] = images.getString(j);
                    }
                    news_adapterList.add(new generic_adapter(imagesArray, child.getString("name"), child.getString("description"), child.getString("district"), child.getString("bestSeason"),child.getString("additionalInformation"),child.getString("nearByPlaces"),child.getDouble("latitude"), child.getDouble("longitude")));

                }
                displayList(items);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }


    private void displayList(final JSONArray par) {
        ArrayAdapter<generic_adapter> adapter = new myNewsListAdapterClass();

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    JSONObject child = par.getJSONObject(position);
                    Fragment fragment = new placeDisplayFragment(child,"PLACE NEWS");
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_news, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    public class myNewsListAdapterClass extends ArrayAdapter<generic_adapter> {

        myNewsListAdapterClass() {
            super(context, R.layout.hillstations_item, news_adapterList);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                itemView = inflater.inflate(R.layout.hillstations_item, parent, false);

            }
            generic_adapter current = news_adapterList.get(position);

            //Code to download image from url and paste.
            Uri uri = Uri.parse(current.getImage()[0]);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.item_hillstationsImage);
            draweeView.setImageURI(uri);
            //Code ends here.
            TextView t_name = (TextView) itemView.findViewById(R.id.item_hillstationsTitle);
            t_name.setText(current.getTitle());

            TextView t_dist = (TextView) itemView.findViewById(R.id.item_hillstationsDistrict);
            t_dist.setText(current.getDistrict());

            return itemView;
        }

    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    @Override
    public void onBackPressed() {
        finish();
    }


    public class NewsVersion extends AsyncTask<String, String, String> {
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
                JSONObject news_version = parent.getJSONObject("news_version");

                serverVersion = news_version.getInt("version");


            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (localVersion != serverVersion) {
                new NewsFile().execute("http://nammakarnataka.net23.net/news/news.json");
            } else {
                Toast.makeText(context, "News List is up to date!", Toast.LENGTH_SHORT).show();
                materialRefreshLayout.finishRefresh();
            }

        }
    }



    public class NewsFile extends AsyncTask<String, String, String> {

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
            news_adapterList.clear();

            SharedPreferences preferences = getSharedPreferences("news_version", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("version", serverVersion);
            editor.apply();


            try {
                JSONObject parent = new JSONObject(s);
                JSONArray items = parent.getJSONArray("list");
                for (int i=0;i<items.length();i++){
                    JSONObject child = items.getJSONObject(i);
                    JSONArray images = child.getJSONArray("image");
                    String [] imagesArray = new String[25];
                    for(int j=0;j<images.length();j++){
                        imagesArray[j] = images.getString(j);
                    }
                    news_adapterList.add(new generic_adapter(imagesArray, child.getString("name"), child.getString("description"), child.getString("district"), child.getString("bestSeason"),child.getString("additionalInformation"),child.getString("nearByPlaces"),child.getDouble("latitude"), child.getDouble("longitude")));

                }
                materialRefreshLayout.finishRefresh();
                displayList(items);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }



    private void saveJsonFile(String data) {
        FileOutputStream stream = null;
        try {
            File path = new File("/data/data/smartAmigos.com.nammakarnataka/news.json");
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
