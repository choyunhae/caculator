package com.google.firebase.codelab.mlkit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Calendar;

public class IncomeActivity extends Activity implements View.OnClickListener {
    private RadioGroup radioGroupIsCash;
    private RadioButton radioCard;
    private RadioButton radioCash;
    private RadioButton selectedIsCash;
    private Boolean isCash;

    private EditText editAmount;
    private EditText editComment;

    private TextView textIncome;
    private TextView textDate;

    private RadioGroup radioGroupCategory;
    private RadioButton radioPocketMoney;
    private RadioButton radioSalary;
    private RadioButton radioCarry;
    private RadioButton radioEtc;
    private RadioButton selectedCategory;

    private Calendar selectedDate;
    private Boolean isIncome;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_income);

        radioGroupIsCash = (RadioGroup)findViewById(R.id.radioGroup_isCash);
        //radioCard = (RadioButton)findViewById(R.id.radio_card);
        //radioCash = (RadioButton)findViewById(R.id.radio_cash);
        editAmount = (EditText)findViewById(R.id.edit_amount);
        editComment = (EditText)findViewById(R.id.edit_comment);
        textIncome = (TextView)findViewById(R.id.text_income);
        textDate = (TextView)findViewById(R.id.text_date);
        radioGroupCategory = (RadioGroup)findViewById(R.id.radioGroup_category);
        //radioPocketMoney = (RadioButton)findViewById(R.id.radio_pocketMoney);
        //radioSalary = (RadioButton)findViewById(R.id.radio_salary);
        //radioCarry = (RadioButton)findViewById(R.id.radio_carry);
        //radioEtc = (RadioButton)findViewById(R.id.radio_etc);
        buttonSave = (Button)findViewById(R.id.button_save);

        selectedDate = Calendar.getInstance();
        isIncome = true;

        Intent intent = getIntent();
        selectedDate = (Calendar)intent.getSerializableExtra("selectedDate");

        //intent.getBooleanExtra("isIncome",false);

        String year = Integer.toString(selectedDate.get(Calendar.YEAR));
        String month =Integer.toString(selectedDate.get(Calendar.MONTH)+1);  //int->String 형변환
        String date = Integer.toString(selectedDate.get(Calendar.DATE));

        textDate.setText(year+"년 "+month+"월 "+date+"일");
        textIncome.setText("수입 내역");



        buttonSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_save){

            //입력값 받아오기
            selectedIsCash = (RadioButton)findViewById(radioGroupIsCash.getCheckedRadioButtonId());
            if(selectedIsCash.getText().toString().equals("현금")){
                isCash = true;
            }
            else{
                isCash = false;
            }

            selectedCategory = (RadioButton)findViewById(radioGroupCategory.getCheckedRadioButtonId());

            if(TextUtils.isEmpty(editComment.getText().toString())){
                editComment.setText("");
            }

            //금액란이 공란인 경우 에러메시지
            if(TextUtils.isEmpty(editAmount.getText().toString())){
                editAmount.setError("This item cannot be empty");
            }
            else{
                //Income income = new Income(selectedDate,"ㅇ",2,"",true);

                Intent intent = new Intent();
                //intent.putExtra("income", (Serializable) income);       //Serializable
                intent.putExtra("isIncome", isIncome);   //Boolean
                intent.putExtra("selectedDate",(Serializable) selectedDate);   //Serializable
                intent.putExtra("category",selectedCategory.getText().toString());  //String
                intent.putExtra("amount",Integer.parseInt(editAmount.getText().toString())); //int
                intent.putExtra("comment",editComment.getText().toString());  //String
                intent.putExtra("isCash",isCash); //Boolean


                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        }
    }
}
