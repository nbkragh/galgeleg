package com.opgaver.galgeleg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MuligeOrdActivity extends AppCompatActivity  {
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listeView = new ListView(this);


        System.out.println("------------>: fra listen:"+GameActivity.galgelogik.muligeOrd);

        if(adapter != null){
            adapter.notifyDataSetChanged();
        }else {
            adapter = new ArrayAdapter(this, R.layout.mulige_ord_liste_element, R.id.ord_tekst, GameActivity.galgelogik.muligeOrd);
        }
        listeView.setAdapter(adapter);
        listeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent intent = new Intent();
                intent.putExtra("selected_ord", (String) adapterView.getItemAtPosition(i));
                setResult(4, intent);
                finish();
            }
        });


        setContentView(listeView);
    }

}
