package com.example.danielmontoya.restauranteqr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Danielmontoya on 2/06/20.
 */

public class Adaptador extends BaseAdapter {
    private ArrayList<Item> list;
    private Context context;

    public Adaptador(ArrayList<Item> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = (Item) getItem(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.item,null);
        ImageView imagenFoto = (ImageView) convertView.findViewById(R.id.imagenQR);
        TextView titulo = (TextView) convertView.findViewById(R.id.nombreArchivo);
        TextView ruta = (TextView) convertView.findViewById(R.id.rutaArchivo);
        System.out.println("URIS: " + item.getUriImagen());
        imagenFoto.setImageURI(item.getUriImagen());
        titulo.setText(item.getTitulo());
        ruta.setText(item.getRuta());

        return convertView;
    }
}
