package smartAmigos.com.nammakarnataka;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

public class feedbacks extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar AB = getSupportActionBar();
        AB.hide();
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_feedbacks);
        this.context = this;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.activity_feedbacks, null);


        final MaterialStyledDialog dialogHeader_3 = new MaterialStyledDialog(context)
                // .setHeaderDrawable(R.drawable.header)
                .setHeaderColor(R.color.colorBlue)
                .setIcon(R.drawable.feedback_icon)
                .withDialogAnimation(true)
                .setTitle("Awesome!")
                .setDescription("Glad to see you like NAMMA KARNATAKA!\nKeep supporting us!")
                .setPositive("Give us 5", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        final String appPackageName = getPackageName();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                })
                .setNegative("Not now", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build();

        final MaterialStyledDialog dialogHeader_4 = new MaterialStyledDialog(context)
                // .setHeaderDrawable(R.drawable.header)
                .setHeaderColor(R.color.colorBlue)
                .setIcon(R.drawable.feedback_icon)
                .withDialogAnimation(true)
                //  .setTitle("Your Feedback")
                .setDescription("What can we Improve? Your feedback is always welcomed.")
                .setPositive("Feedback", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "justmailtoavi@gmail.com, gauthamkumar.0414@gmail.com, charanshetty25595@gmail.com,ashwinchandlapur@gmail.com"));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Namma Karnataka Feedback");
                        startActivity(intent);

                    }
                })
                .setNegative("Not now", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build();


        CardView dialogHeaderView_3 = (CardView) findViewById(R.id.dialog_3);
        CardView dialogHeaderView_4 = (CardView) findViewById(R.id.dialog_4);

        dialogHeaderView_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogHeader_3.show();
            }
        });

        dialogHeaderView_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogHeader_4.show();
            }
        });
    }

}
