package com.edisondeveloper.petagram.Modelo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.edisondeveloper.petagram.R;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BrocastFollow extends BroadcastReceiver {

    private ArrayList<MediaUser> list;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Constantes.KEY_ACTION.equals(intent.getAction())){
            darLike(context);
        }
    }

    public void darLike(final Context context){
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(MediaResponse.class, new MediaDeserializador());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.BASE_URL_API_INSTAGRAM)
                .addConverterFactory(GsonConverterFactory.create(gson.create()))
                .build();
        EndPointsApiInstagram endPointsApiInstagram = retrofit.create(EndPointsApiInstagram.class);
        Call<MediaResponse> response = endPointsApiInstagram.getMediaUser();
        response.enqueue(new Callback<MediaResponse>() {
            @Override
            public void onResponse(Call<MediaResponse> call, Response<MediaResponse> response) {
                MediaResponse mediaResponse = response.body();
                list = mediaResponse.getListMediaUser();
            }

            @Override
            public void onFailure(Call<MediaResponse> call, Throwable t) {
                Log.i("Error en la respuesta", t.toString());
            }
        });

        if(list != null){
            if(DataUtils.idUser != null && DataUtils.tokenDivice != null){
                Random position = new Random();
                MediaUser currentImage = list.get(position.nextInt(list.size()));
                Retrofit retrofitTwo = new Retrofit.Builder()
                        .baseUrl(Constantes.BASE_URL_API_HEROKU)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                EndPointHeroku request = retrofitTwo.create(EndPointHeroku.class);
                Call<Like> responseLike = request.sendNotificationLike(currentImage.getId(), DataUtils.idUser, DataUtils.tokenDivice);
                responseLike.enqueue(new Callback<Like>() {
                    @Override
                    public void onResponse(Call<Like> call, Response<Like> response) {
                        Toast.makeText(context, context.getString(R.string.like), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Like> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        }
    }

}
