package smartAmigos.smartAmigos.com.nammakarnataka;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity  implements View.OnClickListener{


    private TextView appNameTextView;
    EditText email,password;
    Button signup;

    String email_string,password_string;

    ProgressDialog progressDialog;



    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sigin);

        firebaseAuth = FirebaseAuth.getInstance();

        email = (EditText)findViewById(R.id.email_id);
        password = (EditText)findViewById(R.id.password_id);

        signup = (Button)findViewById(R.id.sign_up);
        signup.setOnClickListener(this);


        appNameTextView = (TextView)findViewById(R.id.appNameTextView);
        Typeface myFont = Typeface.createFromAsset(getAssets(), "fonts/Kaushan.otf" );
        appNameTextView.setTypeface(myFont);

        progressDialog = new ProgressDialog(this);


        //login == 1 (true) then skip login page
        SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        if(preferences.getInt("version",0) == 1){
            Intent intent = new Intent(SignInActivity.this,SplasherActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.sign_up){

            email_string = email.getText().toString().trim();
            password_string = password.getText().toString().trim();

            if(TextUtils.isEmpty(email_string) || TextUtils.isEmpty(password_string)){
                Toast.makeText(getApplicationContext(),"email or password field is empty",Toast.LENGTH_SHORT).show();
            }else{

                  if(isNetworkConnected()){
                      progressDialog.setMessage("Registering User..");
                      progressDialog.show();

                      firebaseAuth.createUserWithEmailAndPassword(email_string,password_string)
                              .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                  @Override
                                  public void onComplete(@NonNull Task<AuthResult> task) {
                                      if(task.isSuccessful()){
                                          SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                                          SharedPreferences.Editor editor = preferences.edit();
                                          editor.putInt("version",1);
                                          editor.apply();


                                          String[] user = email_string.split("@");

                                          SharedPreferences sharedPreferences = getSharedPreferences("KarnatakaPref", Context.MODE_PRIVATE);
                                          SharedPreferences.Editor editor2 = sharedPreferences.edit();
                                          editor2.putString("userName",user[0]);
                                          editor2.apply();

                                         // sharedPreferences.getString("userName", null);



                                          Toast.makeText(getApplicationContext(),"Signed up successfully!",Toast.LENGTH_LONG).show();

                                          Intent intent = new Intent(SignInActivity.this,SplasherActivity.class);
                                          startActivity(intent);
                                          finish();

                                      }else {
                                          Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_LONG).show();
                                      }


                                      if(progressDialog.isShowing())
                                          progressDialog.dismiss();
                                  }
                              });
                  }else {
                      Toast.makeText(getApplicationContext(),"No Internet Connection!",Toast.LENGTH_LONG).show();
                  }

            }
        }
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}