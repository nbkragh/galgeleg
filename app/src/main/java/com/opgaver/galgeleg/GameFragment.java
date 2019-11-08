package com.opgaver.galgeleg;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment implements View.OnClickListener {
    static GalgeLogik galgelogik;
    ImageView galgeView;
    TextView synligtOrdView;
    EditText inputText;
    Button gameoverButton;
    public GameFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        galgelogik = GameActivity.galgelogik;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        galgeView = view.findViewById(R.id.imageView);
        setImage(0);


        inputText = view.findViewById(R.id.inputTextView);

        gameoverButton = view.findViewById(R.id.buttonGameOver);
        gameoverButton.setOnClickListener(this);
        inputText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //action når når man trykker OK/Done
                    gæt(v.getText().toString());

                //return false får soft-keyboardet til at lukke når man trykker OK/Done
                return true;
            }
        });

        synligtOrdView = view.findViewById(R.id.synligtOrdView);
        synligtOrdView.setText(galgelogik.getSynligtOrd());
        return view ;
    }

    @Override
    public void onResume() {
        super.onResume();
        inputText.setFocusableInTouchMode(true);
        if (inputText.requestFocus()) {
            ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    @Override
    public void onPause(){
        super.onPause();
/*        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
                InputMethodManager.HIDE_NOT_ALWAYS,
                InputMethodManager.HIDE_NOT_ALWAYS);*/

        ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(inputText.getWindowToken(),0);

    }
    private void gæt(String s) {

        galgelogik.gætBogstav(s);
        synligtOrdView.setText(galgelogik.getSynligtOrd());

        setImage(galgelogik.getAntalForkerteBogstaver());

        if (galgelogik.erSpilletSlut()) {
            inputText.setFocusable(false);
            gameoverButton.setVisibility(View.VISIBLE);
/*            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                ft.replace(R.id.fragment_container, details);

            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();*/


            if (galgelogik.erSpilletTabt()) {
            } else {

            }
        }else {
            inputText.setText("");
        }
    }
    private void setImage(int i) {
        if (i > 0 && i < 7) {
            galgeView.setImageResource(getResources().getIdentifier("forkert" + i, "drawable", getActivity().getPackageName()));
        } else if (i < 1) {
            galgeView.setImageResource(R.drawable.galge);
        } else {
            galgeView.setImageResource(R.drawable.forkert6);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonGameOver) {
            Intent i = new Intent(getActivity(), ResultActivity.class);
            i.putExtra("result", galgelogik.erSpilletVundet())
                    .putExtra("ordet",galgelogik.getOrdet())
                    .putExtra("forsog",galgelogik.getAntalForkerteBogstaver());
            startActivity(i);
        }
    }
}
