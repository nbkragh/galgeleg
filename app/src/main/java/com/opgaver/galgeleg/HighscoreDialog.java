package com.opgaver.galgeleg;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;


public class HighscoreDialog extends DialogFragment {

    EditText input;
    TextView viewScore;
    LinearLayout llayout;
    highscoreDialoglistener listener;
    int score;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        score = ((ResultActivity)getActivity()).score;
        viewScore = new TextView(getActivity());
        viewScore.setText(score+" points");
        input = new EditText(getActivity());
        input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(3)});
        input.setSingleLine(true);

        llayout = new LinearLayout(getActivity());
        llayout.setOrientation(LinearLayout.VERTICAL);
        llayout.setVerticalGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);
        llayout.addView(viewScore);
        llayout.addView(input);
        builder.setTitle("Enter Name");
        builder.setView(llayout);
        input.setFocusableInTouchMode(true);
        if (input.requestFocus()) {
            ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if(input.getText().length() > 0){
                    SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
                    Gson gson = new Gson();

                    String json = prefs.getString("highscore", gson.toJson(new HashMap<String,Integer>()));
                    Map<String,Integer> highscoreMap = gson.fromJson(json, new TypeToken<Map<String,Integer>>(){}.getType());
                    highscoreMap.put(input.getText().toString(), score);


                    json = gson.toJson(highscoreMap);
                    prefs.edit().putString("highscore", json).apply();
                    System.out.println("------------------->saved: "+prefs.getString("highscore", "{}"));
                    listener.onDialogDone();
                }
            }});
        return builder.create();
    }

    public interface highscoreDialoglistener {
        void onDialogDone();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (highscoreDialoglistener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement highscoreDialogListener");
        }
    }
}
