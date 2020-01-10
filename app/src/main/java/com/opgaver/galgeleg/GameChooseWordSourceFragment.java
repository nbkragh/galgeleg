package com.opgaver.galgeleg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;


public class GameChooseWordSourceFragment extends Fragment implements View.OnClickListener {

    public Button button0, button1, button2, button3, button4;
    private ProgressBar loading;
    private LinearLayout readyButtonsLayout;

    public GameChooseWordSourceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_choose_word_source, container, false);

        button0 = view.findViewById(R.id.button0);
        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);
        readyButtonsLayout = view.findViewById(R.id.readyButtons);
        button3 = view.findViewById(R.id.button3);
        button4 = view.findViewById(R.id.button4);

        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

        loading = view.findViewById(R.id.spinner);
        setFreshState();
        ArrayList<String> aktuelleListe = GalgeLogikSingleton.getInstance().muligeOrd;
        if(aktuelleListe.size() < 2 ){
            aktuelleListe.clear();
            aktuelleListe.addAll(((GameActivity)getActivity()).reserveMuligeOrd);
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button0:
                if (((GameActivity) getActivity()).isActiveDownloadFromDR()) {
                    ((GameActivity) getActivity()).stopAllDownloading();
                    setFreshState();
                } else {
                    ((GameActivity) getActivity()).getWordsFromDR();
                    setDownloadingState(view);
                }
                break;
            case R.id.button1:
                if (((GameActivity) getActivity()).isActiveDownloadFromSpreadsheet()) {
                    ((GameActivity) getActivity()).stopAllDownloading();
                    setFreshState();
                } else {
                    GameSpreadsheetWordsDifficultyDialog diff = new GameSpreadsheetWordsDifficultyDialog();
                    diff.show(getActivity().getSupportFragmentManager(), "someTag");
                }
                break;
            case R.id.button2:
                setDoneState();
                ((GameActivity) getActivity()).galgelogik.nulstil();
                break;
            case R.id.button3:
                ((GameActivity)getActivity()).startGame();
                break;
            case R.id.button4:
                ((GameActivity)getActivity()).chooseWordFromList();
        }
    }

    public void setButtonText(Button butt, String s) {
        butt.setText(s);
    }

    public void turnLoadingSpin(boolean onoff) {
        if (onoff) {
            loading.setVisibility(View.VISIBLE);
        } else {
            loading.setVisibility(View.INVISIBLE);
        }
    }
    public void turnReadyButtons(boolean onoff ){
        if(onoff){
            readyButtonsLayout.setVisibility(View.VISIBLE);
        }else{
            readyButtonsLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void setFreshState(){
        button0.setClickable(true);
        button1.setClickable(true);
        button2.setClickable(true);
        button3.setClickable(true);
        button4.setClickable(true);
        turnLoadingSpin(false);
        turnReadyButtons(false);
        setButtonText(button0, "Download fra DR");
        setButtonText(button1, "Download fra Spreadsheet");

    }

    public void setDownloadingState(View butt){
        button0.setClickable(false);
        button1.setClickable(false);
        button2.setClickable(false);
        button3.setClickable(false);
        button4.setClickable(false);
        if(butt.getId() == R.id.button0){
            button0.setClickable(true);
            setButtonText(button0, "henter...\n cancel ?");
        }else if(butt.getId() == R.id.button1){
            button1.setClickable(true);
            setButtonText(button1, "henter...\n cancel ?");
        }
        turnLoadingSpin(true);
        turnReadyButtons(false);
    }

    public void setDoneState(){
        setFreshState();
        turnReadyButtons(true);
    }
}
