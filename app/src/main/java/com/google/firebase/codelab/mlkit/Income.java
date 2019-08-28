package com.google.firebase.codelab.mlkit;
import java.util.Calendar;
import java.io.Serializable;
public class Income extends Account implements Serializable{
    private Boolean isCash;

    public Income() {
        super();

    }
//수정
    public Income(Calendar date, String category, int amount, String comment, Boolean isCash)
    {
        super(date, category, amount, comment,isCash);

    }

    public Account Modify(String category, int amount , String comment, Boolean isCash){
        super.Modify(category, amount, comment,isCash);
        return this;
    }

}
