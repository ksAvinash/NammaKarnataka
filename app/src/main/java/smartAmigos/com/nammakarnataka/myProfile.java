package smartAmigos.com.nammakarnataka;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import smartAmigos.com.nammakarnataka.adapter.DatabaseHelper;

public class myProfile extends AppCompatActivity {



    DatabaseHelper myDBHelper;
    Context context;
    RelativeLayout layout;
    TextView profilePoints, profileTitle,profileTemples, profileHillstations,profileWaterfalls,profileDams,profileTrekking,profileBeaches,profileHeritage,profileOthers;

    int templesCount, hillstationsCount, waterfallsCount, damsCount, trekkingCount, beachesCount, heritageCount, otherCount, pointsCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = getApplicationContext();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });


        layout = (RelativeLayout) findViewById(R.id.profileBackground);
        profilePoints = (TextView) findViewById(R.id.profilePoints);
        profileTitle = (TextView) findViewById(R.id.profileTitle);

        profileTemples = (TextView) findViewById(R.id.profileTemples);
        profileHillstations = (TextView) findViewById(R.id.profileHillstations);
        profileWaterfalls = (TextView) findViewById(R.id.profileWaterfalls);
        profileDams = (TextView) findViewById(R.id.profileDams);
        profileTrekking = (TextView) findViewById(R.id.profileTrekking);
        profileBeaches = (TextView) findViewById(R.id.profileBeaches);
        profileHeritage = (TextView) findViewById(R.id.profileHeritage);
        profileOthers = (TextView) findViewById(R.id.profileOthers);



        Typeface myFont = Typeface.createFromAsset(getAssets(), "fonts/placenames.otf" );
        profileTitle.setTypeface(myFont);

        myDBHelper = new DatabaseHelper(context);
        Cursor cursor = myDBHelper.getAllVisited();

        while(cursor.moveToNext()){
            int id = cursor.getInt(0);

            Cursor place = myDBHelper.getPlaceById(id);
            while(place.moveToNext()){
                String str = place.getString(9);
                switch (str){

                    case "temple":
                            templesCount++;
                        break;

                    case "beach":
                            beachesCount++;
                        break;

                    case "hillstation":
                            hillstationsCount++;
                        break;

                    case "waterfall":
                            waterfallsCount++;
                        break;

                    case "dam":
                            damsCount++;
                        break;

                    case "trekking":
                            trekkingCount++;
                        break;

                    case "heritage":
                            heritageCount++;
                        break;

                    case "other":
                            otherCount++;
                        break;
                }
            }
        }





        pointsCount = cursor.getCount() * 5;

        profilePoints.setText(pointsCount+"");

        if(pointsCount <50){
            profileTitle.setText("BEGINNER");
            layout.setBackgroundResource(R.drawable.beginner);

        }else if(pointsCount < 150){
            profileTitle.setText("INTERMEDIATE");
            layout.setBackgroundResource(R.drawable.intermediate);
        }else{
            profileTitle.setText("PRO-TRAVELLER");
            layout.setBackgroundResource(R.drawable.professional);
        }

        profileTemples.setText(templesCount+"");
        profileHillstations.setText(hillstationsCount+"");
        profileDams.setText(damsCount+"");
        profileTrekking.setText(trekkingCount+"");
        profileBeaches.setText(beachesCount+"");
        profileHeritage.setText(heritageCount+"");
        profileOthers.setText(otherCount+"");
        profileWaterfalls.setText(waterfallsCount+"");



    }




    @Override
    public void onBackPressed() {
        myDBHelper.close();
        finish();
    }
}
