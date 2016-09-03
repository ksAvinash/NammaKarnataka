package smartAmigos.smartAmigos.com.nammakarnataka;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.pushbots.push.Pushbots;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView t;
    SliderLayout mDemoSlider;
    FloatingActionButton fab;
    DrawerLayout drawer;
    private String nameInput = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Pushbots.sharedInstance().init(getApplicationContext());
        Pushbots.sharedInstance().setCustomHandler(customHandler.class);

        fab = (FloatingActionButton) findViewById(R.id.fab);


        //check for permissions on android M
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        final SharedPreferences sharedPreferences = getSharedPreferences("KarnatakaPref", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        boolean nameSet = sharedPreferences.getBoolean("nameSet", false);
        if (!nameSet) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter your Name : ");

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    nameInput = input.getText().toString();
                    editor.putString("userName", nameInput);
                    editor.putBoolean("nameSet", true);
                    editor.commit();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }

        mDemoSlider = (SliderLayout) findViewById(R.id.mainActivitySlider);
        final HashMap<String, Integer> file_maps = new HashMap<>();
        file_maps.put("Hampi", R.drawable.hampi);
        file_maps.put("Bijapur", R.drawable.bijapur);
        file_maps.put("Bangaloreee Fort", R.drawable.bangalorefort);
        file_maps.put("Wonder la", R.drawable.wonderla);


        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(getApplicationContext());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.DepthPage);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(3000);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment;
        FragmentTransaction ft;
        Intent intent;
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_dams:
                fragment = new damsFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.commit();
                break;

            case R.id.nav_temples:
                fragment = new templesFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.commit();
                break;

            case R.id.nav_beaches:
                fragment = new beachesFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.commit();
                break;

            case R.id.nav_hillstations:
                fragment = new hillstationsFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.commit();
                break;

            case R.id.nav_trekking:

                break;

            case R.id.nav_waterfalls:
                fragment = new waterfallsFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.commit();
                break;


            case R.id.new_place:
//                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "justmailtoavi@gmail.com, gauthamkumar.0414@gmail.com, charanshetty25595@gmail.com"));
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Namma Karnataka Place Request");
//                intent.putExtra(Intent.EXTRA_TEXT, "Place Name : \n\n District : \n\nCategory : \n\nAny details of the place ?");
//                startActivity(intent);
                fragment = new addNewPlace();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.commit();
                break;

            case R.id.feedback:
                intent = new Intent(MainActivity.this, feedbacks.class);
                startActivity(intent);
                //fragment = new feedback();
               // ft = getSupportFragmentManager().beginTransaction();
                //ft.replace(R.id.content_main, fragment);
                //ft.commit();
                break;
               // intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "justmailtoavi@gmail.com, gauthamkumar.0414@gmail.com, charanshetty25595@gmail.com"));
               // intent.putExtra(Intent.EXTRA_SUBJECT, "Namma Karnataka Feedback");
                //startActivity(intent);
               // break;


            case R.id.nav_home:
                intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                break;


            case R.id.nav_myLocation:
                intent = new Intent(MainActivity.this,MyLocation.class);
                startActivity(intent);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }
}
