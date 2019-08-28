package com.google.firebase.codelab.mlkit;

import android.graphics.Bitmap;

import java.util.Calendar;

public class Outcome extends Account {
    private Bitmap image;

    public Outcome(){
        super();
        image = null;
    }

    public Outcome(Calendar date, String category, int amount, String comment, boolean isCash,Bitmap image){
        super(date,category,amount,comment,isCash);
        this.image = image;
    }

    public Account Modify(String category, int amount , String comment,boolean isCash, Bitmap image){
        super.Modify(category, amount, comment,isCash);
        this.image = image;

        return this;
    }

    public Bitmap GetImage(){
        return this.image;
    }
}
