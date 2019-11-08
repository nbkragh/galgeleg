package com.opgaver.galgeleg;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HighscoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("highscore", gson.toJson(new HashMap<String,Integer>()));
        Map<String,Integer> highscoreMap = gson.fromJson(json, new TypeToken<Map<String,Integer>>(){}.getType());
        List<Pair<String,Integer>> list = new ArrayList<Pair<String,Integer>>();

        for(Map.Entry<String, Integer> entry : highscoreMap.entrySet()) {
            list.add(new Pair<String,Integer>(entry.getKey(),entry.getValue()));
        }
        setContentView(R.layout.activity_highscore);
        ListView listView = findViewById(R.id.highscoreList);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.frame_highscore, list);
        listView.setAdapter(adapter);

    }
}
