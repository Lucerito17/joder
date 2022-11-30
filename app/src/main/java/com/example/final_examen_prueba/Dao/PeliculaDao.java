package com.example.final_examen_prueba.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.final_examen_prueba.entities.Pelicula;

import java.util.List;

@Dao
public interface PeliculaDao {
    @Query("SELECT * FROM peliculas")
    List<Pelicula> listarTodos();

    @Insert
    void crear(Pelicula pelicula);
}
