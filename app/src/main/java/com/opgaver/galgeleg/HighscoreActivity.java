package com.opgaver.galgeleg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

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
        List<String> presentationList = new ArrayList<String>();
        for(Iterator<Map.Entry<String, Integer>> hsi = highscoreList.iterator(); hsi.hasNext();){
            Map.Entry<String, Integer> entry = hsi.next();
            presentationList.add(""+entry.getKey()+"  "+entry.getValue());
        }

        setContentView(R.layout.activity_highscore);
        ListView listView = findViewById(R.id.highscoreList);


        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.frame_highscore, R.id.text1, presentationList);
        listView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
    }
}
