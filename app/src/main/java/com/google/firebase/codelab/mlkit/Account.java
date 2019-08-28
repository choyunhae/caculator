package com.google.firebase.codelab.mlkit;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import java.util.Calendar;
import java.text.*;
abstract public class Account {
    protected Calendar date;
    protected String category;
    protected int amount;
    protected String comment;
    protected Boolean isCash;

    public Account()
    {
        this.date = null;
        this.category = null;
        this.amount = 0;
        this.comment = null;
        this.isCash=true;
    }

    public Account(Calendar date, String category, int amount, String comment,Boolean isCash)
    {
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.comment = comment;
        this.isCash=isCash;
    }

    public Account Modify(String category, int amount, String comment,Boolean isCash){
        this.category = category;
        this.amount = amount;
        this.comment = comment;
        this.isCash=isCash;
        return this;
    }
    public String StringFromCalendar(Calendar date){
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date.getTime());
    }

    public Calendar GetDate(){ return this.date; }
    public String GetCategory(){ return this.category; }
    public int GetAmount(){
        return this.amount;
    }
    public String GetComment(){
        return this.comment;
    }
    public Boolean GetIsCash() {return this.isCash;}

}
