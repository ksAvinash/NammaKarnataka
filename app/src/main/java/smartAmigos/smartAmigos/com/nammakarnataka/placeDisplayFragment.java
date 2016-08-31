package smartAmigos.smartAmigos.com.nammakarnataka;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;


public class placeDisplayFragment extends Fragment {
    JSONObject child;
    private TextView place_textView, description_textView, location_textView,season_textView,additionalInformation;
    private Button gmapButton;
    private double latitude,longitude;
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
        place_textView = (TextView) view.findViewById(R.id.place_textView);
        description_textView = (TextView) view.findViewById(R.id.description_textView);
        location_textView = (TextView) view.findViewById(R.id.location_textView);
        season_textView = (TextView) view.findViewById(R.id.season_textView);
        additionalInformation = (TextView) view.findViewById(R.id.additionalInformation);

        
        gmapButton = (Button)view.findViewById(R.id.gmapButton);

        try {
            place_textView.setText(child.getString("name"));
            description_textView.setText(child.getString("description"));
            location_textView.setText(child.getString("district")+"  ( "+String.valueOf(child.getDouble("latitude"))+" , "+String.valueOf(child.getDouble("longitude"))+" )");
            season_textView.setText(child.getString("bestSeason"));
            additionalInformation.setText(child.getString("additionalInformation"));
        } catch (Exception e) {

        }

        gmapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    latitude = child.getDouble("latitude");
                    longitude = child.getDouble("longitude");
                    startActivity(
                            new Intent(
                                    android.content.Intent.ACTION_VIEW,
                                    Uri.parse("geo:"+latitude+","+longitude+"?q=("+child.getString("name")+")@"+latitude+","+longitude)));
                }catch (Exception e){

                }
            }
        });
        return view;
    }
}
