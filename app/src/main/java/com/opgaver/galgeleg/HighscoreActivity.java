package com.opgaver.galgeleg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HighscoreActivity extends AppCompatActivity {
    List<String[]> presentationList = new ArrayList<String[]>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("galgeleg_records",Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = prefs.getString("highscore", gson.toJson(new HashMap<String,Integer>()));
        Map<String,Integer> highscoreMap = gson.fromJson(json, new TypeToken<Map<String,Integer>>(){}.getType());

        Set<Map.Entry<String, Integer>> highsscoreSet = highscoreMap.entrySet(); //henter et Set af highscores ud af Mappet, så det kan indsættes i en liste
        List<Map.Entry<String, Integer>> highscoreList = new ArrayList<Map.Entry<String, Integer>>(highsscoreSet);//indsætter Set i List
        //sorterer List
        Collections.sort( highscoreList, new Comparator<Map.Entry<String, Integer>>()
        {
            public int compare( Map.Entry<String, Integer> hs1, Map.Entry<String, Integer> hs2 )
            {
                return (hs2.getValue()).compareTo( hs1.getValue() );
            }
        } );

        for(Iterator<Map.Entry<String, Integer>> hsi = highscoreList.iterator(); hsi.hasNext();){
            Map.Entry<String, Integer> entry = hsi.next();
            presentationList.add(new String[] {entry.getKey(), entry.getValue().toString() });
        }

        setContentView(R.layout.activity_highscore);
        RecyclerView recyclerListView = findViewById(R.id.highscoreList);
        recyclerListView.setLayoutManager(new LinearLayoutManager(this));
        recyclerListView.setAdapter(new ListeelemAdapter());


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
    }
    class ListeelemAdapter extends RecyclerView.Adapter<HighscoreListElementViewHolder> {
        @Override
        public int getItemCount()  {
            return presentationList.size();
        }

        @Override
        public HighscoreListElementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View highscoreFrameView = getLayoutInflater().inflate(R.layout.frame_highscore, parent, false);
            return  new HighscoreListElementViewHolder(highscoreFrameView);
        }

        @Override
        public void onBindViewHolder(HighscoreListElementViewHolder hleVH, int position) {
            hleVH.name.setText(presentationList.get(position)[0]);
            hleVH.score.setText(presentationList.get(position)[1]);

            if(position == 0){ hleVH.frame.setBackgroundColor(Color.rgb(255,223,0)); }
            else if(position == 1){ hleVH.frame.setBackgroundColor(Color.rgb(192,192,192));}
            else if(position == 2){ hleVH.frame.setBackgroundColor(Color.rgb(210,105,30)); }
            else { hleVH.frame.setBackgroundColor(Color.rgb(250, 250, 250)); }

        }
    }
    class HighscoreListElementViewHolder extends RecyclerView.ViewHolder {
        TextView name ,score;
        LinearLayout frame;
        public HighscoreListElementViewHolder(View itemView) {
            super(itemView);
            frame = itemView.findViewById(R.id.highscore_elementFrame);
            name = itemView.findViewById(R.id.highscore_text1);
            score = itemView.findViewById(R.id.highscore_text2);
        }
    }
}
