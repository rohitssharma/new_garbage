package com.garbagebin.models;

/**
 * Created by sharanjeet on 28/10/15.
 */
public class UserListModel {
    String customer_id="",username="",email="";

    public UserListModel() {
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
