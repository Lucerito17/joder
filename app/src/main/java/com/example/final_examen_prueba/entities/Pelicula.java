package com.example.final_examen_prueba.entities;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName =  "peliculas")
public class Pelicula {
    @PrimaryKey(autoGenerate = true)
    public int idPelicula;
    public String titulo;
    public String sinopsis;
    public String imagen;
}
