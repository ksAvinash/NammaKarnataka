package smartAmigos.com.nammakarnataka;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import smartAmigos.com.nammakarnataka.adapter.DatabaseHelper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseHelper myDBHelper;
    Context context;
    Cursor PlaceCursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        context = getApplicationContext();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        LatLng karnataka = new LatLng(12.94,75.37);


        myDBHelper = new DatabaseHelper(context);
        PlaceCursor = myDBHelper.getAllTemples();

        while (PlaceCursor.moveToNext()){
            LatLng place = new LatLng(PlaceCursor.getDouble(7), PlaceCursor.getDouble(8));
            mMap.addMarker(new MarkerOptions().position(place).title(PlaceCursor.getString(1)+" Temple").icon(BitmapDescriptorFactory.fromResource(R.drawable.om)));
        }

        PlaceCursor = myDBHelper.getAllDams();
        while (PlaceCursor.moveToNext()){
            LatLng place = new LatLng(PlaceCursor.getDouble(7), PlaceCursor.getDouble(8));
            mMap.addMarker(new MarkerOptions().position(place).title(PlaceCursor.getString(1)+" Dam").icon(BitmapDescriptorFactory.fromResource(R.drawable.dam_map)));
        }


        PlaceCursor = myDBHelper.getAllTrekkings();
        while (PlaceCursor.moveToNext()){
            LatLng place = new LatLng(PlaceCursor.getDouble(7), PlaceCursor.getDouble(8));
            mMap.addMarker(new MarkerOptions().position(place).title(PlaceCursor.getString(1)+" Trek").icon(BitmapDescriptorFactory.fromResource(R.drawable.climb)));
        }


        PlaceCursor = myDBHelper.getAllHillstations();
        while (PlaceCursor.moveToNext()){
            LatLng place = new LatLng(PlaceCursor.getDouble(7), PlaceCursor.getDouble(8));
            mMap.addMarker(new MarkerOptions().position(place).title(PlaceCursor.getString(1)+" Hillstation").icon(BitmapDescriptorFactory.fromResource(R.drawable.hillstation_map)));
        }


        PlaceCursor = myDBHelper.getAllWaterfalls();
        while (PlaceCursor.moveToNext()){
            LatLng place = new LatLng(PlaceCursor.getDouble(7), PlaceCursor.getDouble(8));
            mMap.addMarker(new MarkerOptions().position(place).title(PlaceCursor.getString(1)+" Falls").icon(BitmapDescriptorFactory.fromResource(R.drawable.waterfalls_map)));
        }

        PlaceCursor = myDBHelper.getAllBeaches();
        while (PlaceCursor.moveToNext()){
            LatLng place = new LatLng(PlaceCursor.getDouble(7), PlaceCursor.getDouble(8));
            mMap.addMarker(new MarkerOptions().position(place).title(PlaceCursor.getString(1)+" Beach").icon(BitmapDescriptorFactory.fromResource(R.drawable.beach_map)));
        }

        PlaceCursor = myDBHelper.getAllHeritages();
        while (PlaceCursor.moveToNext()){
            LatLng place = new LatLng(PlaceCursor.getDouble(7), PlaceCursor.getDouble(8));
            mMap.addMarker(new MarkerOptions().position(place).title(PlaceCursor.getString(1)+" Beach").icon(BitmapDescriptorFactory.fromResource(R.drawable.heritage_map)));
        }


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(karnataka,  (float)8.5));
    }
}
