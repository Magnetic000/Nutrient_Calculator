package ics4u.ics4u_final_project;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

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
        and close this Splash-Screen after some seconds.*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startProgram();
            }
        }, displayLength);
    }
//    public void splashPlayer() {
//        setContentView(R.layout.splash_screen);
//        VideoView videoHolder = (VideoView)findViewById(R.id.videoView);
//        Uri video = Uri.parse("android.resource://" + getPackageName() + "/"
//                + R.raw.splash);
//        videoHolder.setVideoURI(video);
//        videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            public void onCompletion(MediaPlayer mp) {
//                startProgram(); //jump to the next Activity
//            }
//        });
//        videoHolder.start();
//    }
    public void startProgram(){
        Intent intent = new Intent(Splash.this, MainActivity.class);
        Splash.this.startActivity(intent);
        Splash.this.finish();
    }
}
