package smartAmigos.smartAmigos.com.nammakarnataka;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@SuppressLint("ValidFragment")
public class placeDisplayFragment extends Fragment {
    JSONObject child;
    private TextView place_textView, description_textView, location_textView, season_textView, additionalInformation, nearby_textView;
    private Button gmapButton, submit_comment;
    public String category, comment, user_name, place_name;
    public EditText comment_input;
    private double latitude, longitude;
    private LinearLayout comment_layout;
    private CardView comment_CardView;
    SliderLayout mDemoSlider;


    JSONArray images;

    @SuppressLint("ValidFragment")
    public placeDisplayFragment(JSONObject child, String category) {
        this.child = child;
        this.category = category;
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
        mDemoSlider = (SliderLayout) view.findViewById(R.id.layout_images);
        nearby_textView = (TextView) view.findViewById(R.id.nearby_textView);

        final SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("KarnatakaPref", Context.MODE_PRIVATE);


        comment_input = (EditText) view.findViewById(R.id.comment_input);
        comment_layout = (LinearLayout) view.findViewById(R.id.comment_layout);
        comment_CardView = (CardView) view.findViewById(R.id.comment_CardView);
        gmapButton = (Button) view.findViewById(R.id.gmapButton);
        submit_comment = (Button) view.findViewById(R.id.submit_comment);
        submit_comment.setBackgroundColor(Color.TRANSPARENT);

        try {
            place_textView.setText(child.getString("name"));
            description_textView.setText(child.getString("description"));
            nearby_textView.setText(child.getString("nearByPlaces"));
            location_textView.setText(child.getString("district") + "  ( " + String.valueOf(child.getDouble("latitude")) + " , " + String.valueOf(child.getDouble("longitude")) + " )");
            season_textView.setText(child.getString("bestSeason"));
            additionalInformation.setText(child.getString("additionalInformation"));
        } catch (Exception e) {

        }

        submit_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (TextUtils.isEmpty(comment_input.getText().toString())) {
                        Toast.makeText(getActivity(), "Please enter a comment", Toast.LENGTH_LONG).show();
                        return;
                    }
                    comment = comment_input.getText().toString();
                    user_name = sharedPreferences.getString("userName", null);
                    place_name = child.getString("name");
                    new Send().execute(user_name, comment, category, place_name);
                    comment_layout.removeAllViews();
                    place_name = place_name.replaceAll(" ", "\\%20");
                    new GetDataTask().execute(new URL("http://charan.net23.net/modifiedGetData.php?Place=" + place_name));
                } catch (Exception e) {

                }
            }
        });

        gmapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    latitude = child.getDouble("latitude");
                    longitude = child.getDouble("longitude");
                    startActivity(
                            new Intent(
                                    android.content.Intent.ACTION_VIEW,
                                    Uri.parse("geo:" + latitude + "," + longitude + "?q=(" + child.getString("name") + ")@" + latitude + "," + longitude)));
                } catch (Exception e) {

                }
            }
        });

        if (category.equals("TEMPLES")) {
            comment_CardView.setVisibility(View.GONE);
        } else {
            try {
                place_name = child.getString("name");
                place_name = place_name.replaceAll(" ", "\\%20");
                new GetDataTask().execute(new URL("http://charan.net23.net/modifiedGetData.php?Place=" + place_name));
            } catch (Exception e) {
                Log.e("Fetch", "Rod at fetch");
            }
        }


//        //code for multiple images loading starts


        TextSliderView textSliderView;

        String[] imagesArray = new String[25];
        try {
            images = child.getJSONArray("image");
            for (int i = 0; i < images.length(); i++) {
                imagesArray[i] = images.getString(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < images.length(); i++) {
            textSliderView = new TextSliderView(getContext());
            textSliderView
                    .image(imagesArray[i])
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.RotateDown);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(6000);

        //Code for multiple images loading ends


        return view;
    }


    class Send extends AsyncTask<String, Void, Boolean> {

        protected Boolean doInBackground(String... uploadData) {
            boolean result = false;
            String Name = uploadData[0];
            String Comment = uploadData[1];
            String Category = uploadData[2];
            String Place = uploadData[3];

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://charan.net23.net/phpcode.php");

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("Name", Name));
                nameValuePairs.add(new BasicNameValuePair("Comment", Comment));
                nameValuePairs.add(new BasicNameValuePair("Category", Category));
                nameValuePairs.add(new BasicNameValuePair("Place", Place));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));


                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                String responseStr = response.toString();
                if (responseStr.length() > 0) {
                    result = true;
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
            return result;

        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(getContext(), "Comment has been recorded", Toast.LENGTH_LONG).show();
                comment_input.setText(null);
            }
        }
    }


    public class GetDataTask extends AsyncTask<URL, Void, String> {
        private HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(URL... params) {

            StringBuilder builder = new StringBuilder();
            String result = "";
            try {
                URL url = params[0];
                Log.i("URL :", url.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(20 * 1000);
                urlConnection.setReadTimeout(20 * 1000);
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String response;
                while ((response = reader.readLine()) != null) {
                    builder.append(response + "\n");
//                    Log.i("Response",response.toString());
                    result = builder.toString();
                }
                in.close();
            } catch (Exception e) {

            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            // super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONArray jArray = new JSONArray(result);

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    String Name = json.getString("Name");
                    String Comment = json.getString("Comment");

                    TextView nameTextView = new TextView(getContext());
                    nameTextView.setTextColor(Color.parseColor("#3949ab"));
                    nameTextView.setBackgroundColor(Color.WHITE);
                    nameTextView.setText(Name);
                    nameTextView.setPadding(2, 2, 2, 2);
                    comment_layout.addView(nameTextView);

                    TextView commentTextView = new TextView(getContext());
                    LinearLayout.LayoutParams commentlayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    commentlayoutParams.setMargins(35, 2, 2, 2);
                    commentTextView.setLayoutParams(commentlayoutParams);
                    commentTextView.setTextColor(Color.BLACK);
                    commentTextView.setBackgroundColor(Color.WHITE);
                    commentTextView.setText(Comment);
//                    commentTextView.setPadding(10, 2, 2, 2);
                    comment_layout.addView(commentTextView);

                    TextView blankTextView = new TextView(getContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 5, 0, 0);
                    blankTextView.setLayoutParams(layoutParams);
                    comment_layout.addView(blankTextView);
                }
            } catch (Exception e) {

            }
        }
    }


}
