package ics4u.ics4u_final_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Andrew on 2016-01-08.
 */
public class Splash extends AppCompatActivity {
    /**
     * Duration of wait
     **/
    private final int displayLength = 3000;

    /**
     * Called when the activity is first created
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash_screen);

        /* New Handler to start the Menu-Activity
        and close this Splash-Screen after some seconds.
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*Create an Intent that will start the Menu-Activity*/
                Intent intent = new Intent(Splash.this, MainActivity.class);
                Splash.this.startActivity(intent);
                Splash.this.finish();
            }
        }, displayLength);
    }
}
