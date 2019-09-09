package com.wakeparkby.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.stomped.stomped.client.StompedClient;
import com.stomped.stomped.component.StompedFrame;
import com.stomped.stomped.listener.StompedListener;
import com.wakeparkby.Activity.Booking.ChooseTimeAdapter;
import com.wakeparkby.Activity.Booking.ChooseTimeItem;
import com.wakeparkby.Activity.MainMenu.MainMenuActivity;
import com.wakeparkby.Controller.BookingController;
import com.wakeparkby.Database.App;
import com.wakeparkby.Database.DatabaseHelper;
import com.wakeparkby.HTTPController.Booking;
import com.wakeparkby.HTTPController.TimeSpace;
import com.wakeparkby.HTTPController.WsEventDto;
import com.wakeparkby.Observer.Observer;
import com.wakeparkby.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentChooseTime extends Fragment implements View.OnClickListener, MainMenuActivity.OnBackPressedListner {
    private LinearLayout linearLayoutChooseTime;
    private RelativeLayout relativeLayoutProgressBarChooseTime;
    private RecyclerView newsRecyclerView;
    private MaterialButton buttonChooseTime;
    private ChooseTimeAdapter chooseTimeAdapter;
    private final StompedClient client = new StompedClient.StompedClientBuilder().build("http://18.196.191.127:8080/jwtappdemo-0.0.1-SNAPSHOT/gs-guide-websocket/websocket");
    private List<ChooseTimeItem> mData;
    private List<Booking> bookingList = new ArrayList<>();
    private DatabaseHelper databaseHelper;
    private BookingController bookingController = new BookingController();
    private List<TimeSpace> responseTimeSpaceList = new ArrayList<>();
    private Observer observer = new Observer("Time") {
        @Override
        public void update() {
            int n = observer.getStatus();
            if (n == 10) {
                if (observer.getId() == 2) {
                    updateChooseTime();
                    observer.setId(0);
                } else {
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_choose_time, container, false);
        buttonChooseTime = rootView.findViewById(R.id.buttonChooseTime);
        buttonChooseTime.setOnClickListener(this);
        newsRecyclerView = rootView.findViewById(R.id.recyclerViewTime);
        linearLayoutChooseTime = rootView.findViewById(R.id.linearLayoutChooseTime);
        relativeLayoutProgressBarChooseTime = rootView.findViewById(R.id.relativeLayoutProgressBarChooseTime);
        mData = new ArrayList<>();
        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        toolbar.setTitle("Выберите время");
        linearLayoutChooseTime.setVisibility(View.GONE);
        relativeLayoutProgressBarChooseTime.setVisibility(View.VISIBLE);
        openConnect();

        return rootView;
    }

    private void openConnect() {
        databaseHelper = App.getInstance().getDatabaseInstance();
        String userId = databaseHelper.getDataDao().getByTitle("UserId").get(0).getDescription().toString();
        client.subscribe("/topic/activity", new StompedListener() {

            @Override
            public void onNotify(final StompedFrame frame) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String response = frame.getStompedBody().toString().substring(0, frame.getStompedBody().length() - 1);
                        Gson gson = new Gson();
                        WsEventDto wsEventDto = gson.fromJson(response, WsEventDto.class);
                        bookingList =  bookingController.getBookingList();
                        if (getArguments().getString("place").equals(wsEventDto.getBody().getLocation()) &&
                                getArguments().getString("date").equals(wsEventDto.getBody().getBookingDate())&&
                                getArguments().getInt("reverseCableNumber",0) == wsEventDto.getBody().getReversNumber()){
                            for (int i = 0; i < mData.size(); i++) {
                                int startTime = Integer.valueOf(mData.get(i).getStartHours()) * 60 + Integer.valueOf(mData.get(i).getStartMinutes());
                                if (startTime == wsEventDto.getBody().getStartTime()) {
                                    for (int j = 0; j<bookingList.size();j++){
                                        if(bookingList.get(j).getId() == wsEventDto.getBody().getId())
                                        {
                                            mData.get(i).setStatus("MY_BOOKED_NO_ACCEPTED");
                                        }
                                        else{
                                            mData.get(i).setStatus("BOOKED_NO_ACCEPTED");
                                        }
                                    }
                                    chooseTimeAdapter.setFl(1);
                                    chooseTimeAdapter.notifyItemChanged(i);
                                }
                            }
                        }

                    }
                });
            }
        });
    }

    private void updateChooseTime() {
        mData.clear();
        bookingController.refreshFinalTimeSpaceList();
        for (int i = 0; i < bookingController.getFinalTimeSpaceList().size(); i++) {
            mData.add(new ChooseTimeItem(bookingController.getFinalTimeSpaceList().get(i).getStartHours(), bookingController.getFinalTimeSpaceList().get(i).getStartMinutes(),
                    bookingController.getFinalTimeSpaceList().get(i).getEndHours(), bookingController.getFinalTimeSpaceList().get(i).getEndMinutes(),
                    bookingController.getFinalTimeSpaceList().get(i).getStatus()));
        }
        chooseTimeAdapter = new ChooseTimeAdapter(getContext(), mData, getArguments().getString("place"), getArguments().getString("date"), getArguments().getInt("reverseCableNumber", 10));
        newsRecyclerView.setAdapter(chooseTimeAdapter);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        relativeLayoutProgressBarChooseTime.setVisibility(View.GONE);
        linearLayoutChooseTime.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = new FragmentBookingDescription();
       bookingList =  bookingController.getBookingList();
        ArrayList<Booking> arrayListBooking = new ArrayList<>(bookingList.size());
        arrayListBooking.addAll(bookingList);
        Bundle args_fragment = new Bundle();
        args_fragment.putSerializable("bookingList",arrayListBooking);
        fragment.setArguments(args_fragment);
        ((MainMenuActivity) getActivity()).pushFragments(MainMenuActivity.TAB_HOME, fragment, true);
        //  Intent intent_description = new Intent(this, FragmentBookingDescription.class);
        //  startActivity(intent_description);
      /*  mData.add(2,new ChooseTimeItem("9","10",
               "9","40",
                "FREE"));
        ChooseTimeAdapter.notifyItemInserted(2);*/
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        observer.removeFromList(observer);
        client.disconnect();

    }
}
