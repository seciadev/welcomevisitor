package de.novem.bergamo.welcomevisitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class SplashActivity extends AppCompatActivity {

    private Button checkInButton;
    private Button checkOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkInButton = (Button)findViewById(R.id.checkInButton);
        checkInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                Intent intent = new Intent(SplashActivity.this, ScreenSlidePagerActivity.class);
                startActivity(intent);
            }
        });

        checkOutButton = (Button)findViewById(R.id.checkOutButton);
        checkOutButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(SplashActivity.this, CompanyListActivity.class);
                startActivity(intent);
            }
        });
    }


}
