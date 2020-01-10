package com.opgaver.galgeleg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{
    Button button1, button2;
    final static GalgeLogik galgelogik = GalgeLogikSingleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);

    }

    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.button1:
                i = new Intent(this, GameActivity.class);
                startActivity(i);
                break;
            case R.id.button2:
                i = new Intent(this, HighscoreActivity.class);
                startActivity(i);
                break;
            default:
        }

    }

    @Override
    public void onBackPressed() {

    }
}
