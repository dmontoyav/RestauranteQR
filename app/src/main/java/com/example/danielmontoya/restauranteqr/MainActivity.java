package com.example.danielmontoya.restauranteqr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //track Choosing Image Intent
    private static final int CHOOSING_IMAGE_REQUEST = 1234;
    private ImageView imageView;
    private Uri fileUri;
    private Bitmap bitmap;
    private EditText pathArchivo;


    private ManejadorAWS manejadorAWS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image_File);
        pathArchivo = (EditText) findViewById(R.id.pathArchivo);
        findViewById(R.id.buttonCargar).setOnClickListener(this);
        findViewById(R.id.botonsubir).setOnClickListener(this);
        findViewById(R.id.generador).setOnClickListener(this);

        manejadorAWS = new ManejadorAWS(this);

    }


    @Override
    public void onClick(View view) {
        int i = view.getId();

        if (i == R.id.buttonCargar) {
            seleccionarArchivo();
        }
        else if (i == R.id.botonsubir) {
            subirArchivo();
        }
        else if (i == R.id.generador) {
            crearQR();
        }
    }

    public void seleccionarArchivo() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Document"), CHOOSING_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (bitmap != null) {
            bitmap.recycle();
        }

        if (requestCode == CHOOSING_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();
            try {
                pathArchivo.setText(fileUri.getPath());
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Crea el QR
     */
    public void crearQR()
    {
        QRGenerator qr = new QRGenerator(manejadorAWS.getfileName(),getUrl(),this);
        Bitmap bitmap = qr.generadorQR();
        imageView.setImageBitmap(bitmap);
        pathArchivo.setText(getUrl());
    }

    public void subirArchivo()
    {
        manejadorAWS.setFileToUpload(fileUri);
    }

    public String getUrl()
    {
        return manejadorAWS.getUrl();
    }



}
