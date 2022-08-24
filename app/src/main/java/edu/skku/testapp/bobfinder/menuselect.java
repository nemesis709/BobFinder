package edu.skku.testapp.bobfinder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;


public class menuselect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuselect);

        Category.initCat();
        NumberPicker PICK_CAT = findViewById(R.id.Pick_cat);
        PICK_CAT.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        PICK_CAT.setMinValue(0);
        PICK_CAT.setMaxValue(Category.getCatList().size()-1);
        PICK_CAT.setDisplayedValues(Category.CatNames());

        People.initMan();
        NumberPicker PICK_PEO = findViewById(R.id.Pick_peo);
        PICK_PEO.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        PICK_PEO.setMinValue(0);
        PICK_PEO.setMaxValue(People.getPeopleList().size()-1);
        PICK_PEO.setDisplayedValues(People.Men());

        Price.initPrice();
        NumberPicker PICK_PRC = findViewById(R.id.Pick_prc);
        PICK_PRC.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        PICK_PRC.setMinValue(0);
        PICK_PRC.setMaxValue(Price.getPrcList().size()-1);
        PICK_PRC.setDisplayedValues(Price.PrcsNames());

        Button choose_btn = findViewById(R.id.choose_menu);

        choose_btn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), todaymenu.class);

            String s_Cat=Category.getCatList().get(PICK_CAT.getValue()).getCat();
            String s_People=People.getPeopleList().get(PICK_PEO.getValue()).getPeople();
            String s_Price=Price.getPrcList().get(PICK_PRC.getValue()).getPrc();

            Log.d("send People", s_People);
            Log.d("send Cat", s_Cat);
            Log.d("send Price", s_Price);
            intent.putExtra("Price",s_Price);
            intent.putExtra("People",s_People);
            intent.putExtra("Cat",s_Cat);
            startActivity(intent);
        });

        Button rtn_btn = findViewById(R.id.button2);

        rtn_btn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }
}