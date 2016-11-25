package smartAmigos.com.nammakarnataka;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class addNewPlace extends Fragment {

    public static final String URL = "https://docs.google.com/forms/d/e/1FAIpQLSdeAMmRElkXAHy5haAvbT2dzD8RV0D8Yse-bUyhK3Y04rT5xg/formResponse";

    public static final String PLACE_KEY = "entry_27664589";
    public static final String LOCATION_KEY = "entry_1673122530";
    public static final String CATEGORY_KEY = "entry_523891014";
    public static final String NEARBY_KEY = "entry_1278615251";
    public static final String SEASON_KEY = "entry_1299151993";
    public static final String INFO_KEY = "entry_16681967";

    ProgressDialog pd;
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

        pd = new ProgressDialog(getContext());




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
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit_newplace = (Button) view.findViewById(R.id.submit_newplace);

        submit_newplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Uploading please wait..");
                pd.setCancelable(false);
                pd.show();

                if (TextUtils.isEmpty(place_input.getText().toString())) {
                    place_input.setError("Mandatory");
                    if(pd.isShowing())
                            pd.dismiss();
                }
                if (TextUtils.isEmpty(spinner_response.getText().toString())) {
                    ((TextView) category_spinner.getSelectedView()).setError("Category needed");
                    if(pd.isShowing())
                        pd.dismiss();
                } else if (TextUtils.isEmpty(location_input.getText().toString())) {
                    location_input.setError("Mandatory");
                    if(pd.isShowing())
                        pd.dismiss();
                } else {
                    PostDataTask postDataTask = new PostDataTask();
                    postDataTask.execute(URL, place_input.getText().toString(), location_input.getText().toString(), spinner_response.getText().toString(),
                            nearby_input.getText().toString(), season_input.getText().toString(), addinfo_input.getText().toString());
                }
            }
        });

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

    private class PostDataTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... uploadData) {
            Boolean result = true;
            String url = uploadData[0];
            String place = uploadData[1];
            String location = uploadData[2];
            String category = uploadData[3];
            String nearby = uploadData[4];
            String season = uploadData[5];
            String additional = uploadData[6];
            String postBody = "";

            try {
                //all values must be URL encoded to make sure that special characters like & | ",etc.
                //do not cause problems
                postBody = PLACE_KEY + "=" + URLEncoder.encode(place, "UTF-8") +
                        "&" + LOCATION_KEY + "=" + URLEncoder.encode(location, "UTF-8") +
                        "&" + CATEGORY_KEY + "=" + URLEncoder.encode(category, "UTF-8") +
                        "&" + NEARBY_KEY + "=" + URLEncoder.encode(nearby, "UTF-8") +
                        "&" + SEASON_KEY + "=" + URLEncoder.encode(season, "UTF-8") +
                        "&" + INFO_KEY + "=" + URLEncoder.encode(additional, "UTF-8");
                Log.i("postBody", postBody);
            } catch (UnsupportedEncodingException ex) {
                result = false;
            } catch (NullPointerException e) {
                result = false;
            }

            try {
                //Create OkHttpClient for sending request
                OkHttpClient client = new OkHttpClient();
                //Create the request body with the help of Media Type
                RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"), postBody);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                //Send the request
                Response response = client.newCall(request).execute();
            } catch (IOException exception) {
                result = false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            //Print Success or failure message accordingly
            if (result) {
                if(pd.isShowing())
                    pd.dismiss();

                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Thank You!");
                builder.setMessage("We have successfully recieved your place request.\nWe shall process it and update the contents soon");
                builder.setCancelable(true);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.show();
            } else {
                if(pd.isShowing())
                    pd.dismiss();
                Toast.makeText(getContext(), "Unexpected error!\n please try later", Toast.LENGTH_LONG).show();
            }
        }
    }

}
