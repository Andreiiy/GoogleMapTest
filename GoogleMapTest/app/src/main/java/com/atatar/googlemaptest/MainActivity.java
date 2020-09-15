package com.atatar.googlemaptest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.atatar.googlemaptest.models.User;
import com.atatar.googlemaptest.presenters.SigninPresenter;
import com.atatar.googlemaptest.views.SigninView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity  extends AppCompatActivity implements SigninView {

    private EditText _usernameText;
    private EditText _passwordText;
    private Button _loginButton;
    private TextView _signupLink;
    private User currentUser;
    public SharedPreferences pref;
    public ProgressDialog progressDialog;
    CoordinatorLayout cordLayoutSignin;
    private SigninPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new SigninPresenter(this);

         initViews();
        getCurrentUser();


        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = _usernameText.getText().toString().trim();
                String password = _passwordText.getText().toString().trim();
                presenter.login(username,password);

            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews(){

        cordLayoutSignin = (CoordinatorLayout)findViewById(R.id.cordLayoutSignin);

        _usernameText = (EditText)findViewById(R.id.input_email);
        _passwordText = (EditText)findViewById(R.id.input_password);
        _loginButton = (Button) findViewById(R.id._loginButton);
        _signupLink = (TextView)findViewById(R.id.link_signup);


        progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getText(R.string.dialog_auth));
    }

    private void getCurrentUser(){

        pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String username = pref.getString("key_username", null);
        String password = pref.getString("key_pass", null);
        String dateBirth = pref.getString("key_date", null);

        if(username !=null) {
            _usernameText.setText(username);
            currentUser = new User(username, password, dateBirth);
            presenter.setCurrentUser(currentUser);
        }
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
    public void onLoginFailed() {
        _usernameText.setText("");
        _passwordText.setText("");
        Snackbar snackbar = Snackbar
                .make(cordLayoutSignin, getResources().getText(R.string.snack_authfailed), Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundResource(R.color.colorBlue);
        snackbar.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    @Override
    public void onLoginSuccess() {
        Intent intent = new Intent(getBaseContext(),MapActivity.class);
        intent.putExtra("user",currentUser);
        startActivity(intent);
    }

    @Override
    public void usernameTextError(Boolean checked) {
        if(checked)
            _usernameText.setError(getResources().getText(R.string.input_email));
        else
            _usernameText.setError(null);
    }

    @Override
    public void passwordTextError(Boolean checked) {
        if(checked)
            _passwordText.setError(getResources().getText(R.string.input_pass));
        else
            _passwordText.setError(null);
    }
}