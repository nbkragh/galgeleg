package com.opgaver.galgeleg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener, HighscoreDialog.highscoreDialoglistener {
    static GalgeLogik galgelogik;
    TextView textResult;
    TextView textInfo;
    Button backbutton;
    int score = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        galgelogik = SingletonGalgeLogik.getInstance();

        textResult = findViewById(R.id.textGameResult);
        textInfo = findViewById(R.id.textInfo);
        if(getIntent().getBooleanExtra("result", true)){
            textResult.setText(textResult.getText()+"vundet!");
            textInfo.setText("Du brugte "+getIntent().getIntExtra("forsog", 1000)+" forsøg");
        }else{
            textResult.setText(textResult.getText()+"tabt !");
            textInfo.setText("Ordet var: "+getIntent().getStringExtra("ordet"));
        }

        backbutton = findViewById(R.id.buttonDone);
        backbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == backbutton){
            HighscoreDialog hsDialog = new HighscoreDialog();
            hsDialog.show(getSupportFragmentManager(), "someTag");

            /*           Intent i = new Intent(this, MenuActivity.class);
            startActivity(i);*/
        }
    }

    @Override
    public void onDialogDone() {
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onDialogDone();
    }
}
