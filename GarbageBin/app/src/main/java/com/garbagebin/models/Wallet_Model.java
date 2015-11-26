package com.garbagebin.models;

/**
 * Created by sharanjeet on 27/10/15.
 */
public class Wallet_Model {
    String date_time="",wallet_image="",txn_id="",other_val="";

    public Wallet_Model(String date_time, String wallet_image, String txn_id, String other_val) {
        this.date_time = date_time;
        this.wallet_image = wallet_image;
        this.txn_id = txn_id;
        this.other_val = other_val;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getWallet_image() {
        return wallet_image;
    }

    public void setWallet_image(String wallet_image) {
        this.wallet_image = wallet_image;
    }

    public String getTxn_id() {
        return txn_id;
    }

    public void setTxn_id(String txn_id) {
        this.txn_id = txn_id;
    }

    public String getOther_val() {
        return other_val;
    }

    public void setOther_val(String other_val) {
        this.other_val = other_val;
    }
}
