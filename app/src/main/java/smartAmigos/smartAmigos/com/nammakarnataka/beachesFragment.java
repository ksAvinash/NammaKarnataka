package smartAmigos.smartAmigos.com.nammakarnataka;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CHARAN on 8/25/2016.
 */
public class beachesFragment extends Fragment {
    EditText comment_box;
    public TextView user_Name;

    Button comment_submit;

    String comment, name;

    LinearLayout comments_layout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beaches, container, false);

        comment_box = (EditText) view.findViewById(R.id.comment_box);
        user_Name = (TextView) view.findViewById(R.id.user_name);

        comments_layout = (LinearLayout)view.findViewById(R.id.commets_layout);
        comment_submit = (Button) view.findViewById(R.id.submitcomment);

        final SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("KarnatakaPref", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            user_Name.setText(sharedPreferences.getString("userName", null));
        } catch (Exception e) {
            Log.e("Error :", "Name settings error");
        }

        try {
            GetDataTask getDataTask = new GetDataTask();
            URL url = new URL("http://charan.net23.net/getdata.php");
            getDataTask.execute(url);
        }catch (Exception e){
            Toast.makeText(getContext(),"Something wrong in fetching",Toast.LENGTH_LONG).show();
        }

        try {
            comment_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(comment_box.getText().toString())) {
                        Toast.makeText(getActivity(), "Please enter a comment", Toast.LENGTH_LONG).show();
                        return;
                    }
                    comment = comment_box.getText().toString();
                    name = user_Name.getText().toString();
                    Log.i("Comment : ", comment);
                    new Send().execute(name, comment, "Category", "Place");
                    try {
                        comments_layout.removeAllViews();
                        new GetDataTask().execute(new URL("http://charan.net23.net/getdata.php"));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {

        }

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
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(20 * 1000);
                urlConnection.setReadTimeout(20 * 1000);
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String response;
                while ((response = reader.readLine()) != null) {
                    builder.append(response+"\n");
                    Log.i("Response",response.toString());
                    result = builder.toString();
                } in.close();
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
                    nameTextView.setBackgroundColor(Color.parseColor("#DFDFDF"));
                    nameTextView.setText(Name);
                    nameTextView.setPadding(2,2,2,2);
                    comments_layout.addView(nameTextView);

                    TextView commentTextView = new TextView(getContext());
                    commentTextView.setTextColor(Color.BLACK);
                    commentTextView.setBackgroundColor(Color.parseColor("#DFDFDF"));
                    commentTextView.setText(Comment);
                    commentTextView.setPadding(5,2,2,2);
                    comments_layout.addView(commentTextView);

                }
            }catch (Exception e){

            }
        }
    }

}
