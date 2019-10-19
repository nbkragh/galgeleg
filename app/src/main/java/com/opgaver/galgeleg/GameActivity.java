
package com.opgaver.galgeleg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements View.OnClickListener, DifficultyDialog.difficultyDialogListener {
    final static GalgeLogik galgelogik = SingletonGalgeLogik.getInstance();

    ImageView galgeView;
    TextView synligtOrdView;
    EditText inputText;
    Button seAlleOrdeneKnap;
    TextView gameoverText;
    static SpilType spiltype = SpilType.HARDCODED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        galgeView = findViewById(R.id.imageView);
        seAlleOrdeneKnap = findViewById(R.id.seOrdeneButton);

        seAlleOrdeneKnap.setOnClickListener(this);


        synligtOrdView = findViewById(R.id.synligtOrdView);
        synligtOrdView.setText(galgelogik.getSynligtOrd());

        gameoverText = findViewById(R.id.gameoverText);

        inputText = findViewById(R.id.inputTextView);
        inputText.setOnClickListener(this);


        setImage(0);

        inputText.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //action når når man trykker OK/Done
                System.out.println("onEditorAction");

                gæt(v.getText().toString());

                //InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                //return false får soft-keyboardet til at lukke når man trykker OK/Done
                return false;
            }
        });
        anvendSpilType();
    }

    @Override
    public void onClick(View v) {
        // Når tekstfeltet klikkes på så markeres bogstavet der står deri,
        // så brugeren ikke behøver at slette det før han skriver et nyt.
        if (v.getId() == R.id.inputTextView) {
            ((EditText) v).selectAll();
            System.out.println("inputTextView was clicked!");
        } else if (v.getId() == R.id.seOrdeneButton) {
            if (galgelogik.erSpilletSlut()) {
                galgelogik.nulstil();
                finish();
            } else {
                Intent i = new Intent(this, MuligeOrdActivity.class);
                startActivityForResult(i, 4);
            }
        }
    }

    private void gæt(String s) {

        GameActivity.galgelogik.gætBogstav(s);
        synligtOrdView.setText(galgelogik.getSynligtOrd());

        setImage(galgelogik.getAntalForkerteBogstaver());

        activeSeAlleOrdeneKnap(false);

        if (galgelogik.erSpilletSlut()) {
            activeSeAlleOrdeneKnap(true);
            seAlleOrdeneKnap.setText("Tilbage");

            inputText.setFocusable(false);
            if (galgelogik.erSpilletTabt()) {
                gameoverText.setText("Game Lost");
            } else {
                gameoverText.setText("Game Won");

            }
        }
    }


    private void setImage(int i) {
        if (i > 0 && i < 7) {
            galgeView.setImageResource(getResources().getIdentifier("forkert" + i, "drawable", getPackageName()));
        } else if (i < 1) {
            galgeView.setImageResource(R.drawable.galge);
        } else {
            galgeView.setImageResource(R.drawable.forkert6);
        }
    }

    protected enum SpilType {
        HARDCODED,
        DR,
        SPREADSHEET
    }

    private void anvendSpilType() {
        spiltype = (SpilType) getIntent().getSerializableExtra("spiltype");
        System.out.println("-----------------> spiltype:" + spiltype);
        activeSeAlleOrdeneKnap(false);
        inputText.setFocusable(true);
        //synligtOrdView.setText(galgelogik.getSynligtOrd());
        try {
            if (spiltype == SpilType.DR) {
                hentOrdfraDR();
            } else if (spiltype == SpilType.SPREADSHEET) {
                DifficultyDialog diff = new DifficultyDialog();
                diff.show(getSupportFragmentManager(), "someTag");

            } else {
                //i tilfældet af at muligeOrd er blevet ændret i et af de andre scenarier,
                // så hentes de hardcodede ord fra et midlertidigt Galgelogik object
                //og indsættes i singleton Galgelogik objectet's muligeOrd igen
                GalgeLogik newlogik = new GalgeLogik();
                galgelogik.muligeOrd.clear();
                galgelogik.muligeOrd.addAll(newlogik.muligeOrd);
                newlogik = null;
                galgelogik.nulstil();
                synligtOrdView.setText(galgelogik.getSynligtOrd());
                activeSeAlleOrdeneKnap(true);
            }

        } catch (Exception e) {
            System.out.println(e.getClass() + ": " + e.getMessage());
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onDialogPositiveClick(Integer[] in) {

        new AsyncTask<Integer, Void, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(Integer... numbers) {
                try {
                    String numbersAsString = TextUtils.join("", numbers);
                    System.out.println("-------->: " + numbersAsString);
                    galgelogik.hentOrdFraRegneark(numbersAsString);
                    return galgelogik.muligeOrd;
                } catch (Exception e) {
                    System.out.println(e.getClass() + ": " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<String> strings) {
                System.out.println("--------------->:" + strings);
                activeSeAlleOrdeneKnap(true);
                synligtOrdView.setText(galgelogik.getSynligtOrd());
            }
        }.execute(in);
    }


    @Override
    public void onDialogNegativeClick() {
        finish();
    }

    @SuppressLint("StaticFieldLeak")
    private void hentOrdfraDR() {
        new AsyncTask<Void, Void, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(Void... voids) {
                try {
                    galgelogik.hentOrdFraDr();
                    return galgelogik.muligeOrd;
                } catch (Exception e) {
                    System.out.println(e.getClass() + ": " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<String> strings) {
                System.out.println("--------------->:" + strings);
                activeSeAlleOrdeneKnap(true);
                synligtOrdView.setText(galgelogik.getSynligtOrd());

            }
        }.execute();
    }

    @Override
    protected void onActivityResult(int request, int result, Intent intent) {
        super.onActivityResult(request, result, intent);
        galgelogik.muligeOrd = new ArrayList<String>();

        if (intent == null) {
            anvendSpilType();
        } else {

            System.out.println("---------->ordet valgt: " + intent.getStringExtra("selected_ord"));
            galgelogik.muligeOrd.add(intent.getStringExtra("selected_ord"));
            galgelogik.nulstil();
        }
        synligtOrdView.setText(galgelogik.getSynligtOrd());
    }

    private void activeSeAlleOrdeneKnap(boolean active){
        if(active) {
            seAlleOrdeneKnap.setClickable(true);
            seAlleOrdeneKnap.setTextColor(Color.BLACK);
        }else{
            seAlleOrdeneKnap.setClickable(false);
            seAlleOrdeneKnap.setTextColor(Color.LTGRAY);

        }

    }
}