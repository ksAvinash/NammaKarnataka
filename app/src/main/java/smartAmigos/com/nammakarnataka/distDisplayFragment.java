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
import android.widget.Toast;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import java.util.List;
import smartAmigos.com.nammakarnataka.adapter.DatabaseHelper;
import smartAmigos.com.nammakarnataka.adapter.generic_adapter;


public class distDisplayFragment extends Fragment {

    private List<generic_adapter> district_specific_adapterList = new ArrayList<>();
    static SimpleDraweeView draweeView;
    TextView current_dist;
    ListView list;
    View view;
    String district;
    Context context;
    DatabaseHelper myDBHelper;
    Cursor PlaceCursor;

    @SuppressLint("ValidFragment")
    public distDisplayFragment(String district) {
        this.district = district;
    }



    public distDisplayFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dist_display, container, false);

        current_dist = (TextView)view.findViewById(R.id.current_dist);
        list = (ListView) view.findViewById(R.id.distCurrentList);
        context = getActivity().getApplicationContext();

        TextView current_dist = (TextView)view.findViewById(R.id.current_dist);
        Typeface myFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Kaushan.otf" );
        current_dist.setTypeface(myFont);
        current_dist.setText(district.toUpperCase());

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        Fresco.initialize(getActivity());
        district_specific_adapterList.clear();


        myDBHelper = new DatabaseHelper(context);
        PlaceCursor = myDBHelper.getPlaceByDistrict(district);

        while(PlaceCursor.moveToNext()){

            String [] imagesArray = new String[25];
            Cursor imageURLCursor = myDBHelper.getAllImagesArrayByID(PlaceCursor.getInt(0));
            for (int i=0;imageURLCursor.moveToNext();i++){
                imagesArray[i] = imageURLCursor.getString(1);
            }


            district_specific_adapterList.add(
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
        ArrayAdapter<generic_adapter> adapter = new myPlacesListAdapterClass();
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


    public class myPlacesListAdapterClass extends ArrayAdapter<generic_adapter> {

        myPlacesListAdapterClass() {
            super(context, R.layout.item, district_specific_adapterList);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                itemView = inflater.inflate(R.layout.item, parent, false);

            }
            generic_adapter current = district_specific_adapterList.get(position);

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
