package ics4u.ics4u_final_project;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Andrew on 2016-01-08.
 */
public class Splash extends Activity{
    /** Duration of wait **/
    private final int displayLength = 1000;

    /**Called when the activity is first created */
    @Override
    public void onCreate(Bundle icicle){
        super.onCreate(icicle);
        setContentView(R.layout.splash);

        /* New Handler to start the Menu-Activity
        and close this Splash-Screen after some seconds.
         */
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                /*Create an Intent that will start the Menu-Activity*/
                Intent mainIntent = new Intent(Splash.this,MainActivity.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, displayLength);
    }
}
