package com.opgaver.galgeleg;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class GamePlayFragment extends Fragment implements View.OnClickListener {
    static GalgeLogik galgelogik;
    ImageView galgeView;
    TextView synligtOrdView, gameoverTextView;
    EditText inputText;
    Button gameoverButton;
    TableLayout tastaturLayout;
    View view;
    TextView flyingLetter;
    int galgecoordinates;
    boolean sidstebogstavkorrekt = false;
    public GamePlayFragment() {
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
        view = inflater.inflate(R.layout.fragment_game, container, false);
        galgeView = view.findViewById(R.id.imageView);

        DisplayMetrics displayDimensioner = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayDimensioner);

        galgecoordinates = displayDimensioner.widthPixels/2;
        setImage(0);

        gameoverButton = view.findViewById(R.id.buttonGameOver);
        gameoverButton.setOnClickListener(this);
        gameoverTextView = view.findViewById(R.id.textGameover);
        synligtOrdView = view.findViewById(R.id.synligtOrdView);


        tastaturLayout = view.findViewById(R.id.tastaturView);

        generateTastatur(tastaturLayout);
        flyingLetter = view.findViewById(R.id.flyvendeBogstav);
        synligtOrdView.setText(galgelogik.getSynligtOrd());
        System.out.println("---->: "+galgelogik.getOrdet());
        return view ;
    }

    private void gæt(String s) {
        String t = galgelogik.getSynligtOrd();
        galgelogik.gætBogstav(s);
        if(!galgelogik.getSynligtOrd().equals( t)){
            sidstebogstavkorrekt = true;
        }else{
            sidstebogstavkorrekt = false;
        }
        setImage(galgelogik.getAntalForkerteBogstaver());

        if (galgelogik.erSpilletSlut()) {
            gameoverButton.setVisibility(View.VISIBLE);
            gameoverTextView.setVisibility(View.VISIBLE);

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
            Intent i = new Intent(getActivity(), GameResultActivity.class);
            i.putExtra("result", galgelogik.erSpilletVundet())
                    .putExtra("ordet",galgelogik.getOrdet())
                    .putExtra("forsog",galgelogik.getAntalForkerteBogstaver());
            startActivity(i);
        }else if(v.getClass() == Button.class && ((Button)v).getText() != null ){
            System.out.println(((Button)v).getText().toString());

            int[] viewcoordinates = new int[2];
            v.getLocationInWindow(viewcoordinates);
            gæt(((Button)v).getText().toString().toLowerCase());
            animateLetter(((Button)v).getText(),viewcoordinates[0], viewcoordinates[1]);


        }
    }

    private void generateTastatur(TableLayout tastaturFrame){

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZÆØÅ";

        TableRow tablerow = new TableRow(getActivity());

        tastaturFrame.setShrinkAllColumns(true);
        Button button ;
        int i = 0;
        for (Character c: alphabet.toCharArray()) {

            button = new Button(getActivity());
            button.setText(String.valueOf(c));
            button.setOnClickListener(this);
            button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));


            tablerow.addView(button);

            if( ++i >= 7){
                i = 0;
                tastaturFrame.addView(tablerow);
                tablerow = new TableRow(getActivity());
                tablerow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            }
        }
    }

    private void animateLetter(CharSequence c, int startX, int startY){
        flyingLetter.setText(c);
        if(sidstebogstavkorrekt) {
            flyingLetter.setTextColor(Color.GREEN);
        }else{
            flyingLetter.setTextColor(Color.RED);
        }
        flyingLetter.setVisibility(TextView.VISIBLE);
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation moveAnimation = new TranslateAnimation(startX, galgecoordinates-40,startY, 200);

        moveAnimation.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                flyingLetter.setVisibility(TextView.INVISIBLE);
                synligtOrdView.setText(galgelogik.getSynligtOrd());
            }
        });

        AlphaAnimation fadeAnimation = new AlphaAnimation(1,0);


        animationSet.addAnimation(moveAnimation);
        animationSet.addAnimation(fadeAnimation);
        animationSet.setDuration(700);
        flyingLetter.startAnimation(animationSet);

    }


}
