package de.novem.bergamo.welcomevisitor;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    private static final String DIALOG_PASSWORD = "DialogPassword";

    private ImageButton checkInButton;
    private ImageButton checkOutButton;
    private TextClock clockTextView;
    private TextClock dateclockTextView;
    private TextView counterVisitorsTextView;
    private TextView adminAreaTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.activity_splash);

        checkInButton = (ImageButton)findViewById(R.id.checkInButton);
        checkInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                Intent intent = new Intent(SplashActivity.this, ScreenSlidePagerActivity.class);
                startActivity(intent);
            }
        });

        checkOutButton = (ImageButton)findViewById(R.id.checkOutButton);
        checkOutButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(SplashActivity.this, CompanyListActivity.class);
                startActivity(intent);
            }
        });

        clockTextView = (TextClock)findViewById(R.id.timestamp_text_view);
        clockTextView.setFormat24Hour("k:mm");

        dateclockTextView = (TextClock)findViewById(R.id.date_stamp_text_clock);
        dateclockTextView.setFormat24Hour("EEE dd MMM");

        adminAreaTextView = (TextView)findViewById(R.id.admin_link_textview);
        adminAreaTextView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                PasswordDialogFragment dialog = new PasswordDialogFragment();
                dialog.show(getSupportFragmentManager(), DIALOG_PASSWORD);
                /*Intent intent = new Intent(SplashActivity.this, AdminTableActivity.class);
                startActivity(intent);*/
            }
        });


    }

    @Override
    protected void onResume(){
        super.onResume();

        counterVisitorsTextView = (TextView)findViewById(R.id.visitors_counter_text_view);
        VisitorLab visitorLab = VisitorLab.get(getApplicationContext());
        int counterVisitor = visitorLab.getUncompletedVisitors().size();

        Resources res = getResources();
        String counterVis = res.getQuantityString(R.plurals.numberOfVisitorsPresent, counterVisitor, counterVisitor);
        counterVisitorsTextView.setText(counterVis);

    }

    @Override
    public void onBackPressed(){
        //do nothing
    }


}
