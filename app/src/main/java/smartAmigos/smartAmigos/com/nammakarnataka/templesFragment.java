package smartAmigos.smartAmigos.com.nammakarnataka;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import smartAmigos.smartAmigos.com.nammakarnataka.adapter.temples_adapter;


public class templesFragment extends Fragment {


    private List<temples_adapter> temples_adapterList = new ArrayList<>();

    static SimpleDraweeView draweeView;

    View view;
    Context context;
    MaterialRefreshLayout materialRefreshLayout;
    static int serverVersion, localVersion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_temples, container, false);
        context = getActivity().getApplicationContext();
        materialRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);

        Fresco.initialize(getActivity());

        if (!loadJsonFile()) {
            temples_adapterList.add(new temples_adapter("", "", "", "", 0.0, 0.0));
            displayList();
        }

        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                //refreshing...
                if (!loadJsonFile()) {
                    temples_adapterList.clear();
                }
                if (isNetworkConnected()) {
                    SharedPreferences preferences = getActivity().getSharedPreferences("temple_version", Context.MODE_PRIVATE);
                    localVersion = preferences.getInt("version", 0);
                    new TempleVersion().execute("https://googledrive.com/host/0B4MrAIPM8gwfVmZfMHR5NVJuLTA/temple_version.json");
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
                    materialRefreshLayout.finishRefresh();
                }
            }

        });


        return view;
    }


    public class TempleVersion extends AsyncTask<String, String, String> {
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
                JSONObject news_version = parent.getJSONObject("temple_version");

                serverVersion = news_version.getInt("version");

                SharedPreferences preferences = getActivity().getSharedPreferences("temple_version", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("version", serverVersion);
                editor.apply();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (localVersion != serverVersion) {
                new templeFile().execute("https://googledrive.com/host/0B4MrAIPM8gwfVmZfMHR5NVJuLTA/temple.json");
            } else {
                Toast.makeText(getActivity(), "Temple List is up to date!", Toast.LENGTH_SHORT).show();
                materialRefreshLayout.finishRefresh();
            }

        }
    }


    private boolean loadJsonFile() {
        String ret = null;
        BufferedReader reader = null;
        File file = new File("/data/data/avinashks.smartAmigos.com.nammakarnataka/temple.json");
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
                JSONArray eventJson = parent.getJSONArray("temple_list");

                for (int i = 0; i < eventJson.length(); i++) {
                    JSONObject child = eventJson.getJSONObject(i);
                    temples_adapterList.add(new temples_adapter(child.getString("temple_image"), child.getString("temple_name"), child.getString("temple_description"), child.getString("temple_district"), child.getDouble("latitude"), child.getDouble("longitude")));
                }
                displayList();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    private void saveJsonFile(String data) {
        FileOutputStream stream = null;
        try {
            File path = new File("/data/data/avinashks.smartAmigos.com.nammakarnataka/temple.json");
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


    public class templeFile extends AsyncTask<String, String, String> {

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
            try {

                JSONObject parent = new JSONObject(s);
                JSONArray eventJson = parent.getJSONArray("temple_list");
                for (int i = 0; i < eventJson.length(); i++) {
                    JSONObject child = eventJson.getJSONObject(i);
                    temples_adapterList.add(new temples_adapter(child.getString("temple_image"), child.getString("temple_name"), child.getString("temple_description"), child.getString("temple_district"), child.getDouble("latitude"), child.getDouble("longitude")));
                }
                materialRefreshLayout.finishRefresh();
                displayList();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    private void displayList() {
        ArrayAdapter<temples_adapter> adapter = new myTempleListAdapterClass();
        ListView list = (ListView) view.findViewById(R.id.templeList);
        list.setAdapter(adapter);
    }


    public class myTempleListAdapterClass extends ArrayAdapter<temples_adapter> {

        myTempleListAdapterClass() {
            super(context, R.layout.temples_item, temples_adapterList);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                itemView = inflater.inflate(R.layout.temples_item, parent, false);

            }
            temples_adapter current = temples_adapterList.get(position);

            //Code to download image from url and paste.
            Uri uri = Uri.parse(current.getImage());
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.item_templeImage);
            draweeView.setImageURI(uri);
            //Code ends here.
            TextView t_name = (TextView) itemView.findViewById(R.id.item_templeTitle);
            t_name.setText(current.getTempleTitle());

            TextView t_dist = (TextView) itemView.findViewById(R.id.item_templeDistrict);
            t_dist.setText(current.getDistrict());

            return itemView;
        }


    }
}
