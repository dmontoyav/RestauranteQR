package com.example.danielmontoya.restauranteqr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //track Choosing Image Intent
    private static final int CHOOSING_IMAGE_REQUEST = 1234;
    private static final int QRGENERATED = 2222;
    public static final int RESULT_BACK = 3333;
    private ImageView imageView;
    private Uri fileUri;
    private Bitmap bitmap;
    private EditText pathArchivo;


    private ManejadorAWS manejadorAWS;

    private ListView listView;
    private Adaptador adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listaItems);
        adaptador = new Adaptador(getArrayList(),getApplicationContext());
        listView.setAdapter(adaptador);
        findViewById(R.id.botonAgregar1).setOnClickListener(this);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item) adaptador.getItem(position);
                getMostrarImagen(item);
            }
        });

    }

    private void getMostrarImagen(Item item) {
        Intent intent = new Intent(this,activityQRview.class);
        intent.putExtra("fileUri",item.getUriImagen().getPath());
        startActivity(intent);
    }

    private ArrayList<Item> getArrayList(){
        ArrayList<Item> lista = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/QrCodeGenerator";
        File f = new File(path);
        File[] files = f.listFiles();
        for(int i = 0 ; i < files.length; i++)
        {
            File file = files[i];
            Item item = new Item(file,Uri.fromFile(file));
            lista.add(item);
        }

        return lista;
    }
    @Override
    public void onClick(View view) {
        int i = view.getId();

        if (i == R.id.botonAgregar1) {
            getParteSubirArchivo();
        }
    }


    public void getParteSubirArchivo()
    {
        Intent intent = new Intent(this,Main2Activity.class);
        startActivityForResult(intent,QRGENERATED);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("RequestCode: " + requestCode + "Result Code: " + resultCode);
        if(requestCode == Constantes.QRGENERATED && resultCode == Constantes.QRGENERATED){
            listView.invalidateViews();
            adaptador = new Adaptador(getArrayList(),getApplicationContext());
            listView.setAdapter(adaptador);
            Toast.makeText(this,"QR guardado",Toast.LENGTH_LONG).show();
        }



    }
}
