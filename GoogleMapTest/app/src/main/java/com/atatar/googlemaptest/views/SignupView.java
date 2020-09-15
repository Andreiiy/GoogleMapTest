package com.atatar.googlemaptest.views;

public interface SignupView {
    void showProgressDialog();
    void hideProgressDialog();
    void onRegistrationSuccess();
    void usernameTextError(Boolean checked);
    void passwordTextError(Boolean checked);
    void dateBirthError();
}
