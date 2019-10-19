package com.opgaver.galgeleg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{
    Button button1, button2, button3;
    final static GalgeLogik galgelogik = SingletonGalgeLogik.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);

    }

    public void onClick(View v) {
        Intent i = new Intent(this, GameActivity.class);
        switch (v.getId()) {
            case R.id.button1:
                i.putExtra("spiltype", GameActivity.SpilType.HARDCODED);
                break;
            case R.id.button2:
                i.putExtra("spiltype", GameActivity.SpilType.DR);
                break;
            case R.id.button3:
                i.putExtra("spiltype", GameActivity.SpilType.SPREADSHEET);
                break;
            default:
        }
        startActivity(i);
    }
}
