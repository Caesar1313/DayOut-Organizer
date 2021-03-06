package com.example.dayout_organizer.ui.dialogs.pick;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Pair;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dayout_organizer.R;
import com.example.dayout_organizer.adapter.recyclers.PickTypeAdapter;
import com.example.dayout_organizer.models.tripType.TripType;
import com.example.dayout_organizer.models.tripType.TripTypeModel;
import com.example.dayout_organizer.models.trip.create.CreateTripType;
import com.example.dayout_organizer.ui.activities.MainActivity;
import com.example.dayout_organizer.ui.dialogs.notify.ErrorDialog;
import com.example.dayout_organizer.viewModels.TripViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickTripTypeDialog extends Dialog {

    Context context;
    PickTypeAdapter pickTypeAdapter;
    @BindView(R.id.pick_type_rc)
    RecyclerView pickTypeRc;

    int tripId;
    CreateTripType createTripType;

    public PickTripTypeDialog(@NonNull Context context,int tripId) {
        super(context);
        setContentView(R.layout.pick_type_dialog);

        ButterKnife.bind(this);
        this.context = context;
        this.tripId = tripId;
        initView();
    }

    private void initView() {
        createTripType  = new CreateTripType(tripId,new ArrayList<>());
        initRc();
    }

    private void initRc() {
        pickTypeRc.setHasFixedSize(true);
        pickTypeRc.setLayoutManager(new LinearLayoutManager(context));
        pickTypeAdapter = new PickTypeAdapter(new ArrayList<>(),context);
        pickTypeAdapter.setOnItemClick(onItemClick);
        pickTypeRc.setAdapter(pickTypeAdapter);
    }

    public void setTripType(List<TripType> list){
        createTripType.types.addAll(list);
    }

    public CreateTripType getCreateTripType (){
        return  createTripType;
    }

    private final PickTypeAdapter.OnItemClick onItemClick = new PickTypeAdapter.OnItemClick() {
        @Override
        public void OnCreateTripPlaceItemClicked(int position, List<TripType> list) {
            createTripType.types.add(new TripType(list.get(position).id,list.get(position).name));
            cancel();
        }
    };
    private void getDataFromApi(){
        TripViewModel.getINSTANCE().tripTypeTripMutableLiveData.observe((MainActivity) context, new Observer<Pair<TripTypeModel,String>>() {
            @Override
            public void onChanged(Pair<TripTypeModel, String> listStringPair) {
                if (listStringPair != null){
                    if (listStringPair.first != null){
                        pickTypeAdapter.refresh(listStringPair.first.data);
                    }
                    else {
                        new ErrorDialog(context,listStringPair.second).show();
                    }
                }
                else {
                    new ErrorDialog(context,context.getResources().getString(R.string.error_connection)).show();
                }
            }
        });
    }

    @Override
    public void show() {

        getDataFromApi();
        WindowManager.LayoutParams wlp = getWindow().getAttributes();
        wlp.gravity = Gravity.CENTER;
        getWindow().setAttributes(wlp);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // match width dialog
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        super.show();
    }
}
