package smartAmigos.smartAmigos.com.nammakarnataka;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import smartAmigos.smartAmigos.com.nammakarnataka.adapter.generic_adapter;


public class distDisplayFragment extends Fragment {

    private List<generic_adapter> district_specific_adapterList = new ArrayList<>();
    static SimpleDraweeView draweeView;
    JSONObject child;
    TextView current_dist;
    ListView list;
    View view;
    Context context;
    @SuppressLint("ValidFragment")
    public distDisplayFragment(JSONObject child) {
        this.child = child;
    }



    public distDisplayFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dist_display, container, false);

        current_dist = (TextView)view.findViewById(R.id.current_dist);
        list = (ListView) view.findViewById(R.id.distCurrentList);
        context = getActivity().getApplicationContext();

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        Fresco.initialize(getActivity());
        district_specific_adapterList.clear();
        try {
            current_dist.setText(child.getString("name"));
            JSONArray places = child.getJSONArray("places");

            for(int i=0;i<places.length();i++){
                JSONObject pp = places.getJSONObject(i);
                JSONArray images = pp.getJSONArray("image");
                String [] imagesArray = new String[25];
                for(int j=0;j<images.length();j++){
                    imagesArray[j] = images.getString(j);
                }
                district_specific_adapterList.add(new generic_adapter(imagesArray, pp.getString("name"), pp.getString("description"), pp.getString("district"), pp.getString("bestSeason"),pp.getString("additionalInformation"),pp.getDouble("latitude"), pp.getDouble("longitude")));
            }
            displayList(places);
        }catch (JSONException e){}

        //handle backpress
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        getActivity().finish();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);

                        return true;
                    }
                }
                return false;
            }
        });
        return view;
    }


    private void displayList(final JSONArray par) {
        ArrayAdapter<generic_adapter> adapter = new myPlacesListAdapterClass();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                JSONObject child;
                try {
                    child = par.getJSONObject(position);
                    Fragment fragment = new placeDisplayFragment(child,"DISTRICT");
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_my_location, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                }

        });
    }




    public class myPlacesListAdapterClass extends ArrayAdapter<generic_adapter> {

        myPlacesListAdapterClass() {
            super(context, R.layout.hillstations_item, district_specific_adapterList);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                itemView = inflater.inflate(R.layout.hillstations_item, parent, false);

            }
            generic_adapter current = district_specific_adapterList.get(position);

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

}
