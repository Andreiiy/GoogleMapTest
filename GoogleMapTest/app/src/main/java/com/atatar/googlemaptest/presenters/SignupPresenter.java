package com.atatar.googlemaptest.presenters;

import com.atatar.googlemaptest.views.SignupView;

public class SignupPresenter {

    private SignupView view;

    public SignupPresenter(SignupView view) {
        this.view = view;
    }

    public void registration(String username,String password,String dateBirth){
        if(validate(username, password, dateBirth)){
            view.showProgressDialog();
            view.onRegistrationSuccess();
        }
    }


    private boolean validate(String username,String password,String dateBirth) {
        boolean valid = true;

        if (username.isEmpty()) {
            view.usernameTextError(true);
            valid = false;
        } else {
            view.usernameTextError(false);
        }

        if (password.isEmpty() || password.length() < 6 ) {
            view.passwordTextError(true);
            valid = false;
        } else {
            view.passwordTextError(false);
        }
        if (dateBirth.isEmpty()) {
            view.dateBirthError();
            valid = false;

        }
        return valid;
    }
}
