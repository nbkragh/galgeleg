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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HighscoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("galgeleg_records",Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = prefs.getString("highscore", gson.toJson(new HashMap<String,Integer>()));
        Map<String,Integer> highscoreMap = gson.fromJson(json, new TypeToken<Map<String,Integer>>(){}.getType());
        List<String> list = new ArrayList<String>();

        for(Map.Entry<String, Integer> entry : highscoreMap.entrySet()) {
            //TODO: sort list by Value
            list.add(entry.getKey()+" \t "+entry.getValue());
        }
        setContentView(R.layout.activity_highscore);
        ListView listView = findViewById(R.id.highscoreList);


        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.frame_highscore, R.id.text1, list);
        listView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
    }
}
