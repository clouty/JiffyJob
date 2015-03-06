package com.jiffyjob.nimblylabs.login;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by NimblyLabs on 29/1/2015.
 */
public class LoginFragmentController implements Observer {

    public LoginFragmentController(LoginFragmentView view, LoginFragmentModel model) {
        this.view = view;
        this.model = model;
    }


    /*Update by model when changed*/
    @Override
    public void update(Observable observable, Object data) {
        this.view.getLocationTextView().setText(this.model.getLocation());
        this.view.getGenderTextView().setText(this.model.getGender());
        this.view.getNameTextView().setText(this.model.getName());
        this.view.getUserProfileImageView().setImageBitmap(this.model.getUserProfileImage());
    }

    /*Variables and properties*/
    private LoginFragmentView view;
    private LoginFragmentModel model;
}
