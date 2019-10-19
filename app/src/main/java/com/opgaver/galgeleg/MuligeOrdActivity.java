package com.opgaver.galgeleg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MuligeOrdActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listeView = new ListView(this);
        listeView.setOnItemClickListener(this);

        System.out.println("------------>: fra listen:"+GameActivity.galgelogik.muligeOrd);

        if(adapter != null){
            adapter.notifyDataSetChanged();
        }else {
            adapter = new ArrayAdapter(this, R.layout.ord_liste, R.id.ord_tekst, GameActivity.galgelogik.muligeOrd);
        }
        listeView.setAdapter(adapter);

        LinearLayout layout = new LinearLayout(this);
        layout.addView(listeView);
        setContentView(layout);


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
        System.out.println(adapterView);
        intent.putExtra("selected_ord", (String) adapterView.getItemAtPosition(i));
        setResult(4, intent);
        finish();
    }
}
