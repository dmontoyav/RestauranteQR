package com.example.danielmontoya.restauranteqr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Danielmontoya on 1/06/20.
 */

public class QRGenerator {
    String url;
    Bitmap bitmap;
    MainActivity mainActivity;
    private File codeFile;
    private String nameFile;

    public QRGenerator(String pnameFile , String purl, MainActivity pmainActivity)
    {
        url = purl;
        mainActivity = pmainActivity;
        nameFile = pnameFile;
    }
    public Bitmap generadorQR()
    {
        String text=url; // Whatever you need to encode in the QR code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,1000,1000);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            saveCodeInToFile();
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveCodeInToFile(){
        if(bitmap == null) {
            //showSnackbar(mainActivity.getResources().getString(R.string.errorNullBitmap));
        }
        else
            {
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/QrCodeGenerator");
            dir.mkdirs();
            codeFile = new File(dir, "QrCode-"+nameFile+".png");
            try {
                OutputStream out = new FileOutputStream(codeFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //showSnackbar(mainActivity.getResources().getString(R.string.correctImageSaved));
        }
    }

    /**private void actionShareCode(){
        Uri imageUri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/QrCodeGenerator/QrCode.png");
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/png");
        i.putExtra(Intent.EXTRA_STREAM, imageUri);
        i.putExtra(Intent.EXTRA_TEXT, mainActivity.getResources().getString(R.string.codeGeneratedMessage));
        mainActivity.startActivity(i);
    }*/



}
