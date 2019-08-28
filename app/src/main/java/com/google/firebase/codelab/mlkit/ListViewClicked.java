package com.google.firebase.codelab.mlkit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class ListViewClicked extends Activity implements View.OnClickListener {
    private Button modifyButton;
    private Button deleteButton;
    private Calendar selectedDate;
    private TextView textView;
    private Boolean isIncome;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀 바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_button);

        modifyButton = (Button)findViewById(R.id.modifyButton);
        deleteButton = (Button)findViewById(R.id.deleteButton);
        textView = (TextView)findViewById(R.id.textView);

        modifyButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        Intent intent = getIntent();
        selectedDate = (Calendar) intent.getSerializableExtra("selectedDate");   //날짜 받아오기

        textView.setText(String.format("%d년 %02d월 %02d일",selectedDate.get(Calendar.YEAR),selectedDate.get(Calendar.MONTH)+1,selectedDate.get(Calendar.DATE)));
    }
    @Override
    public void onClick(View v) {
        Intent intent;

        if (v.getId() == R.id.modifyButton) {//수정띄우기


        } else if (v.getId() == R.id.deleteButton){ //삭제 팝업 띄우기

        }

    }
}
