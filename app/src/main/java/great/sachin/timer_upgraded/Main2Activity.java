package great.sachin.timer_upgraded;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {
    //Timing for the Home Screen
    private static int SPLASH_TIMEOUT = 1800;
    ImageView bgapp;
    Animation bganim;
    TextView Welcome;
    TextView BelowWelcome;
    TextView MainText;
    int startDelay = 600;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        bgapp = findViewById(R.id.bgapp);
        MainText = findViewById(R.id.MainText);
        Welcome = findViewById(R.id.Welcome);
        BelowWelcome = findViewById(R.id.BelowWelcome);

        bganim = AnimationUtils.loadAnimation(this,R.anim.bganim);

        bgapp.animate().translationY(-1900).setDuration(1200).setStartDelay(startDelay);
        MainText.animate().translationY(100).alpha(0).setDuration(600).setStartDelay(startDelay);
        Welcome.animate().translationX(-50).alpha(0).setDuration(600).setStartDelay(startDelay);
        BelowWelcome.animate().translationX(-50).alpha(0).setDuration(600).setStartDelay(startDelay);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(
                        Main2Activity.this,Main3Activity.class);
                startActivity(homeIntent);
                overridePendingTransition(R.anim.goup,R.anim.godown);
                finish();
            }
        },SPLASH_TIMEOUT);
    }
}
