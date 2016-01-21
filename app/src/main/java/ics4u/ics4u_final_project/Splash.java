/*
this class is for the splash screen of the app
 */
/*
Copyright (C) 2016 Isaac Wismer & Andrew Xu

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ics4u.ics4u_final_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Splash extends AppCompatActivity {
    /**
     * Duration of wait
     **/
    private final int displayLength = 3000;
    public static Activity fa;
    /**
     * Called when the activity is first created
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        //Links .java file with the corresponding xml file
        setContentView(R.layout.splash_screen);
        fa = this;
        ImportData im = new ImportData(this);
        im.execute("Import");
        /* New Handler to start the Menu-Activity
        and close this Splash-Screen after some seconds.*/

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startProgram();
            }
        }, displayLength);*/
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

    /**
     * Starts the main activity after the splash is done loading
     */
    public void startProgram() {
        Intent intent = new Intent(Splash.this, MainActivity.class);
        Splash.this.startActivity(intent);
        Splash.this.finish();
    }
}
