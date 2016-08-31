package smartAmigos.smartAmigos.com.nammakarnataka;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by CHARAN on 8/25/2016.
 */
public class beachesFragment extends Fragment {
    public static final String URL = "https://docs.google.com/forms/d/e/1FAIpQLSdEGmepf4osIp9zZYleR2q4WdOEK58-_G8lmufA5sf-6b1EZg/formResponse";
//    public static final MediaType FORM_DATA_TYPE
//            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    public static final String NAME_KEY = "entry_1327858735";
    public static final String COMMENT_KEY = "entry_466021146";

    EditText comment_box;
    TextView user_Name;

    Button comment_submit;

    String comment,name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beaches, container, false);

        comment_box = (EditText) view.findViewById(R.id.comment_box);
        user_Name = (TextView) view.findViewById(R.id.user_name);

        comment_submit = (Button) view.findViewById(R.id.submitcomment);

        final SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("KarnatakaPref", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            user_Name.setText(sharedPreferences.getString("userName", null));
        } catch (Exception e) {
            Log.e("Error :", "Name settings error");
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
                if(responseStr.length()>0){
                    result = true;
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
            return result;

        }

        protected void onPostExecute(Boolean result) {
            if(result){
                Toast.makeText(getContext(),"Comment has been recorded",Toast.LENGTH_LONG).show();
            }
        }
    }


}
