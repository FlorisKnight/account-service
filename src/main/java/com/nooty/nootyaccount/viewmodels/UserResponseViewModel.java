package com.nooty.nootyaccount.viewmodels;

import com.nooty.nootyaccount.models.User;

public class UserResponseViewModel extends User {
    String token;

    public UserResponseViewModel (User user, String token) {
        setDisplayname(user.getDisplayname());
        setEmail(user.getEmail());
        setUsername(user.getUsername());
        setId(user.getId());
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
