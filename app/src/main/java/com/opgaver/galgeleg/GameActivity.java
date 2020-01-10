
package com.opgaver.galgeleg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements GameSpreadsheetWordsDifficultyDialog.DifficultyDialogListener {
    final static GalgeLogik galgelogik = GalgeLogikSingleton.getInstance();

    GamePlayFragment gameFragment;

    GameChooseWordSourceFragment CWSfragment;

    private AsyncTask asyncTaske;
    private Thread downloadThread;
    private boolean activeDownloadFromDR = false;
    private boolean activeDownloadFromSpreadsheet = false;

    // antipattern for at kunne genskabe muligeord efter at have valgt et bestemt ord fra GalgeLogik til at spille
    // muligeOrd listen bliver genskabt i GameChooseWordSourceFragment
    public ArrayList<String> reserveMuligeOrd = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if (savedInstanceState == null) {
            gameFragment = new GamePlayFragment();
            CWSfragment = new GameChooseWordSourceFragment();
            reserveMuligeOrd.addAll(galgelogik.muligeOrd);
        }
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, CWSfragment).commit();
    }


    @Override
    public void onBackPressed() {
        galgelogik.nulstil();
        super.onBackPressed();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onDialogPositiveClick(Integer[] in) {
        activeDownloadFromSpreadsheet = true;
        asyncTaske = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                CWSfragment.setDownloadingState(CWSfragment.button1);
            }

            @Override
            protected Void doInBackground(Integer... numbers) {

                try {
                    Thread.sleep(2000);
                    String numbersAsString = TextUtils.join("", numbers);
                    System.out.println("-------->: " + numbersAsString);

                    if (!isCancelled()) {
                        galgelogik.hentOrdFraRegneark(numbersAsString);
                    }
                } catch (Exception e) {
                    System.out.println(e.getClass() + ": " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void resultat) {
                activeDownloadFromSpreadsheet = false;
                CWSfragment.setDoneState();
            }

            @Override
            protected void onCancelled() {
                activeDownloadFromSpreadsheet = false;
            }

        }.execute(in);
    }


    @Override
    public void onDialogNegativeClick() {

    }

    public void getWordsFromDR() {
        activeDownloadFromDR = true;
        Runnable r = new Runnable() {
            public void run() {
                try {
                    Thread.sleep(2000);
                    if (!Thread.currentThread().isInterrupted()) {
                        galgelogik.hentOrdFraDr();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                activeDownloadFromDR = false;
                                CWSfragment.setDoneState();
                                stopAllDownloading();
                            }
                        });
                    }
                } catch (Exception e) {
                    activeDownloadFromDR = false;
                    System.out.println(e.getClass() + ": " + e.getMessage());
                }
            }
        };
        downloadThread = new Thread(r);
        downloadThread.start();
    }

    public boolean isActiveDownloadFromDR() {
        return activeDownloadFromDR;
    }

    public boolean isActiveDownloadFromSpreadsheet() {
        return activeDownloadFromSpreadsheet;
    }


    public void startGame() {
        getSupportFragmentManager().beginTransaction().addToBackStack("CWLSFragment").replace(R.id.fragment_container, gameFragment).commit();
    }

    public void chooseWordFromList() {
        Intent i = new Intent(this, MuligeOrdActivity.class);
        startActivityForResult(i, 4);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED && resultCode == 4) {
            galgelogik.muligeOrd = new ArrayList<String>();
            galgelogik.muligeOrd.add((String) data.getStringExtra("selected_ord"));
            galgelogik.nulstil();
            startGame();
        }
    }

    public void stopAllDownloading() {
        if (asyncTaske != null && !asyncTaske.isCancelled()) {
            asyncTaske.cancel(true);
            asyncTaske = null;
        }
        if (downloadThread != null && !downloadThread.isInterrupted()) {
            downloadThread.interrupt();
            downloadThread = null;

        }
    }

    @Override
    protected void onDestroy() {
        stopAllDownloading();
        super.onDestroy();
    }
}
