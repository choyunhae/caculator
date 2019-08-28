package com.google.firebase.codelab.mlkit;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Extractor{
    private Bitmap image;
    private int amount;

    private String resultText;

    private String blockText;
    private Float blockConfidence;
    private List<RecognizedLanguage> blockLanguages;
    private Point[] blockCornerPoints;
    private Rect blockFrame;

    private String lineText;
    private Float lineConfidence;
    private List<RecognizedLanguage> lineLanguages;
    private Point[] lineCornerPoints;
    private Rect lineFrame;

    private String elementText;
    private Float elementConfidence;
    private List<RecognizedLanguage> elementLanguages;
    private Point[] elementCornerPoints;
    private Rect elementFrame;

    public Extractor() {
        this.image = null;
        this.amount = -1;
        this.resultText = "";

        this.blockText = "";
        this.blockConfidence = 0.0f;
        this.blockLanguages = null;
        this.blockCornerPoints = null;
        this.blockFrame = null;

        this.lineText="";
        this.lineConfidence= 0.0f;
        this.lineLanguages = null;
        this.lineCornerPoints = null;
        this.lineFrame = null;

        this.elementText="";
        this.elementConfidence= 0.0f;
        this.elementLanguages = null;
        this.elementCornerPoints = null;
        this.elementFrame = null;
    }

    public Extractor(Bitmap image) {
        this.image = image;
        this.amount = -1;
        this.resultText = "";

        this.blockText = "";
        this.blockConfidence = 0.0f;
        this.blockLanguages = null;
        this.blockCornerPoints = null;
        this.blockFrame = null;

        this.lineText="";
        this.lineConfidence= 0.0f;
        this.lineLanguages = null;
        this.lineCornerPoints = null;
        this.lineFrame = null;

        this.elementText="";
        this.elementConfidence= 0.0f;
        this.elementLanguages = null;
        this.elementCornerPoints = null;
        this.elementFrame = null;
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

    public FirebaseVisionImage ImageFromBitmap(Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        return image;
    }

    public Task<FirebaseVisionText> RecognizeText(FirebaseVisionImage image) {
        //FirebaseVisionText text;

        FirebaseVisionCloudTextRecognizerOptions options = new FirebaseVisionCloudTextRecognizerOptions.Builder()
                // .setLanguageHints(Arrays.asList("en", "hi"))
                .build();
        // [END set_detector_options_cloud]

        // [START get_detector_cloud]

        /*
        //더 dense한 model
         FirebaseVisionDocumentTextRecognizer detector = FirebaseVision.getInstance()
                .getCloudDocumentTextRecognizer();
        */

        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getCloudTextRecognizer();
        // Or, to change the default settings:

        //   FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
        //          .getCloudTextRecognizer(options);
        // [END get_detector_cloud]

        // [START run_detector_cloud]
        Task<FirebaseVisionText> result = detector.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText result) {
                        // Task completed successfully
                        // [START_EXCLUDE]
                        // [START get_text_cloud]
                        for (FirebaseVisionText.TextBlock block : result.getTextBlocks()) {
                            Rect boundingBox = block.getBoundingBox();
                            Point[] cornerPoints = block.getCornerPoints();
                            String text = block.getText();

                            for (FirebaseVisionText.Line line : block.getLines()) {
                                // ...
                                for (FirebaseVisionText.Element element : line.getElements()) {
                                    // ...
                                }
                            }
                        }
                        // [END get_text_cloud]
                        // [END_EXCLUDE]
                    }
                })
                .addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                    }
                });

        // [END run_detector_cloud]
        return result;
    }

    public void processTextBlocks(Task<FirebaseVisionText> taskResult) {
        FirebaseVisionText result = taskResult.getResult();
        assert result != null;
        this.resultText = result.getText();
        for (FirebaseVisionText.TextBlock block : result.getTextBlocks()) {
            this.blockText = block.getText();
            this.blockConfidence = block.getConfidence();
            this.blockLanguages = block.getRecognizedLanguages();
            this.blockCornerPoints = block.getCornerPoints();
            this.blockFrame = block.getBoundingBox();
            for (FirebaseVisionText.Line line : block.getLines()) {
                this.lineText = line.getText();
                this.lineConfidence = line.getConfidence();
                this.lineLanguages = line.getRecognizedLanguages();
                this.lineCornerPoints = line.getCornerPoints();
                this.lineFrame = line.getBoundingBox();
                for (FirebaseVisionText.Element element : line.getElements()) {
                    this.elementText = element.getText();
                    this.elementConfidence = element.getConfidence();
                    this.elementLanguages = element.getRecognizedLanguages();
                    this.elementCornerPoints = element.getCornerPoints();
                    this.elementFrame = element.getBoundingBox();
                }
            }
        }
        //return result;
    }

    public int Extract(FirebaseVisionText text) {
        int amount = -1;

        return amount;
    }

    public Bitmap GetImage() { return this.image; }
    public int GetAmount() { return this.amount; }

    public String GetResultText(){ return this.resultText; }

    public String GetBlockText(){return this.blockText;}
    public Float GetBlockConfidence(){return this.blockConfidence;}
    public List<RecognizedLanguage> GetBlockLanguages(){return this.blockLanguages;}
    public Point[] GetBlockCornerPoints() {return this.blockCornerPoints;}
    public Rect GetBlockFrame(){return this.blockFrame;}

    public String GetLineText(){return this.lineText;}
    public Float GetLineConfidence(){return this.lineConfidence;}
    public List<RecognizedLanguage> GetLineLanguages(){return this.lineLanguages;}
    public Point[] GetLineCornerPoints(){return this.lineCornerPoints;}
    public Rect GetLineFrame(){return  this.lineFrame;}

    public String GetElementText(){return this.elementText;}
    public Float GetElementConfidence(){return this.elementConfidence;}
    public List<RecognizedLanguage> GetElementLanguages(){return this.elementLanguages;}
    public Point[] GetElementCornerPoints(){return this.elementCornerPoints;}
    public Rect GetElementFrame(){return this.elementFrame;}
/*
    public void main(int argc, char[] argv){
        AssetManager assetMgr = getAssets();
        InputStream is;
        Bitmap bitmap = null;
        try {
            is = assetMgr.open("0001.bmp");
            //is = mContext.getAssets().open("0001.bmp");

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            bitmap = BitmapFactory.decodeByteArray( buffer, 0, buffer.length ) ;
            bitmap = Bitmap.createScaledBitmap(bitmap, 300, 340, true);

            // TODO:
        } catch (IOException e) {
            e.printStackTrace();
        }

        Extractor extractor = new Extractor(bitmap);
        FirebaseVisionImage img =extractor.ImageFromBitmap(bitmap);
        Task<FirebaseVisionText> textTask = extractor.RecognizeText(img);
        extractor.processTextBlocks(textTask);
        System.out.print(this.resultText);

    }
    */

}

