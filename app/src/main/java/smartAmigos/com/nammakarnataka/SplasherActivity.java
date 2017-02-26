package smartAmigos.com.nammakarnataka;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class SplasherActivity extends AppCompatActivity {

    TextView t, t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splasher);

        t = (TextView) findViewById(R.id.tv);
        t1 = (TextView) findViewById(R.id.tvs);


        Typeface myFont = Typeface.createFromAsset(this.getAssets(), "fonts/Kaushan.otf" );
        t.setTypeface(myFont);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplasherActivity.this, MainActivity.class);
                startActivity(intent);
                SplasherActivity.this.finish();
            }
        }, 2500);


    }

}
