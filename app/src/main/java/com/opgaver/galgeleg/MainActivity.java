
package com.opgaver.galgeleg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText chooseText = findViewById(R.id.textView2);

        final TextView synligtOrdView = findViewById(R.id.synligtOrdView);

        chooseText.setOnClickListener(this);
        chooseText.setOnEditorActionListener(new EditText.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                v.requestFocus();

                    synligtOrdView.setText(v.getText());

                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                return  true;

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.textView2){
            ((EditText)v).selectAll();
            System.out.println("i was clicked!");
        }
    }
}
