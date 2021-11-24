package com.example.insuranceproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class AgentActivity extends AppCompatActivity {

    private Button insureCarsButton;
    private Button insureHousesButton;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userId = getIntent().getExtras().get("userId").toString();

        insureCarsButton = findViewById(R.id.button_insure_cars);
        insureHousesButton = findViewById(R.id.button_insure_houses);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.button_insure_cars:
                        Intent intentCar = new Intent(
                                AgentActivity.this, CarInsurance.class);
                        intentCar.putExtra("userId", userId);
                        startActivity(intentCar);
                        break;
                    case R.id.button_insure_houses:
                        Intent intentHouse = new Intent(
                                AgentActivity.this, HouseInsurance.class);
                        intentHouse.putExtra("userId", userId);
                        startActivity(intentHouse);
                        break;

                }
            }
        };

        insureCarsButton.setOnClickListener(listener);
        insureHousesButton.setOnClickListener(listener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}