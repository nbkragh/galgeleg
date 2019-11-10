package com.opgaver.galgeleg;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Arrays;

public class DifficultyDialog extends DialogFragment {
    public interface DifficultyDialogListener {
        void onDialogPositiveClick(Integer[] in);
        void onDialogNegativeClick();
    }
    DifficultyDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DifficultyDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DifficultyDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)  {
        final ArrayList<Integer> selectedItems = new ArrayList( Arrays.asList(0,1,2));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Hvor svært skal det være?")
                .setMultiChoiceItems(
                        new String[]{"let","middel","svær"},
                        new boolean[]{true,true,true},
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    selectedItems.add(which);
                                } else if (selectedItems.contains(which)) {
                                    selectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        System.out.println("----------->trykkede: "+"OK - "+id);
                        listener.onDialogPositiveClick(selectedItems.toArray(new Integer[0]));
                    }
                })
                .setNegativeButton("Jeg tør ikke!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        System.out.println("----------->trykkede: "+"ikke OK - "+id);
                        listener.onDialogNegativeClick();

                    }
                });

        return builder.create();
    }
}
