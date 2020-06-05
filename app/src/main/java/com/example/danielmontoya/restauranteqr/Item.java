package com.example.danielmontoya.restauranteqr;

import android.net.Uri;

import java.io.File;

/**
 * Created by Danielmontoya on 2/06/20.
 */

public class Item {

    private Uri uriImagen;
    private String titulo;
    private String ruta;

    public Item(File file, Uri pUri)
    {
        this.uriImagen = Uri.fromFile(file);

        this.titulo = file.getName();
        this.ruta = file.getPath();
    }

    public Uri getUriImagen() {
        return uriImagen;
    }

    public void setUriImagen(Uri uriImagen) {
        this.uriImagen = uriImagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
