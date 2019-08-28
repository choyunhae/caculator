package com.google.firebase.codelab.mlkit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentTextRecognizer;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class OutcomeActivity extends Activity implements View.OnClickListener {
    private ScrollView scrollView;
    private RadioGroup radioGroupIsCash;
    private RadioButton radioCard;
    private RadioButton radioCash;
    private RadioButton selectedIsCash;
    private Boolean isCash;

    private EditText editAmount;
    private Button buttonReceipt;
    private EditText editComment;

    private TextView textOutcome;
    private TextView textDate;

    private RadioGroup radioGroupCategory;
    private RadioButton radioFood;
    private RadioButton radioShop;
    private RadioButton radioTax;
    private RadioButton radioResidence;
    private RadioButton radioCommunication;
    private RadioButton radioTransport;
    private RadioButton selectedCategory;

    private Calendar selectedDate;
    private Boolean isIncome;
    private Button buttonSave;

    private ImageView imageViewReceipt;
    private TextView textRecognized;

    private Bitmap image;
    private static File tempFile;

    private List<FirebaseVisionDocumentText.Block> blocks;
    private List<FirebaseVisionDocumentText.Paragraph> paragraphs;
    //  private List<FirebaseVisionDocumentText.Symbol> symbols;
    private StringBuilder wordStr;
    // private List<Rect> wordRects;
    private FirebaseVisionDocumentText text;

    private static final int PICK_FROM_ALBUM = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_outcome);

        scrollView = (ScrollView)findViewById(R.id.scrollView);
        radioGroupIsCash = (RadioGroup)findViewById(R.id.radioGroup_isCash);
        //radioCard = (RadioButton)findViewById(R.id.radio_card);
        //radioCash = (RadioButton)findViewById(R.id.radio_cash);
        editAmount = (EditText)findViewById(R.id.edit_amount);
        editComment = (EditText)findViewById(R.id.edit_comment);
        textOutcome = (TextView)findViewById(R.id.text_outcome);
        textDate = (TextView)findViewById(R.id.text_date);
        radioGroupCategory = (RadioGroup)findViewById(R.id.radioGroup_category);
        //radioFood = (RadioButton)findViewById(R.id.radio_food);
        //radioShop = (RadioButton)findViewById(R.id.radio_shop);
        //radioTax = (RadioButton)findViewById(R.id.radio_tax);
        //radioResidence = (RadioButton)findViewById(R.id.radio_residence);
        //radioCommunication = (RadioButton)findViewById(R.id.radio_communication);
        //radioTransport = (RadioButton)findViewById(R.id.radio_transport);
        buttonSave = (Button)findViewById(R.id.button_save);
        textRecognized = (TextView)findViewById(R.id.text_recognizedTexts);
        buttonReceipt = (Button)findViewById(R.id.button_receipt);
        imageViewReceipt = findViewById(R.id.imageView_receipt);
        image = null;
        tedPermission();
        selectedDate = Calendar.getInstance();
        isIncome = false;

        Intent intent = getIntent();
        selectedDate = (Calendar)intent.getSerializableExtra("selectedDate");
        ////intent.getBooleanExtra("isIncome",false);

        String year = Integer.toString(selectedDate.get(Calendar.YEAR));
        String month =Integer.toString(selectedDate.get(Calendar.MONTH)+1);  //int->String 형변환
        String date = Integer.toString(selectedDate.get(Calendar.DATE));

        textDate.setText(year+"년 "+month+"월 "+date+"일");
        textOutcome.setText("지출 내역");

        editAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {    //스크롤 보정
                if( hasFocus == true ){
                    scrollView.postDelayed( new Runnable(){
                        @Override
                        public void run() {
                            scrollView.smoothScrollBy(0, 800);}}, 100);}}});


        buttonReceipt.setOnClickListener(this);
        buttonSave.setOnClickListener(this);

    }

    private void tedPermission() {  //권한요청
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //권한 요청 성공
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                //권한 요청 실패
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission2))
                .setDeniedCloseButtonText(getResources().getString(R.string.permission1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                .check();
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.button_receipt){  //영수증추가
            // 앨범에서 고르는 방법
            Intent intent = new Intent(Intent.ACTION_PICK);
            //intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,PICK_FROM_ALBUM);


            //Asset에서 이미지 받아오는 방법
            /*
            Bitmap image = getBitmapFromAsset(this,"1_copy.jpg");   //(임의의 특정한)이미지 불러오기
            imageViewReceipt.setImageBitmap(image);  //이미지뷰에 이미지 표시
            runCloudTextRecognition(image);
            */

        }

        if(v.getId() == R.id.button_save) {  //저장

            //입력값 받아오기
            selectedIsCash = (RadioButton) findViewById(radioGroupIsCash.getCheckedRadioButtonId());
            if (selectedIsCash.getText().toString().equals("현금")) {
                isCash = true;
            } else {
                isCash = false;
            }

            selectedCategory = (RadioButton) findViewById(radioGroupCategory.getCheckedRadioButtonId());

            if (TextUtils.isEmpty(editComment.getText().toString())) {
                editComment.setText("");
            }

            //금액란이 공란인 경우 에러메시지
            if (TextUtils.isEmpty(editAmount.getText().toString())) {
                editAmount.setError("This item cannot be empty");
            }

            else {

                Intent intent = new Intent();
                //intent.putExtra("income", (Serializable) income);       //Serializable
                intent.putExtra("isIncome", isIncome);   //Boolean
                intent.putExtra("selectedDate", (Serializable) selectedDate);   //Serializable
                intent.putExtra("category", selectedCategory.getText().toString());  //String
                intent.putExtra("amount", Integer.parseInt(editAmount.getText().toString())); //int
                intent.putExtra("comment", editComment.getText().toString());  //String
                intent.putExtra("isCash", isCash); //Boolean
                if(image!=null) {
                    // intent.putExtra("image", image); //Bitmap  비트맵 용량문제로 putExtra 불가

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 1, stream);  //이미지압축
                    byte[] bytes = stream.toByteArray();
                    intent.putExtra("image",bytes);  // Bytes

                }
                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data!=null){
            if(requestCode == PICK_FROM_ALBUM) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    image = BitmapFactory.decodeStream(in);
                    assert in != null;
                    in.close();
                    imageViewReceipt.setImageBitmap(image);  //이미지 출력

                    runCloudTextRecognition(image);  //텍스트 인식 실행

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        else { //예외처리 토스트메시지 출력
            Toast.makeText(getApplicationContext(), "Result Canceled", Toast.LENGTH_LONG).show();
        }
    }



    private void runCloudTextRecognition(Bitmap bitmap) {
        FirebaseVisionCloudDetectorOptions options =
                new FirebaseVisionCloudDetectorOptions.Builder()
                        .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                        .setMaxResults(25)
                        .build();
        // FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(((GlideBitmapDrawable)imageViewReceipt.getDrawable()).getBitmap());
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionDocumentTextRecognizer detector = FirebaseVision.getInstance().getCloudDocumentTextRecognizer();
        ;
        detector.processImage(image)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseVisionDocumentText>() {
                            @Override
                            public void onSuccess(FirebaseVisionDocumentText texts) {
                                processCloudTextRecognitionResult(texts);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });
    }

    private void processCloudTextRecognitionResult(FirebaseVisionDocumentText text) {
        if (text == null) {
            showToast("No text found");
            return;
        }
        showToast("가격을 추출합니다.");                //추가

        this.text = text;
        wordStr = new StringBuilder();
        this.blocks = text.getBlocks();

        int i;
        for (i = 0; i < blocks.size(); i++) {
            paragraphs = blocks.get(i).getParagraphs();

            if(blocks.get(i).getText().contains("€")){  //유로 기호 포함된 문자 찾아내기
                wordStr.append(blocks.get(i).getText());
            }
        }

        if(wordStr!=null) {
            String []filter_word = {"€","[A-z]","%","","[?]","/",">~","!","@","#","$","^"};

            String array[] = wordStr.toString().split("\n");

            for(int n=0;n< array.length;n++){       // 숫자와 . 남기고 제거
                for(int o=0;o<filter_word.length;o++){
                    array[n] = array[n].replaceAll(filter_word[o],"");
                }
            }

            double amount = Double.parseDouble(array[0]);     //가장 큰 숫자 구하기
            for(int p=1;p<array.length;p++){
                if(amount<Double.parseDouble(array[p])){
                    amount = Double.parseDouble(array[p]);
                }
            }
            double exchangeRate = 1328.58;
            int result = (int)(exchangeRate * amount);   //환율 계산
            showToast("환율을 계산합니다.");

            editAmount.setText(String.format("%d",result));  //array -> string 출력해보기
        }
        else{
            showToast("추출에 실패했습니다.");
        }
    }


    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream is;
        Bitmap bitmap = null;
        try {
            is = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}