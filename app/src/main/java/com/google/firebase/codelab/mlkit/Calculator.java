package com.google.firebase.codelab.mlkit;

import java.util.Calendar;

abstract class Calculator {
    protected Calendar date;
    protected int sumOfIncome;
    protected int sumOfOutgo;
    protected int balance;

    public Calculator(){
        this.date = date.getInstance();
        this.sumOfIncome = 0;
        this.sumOfOutgo =0 ;
        this.balance = 0;
    }

    public Calculator(Calendar date,int sumOfIncome, int sumOfOutgo, int balance){
        this.date = date;
        this.sumOfIncome = sumOfIncome;
        this.sumOfOutgo = sumOfOutgo;
        this.balance = balance;
    }

    public Calendar GetDate(){
        return this.date;
    }
    public int GetSumOfIncome(){
        return this.sumOfIncome;
    }
    public int GetSumOfOutgo(){
        return this.sumOfOutgo;
    }
    public int GetBalance(){
        return this.balance;
    }

    public int Optimize(Calendar date, int sumOfIncome, int sumOfOutgo){
        this.balance = this.balance + sumOfIncome-sumOfOutgo;
        return this.balance;
    }

    abstract int Add(Calendar date, int sumOfIncome, int sumOfOutgo);

}
