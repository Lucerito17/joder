package com.example.final_examen_prueba;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.final_examen_prueba.database.databaseApp;
import com.example.final_examen_prueba.entities.Imagen;
import com.example.final_examen_prueba.entities.Imagen64;
import com.example.final_examen_prueba.entities.Pelicula;
import com.example.final_examen_prueba.services.ServicioImagen;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistroActivity extends AppCompatActivity {

    private final static int CAMERA_REQUEST = 1000;

    EditText nombre, sinopsis, imagenUrl;
    Button btnCrear, btnCamera;
    Pelicula pelicula = new Pelicula();
    ImageView imgFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //BASE DE DATOS (DECLARACIÓN)
        databaseApp data = databaseApp.getDataBase(this);

        nombre = findViewById(R.id.editNombre);
        sinopsis = findViewById(R.id.editSinopsis);
        imagenUrl = findViewById(R.id.editUrlImagen);
        btnCamera = findViewById(R.id.btnTakePhoto);
        imgFoto = findViewById(R.id.imgFoto);
        btnCrear = findViewById(R.id.btnCreacion);

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pelicula.titulo = nombre.getText().toString();
                pelicula.sinopsis = sinopsis.getText().toString();
                pelicula.imagen = imagenUrl.getText().toString();
                data.peliculaDao().crear(pelicula);

                List<Pelicula> lista = data.peliculaDao().listarTodos();
                Log.i("MAIN_APP", new Gson().toJson(lista));
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    abrirCam();
                }
                else{
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                }
            }
        });
    }

    public void abrirCam(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1001);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CAMERA_REQUEST && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgFoto.setImageBitmap(imageBitmap);
        }

        if(requestCode==1001){
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(data.getData(),filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap imageBitmap = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String imgBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.imgur.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ServicioImagen imageService = retrofit.create(ServicioImagen.class);
            Imagen imagenalgo = new Imagen();
            imagenalgo.imagen = imgBase64;

            imageService.subirImagen(imagenalgo).enqueue(new Callback<Imagen64>() {
                @Override
                public void onResponse(Call<Imagen64> call, Response<Imagen64> response) {
                    String imR = response.body().temp.link;
                    imagenUrl.setText(imR);
                    Log.i("MAIN_APP", imR);
                }

                @Override
                public void onFailure(Call<Imagen64> call, Throwable t) {

                }
            });
        }
    }

}