package com.example.insuranceproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class AdministratorActivity extends AppCompatActivity {

    private Button addAgentButton;
    private Button viewAgentsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addAgentButton = findViewById(R.id.button_add_administrator);
        viewAgentsButton = findViewById(R.id.button_view_administrators);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.button_add_administrator:
                        Intent intentAdd = new Intent(
                                AdministratorActivity.this, AgentRegistration.class);
                        startActivity(intentAdd);
                        break;
                    case R.id.button_view_administrators:
                        Intent intentView = new Intent(
                                AdministratorActivity.this, AgentViews.class);
                        startActivity(intentView);
                        break;
                }

            }
        };

        addAgentButton.setOnClickListener(listener);
        viewAgentsButton.setOnClickListener(listener);
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