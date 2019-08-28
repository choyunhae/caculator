package com.google.firebase.codelab.mlkit;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.annotation.SuppressLint;
import com.google.firebase.analytics.FirebaseAnalytics;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import java.text.*;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.view.ViewGroup;
import android.annotation.*;
import java.io.Serializable;
import java.lang.Object.*;
import android.graphics.*;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.lang.annotation.Target;
import java.util.Calendar;
import java.util.List;
import static com.google.firebase.codelab.mlkit.R.id.radio_cash;
import static com.google.firebase.codelab.mlkit.R.id.textView2;
/*버튼 추가*/
/*하단 바*/
/*달력*/
/*implements RadioGroup.OnCheckedChangeListener*/



public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAnalytics mFirebaseAnalytics;
    //ArrayAdapter<String> adapter;
    /*TextView viewDatePick;*/
    private TextView t;
    private DatabaseReference mDatabase;
    private Button btnSave;  //저장버튼
    private CalendarView calendarView;
    private Button selectIncomeOutgo;
    private Button selectupre;
    private Calendar selectedDate;
    private CashBook cashBook;
    private Boolean isIncome;
    private ListView textView2;
    private ImageView imageView;
    private Bitmap image;
    private View view;
    private ImageView imageView_receipt;
    private static final int ADD_NEW_ITEM = 1000;
    //private List<Calendar> selectedDate;
    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //방금추가
        t=findViewById(R.id.text_balance);
        cashBook=new CashBook();
        textView2=(ListView)findViewById(R.id.textView2);
        textView2.setAdapter(cashBook);
        selectedDate=Calendar.getInstance();//오늘날짜로 설정
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        calendarView=findViewById(R.id.calendar_view);
        selectIncomeOutgo = (Button)findViewById(R.id.selectIncomeOutgo);
        selectIncomeOutgo.setOnClickListener(this);
        //리스트뷰 아이템 클릭시 발생하는 엑티비티
        textView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),ListViewClicked.class);
                intent.putExtra("selectedDate",(Serializable)cashBook.GetAccounts().get(position).GetDate()); //Serializable
                startActivity(intent);
            }
        });


    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Account account;

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == ADD_NEW_ITEM) {
                    isIncome=data.getBooleanExtra("isIncome",false);
                    Calendar date = (Calendar) data.getSerializableExtra("selectedDate");
                    String category = data.getStringExtra("category");
                    int amount = data.getIntExtra("amount", -1);
                    String comment = data.getStringExtra("comment");
                    Boolean isCash = data.getBooleanExtra("isCash", false);

                    if(isIncome) {
                        account = cashBook.Add(date,isIncome,category,amount,comment,isCash,null);
                        /*String txt_cate;
                        txt_cate=account.GetCategory().toString();

                        Bundle bundle=new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.INDEX,txt_cate);
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle);
                        Toast.makeText(getApplicationContext(),"save!",Toast.LENGTH_LONG).show();*/
                        cashBook.notifyDataSetChanged();
                        //String today=account.GetCategory();
                        //mDatabase= FirebaseDatabase.getInstance().getReference();
                        //mDatabase.child("category").setValue(account);


                    }
                       else if(!isIncome){
                           if(data.hasExtra("image")) {
                               byte[] bytes = data.getByteArrayExtra("image");
                               image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                               account = cashBook.Add(date, isIncome, category, amount, comment, isCash, image);
                               cashBook.notifyDataSetChanged();
                           }
                           else{
                               account=cashBook.Add(date,isIncome,category,amount,comment,isCash,null);
                               cashBook.notifyDataSetChanged();
                           }
                           t.setText(String.format("총 잔액 : %d원",cashBook.GetBalance()));
                    }

                    }

        } else {
            Toast.makeText(getApplicationContext(), "Result Canceled", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.selectIncomeOutgo) {

            if(calendarView.getSelectedDates().size() != 0){
                selectedDate = (Calendar) calendarView.getSelectedDates().get(0);
            }

            Intent intent = new Intent(getApplicationContext(), ButtonActivity.class);
            intent.putExtra("selectedDate", (Serializable) selectedDate);  //Serializable. 날짜 보내기

            startActivityForResult(intent, ADD_NEW_ITEM);   //변경

        }

    }
/*
    public void onSendEvent(View view){

        Bundle bundle = new Bundle();

        //bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);

       // bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, contentsName);

      //  bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentsCategory);

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);



        //Toast.makeText(getApplicationContext(), "Sent event", Toast.LENGTH_LONG).show();

    }

*/

}