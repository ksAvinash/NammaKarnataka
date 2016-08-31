package smartAmigos.smartAmigos.com.nammakarnataka;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class addNewPlace extends Fragment {

    public static final String URL = "https://docs.google.com/forms/d/e/1FAIpQLSdeAMmRElkXAHy5haAvbT2dzD8RV0D8Yse-bUyhK3Y04rT5xg/formResponse";

    public static final String PLACE_KEY = "entry_27664589";
    public static final String LOCATION_KEY = "entry_1673122530";
    public static final String CATEGORY_KEY = "entry_523891014";
    public static final String NEARBY_KEY = "entry_1278615251";
    public static final String SEASON_KEY = "entry_1299151993";
    public static final String INFO_KEY = "entry_16681967";

    EditText place_input, location_input, nearby_input, season_input, addinfo_input;
    Spinner category_spinner;
    Button submit_newplace;
    TextView spinner_response;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_place, container, false);

        place_input = (EditText) view.findViewById(R.id.place_input);
        location_input = (EditText) view.findViewById(R.id.location_input);
        nearby_input = (EditText) view.findViewById(R.id.nearby_input);
        season_input = (EditText) view.findViewById(R.id.season_input);
        addinfo_input = (EditText) view.findViewById(R.id.addinfo_input);

        spinner_response = (TextView) view.findViewById(R.id.spinner_response);

        category_spinner = (Spinner) view.findViewById(R.id.category_input);
        ArrayAdapter<CharSequence> event_adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.category, R.layout.spinner_item);
        event_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(event_adapter);

        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        spinner_response.setText("TEMPLES");
                        break;
                    case 2:
                        spinner_response.setText("HILL");
                        break;
                    case 3:
                        spinner_response.setText("WATERFALL");
                        break;
                    case 4:
                        spinner_response.setText("DAMS");
                        break;
                    case 5:
                        spinner_response.setText("TREK");
                        break;
                    case 6:
                        spinner_response.setText("BEACHES");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit_newplace = (Button) view.findViewById(R.id.submit_newplace);
        return view;
    }

}
