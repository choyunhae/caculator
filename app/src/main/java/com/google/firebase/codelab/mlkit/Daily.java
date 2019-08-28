package com.google.firebase.codelab.mlkit;

import java.util.Calendar;

public class Daily extends Calculattor {
    private int day;

    public Daily(){
        super();
        this.day = 0;
    }

    public Daily(Calendar date, int sumOfIncome,int sumOfOutgo, int balance){
        super(date,sumOfIncome,sumOfOutgo,balance);
        this.day = date.DATE;
    }

    public int GetDay(){
        return this.day;
    }

    public int Add(Calendar date, int sumOfIncome, int sumOfOutgo){
        int index = -1;
        return index;
    }
}
