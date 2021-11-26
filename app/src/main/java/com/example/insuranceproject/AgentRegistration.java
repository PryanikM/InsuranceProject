package com.example.insuranceproject;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Objects;

public class AgentRegistration extends AppCompatActivity {

    private MaterialEditText agentLogin;

    private MaterialEditText agentPassword;
    private MaterialEditText agentPasswordAgain;
    private MaterialEditText agentId;
    private MaterialEditText agentName;
    private MaterialEditText agentSurname;
    private MaterialEditText agentPatronymic;

    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_registration);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        agentLogin = findViewById(R.id.loginAgent);
        agentPassword = findViewById(R.id.passwordAgent);
        agentPasswordAgain = findViewById(R.id.passwordAgainAgent);
        agentId = findViewById(R.id.idAgent);
        agentName = findViewById(R.id.nameAgent);
        agentSurname = findViewById(R.id.surnameAgent);
        agentPatronymic = findViewById(R.id.patronymicAgent);
        registerButton = findViewById(R.id.button_register_agent);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAgent();
            }
        });

        InputFilter filterEditText = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                if(charSequence.equals("")){ // for backspace
                    return charSequence;
                }
                if(charSequence.toString().matches("[a-zA-Zа-яА-Я]+")){
                    return charSequence;
                }
                return "";
            }
        };
        agentName.setFilters(new InputFilter[]{filterEditText});
        agentSurname.setFilters(new InputFilter[]{filterEditText});
        agentPatronymic.setFilters(new InputFilter[]{filterEditText});

    }

    void addAgent() {
        String inputNameAgent = Objects.requireNonNull(
                agentLogin.getText()).toString();

        String inputPasswordAgent = Objects.requireNonNull(agentPassword.getText()).toString();

        if (checkFields(inputNameAgent, inputPasswordAgent)) {

            if (checkUserExistence(inputNameAgent)) {
                writeData(inputNameAgent, inputPasswordAgent);
            }


        }

    }

    boolean checkFields(String nameAgent, String passwordAgent) {
        boolean answer = true;
        if (TextUtils.isEmpty(nameAgent.replace(" ", ""))) {
            agentLogin.setError("пустая строка");
            answer = false;
        }
        if (TextUtils.isEmpty(passwordAgent.replace(" ", ""))) {
            agentPassword.setError("пустая строка");
            answer = false;
        }

        if (TextUtils.isEmpty(agentName.getText().toString().replace(" ", ""))) {
            agentName.setError("пустая строка");
            answer = false;
        }
        if (TextUtils.isEmpty(agentSurname.getText().toString().replace(" ", ""))) {
            agentSurname.setError("пустая строка");
            answer = false;
        }
        if (TextUtils.isEmpty(agentPatronymic.getText().toString().replace(" ", ""))) {
            agentPatronymic.setError("пустая строка");
            answer = false;
        }
        if (TextUtils.isEmpty(agentId.getText().toString().replace(" ", ""))) {
            agentId.setError("пустая строка");
            answer = false;
        }
        else {
            try {
                if( Integer.parseInt(agentId.getText().toString()) < 0){
                    answer = false;
                    agentId.setError("Вы ввели неправильное число");
                }
            } catch (NumberFormatException e) {
                answer = false;
                agentId.setError("Вы ввели неправильное число");
                e.printStackTrace();
            }
        }
        if (TextUtils.isEmpty(agentPasswordAgain.getText().toString().replace(" ", ""))) {
            agentPasswordAgain.setError("пустая строка");
            answer = false;
        } else if (agentPassword.getText().toString().compareTo(
                agentPasswordAgain.getText().toString()) != 0) {
            agentPasswordAgain.setError("Пароли не совпадают");
            answer = false;
        }

        if(!answer){
            Toast.makeText(this, "Проверьте поля.",
                    Toast.LENGTH_SHORT).show();
        }

        return answer;
    }

    boolean checkUserExistence(String inputNameAgent) {

        String readLine;

        boolean answer = true;

        String[] arrayStr;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(openFileInput("UsersPasswordsAndLogins.txt")))) {

            while ((readLine = reader.readLine()) != null) {
                arrayStr = readLine.split("\t");
                if (arrayStr[0].compareTo(agentId.getText().toString()) == 0) {
                    agentId.setError("такой ID уже существует");
                    answer = false;
                }
                if (arrayStr[1].compareTo(inputNameAgent) == 0) {
                    agentLogin.setError("Такой логин уже существует");
                    answer = false;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();

        }


        return answer;

    }

    void writeData(String agentNameStr, String agentPasswordStr) {
        String id = agentId.getText().toString();
        String name = agentName.getText().toString();
        String surname = agentSurname.getText().toString();
        String patronymic = agentPatronymic.getText().toString();

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(
                "UsersPasswordsAndLogins.txt", MODE_APPEND)))) {
            String text = id + "\t" + agentNameStr + "\t" + agentPasswordStr + "\t" + name +
                    "\t" + surname + "\t" + patronymic + "\n";
            writer.write(text);
            agentLogin.setText("");
            agentPassword.setText("");
            agentId.setText("");
            agentName.setText("");
            agentSurname.setText("");
            agentPatronymic.setText("");
            agentPasswordAgain.setText("");

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