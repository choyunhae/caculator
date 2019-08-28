package com.google.firebase.codelab.mlkit;

import android.app.Activity;
import android.content.Intent;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Calendar;

public class ButtonActivity extends Activity implements View.OnClickListener {
    private Button incomeButton;
    private Button outcomeButton;
    private Calendar selectedDate;
    private TextView textView;
    private Boolean isIncome;
    private Boolean isOutcome;
    String year;
    String month;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀 바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_button);

        incomeButton = (Button)findViewById(R.id.incomeButton);


        outcomeButton = (Button)findViewById(R.id.outcomeButton);

        textView = (TextView)findViewById(R.id.textView);

        incomeButton.setOnClickListener(this);
        outcomeButton.setOnClickListener(this);
        Intent intent = getIntent();
        selectedDate = (Calendar) intent.getSerializableExtra("selectedDate");


        year = Integer.toString(selectedDate.get(Calendar.YEAR));
        month =Integer.toString(selectedDate.get(Calendar.MONTH)+1);  //int->String 형변환
        date = Integer.toString(selectedDate.get(Calendar.DATE));

        textView.setText(year+month+date);

    }


    @Override
    public void onClick(View v) {
        Intent intent;
        if(v.getId() == R.id.incomeButton) {
            isIncome = true;
            intent= new Intent(getApplicationContext(), IncomeActivity.class);
            intent.putExtra("selectedDate",(Serializable)selectedDate);  //날짜 보내기
            intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intent);    //변경
            finish(); //인컴띄우기

        }
        else if(v.getId() == R.id.outcomeButton) {//아웃고띄우기
            isIncome = false;

            intent = new Intent(getApplicationContext(), OutcomeActivity.class);
            intent.putExtra("selectedDate", (Serializable)selectedDate);  //날짜 보내기
            //intent.putExtra("isIncome", isIncome); //수입여부 true
            intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intent);
        }

        finish();

    }
}
