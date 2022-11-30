package com.example.final_examen_prueba.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.final_examen_prueba.Dao.PeliculaDao;
import com.example.final_examen_prueba.entities.Pelicula;

@Database(entities = {Pelicula.class}, version = 1)
public abstract class databaseApp extends RoomDatabase {
    public abstract PeliculaDao peliculaDao();

    public static databaseApp getDataBase(Context context){
        return Room.databaseBuilder(context, databaseApp.class, "com.example.final_examen_prueba.database_db")
                .allowMainThreadQueries().build();
    }
}
