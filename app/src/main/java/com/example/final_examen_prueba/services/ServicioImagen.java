package com.example.final_examen_prueba.services;

import com.example.final_examen_prueba.entities.Imagen;
import com.example.final_examen_prueba.entities.Imagen64;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ServicioImagen {
    @Headers("Authorization: Client-ID 8bcc638875f89d9")
    @POST("3/image")
    Call<Imagen64>subirImagen(@Body Imagen imagen);
}
