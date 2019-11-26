package com.example.glucose;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {

    EditText name;
    EditText glucose;
    EditText weight;
    DatabaseHelper db;
    double time;
    Button button;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = findViewById(R.id.name_enter);
        glucose = findViewById(R.id.glucose_enter);
        weight = findViewById(R.id.weight_enter);
        button = findViewById(R.id.button_enter);

        Calendar calendar = Calendar.getInstance();
        time = calendar.get(Calendar.HOUR_OF_DAY)*60+calendar.get(Calendar.MINUTE)+calendar.get(Calendar.SECOND)/60;
        db = new DatabaseHelper(this);
        cursor = db.getdata_user();
        if(cursor.getCount()!=0){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name_value = name.getText().toString().trim();
                String weight_value = weight.getText().toString().trim();
                String glucose_value = glucose.getText().toString().trim();
                if(TextUtils.isEmpty(glucose_value)){
                    glucose.setError("This field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(weight_value)){
                    weight.setError("This field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(name_value)){
                    name.setError("This field cannot be empty");
                    return;
                }
                else{
                    db.insert_user(name_value, Double.parseDouble(weight_value), Double.parseDouble(glucose_value), time, time, Double.parseDouble(glucose_value));
                    Log.e("TAG", String.valueOf(time));
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();

                }
            }
        });
    }
}
