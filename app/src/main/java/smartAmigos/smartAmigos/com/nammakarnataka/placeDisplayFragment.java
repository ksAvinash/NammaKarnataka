package smartAmigos.smartAmigos.com.nammakarnataka;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

/**
 * Created by CHARAN on 8/26/2016.
 */
public class placeDisplayFragment extends Fragment {
    JSONObject child;
    private TextView place_textView,description_textView,location_textView,lat_longtextView;
    public placeDisplayFragment() {
    }

    @SuppressLint("ValidFragment")
    public placeDisplayFragment(JSONObject child) {
        this.child = child;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_general, container, false);
        place_textView = (TextView)view.findViewById(R.id.place_textView);
        description_textView = (TextView)view.findViewById(R.id.description_textView);
        location_textView = (TextView)view.findViewById(R.id.location_textView);
        lat_longtextView = (TextView)view.findViewById(R.id.lat_longtextView);

        try {
            place_textView.setText(child.getString("temple_name"));
            description_textView.setText(child.getString("temple_description"));
            location_textView.setText(child.getString("temple_district"));
            lat_longtextView.setText((int) child.getDouble("latitude"));
        }catch (Exception e){

        }
        return view;
    }
}
