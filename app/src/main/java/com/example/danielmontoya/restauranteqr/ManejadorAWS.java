package com.example.danielmontoya.restauranteqr;

import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;

/**
 * Created by Danielmontoya on 1/06/20.
 */

public class ManejadorAWS extends AppCompatActivity {

    private AmazonS3Client s3;
    TransferUtility transferUtility;
    private static final String NOMBRE_BUCKET = "qrcoderestaurantes";
    private String Key;
    private File file;
    private String url;
    private Main2Activity mainActivity;
    private boolean creado;

    public ManejadorAWS(Main2Activity pmainActivity)
    {
        mainActivity = pmainActivity;
        credentialProvider();
        setTransferUtility();
    }

    private void credentialProvider()
    {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                mainActivity.getApplicationContext(),
                "us-east-2:c56e1ae4-e00f-47e4-adab-2aebe82aac06", // ID del grupo de identidades
                Regions.US_EAST_2 // Región
        );
        setAmazonS3Client(credentialsProvider);
    }

    private void setAmazonS3Client(CognitoCachingCredentialsProvider credentialsProvider){

        // Create an S3 client
        s3 = new AmazonS3Client(credentialsProvider);

        // Set the region of your S3 bucket
        s3.setRegion(Region.getRegion(Regions.US_EAST_2));

    }

    private void setTransferUtility() {

        transferUtility = new TransferUtility(s3, mainActivity.getApplicationContext());
    }

    /**
     * This method is used to upload the file to S3 by using TransferUtility class
     */
    public boolean setFileToUpload(Uri uri){
        creado = false;
        String filepath = getRealPathFromURI(uri);
        url = null;
        createFile(filepath);
        Key = file.getName();
        Key = Key.replaceAll(" ","");
        TransferObserver transferObserver = transferUtility.upload(
                NOMBRE_BUCKET, // The bucket to upload to
                Key,  // The key for the uploaded object
                file   // The file where the data to upload exists
        );

        transferObserverListener(transferObserver);
        url = s3.getUrl(NOMBRE_BUCKET,Key).toExternalForm();
        return creado;
    }

    private void transferObserverListener(TransferObserver transferObserver){

        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                if(state.equals(TransferState.COMPLETED))
                {
                    creado = true;
                    crearQR();
                    mainActivity.setResult(Constantes.QRGENERATED);
                    mainActivity.finish();
                }
                else
                {
                    Toast.makeText(mainActivity.getApplicationContext(),"Se está generando el QR",Toast.LENGTH_SHORT).show();
                }
                Log.e("statechange: ", state + "url: "+ url);


            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent/bytesTotal * 100);
                Log.e("percentage: ", percentage + "");
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error: ", ex.getMessage());
            }

        });
    }

    private String getRealPathFromURI(Uri contentUri) {
        RealPathUtil realPathUtil = new RealPathUtil();
        String result = realPathUtil.getRealPath(mainActivity.getApplicationContext(),contentUri);
        return result;
    }

    private void createFile(String path)
    {
        file = new File(path);

    }

    public String getUrl()
    {
        return url;
    }

    public String getFilePath()
    {
        return file.getPath();
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public String getfileName()
    {
        return Key;
    }

    public void crearQR()
    {
        QRGenerator qr = new QRGenerator(Key,getUrl(),mainActivity.getApplicationContext());
        Bitmap bp = qr.generadorQR();
        mainActivity.setImageView(bp);
    }
}
