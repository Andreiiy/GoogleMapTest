package com.atatar.googlemaptest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.atatar.googlemaptest.models.User;
import com.atatar.googlemaptest.presenters.SignupPresenter;
import com.atatar.googlemaptest.views.SignupView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SignupActivity extends AppCompatActivity implements SignupView {

    private ImageButton imageButton;
    private TextView tv_date;
    private EditText et_username;
    private EditText et_password;
    private Button registrButton;
    private SharedPreferences pref;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final SignupPresenter presenter = new SignupPresenter(this);

        initViews();

        registrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String dateBirth = tv_date.getText().toString().trim();

             presenter.registration(username,password,dateBirth);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener pikerListener = new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                      Date date = new GregorianCalendar(year,monthOfYear,dayOfMonth).getTime();
                        SimpleDateFormat fromDateFormat =  new SimpleDateFormat("dd/MM/yyyy");
                        tv_date.setText(fromDateFormat.format(date));

                    }
                };
                showDatePickerDialog(pikerListener);

            }
        });
    }

    private void initViews(){
        tv_date = (TextView) findViewById(R.id.tv_date);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.input_password);

        registrButton = (Button) findViewById(R.id._registrButton);
        imageButton = (ImageButton) findViewById(R.id.imageButton);

        progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getText(R.string.dialog_auth));

    }

    private void showDatePickerDialog(DatePickerDialog.OnDateSetListener pikerListener){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog piker =    new DatePickerDialog(SignupActivity.this, pikerListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        piker.show();
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void onRegistrationSuccess() {
        pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor  editor = pref.edit();
        editor.putString("key_username", et_username.getText().toString()); // Storing string
        editor.putString("key_pass", et_password.getText().toString());
        editor.putString("key_date", tv_date.getText().toString());
        editor.apply();

        hideProgressDialog();
        Toast.makeText(SignupActivity.this, getResources().getText(R.string.tost_succes), Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getBaseContext(),MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void usernameTextError(Boolean checked) {
        if(checked)
            et_username.setError(getResources().getText(R.string.input_username));
        else
            et_username.setError(null);
    }

    @Override
    public void passwordTextError(Boolean checked) {
        if(checked)
            et_password.setError(getResources().getText(R.string.input_pass));
        else
            et_password.setError(null);
    }

    @Override
    public void dateBirthError() {
        Toast.makeText(SignupActivity.this, getResources().getText(R.string.tost_dateerror), Toast.LENGTH_LONG).show();
    }


}
