package com.example.insuranceproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AgentViews extends AppCompatActivity {

    //    private ArrayList<String> usersList = new ArrayList<String>();
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_views);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listViewAgents);

        adapter = new ArrayAdapter<String>(
                this,
                R.layout.list_item_style,
                makeUsersList());

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intentAgent = new Intent(
                        AgentViews.this, InformationAboutAgent.class);
                intentAgent.putExtra("agentName", adapterView.getItemAtPosition(i).toString());
                startActivity(intentAgent);
            }
        });
    }

    private ArrayList<String> makeUsersList(){
        BufferedReader reader;
        ArrayList<String> usersList = new ArrayList<>();
        try {
            reader = new BufferedReader(
                    new InputStreamReader(openFileInput("UsersPasswordsAndLogins.txt")));

            String readLine;
            String[] arrayStr;

            while ((readLine = reader.readLine()) != null) {
                arrayStr = readLine.split("\t");
                usersList.add(arrayStr[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
            usersList.add("");
        }
        return usersList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.clear();
        adapter.addAll(makeUsersList());
        adapter.notifyDataSetChanged();
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