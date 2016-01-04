package ics4u.ics4u_final_project;

/**
 * Created by Andrew on 2016-01-04.
 */
import android.view.View;
import android.widget.Button;

public class OnClickListener implements View.OnClickListener {
    public void onClick(View v){
        Button btn =(Button) v;
        btn.setText("Reached");
    }
}
