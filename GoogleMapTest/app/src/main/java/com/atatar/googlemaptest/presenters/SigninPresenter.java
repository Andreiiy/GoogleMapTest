package com.atatar.googlemaptest.presenters;

import com.atatar.googlemaptest.models.User;
import com.atatar.googlemaptest.views.SigninView;

public class SigninPresenter {

    private SigninView view;
    private User currentUser;

    public SigninPresenter(SigninView view) {
        this.view = view;
    }


    public void login(String username,String password){
        if(validate(username, password)){
            if(currentUser == null)
                view.onLoginFailed();
            else if(username.equals(currentUser.getUsername())  &&  password.equals(currentUser.getPassword())){
                view.onLoginSuccess();
            }else
                view.onLoginFailed();
        }
    }



    private boolean validate(String username,String password) {
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

        return valid;
    }

    public void setCurrentUser(User user){
        this.currentUser = user;
    }
}
