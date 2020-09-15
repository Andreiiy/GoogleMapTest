package com.atatar.googlemaptest.views;

public interface SigninView {
    void showProgressDialog();
    void hideProgressDialog();
    void onLoginFailed();
    void onLoginSuccess();
    void usernameTextError(Boolean checked);
    void passwordTextError(Boolean checked);
}
