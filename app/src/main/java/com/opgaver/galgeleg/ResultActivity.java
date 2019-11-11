package com.opgaver.galgeleg;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.jinatonic.confetti.CommonConfetti;
import com.github.jinatonic.confetti.ConfettiManager;
import com.github.jinatonic.confetti.ConfettiSource;
import com.github.jinatonic.confetti.ConfettoGenerator;
import com.github.jinatonic.confetti.Utils;
import com.github.jinatonic.confetti.confetto.BitmapConfetto;
import com.github.jinatonic.confetti.confetto.Confetto;

import java.util.List;
import java.util.Random;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener, HighscoreDialog.highscoreDialoglistener {
    static GalgeLogik galgelogik;
    TextView textResult;
    TextView textInfo;
    Button backbutton;
    int score = 1;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        galgelogik = SingletonGalgeLogik.getInstance();
        galgelogik.nulstil();
        textResult = findViewById(R.id.textGameResult);
        textInfo = findViewById(R.id.textInfo);

        if(getIntent().getBooleanExtra("result", true)){
            textResult.setText(textResult.getText()+"vundet!");
            textInfo.setText("Du brugte "+getIntent().getIntExtra("forsog", 1000)+" forkerte bogstaver");

            handler.post(new Runnable() {
                @Override
                public void run() {
                    CommonConfetti.rainingConfetti(
                            (ViewGroup) ((ViewGroup) ResultActivity.this.findViewById(android.R.id.content)).getChildAt(0)
                            ,
                            new int[]{Color.BLACK, Color.RED, Color.YELLOW })
                            .infinite();
                }
            });
        }else{
            textResult.setText(textResult.getText()+"tabt !");
            textInfo.setText("Ordet var: "+getIntent().getStringExtra("ordet"));
            startØvKonfetti();
        }

        backbutton = findViewById(R.id.buttonDone);
        backbutton.setOnClickListener(this);

        score = someAdvancedAndVeryFairScoreCalculation(textInfo.length(), getIntent().getIntExtra("forsog", 1000) );
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
    public void onDialogDone(Boolean namentered) {
        Intent i;
        if (namentered) {
            i = new Intent(this, HighscoreActivity.class);
            startActivity(i);
        }else{
            i = new Intent(this, MenuActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onDialogDone(false);
    }

    private int someAdvancedAndVeryFairScoreCalculation(int wordlength, int antalforsog){
        return new Random().nextInt(1000);
    }

    private void startØvKonfetti(){
        final ViewGroup container = (ViewGroup) ((ViewGroup) ResultActivity.this.findViewById(android.R.id.content)).getChildAt(0);
        final Context context = this;

        handler.post(new Runnable() {
            @Override
            public void run() {
                final int containerMiddleX = container.getWidth() / 2;
                final int containerMiddleY = container.getHeight() / 2;
                final ConfettiSource confettiSource = new ConfettiSource(containerMiddleX, containerMiddleY);
                final List<Bitmap> allPossibleConfetti = Utils.generateConfettiBitmaps(new int[] { Color.parseColor("#7d5441") }, 20 /* size */);

                final int numConfetti = allPossibleConfetti.size();
                final ConfettoGenerator confettoGenerator = new ConfettoGenerator() {
                    @Override
                    public Confetto generateConfetto(Random random) {
                        final Bitmap bitmap = allPossibleConfetti.get(random.nextInt(numConfetti));
                        return new BitmapConfetto(bitmap);
                    }
                };
                new ConfettiManager(context, confettoGenerator, confettiSource, container)
                        .setEmissionDuration(3000)
                        .setEmissionRate(100)
                        .setVelocityX(0, containerMiddleX)
                        .setVelocityY(0, containerMiddleY)
                        .setRotationalVelocity(180, 180)
                        .animate();
            }
        });

    }
}
