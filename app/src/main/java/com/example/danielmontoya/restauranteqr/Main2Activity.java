package com.example.danielmontoya.restauranteqr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{


    private ImageView imageView;
    private Uri fileUri;
    private Bitmap bitmap;
    private EditText pathArchivo;


    private ManejadorAWS manejadorAWS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        imageView = (ImageView) findViewById(R.id.image_File);
        findViewById(R.id.botonsubir).setOnClickListener(this);
        findViewById(R.id.boton_atras).setOnClickListener(this);
        seleccionarArchivo();

        manejadorAWS = new ManejadorAWS(this);

    }


    @Override
    public void onClick(View view) {
        int i = view.getId();

         if (i == R.id.botonsubir) {
            subirArchivo();
        }
        else if (i == R.id.boton_atras) {
            this.setResult(Constantes.RESULT_BACK);
             this.finish();
        }
    }

    public void seleccionarArchivo() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Document"), Constantes.CHOOSING_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (bitmap != null) {
            bitmap.recycle();
        }

        if (requestCode == Constantes.CHOOSING_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();
            System.out.println(fileUri);
            try {
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
        this.finish();
    }

    public void subirArchivo()
    {
        boolean creado = manejadorAWS.setFileToUpload(fileUri);
    }

    public String getUrl()
    {
        return manejadorAWS.getUrl();
    }

    public void setImageView(Bitmap bp)
    {
        imageView.setImageBitmap(bp);
    }

}
