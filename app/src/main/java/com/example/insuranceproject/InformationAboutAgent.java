package com.example.insuranceproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.TypedArrayUtils;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class InformationAboutAgent extends AppCompatActivity {

    private EditText agentLogin;
    private EditText agentPassword;

    private String agentName;

    private EditText insuredHouses;

    private String agentID;
    private EditText insuredCars;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_about_agent);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        agentLogin = findViewById(R.id.textViewAgentLogin);
        agentPassword = findViewById(R.id.textViewAgentPassword);
        insuredHouses = findViewById(R.id.textViewCountInsuredHouses);
        insuredCars = findViewById(R.id.textViewCountInsuredCars);
        ListView items = findViewById(R.id.list_item);

        Button buttonChange = findViewById(R.id.buttonChange);
        Button buttonDelete = findViewById(R.id.buttonDelete);

        agentName = getIntent().getExtras().get("agentName").toString();
        findPasswordAndPositionInFile();

        ArrayList<String> arrayForAdapterCar = getArrayAdapterCars();
        ArrayList<String> arrayForAdapterHouse = getArrayAdapterHouses();
        ArrayList<String> arrayForAdapter = getArrayAdapterCars();

        if (arrayForAdapterHouse.isEmpty() && arrayForAdapterCar.isEmpty()){
            arrayForAdapter.add("");
        }
        else if(arrayForAdapterCar.isEmpty()){
            arrayForAdapter = arrayForAdapterHouse;
        }
        else if(arrayForAdapterHouse.isEmpty()){
            arrayForAdapter = arrayForAdapterCar;
        }
        else {
            arrayForAdapter = arrayForAdapterCar;
            arrayForAdapter.add("\n");
            arrayForAdapter.addAll(arrayForAdapterHouse);
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.list_item_style_view,
                arrayForAdapter
        );


        items.setAdapter(adapter);

        agentLogin.setText(agentName);


        View.OnClickListener listener = new View.OnClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.buttonDelete:
                        deleteAgent();
                        finish();
                        break;
                    case R.id.buttonChange:
                        showDialogChangeLoginPassword();
                        break;
                }
            }
        };
        buttonDelete.setOnClickListener(listener);
        buttonChange.setOnClickListener(listener);

    }

    private ArrayList<String> getArrayAdapterHouses() {
        int countHouse = 0;
        String[] arrayStr;
        BufferedReader reader = null;
        ArrayList<String> arrayList = new ArrayList<>();

        try {
            reader = new BufferedReader(
                    new InputStreamReader(openFileInput("InsuredHouses.txt")));
            String readLine;

            while ((readLine = reader.readLine()) != null) {
                arrayStr = readLine.split("\t");
                if (arrayStr[0].compareTo(agentID) == 0) {
                    String str = "Квартира на улице: " + arrayStr[1] + ", " + "номер : " +
                            arrayStr[2] + "\n" + "Этаж: " + arrayStr[3] + " " +
                            "Квартира: " + arrayStr[4] + "\n" + "Цена: " + arrayStr[12];

                    countHouse++;
                    arrayList.add(str);
                }
            }

        } catch (IOException e) {
            arrayList = null;
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

        insuredHouses.setText(countHouse + " Квартир");

        return arrayList;
    }

    private ArrayList<String> getArrayAdapterCars() {
        int countCar = 0;
        String[] arrayStr;
        BufferedReader reader = null;
        ArrayList<String> arrayList = new ArrayList<>();

        try {
            reader = new BufferedReader(
                    new InputStreamReader(openFileInput("InsuredСars.txt")));
            String readLine;

            while ((readLine = reader.readLine()) != null) {
                arrayStr = readLine.split("\t");
                if (arrayStr[0].compareTo(agentID) == 0) {
                    String str = "Машина: " + arrayStr[1] + "\n" + "Опыт вождения: " + arrayStr[2] +
                            "\n" + "Мощность двигателя: " + arrayStr[8] + "\n" +
                            "Цена: " + arrayStr[10];
                    countCar++;
                    arrayList.add(str);
                }
            }

        } catch (IOException e) {
            arrayList = null;
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }

        insuredCars.setText(countCar + " Машин");

        return arrayList;
    }

    @SuppressLint("SetTextI18n")
    private void findPasswordAndPositionInFile() {

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(openFileInput("UsersPasswordsAndLogins.txt")))) {

            String readLine;
            String[] arrayStr;
            while ((readLine = reader.readLine()) != null) {
                arrayStr = readLine.split("\t");
                if (arrayStr[1].compareTo(agentName) == 0) {

                    agentID = arrayStr[0];
                    agentPassword.setText(arrayStr[2]);


                    return;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //log the exception
    }

    private void deleteAgent() {
        deleteFromFile("InsuredHouses.txt");
        deleteFromFile("InsuredСars.txt");
        deleteFromFile("UsersPasswordsAndLogins.txt");
    }

    private void deleteFromFile(String fileName) {
        StringBuilder stringBuffer = new StringBuilder();
        String readLine;
        String readName;
        String str = null;
        int positionToSpace;

        String[] arrayStr;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(openFileInput((fileName))))) {
            while ((readLine = reader.readLine()) != null) {

                arrayStr = readLine.split("\t");

                if (arrayStr[0].compareTo(agentID) != 0) {
                    stringBuffer.append(readLine).append("\n");
                }

            }

            reader.close();
            str = stringBuffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        char[] buffer = new char[0];
        if (str != null) {
            buffer = new char[str.length()];
        }
        if (str != null) {
            str.getChars(0, str.length(), buffer, 0);
        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(
                fileName, MODE_PRIVATE)))) {
            writer.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showDialogChangeLoginPassword() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);

        View registerWindow = inflater.inflate(R.layout.activity_change_login_password, null);
        builder.setView(registerWindow);
        MaterialEditText editTextLogin = registerWindow.findViewById(R.id.changeLogin);
        MaterialEditText editTextPassword = registerWindow.findViewById(R.id.changePassword);

        editTextLogin.setText(agentLogin.getText().toString());
        editTextPassword.setText(agentPassword.getText().toString());


        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setNeutralButton("Изменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        final AlertDialog dialog = builder.create();



        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (changeAgentData(editTextLogin, editTextPassword)) {
                    agentLogin.setText(editTextLogin.getText().toString());
                    agentPassword.setText(editTextPassword.getText().toString());
                    dialog.dismiss();

                }
            }
        });
    }

    private boolean changeAgentData(MaterialEditText editTextLogin,
                                    MaterialEditText editTextPassword) {

        boolean answer = false;


        String newLogin = editTextLogin.getText().toString().compareTo(
                agentLogin.getText().toString()) == 0 ?
                null : editTextLogin.getText().toString();

        String newPassword = editTextPassword.getText().toString().compareTo(
                agentPassword.getText().toString()) == 0 ?
                null : editTextPassword.getText().toString();

        if(checkFields(newLogin, newPassword, editTextLogin, editTextPassword)) {

            changeData(newLogin, newPassword, "UsersPasswordsAndLogins.txt");
            answer = true;
        }
        return answer;
    }

    private boolean checkFields(String textLogin, String textPassword,
                                MaterialEditText editTextLogin,
                                MaterialEditText editTextPassword) {
        boolean answer = true;
        if(textLogin != null) {
            String newLogin = textLogin.replace(" ", "");



            if (TextUtils.isEmpty(newLogin)) {
                editTextLogin.setError("пустая строка");
                answer = false;
            }
        }
        if(textPassword != null) {
            String newPassword = textPassword.replace(" ", "");
            if (TextUtils.isEmpty(newPassword)) {
                editTextPassword.setError("пустая строка");
                answer = false;
            }
        }

        if(textLogin != null){

            if(!checkUserExistence(editTextLogin)){

                answer = false;
            }
        }

        return answer;
    }

    private boolean checkUserExistence(MaterialEditText editTextLogin) {

        String readLine;
        String[] arrayStr;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(openFileInput("UsersPasswordsAndLogins.txt")))) {

            while ((readLine = reader.readLine()) != null) {
                arrayStr = readLine.split("\t");
                if (arrayStr[1].compareTo(editTextLogin.getText().toString()) == 0) {
                    editTextLogin.setError("Такой логин уже существует");
                    return false;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();

        }

        return true;

    }

    private void changeData(String login, String password, String fileName) {
        StringBuilder stringBuffer = new StringBuilder();
        String readerLine;
        String[] arrayStr;
        String str = null;
        boolean answer = true;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(openFileInput((fileName))))) {
            while ((readerLine = reader.readLine()) != null) {
                arrayStr = readerLine.split("\t");
                System.out.println(agentName + "\t" + login);
                if (arrayStr[1].compareTo(agentName) != 0) {
                    stringBuffer.append(readerLine).append("\n");
                } else {
                    String newStr = null;
                    if (password != null && login != null) {
                        agentName = login;
                        newStr = arrayStr[0] + "\t" + login + "\t" + password + "\t" +
                                readerLine.substring(arrayStr[0].length() + arrayStr[1].length() +
                                        arrayStr[2].length() + 2);
                    }

                    else if (login != null) {
                        agentName = login;
                        newStr = arrayStr[0] + "\t" + login + readerLine.substring(
                                arrayStr[0].length() + arrayStr[1].length() + 1);
                    }
                    else if (password != null) {
                        newStr = readerLine.substring(
                                0, arrayStr[0].length() + arrayStr[1].length() + 2) +
                                password +
                                readerLine.substring(arrayStr[0].length() + arrayStr[1].length() +
                                        arrayStr[2].length() + 2);
                    }
                    stringBuffer.append(newStr).append("\n");
                }

            }
            reader.close();
            str = stringBuffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        char[] buffer = new char[0];
        if (str != null) {
            buffer = new char[str.length()];
        }
        if (str != null) {
            str.getChars(0, str.length(), buffer, 0);
        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(
                fileName, MODE_PRIVATE)))) {
            writer.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

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