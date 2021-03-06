package com.example.dayout_organizer.viewModels;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.dayout_organizer.api.ApiClient;
import com.example.dayout_organizer.models.place.PlacePaginationModel;
import com.example.dayout_organizer.models.place.PlaceModel;
import com.example.dayout_organizer.models.place.PlaceDetailsModel;
import com.google.gson.JsonObject;


import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.dayout_organizer.config.AppConstants.getErrorMessage;
import static com.example.dayout_organizer.config.AppSharedPreferences.GET_USER_ID;

public class PlaceViewModel extends ViewModel {

    private static final String TAG = "PlaceViewModel";

    private final ApiClient apiClient = new ApiClient();

    private static PlaceViewModel instance;

    public static PlaceViewModel getINSTANCE() {
        if (instance == null) {
            instance = new PlaceViewModel();
        }
        return instance;
    }

   public MutableLiveData<Pair<PlaceModel,String>> popularPlaceMutableLiveData;
    public MutableLiveData<Pair<PlaceDetailsModel, String>> placeDetailsMutableLiveData;
   public MutableLiveData<Pair<PlacePaginationModel,String>> placeMutableLiveData;

    public MutableLiveData<Pair<Boolean,String>> successfulMutableLiveData;

    public void getPopularPlace(){
        popularPlaceMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().getPopularPlace(GET_USER_ID()).enqueue(new Callback<PlaceModel>() {
            @Override
            public void onResponse(@NonNull Call<PlaceModel> call, @NonNull Response<PlaceModel> response) {
                if (response.isSuccessful()){
                    popularPlaceMutableLiveData.setValue(new Pair<>(response.body(),null));
                }
                else {
                    try {
                        popularPlaceMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlaceModel> call, @NonNull Throwable t) {
                popularPlaceMutableLiveData.setValue(null);
            }
        });
    }

    public void getPlaces(int page){
        placeMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().getPlaces(page).enqueue(new Callback<PlacePaginationModel>() {
            @Override
            public void onResponse(@NonNull Call<PlacePaginationModel> call, @NonNull Response<PlacePaginationModel> response) {
                if (response.isSuccessful()){
                    placeMutableLiveData.setValue(new Pair<>(response.body(),null));
                }
                else {
                    try {
                        placeMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlacePaginationModel> call, @NonNull Throwable t) {
                placeMutableLiveData.setValue(null);
            }
        });
    }

    public void getPlaceDetails(int placeId){
        placeDetailsMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().getPlaceDetails(placeId).enqueue(new Callback<PlaceDetailsModel>() {
            @Override
            public void onResponse(@NonNull Call<PlaceDetailsModel> call, @NonNull Response<PlaceDetailsModel> response) {
                if (response.isSuccessful()){
                    placeDetailsMutableLiveData.setValue(new Pair<>(response.body(),null));
                }
                else {
                    try {
                        placeDetailsMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlaceDetailsModel> call, @NonNull Throwable t) {
                placeDetailsMutableLiveData.setValue(null);
            }
        });
    }


    public void suggestPlace(JsonObject place) {
        successfulMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().suggestPlace(place).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    successfulMutableLiveData.setValue(new Pair<>(true, null));
                } else {
                    try {
                        successfulMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                successfulMutableLiveData.setValue(null);
            }
        });
    }



}
