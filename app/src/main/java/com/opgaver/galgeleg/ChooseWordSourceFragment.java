package com.opgaver.galgeleg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;


public class ChooseWordSourceFragment extends Fragment implements View.OnClickListener {

    public Button button0, button1, button2;
    private RelativeLayout loading;

    public ChooseWordSourceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_word_source, container, false);

        button0 = view.findViewById(R.id.button0);
        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);

        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);

        loading = view.findViewById(R.id.loadingPanel);
        toggleLoadingSpin(false);
        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.button0:
                if (((GameActivity) getActivity()).isActiveDownloadFromDR()) {
                    ((GameActivity) getActivity()).cancelDRDownload();
                } else {
                    ((GameActivity) getActivity()).getWordsFromDR();
                }
                break;
            case R.id.button1:
                if (((GameActivity) getActivity()).isActiveDownloadFromSpreadsheet()) {
                    ((GameActivity) getActivity()).cancelSpreadsheetDownload();
                } else {
                    DifficultyDialog diff = new DifficultyDialog();
                    diff.show(getActivity().getSupportFragmentManager(), "someTag");
                }
                break;
            default:
                ((GameActivity)getActivity()).startGame();
        }
    }

    public void setButtonText(Button butt, String s) {
        butt.setText(s);
    }

    public void toggleLoadingSpin(boolean onoff) {
        if (onoff) {
            loading.setVisibility(View.VISIBLE);
        } else {
            loading.setVisibility(View.GONE);
        }
    }

}
