package com.example.insuranceproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.icu.util.LocaleData;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CarInsurance extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private MaterialEditText purchaseDate;
    private MaterialEditText carName;
    private MaterialEditText ownerName;
    private MaterialEditText ownerSurname;
    private MaterialEditText ownerPatronymic;
    private MaterialEditText ownerPassportDate;
    private MaterialEditText powerCar;
    private MaterialEditText discountCar;
    private MaterialEditText numberCar;
    private Button getPrice;

    private NumberFormat numberFormat;

    private String userId;
    private String priceCar;

//    private MaterialEditText

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_insurance);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        numberFormat = NumberFormat.getCurrencyInstance(new Locale("ru", "RU"));

        purchaseDate = findViewById(R.id.purchaseDateCar);

        ownerName = findViewById(R.id.ownerNameCar);
        ownerSurname = findViewById(R.id.surnameOwnerCar);
        ownerPatronymic = findViewById(R.id.patronymicOwnerCar);

        ownerPassportDate = findViewById(R.id.passportDateCar);
        powerCar = findViewById(R.id.powerCar);
        discountCar = findViewById(R.id.discountCar);
        numberCar = findViewById(R.id.carNumber);

        carName = findViewById(R.id.carName);

//        spinnerConditions = findViewById(R.id.spinnerConditionsCar);

        getPrice = findViewById(R.id.button_get_price_car);

        userId = getIntent().getExtras().get("userId").toString();



        getPrice.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(checkFields()) {
                    showDialogPrice(getPrice());
                }
            }
        });

        purchaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialogDate();
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

        ownerName.setFilters(new InputFilter[]{filterEditText});
        ownerPatronymic.setFilters(new InputFilter[]{filterEditText});
        ownerSurname.setFilters(new InputFilter[]{filterEditText});

    }


    private void showDateDialogDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "." + month + "." + year;
        purchaseDate.setText(date);

    }

    private void showDialogPrice(double price) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View registerWindow = inflater.inflate(R.layout.activity_price, null);
        dialog.setView(registerWindow);
        TextView textViewPrice = registerWindow.findViewById(R.id.viewPrice);
        priceCar = numberFormat.format(price);
        textViewPrice.setText(priceCar);

        dialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Застраховать", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addCar();
                purchaseDate.setText("");
                carName.setText("");
                ownerName.setText("");
                ownerSurname.setText("");
                ownerPatronymic.setText("");
                ownerPassportDate.setText("");
                powerCar.setText("");
                discountCar.setText("");
                numberCar.setText("");
            }
        });
        dialog.show();
    }

    private boolean checkFields() {
        boolean answer = true;
        String carNameStr = Objects.requireNonNull(
                carName.getText()).toString();

        String date = Objects.requireNonNull(purchaseDate.getText()).toString();

        if (TextUtils.isEmpty(carNameStr.replace(" ", ""))) {
            carName.setError("пустая строка");
            answer = false;
        }
        if (TextUtils.isEmpty(date.replace(" ", ""))) {
            purchaseDate.setError("пустая строка");
            answer = false;
        }

        if (TextUtils.isEmpty(ownerName.getText().toString().replace(" ", ""))) {
            ownerName.setError("пустая строка");
            answer = false;
        }
        if (TextUtils.isEmpty(ownerSurname.getText().toString().replace(" ", ""))) {
            ownerSurname.setError("пустая строка");
            answer = false;
        }
        if (TextUtils.isEmpty(ownerPatronymic.getText().toString().replace(" ", ""))) {
            ownerPatronymic.setError("пустая строка");
            answer = false;
        }
        if (TextUtils.isEmpty(powerCar.getText().toString().replace(" ", ""))) {
            powerCar.setError("пустая строка");
            answer = false;
        }
        else {
            try {
                if (Integer.parseInt(powerCar.getText().toString()) <= 0) {
                    powerCar.setError("Вы ввели неправильное число");
                    answer = false;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                powerCar.setError("Вы ввели неправильное число");
                answer = false;
            }

        }
        if (TextUtils.isEmpty(ownerPassportDate.getText().toString().replace(" ", ""))) {
            ownerPassportDate.setError("пустая строка");
            answer = false;
        }
        else if (ownerPassportDate.getText().toString().length() != 10){
            ownerPassportDate.setError("Неверное количество символов");
            answer = false;
        }

        if (TextUtils.isEmpty(discountCar.getText().toString().replace(" ", ""))) {
            discountCar.setError("пустая строка");
            answer = false;
        }
        else {
            try {
                if(Double.parseDouble(discountCar.getText().toString()) < 0. ||
                        Double.parseDouble(discountCar.getText().toString()) > 100.){
                    discountCar.setError("Вы ввели неправильное число");
                    answer = false;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                discountCar.setError("Вы ввели неправильное число");
                answer = false;
            }

        }

        if (TextUtils.isEmpty(numberCar.getText().toString().replace(" ", ""))) {
            numberCar.setError("пустая строка");
            answer = false;
        }
        else if (numberCar.getText().toString().length() != 6){
            numberCar.setError("Неверное количество символов");
            answer = false;
        }

        if(!answer){
            Toast.makeText(this, "Проверьте поля.",
                    Toast.LENGTH_SHORT).show();
        }

        return answer;
    }

    private boolean addCar(){
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(
                "InsuredСars.txt", MODE_APPEND)))) {
            String text = userId + "\t" + carName.getText().toString() + "\t" +
                    purchaseDate.getText().toString() + "\t" + ownerName.getText().toString() +
                    "\t" + ownerSurname.getText().toString() + "\t" +
                    ownerPatronymic.getText().toString() + "\t" +
                    ownerPassportDate.getText().toString() + "\t" + numberCar.getText().toString() +
                    "\t" + powerCar.getText().toString() + "\t" +
                    discountCar.getText().toString() + "\t" +
                    priceCar + "\n";
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
            return false;

        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private double getPrice(){

        int power = Integer.parseInt(powerCar.getText().toString());

        int yearNow = Calendar.getInstance().get(Calendar.YEAR);
        int monthNow = Calendar.getInstance().get(Calendar.MONTH);
        int dayNow = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);


        System.out.println(yearNow + "   " + monthNow + "   " + dayNow);

        String[] setDate = purchaseDate.getText().toString().split("\\.");

        LocalDate start = LocalDate.of(Integer.parseInt(setDate[2]), Integer.parseInt(setDate[1]),
                Integer.parseInt(setDate[0]));
        LocalDate end = LocalDate.of(yearNow, monthNow, dayNow);



        int experience = Period.between(start, end).getYears();

        int experienceFinal = experience > 0 ? experience : 1;

        double discount = Double.parseDouble(discountCar.getText().toString());

        return (power * 10 + (double) 10000 / experienceFinal) * (1. - (double) discount / 100.);

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