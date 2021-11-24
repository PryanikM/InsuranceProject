package com.example.insuranceproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.NumberFormat;
import java.util.Locale;

public class HouseInsurance extends AppCompatActivity {

    private String idUser;

    private MaterialEditText houseNumber;
    private MaterialEditText houseStreet;
    private MaterialEditText houseSize;
    private MaterialEditText houseCeilingHeight;
    private MaterialEditText houseDiscount;
    private MaterialEditText houseFloorNumber;
    private MaterialEditText apartmentNumber;


    private MaterialEditText ownerName;
    private MaterialEditText ownerSurname;
    private MaterialEditText ownerPatronymic;
    private MaterialEditText ownerPassportDate;


    private String userId;

    private Button getPrice;

    String pariceHouse;

    private NumberFormat numberFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_insurance);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        idUser = getIntent().getExtras().get("userId").toString();

        numberFormat = NumberFormat.getCurrencyInstance(new Locale("ru", "RU"));

        houseNumber = findViewById(R.id.numberHouse);
        houseStreet = findViewById(R.id.streetHouse);
        houseSize = findViewById(R.id.houseSize);
        houseCeilingHeight = findViewById(R.id.ceilingHeight);
        houseDiscount = findViewById(R.id.discountHouse);
        houseFloorNumber = findViewById(R.id.floorHouse);
        apartmentNumber = findViewById(R.id.apartmentNumber);
        ownerName = findViewById(R.id.ownerNameHouse);
        ownerSurname = findViewById(R.id.surnameOwnerHouse);
        ownerPatronymic = findViewById(R.id.patronymicOwnerHouse);
        ownerPassportDate = findViewById(R.id.passportDateHouse);

        getPrice = findViewById(R.id.button_get_price_house);


        getPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFields()) {
                    showDialogPrice(getPrice());
                }
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
        houseStreet.setFilters(new InputFilter[]{filterEditText});
    }
    private void showDialogPrice(double price) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View registerWindow = inflater.inflate(R.layout.activity_price, null);
        dialog.setView(registerWindow);
        TextView textViewPrice = registerWindow.findViewById(R.id.viewPrice);
        pariceHouse = numberFormat.format(price);
        textViewPrice.setText(pariceHouse);

        dialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Застраховать", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addHouse();
                houseNumber.setText("");
                houseStreet.setText("");
                houseSize.setText("");
                houseCeilingHeight.setText("");
                houseDiscount.setText("");
                houseFloorNumber.setText("");
                apartmentNumber.setText("");
                ownerName.setText("");
                ownerSurname.setText("");
                ownerPatronymic.setText("");
                ownerPassportDate.setText("");

            }
        });
        dialog.show();
    }

    private boolean checkFields() {
        boolean answer = true;

        if (TextUtils.isEmpty(houseStreet.getText().toString().replace(
                " ", ""))) {
            houseStreet.setError("пустая строка");
            answer = false;
        }
        if (TextUtils.isEmpty(houseNumber.getText().toString().replace(
                " ", ""))) {
            houseNumber.setError("пустая строка");
            answer = false;
        }

        else if(Integer.parseInt(houseNumber.getText().toString()) <= 0){
            houseNumber.setError("Вы ввели неправильное число");
            answer = false;
        }

        if (TextUtils.isEmpty(houseSize.getText().toString().replace(
                " ", ""))) {
            houseSize.setError("пустая строка");
            answer = false;
        }

        else if(Double.parseDouble(
                houseSize.getText().toString().replace(",", ".")) <= 0){
            houseSize.setError("Вы ввели неправильное число");
            answer = false;
        }

        if (TextUtils.isEmpty(houseCeilingHeight.getText().toString().replace(
                " ", ""))) {
            houseCeilingHeight.setError("пустая строка");
            answer = false;
        }

        else if(Double.parseDouble(
                houseCeilingHeight.getText().toString().replace(",", ".")) <= 0){
            houseCeilingHeight.setError("Вы ввели неправильное число");
            answer = false;
        }


        if (TextUtils.isEmpty(houseDiscount.getText().toString().replace(
                " ", ""))) {
            houseDiscount.setError("пустая строка");
            answer = false;
        }

        else if(Double.parseDouble(houseDiscount.getText().toString()) < 0 ||
                Double.parseDouble(houseDiscount.getText().toString()) > 100){
            houseDiscount.setError("Вы ввели неправильное число");
            answer = false;
        }

        if (TextUtils.isEmpty(houseFloorNumber.getText().toString().replace(
                " ", ""))) {
            houseFloorNumber.setError("пустая строка");
            answer = false;
        }
        else if(Integer.parseInt(houseFloorNumber.getText().toString()) <= 0){
            houseFloorNumber.setError("Вы ввели неправильное число");
            answer = false;
        }
        if (TextUtils.isEmpty(apartmentNumber.getText().toString().replace(
                " ", ""))) {
            apartmentNumber.setError("пустая строка");
            answer = false;
        }

        else if(Integer.parseInt(apartmentNumber.getText().toString()) <= 0){
            apartmentNumber.setError("Вы ввели неправильное число");
            answer = false;
        }

        if (TextUtils.isEmpty(ownerName.getText().toString().replace(
                " ", ""))) {
            ownerName.setError("пустая строка");
            answer = false;
        }
        if (TextUtils.isEmpty(ownerSurname.getText().toString().replace(
                " ", ""))) {
            ownerSurname.setError("пустая строка");
            answer = false;
        }
        if (TextUtils.isEmpty(ownerPatronymic.getText().toString().replace(
                " ", ""))) {
            ownerPatronymic.setError("пустая строка");
            answer = false;
        }

        if (TextUtils.isEmpty(ownerPassportDate.getText().toString().replace(
                " ", ""))) {
            ownerPassportDate.setError("пустая строка");
            answer = false;
        }

        else if (ownerPassportDate.getText().toString().length() != 10){
            ownerPassportDate.setError("Неверное количество символов");
            answer = false;
        }

        return answer;
    }


    private boolean addHouse(){
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(
                "InsuredHouses.txt", MODE_APPEND)))) {
            String text = idUser + "\t" + houseStreet.getText().toString() + "\t" +
                    houseNumber.getText().toString() + "\t" +
                    houseFloorNumber.getText().toString() + "\t" +
                    apartmentNumber.getText().toString() + "\t" +
                    houseSize.getText().toString() + "\t" +
                    houseCeilingHeight.getText().toString() + "\t" +
                    houseDiscount.getText().toString() + "\t" +
                    ownerName.getText().toString() + "\t" +
                    ownerSurname.getText().toString() + "\t" +
                    ownerPatronymic.getText().toString() + "\t" +
                    ownerPassportDate.getText().toString() + "\t" +
                    pariceHouse + "\n";
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
            return false;

        }
        return true;
    }
    private double getPrice(){

        double apartmentSize = Double.parseDouble(
                houseSize.getText().toString().replace(",", ".")) * 100.;
        double ceilingHeigt = Double.parseDouble(
                houseCeilingHeight.getText().toString().replace(",", ".")) * 100.;
        double discount = Double.parseDouble(houseDiscount.getText().toString());

        return  (apartmentSize * ceilingHeigt) * (1. - (double) discount / 100.);

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