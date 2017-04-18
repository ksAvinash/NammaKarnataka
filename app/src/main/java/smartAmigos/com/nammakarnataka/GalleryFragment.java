package smartAmigos.com.nammakarnataka;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
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

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import smartAmigos.com.nammakarnataka.adapter.gallery_adapter;

public class GalleryFragment extends Fragment {


    int image_id;
    String placename;
    ListView list;
    Context context;
    Uri uri;

    private InterstitialAd interstitial;

    public GalleryFragment(){}

    @SuppressLint("ValidFragment")
    public GalleryFragment(int image_id, String placename){
        this.image_id = image_id;
        this.placename = placename;
    }


    private List<gallery_adapter> gallery_adapterList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_gallery, container, false);

        context = getActivity().getApplicationContext();


        list = (ListView) view.findViewById(R.id.galleryList);


       // Call ads
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitial = new InterstitialAd(context);
        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
        interstitial.loadAd(adRequest);
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                    interstitial.show();
            }
        });
        // Finish calling ads



        Toast.makeText(context, "You can mail us photos of places you've visited, We will add it to the Gallery\nmail: justmailtoavi@gmail.com",Toast.LENGTH_LONG).show();

        //set the place name and font
        TextView gallery_name = (TextView)view.findViewById(R.id.gallery_name);
        gallery_name.setText(placename);
        Typeface myFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/placenames.otf" );
        gallery_name.setTypeface(myFont);


        gallery_adapterList.clear();


        if(isNetworkConnected()){
            new galleryImages().execute("http://nammakarnataka.net23.net/BigImages/"+image_id+".json");
        }else
            Toast.makeText(context,"No Internet Connection!",Toast.LENGTH_SHORT).show();

        return view;
    }



    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }





    public class galleryImages extends AsyncTask<String, String, String> {

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
                return str;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null){


                try {
                    JSONObject parent = new JSONObject(s);
                    JSONArray images = parent.getJSONArray("images");

                    if(images!=null){

                        for(int i=0; i<images.length();i++){
                            JSONObject child = images.getJSONObject(i);
                            gallery_adapterList.add(new gallery_adapter(child.getString("url"),child.getString("text")));
                        }
                        displayList();

                    }else
                        Toast.makeText(context,"error",Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    Toast.makeText(context, "Images not yet added. Try later",Toast.LENGTH_SHORT).show();
                }


            }else
                Toast.makeText(context, "Images not yet added. Try later",Toast.LENGTH_SHORT).show();
        }
    }


    private void displayList() {
        ArrayAdapter<gallery_adapter> adapter = new galleryAdapterList();
        list.setAdapter(adapter);
    }


    public class galleryAdapterList extends ArrayAdapter<gallery_adapter> {

        galleryAdapterList() {
            super(context, R.layout.gallery_item, gallery_adapterList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.gallery_item, parent, false);
            }

            gallery_adapter current = gallery_adapterList.get(position);


            TextView mText=(TextView) convertView.findViewById(R.id.galleryText);
            mText.setText(current.getText());


            uri = Uri.parse(current.getUrl());
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            imagePipeline.evictFromCache(uri);

            SimpleDraweeView mImage = (SimpleDraweeView) convertView.findViewById(R.id.galleryImage);
            mImage.getHierarchy().setProgressBarImage(new CircleProgressBarDrawable(2));
            mImage.setImageURI(uri);


            return convertView;
        }

    }




}