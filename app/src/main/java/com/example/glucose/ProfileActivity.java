package com.example.glucose;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Currency;

public class ProfileActivity extends AppCompatActivity {

    Button exit;
    Button update;
    EditText glucose;
    EditText weight;

    DatabaseHelper db;
    Cursor cursor;
    double time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        exit = findViewById(R.id.Exit);
        update = findViewById(R.id.update);
        glucose = findViewById(R.id.edit_glucose);
        weight = findViewById(R.id.edit_weight);

        Calendar calendar = Calendar.getInstance();
        time = calendar.get(Calendar.HOUR_OF_DAY)*60+calendar.get(Calendar.MINUTE)+calendar.get(Calendar.SECOND)/60;
        db = new DatabaseHelper(this);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String glucose_value = glucose.getText().toString().trim();
                String weight_value = weight.getText().toString().trim();
                if(TextUtils.isEmpty(glucose_value)){
                    glucose.setError("This field cannot be empty");
                    return;
                }
                else if (TextUtils.isEmpty(weight_value)){
                    weight.setError("This field cannot be empty");
                    return;
                }
                else{
                    db.update_user(1, Double.parseDouble(glucose_value), Double.parseDouble(weight_value), time, time, Double.parseDouble(glucose_value));
                    Toast.makeText(ProfileActivity.this, "Glucose Level updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
