package cashflow.getmoney.amazeme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    TextView name;
    TextView highScore;
    TextView mazesCompleted;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = (TextView) findViewById(R.id.name_view);
        highScore = (TextView) findViewById(R.id.fastest_maze_completion);
        mazesCompleted = (TextView) findViewById(R.id.mazes_completed);
        logo = (ImageView) findViewById(R.id.logo);
        logo.setImageResource(R.drawable.logo);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String nameString = sharedPreferences.getString("USER", "");
        name.setText(nameString);
//        highScore.setText(intent.getStringExtra("highScore"));

        // Adds toolbar to activity
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_back);

    }
}
