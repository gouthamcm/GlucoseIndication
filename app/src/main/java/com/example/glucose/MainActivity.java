package com.example.glucose;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView glucose;
    Spinner food_item;
    Spinner food_serve;
    Button submit;
    EditText value;
    double weight;
    int position = -1;
    int p2 = -1;
    double result;
    int flag = 0;
    int gluco_pos;

    double slope;
    double time;
    Cursor cursor;

    Calendar calendar;
    DatabaseHelper mydb;
    TextView serve_in_gms;

    String[] food_items = {"Banana", "Dosai with Chutney","Apple", "Banana ripe", "Dates dried", "Grapefruit", "Grapes average","Orange average", "Peach average", "Peach canned in light syrup", "Pear average", "Pear canned in pear juice", "Prunes pitted", "Raisins", "Watermelon", "Baked beans average", "Black beans", "Chickpeas", "Chickpeas canned in brine", "Navy beans",
            "Kidney beans", "Lentils", "Soya beans", "Cashews", "Peanut", "Macaroni", "Macaroni and Cheese", "Spaghetti", "Spaghetti white boiled 20 min", "Spaghetti wholemeal boiled", "Banana cake made with sugar", "Banana cake made without sugar", "Sponge cake plain", "Vanilla cake made from packet mix with vanilla frosting","Waffles Aunt Jemima (Quaker Oats)", "Bagel white frozen",
            "Baguette white plain", "Hamburger bun", "Kaiser rol", "Pumpernickel bread", "50% cracked wheat kernel bread", "White wheat flour bread", "Wonder™ bread average", "Wonder bread average", "Pita bread white", "Corn tortilla", "Wheat tortilla", "Coca Cola average", "Fanta orange soft drink", "Lucozade original sparkling glucose drink", "Apple juice unsweetened average", "Cranberry juice cocktail (Ocean Spray)", "Gatorade", "Orange juice unsweetened",
            "Tomato juice canned", "all bran", "coco pops", "cornflakes" ,"cream of wheat" , "cream of wheat(instant)" , "grapenuts" , "muesli", "oatmeal" , "instant oatmeal", "puffed wheat", "raisin bran", "special K", "pearled barley", "sweet corn on the corb", "couscous", "quinoa", "white rice", "quick cooking white basmati", "brown rice", "converted white rice", "whole wheat kernels", "bulgur", "graham crackers", "vanilla wafers", "shortbread", "rice cakes", "rye crisps", "soda crackers", "ice cream regular", "ice cream premium", "milk full fat","milk skim", "reduced-fat yogurt with fruit"};
    String[] serve = {"1 serve", "2 serve", "3 serve"};
    Integer[] carbs_value = {24, 39, 16,25,33,11,17,11,13,18,13,11,33,44,6,15,23,30,22,30,25,18,6,12,4,48,51,48,22,27,29,22,36,58,13,35,15,15,16,12,20,68,50,14,15,24,26,26,34,42,25,31,15,18,9,15,26,25,26,30,22,24,17,26,21,19,21,42, 16,34,25,53,28,42,30,53,20,20,18,16,21,38,17,13,9,12,13,21};

    Integer[] gluco_value = {6, 5, 4, 3, 1};

    Integer[] serve_vaues = {120,150,120,120,60,120,120,120,120,120,120,120,60,60,120,150,150,150,150,150,150,150,150,50,50,180,180,180,200,220,60,60,63,111,35,70,30,30,30,30,30,200,170,30,30,50,50,250,250,250,250,250,250,250,250,30,30,30,250,250,30,30,250,250,30,30,30,150,150,150,150,150,150,150,150,50,150,25,25,25,25,25,25,50,50,250,250,200};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        glucose = findViewById(R.id.glucose);
        food_item = findViewById(R.id.spinner_fooditems);
        food_serve = findViewById(R.id.spinner_foodserve);
        value = findViewById(R.id.food_value_intake);
        submit = findViewById(R.id.submit);
        serve_in_gms = findViewById(R.id.serve_value);
        final Calendar calendar = Calendar.getInstance();
        time = calendar.get(Calendar.HOUR_OF_DAY)*60+calendar.get(Calendar.MINUTE)+calendar.get(Calendar.SECOND)/60;
        mydb = new DatabaseHelper(this);
        cursor = mydb.getdata_user();
        if(cursor.moveToNext()){
            glucose.setText(String.format("%.2f", cursor.getDouble(2)));
            weight = cursor.getDouble(3);
        }

        if(weight<28)
            gluco_pos = 0;
        else if(weight<47)
            gluco_pos = 1;
        else if(weight<76)
            gluco_pos = 2;
        else if(weight<105)
            gluco_pos = 3;
        else gluco_pos = 4;

        result = cursor.getDouble(2);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, food_items);
        food_item.setAdapter(arrayAdapter1);
        food_item.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                serve_in_gms.setText(serve_vaues[i] + "gms");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, serve);
        food_serve.setAdapter(arrayAdapter2);
        food_serve.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                p2 = i;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position == -1){
                    Toast.makeText(MainActivity.this, "Please select your food item", Toast.LENGTH_SHORT).show();
                    flag=0;
                }
                else{
                    String v = value.getText().toString().trim();

                    if(v.isEmpty()){
                        if(p2 == -1){
                            Toast.makeText(MainActivity.this, "Please enter ar select you number of serves", Toast.LENGTH_SHORT).show();
                            flag=0;
                        }
                        else{
                            result += (p2+1)*carbs_value[position]*gluco_value[gluco_pos]/10;
                            flag=1;
                        }
                    }
                    else{
                        result += Double.valueOf(v)*carbs_value[position]*gluco_value[gluco_pos]/10;

                        flag=1;
                    }
                }
                if(flag!=0){
                    Calendar calendar = Calendar.getInstance();
                    mydb.update_user(1, result, cursor.getDouble(3), cursor.getDouble(4), calendar.get(Calendar.HOUR_OF_DAY)*60+calendar.get(Calendar.MINUTE)+calendar.get(Calendar.SECOND)/60, result);
                    Log.e("TAG", "updated "+cursor.getString(2)+" "+cursor.getString(6)+" "+result);

                    glucose.setText(String.format("%.2f", result));
                    //update_glucose_value();
                    //Toast.makeText(MainActivity.this, String.valueOf(time)+","+ cursor.getDouble(4), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //update_glucose_value();


    }

    @Override
    protected void onResume() {
        mydb = new DatabaseHelper(this);
        calendar = Calendar.getInstance();
        cursor = mydb.getdata_user();

        //calendar.get(Calendar.HOUR_OF_DAY)*60+calendar.get(Calendar.MINUTE)+calendar.get(Calendar.SECOND)/60;
        if(cursor.moveToNext()){
            glucose.setText(String.format("%.2f", cursor.getDouble(2)));
        }


        mydb.update_user(1, cursor.getDouble(2),cursor.getDouble(3),cursor.getDouble(4),calendar.get(Calendar.HOUR_OF_DAY)*60+calendar.get(Calendar.MINUTE)+calendar.get(Calendar.SECOND)/60, cursor.getDouble(6));
        update_glucose_value();

        super.onResume();
    }

    private void update_glucose_value(){
        if(cursor.getDouble(2) > 110) {
            Calendar calendar = Calendar.getInstance();
            //Toast.makeText(this, cursor.getString(4)+","+ cursor.getString(5) , Toast.LENGTH_SHORT).show();
            slope = (110 - cursor.getDouble(6)) / (120);
            double r = cursor.getDouble(6) + slope * (calendar.get(Calendar.HOUR_OF_DAY)*60+calendar.get(Calendar.MINUTE)+calendar.get(Calendar.SECOND)/60 - cursor.getDouble(4));
            if(r<110){ r=110;}
            Log.e("TAG", "inside "+cursor.getString(2)+" "+cursor.getString(6));
            mydb.update_user(1, r, cursor.getDouble(3), cursor.getDouble(4), calendar.get(Calendar.HOUR_OF_DAY)*60+calendar.get(Calendar.MINUTE)+calendar.get(Calendar.SECOND)/60, cursor.getDouble(6));
            Log.e("TAG", "updated "+cursor.getString(2)+" "+cursor.getString(6));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}

