package com.example.danielmontoya.restauranteqr;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;

public class activityQRview extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private Uri fileUri;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrview);
        String uri = getIntent().getStringExtra("fileUri");
        System.out.println(uri);
        uri = "file://"+uri;
        fileUri = Uri.parse(uri);
        System.out.println(fileUri);
        imageView = (ImageView) findViewById(R.id.imagen_QR);
        findViewById(R.id.boton_atras2).setOnClickListener(this);
        findViewById(R.id.boton_compartir);
        imageView.setImageURI(fileUri);

    }

    @Override
    public void onClick(View v) {
        
        int i = v.getId();
        if(i == R.id.boton_atras2)
        {
            this.finish();
        }
        else if(i == R.id.boton_compartir)
        {
            compartirQR();
        }

    }

    private void compartirQR() {

    }
}