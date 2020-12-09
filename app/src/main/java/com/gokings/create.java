package com.gokings;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class create extends AppCompatActivity {

    EditText o, time2;
    EditText time, on;
    TextView anyeca;
    TextView any;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        time = findViewById(R.id.time);
        on = findViewById(R.id.on);
        anyeca = findViewById(R.id.anyeca);
        o = findViewById(R.id.o);
        time2 = findViewById(R.id.time2);
        any = findViewById(R.id.any);




        anyeca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time.setVisibility(View.VISIBLE);
                on.setVisibility(View.VISIBLE);
            }
        });
        any.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time2.setVisibility(view.VISIBLE);
                o.setVisibility(view.VISIBLE);
            }


        });
    }
}
