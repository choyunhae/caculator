package com.google.firebase.codelab.mlkit;
import android.content.Intent;
import android.widget.Button;
import android.graphics.Bitmap;
import android.widget.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import android.view.*;
import android.content.Context;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import org.w3c.dom.Text;
import android.view.LayoutInflater;

import static com.google.firebase.codelab.mlkit.R.id.radio_cash;

public class CashBook extends BaseAdapter{
    private ArrayList<Account> accounts;
    //  private ArrayList<Calculator> months;
    //private Button removebutton;
    //private Button updatebutton;
    private Extractor extractor;
    static private int balance =0;


    public CashBook(){
        accounts = new ArrayList<Account>();

        //    months = new ArrayList<Calculator>();
        extractor = new Extractor();
    }
    public void UpdateBalance(){
        int balance = 0;
        int amount;
        int i=0;
        while(i<accounts.size()) {
            amount = this.accounts.get(i).GetAmount();
            if(this.accounts.get(i) instanceof Income){
                balance += amount;
            }
            else{
                balance -= amount;
            }
            i++;
        }

        this.balance = balance;

    }

    public int GetBalance(){
        return balance;
    }

    @Override
    public int getCount(){
        return accounts.size();
    }
    @Override
    public Object getItem(int position){
        return accounts.get(position);
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos=position;
        final Context context=parent.getContext();
        final Intent intent;
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.activity_listltem,parent,false);
            convertView.setClickable(true);
        }

        TextView is=(TextView)convertView.findViewById(R.id.text_iscash);
        TextView date=(TextView)convertView.findViewById(R.id.text_calendar);
        TextView ca=(TextView)convertView.findViewById(R.id.text_category);
        TextView am=(TextView)convertView.findViewById(R.id.text_amount);


        Account account=accounts.get(position);
        int year = account.GetDate().get(Calendar.YEAR);
        int month = account.GetDate().get(Calendar.MONTH) + 1;  //int->String 형변환
        int day =account.GetDate().get(Calendar.DATE);
        int amount=account.GetAmount();
        if(account.GetIsCash()==true) {
            is.setText("현금");}

        else if(account.GetIsCash()==false) {
            is.setText("카드");
        }
        date.setText(String.format("%d년%02d월%02d일", year,month,day));
        am.setText(String.format("%d원",amount));
        ca.setText(account.GetCategory());
        if(account instanceof Income) {
            am.setText(String.format("%d원", amount));
        }
        else{
            am.setText(String.format("%d원",-amount));
        }


        return convertView;
    }

    public CashBook(Bitmap bitmap){
        this.accounts = new ArrayList<Account>();
        //    this.months = new ArrayList<Calculator>();
        extractor=new Extractor(bitmap);
    }

    public ArrayList<Account> GetAccounts(){
        return this.accounts;
    }
    //  public ArrayList<Calculator> GetMonths(){ return this.months; }
    public Extractor GetExtractor(){return this.extractor;}

    public Account Add(Calendar date, Boolean isIncome, String category, int amount, String comment, Boolean isCash, Bitmap image) {

        Account account;
        if (isIncome) {
            account = new Income(date, category, amount, comment, isCash);
        } else {
            account = new Outcome(date, category, amount, comment, isCash, image);
        }
        int i = 0;
        int flag = 0;
        int index = 0;
        if (this.accounts.size() != 0) {
            while (i < this.accounts.size() && flag == 0) {
                if(date.compareTo(this.accounts.get(i).GetDate())<0){ flag = 1; index = i; };
                i++;
            }
        }

        this.accounts.add(index, account);

        if (isIncome) {
            this.balance += account.GetAmount();
        } else {
            this.balance -= account.GetAmount();
        }

        return account;
    }

    public ArrayList<Account> Find(Calendar date){
        ArrayList<Account> accountLinks = new ArrayList<Account>();
        int i = 0;
        while(i<this.accounts.size()){
            if(date.compareTo(this.accounts.get(i).GetDate())==0){
                accountLinks.add(this.accounts.get(i));
            }
            i++;
        }
        return accountLinks;
    }

    public Account Modify(int index,String category, int amount, String comment, Boolean isCash, Bitmap image) {
        Object obj = this.accounts.get(index);
        if(obj instanceof Income){
            ((Income) this.accounts.get(index)).Modify(category,amount,comment,isCash);
        }
        else{
            ((Outcome)this.accounts.get(index)).Modify(category,amount,comment,isCash,image);
        }

        UpdateBalance();

        return this.accounts.get(index);
    }

    public Account Remove(int index){
        return this.accounts.remove(index);
    }

    public Account GetAt(int i){
        return this.accounts.get(i);
    }
    public void addItem(Account account){
        accounts.add(account);
    }
}


