package a2dv606_aa223de.movieapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Abeer on 8/12/2017.
 * splash screen show stc logo will appear for few seconds
 */

public class SplashActivity extends Activity {
    /** Duration of wait **/
    private final int DELAY_LENGTH = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        /* New Handler to start the Main Activity and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, DELAY_LENGTH);

    }
}