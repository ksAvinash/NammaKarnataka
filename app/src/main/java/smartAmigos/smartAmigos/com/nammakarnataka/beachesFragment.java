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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by CHARAN on 8/25/2016.
 */
public class beachesFragment extends Fragment {
    public static final String URL= "https://docs.google.com/forms/d/e/1FAIpQLSdEGmepf4osIp9zZYleR2q4WdOEK58-_G8lmufA5sf-6b1EZg/formResponse";
//    public static final MediaType FORM_DATA_TYPE
//            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    public static final String NAME_KEY="entry_1327858735";
    public static final String COMMENT_KEY="entry_466021146";

    EditText comment_box;
    TextView user_Name;

    Button comment_submit;

    String comment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dams,container,false);

        comment_box = (EditText)view.findViewById(R.id.comment_box);
        user_Name = (TextView) view.findViewById(R.id.user_name);

        comment_submit = (Button)view.findViewById(R.id.submitcomment);

        final SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("FeedbackSettings", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            user_Name.setText(sharedPreferences.getString("userName", null));
        }catch (Exception e){
            Log.e("Error :","Name settings error");
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
                Log.i("Comment : ", comment);

                PostDataTask postDataTask = new PostDataTask();
                postDataTask.execute(URL, user_Name.getText().toString(), comment_box.getText().toString());
            }
        });
    }catch (Exception e){

    }

        return view;
    }

    private class PostDataTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... uploadData) {
            Boolean result = true;
            String url = uploadData[0];
            String name = uploadData[1];
            String posting_comment = uploadData[2];
            String postBody="";

            try {
                //all values must be URL encoded to make sure that special characters like & | ",etc.
                //do not cause problems
                postBody = NAME_KEY+"=" + URLEncoder.encode(name,"UTF-8") +
                        "&" +COMMENT_KEY+"="+URLEncoder.encode(posting_comment,"UTF-8");
                Log.i("postBody",postBody);
            } catch (UnsupportedEncodingException ex) {
                result=false;
            } catch (NullPointerException e){
                result = false;
            }

            try{
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
            }catch (IOException exception){
                result=false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result){
            //Print Success or failure message accordingly
            Toast.makeText(getContext(),result?"Uploaded to Drive!":"There was some error in sending message. No Internet Connection!.",Toast.LENGTH_LONG).show();
        }
    }


}
