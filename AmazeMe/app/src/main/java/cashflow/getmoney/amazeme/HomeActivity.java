package cashflow.getmoney.amazeme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class HomeActivity extends AppCompatActivity {

    Button viewProfile;
    Button newGame;
    String user;

    BroadcastReceiver br;

    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        user = intent.getStringExtra("USERNAME");

        logo = (ImageView) findViewById(R.id.logo);
        logo.setImageResource(R.drawable.logo);

        // Listens for a logout broadcast
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.package.ACTION_LOGOUT");

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive", "Logout in progress");
                finish();
            }
        };
        registerReceiver(br, filter);

        newGame = (Button) findViewById(R.id.newGame);
        viewProfile = (Button) findViewById(R.id.viewProfile);

        // Adds toolbar to activity
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        // Sets up toolbar
        Drawable settings = ContextCompat.getDrawable(this, R.drawable.ic_settings);

        mToolbar.setNavigationIcon(settings);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("HOME", "CLICKED ON SETTINGS");
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);

                startActivity(intent);
            }
        });

        // Set onClickListener for buttons
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, GoogleMapsActivity.class);
                startActivity(intent);
            }
        });

        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }


}
