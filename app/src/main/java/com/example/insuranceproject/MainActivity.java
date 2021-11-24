package com.example.insuranceproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private enum answer {
        ERROR,
        ADMIN,
        USER,
        NOT_FOUND
    }

//    private final int MY_PERMISSIONS_GRANTED = 100;


    private MaterialEditText nameField;
    private MaterialEditText passwordField;
    private Button loginButton;

    private String userId;

    private Boolean exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        exit = false;
        nameField = findViewById(R.id.nameField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.button_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (findUser()) {
                    case USER:
                        Intent intentUser = new Intent(
                                MainActivity.this, AgentActivity.class);
                        intentUser.putExtra("userId", userId);
                        startActivity(intentUser);
                        clearEditText();
                        break;
                    case ADMIN:
                        Intent intentAdmin = new Intent(
                                MainActivity.this, AdministratorActivity.class);
                        startActivity(intentAdmin);
                        clearEditText();
                        break;
                    case NOT_FOUND:
                        nameField.setError("Проверьте данные");
                        passwordField.setError("Проверьте данные");
                        break;
                }

            }
        });

    }

    private answer findUser() {

        String inputName = Objects.requireNonNull(
                nameField.getText()).toString();

        String inputPassword = Objects.requireNonNull(passwordField.getText()).toString();

        if (TextUtils.isEmpty(inputName.replace(" ", ""))) {
            nameField.setError("пустая строка");
            return answer.ERROR;
        }
        if (TextUtils.isEmpty(inputPassword.replace(" ", ""))) {
            passwordField.setError("пустая строка");
            return answer.ERROR;
        }

        BufferedReader reader = null;


        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("AdminPasswordsAndLogins.txt")));
            if (checkUsersInBase(reader, inputName, inputPassword)) {
                return answer.ADMIN;
            }

            reader = new BufferedReader(
                    new InputStreamReader(openFileInput("UsersPasswordsAndLogins.txt")));

            if (checkUsersInBase(reader, inputName, inputPassword)) {
                return answer.USER;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return answer.NOT_FOUND;
    }

    private void clearEditText() {
        nameField.setText("");
        passwordField.setText("");
        nameField.setSelection(0);
    }

    private boolean checkUsersInBase(BufferedReader reader, String inputName, String inputPassword)
            throws IOException {
        String readLine;
        String[] arrayStr;

        while ((readLine = reader.readLine()) != null) {
            arrayStr = readLine.split("\t");
            if (arrayStr[1].compareTo(inputName) == 0) {

                if (inputPassword.compareTo(arrayStr[2]) == 0) {
                    userId = arrayStr[0];
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (exit) {
                    this.finish();
                    return true;
                } else {
                    Toast.makeText(this, "Press Back again to Exit.",
                            Toast.LENGTH_SHORT).show();
                    exit = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            exit = false;
                        }
                    }, 3 * 1000);
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
