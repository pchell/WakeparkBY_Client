package com.wakeparkby.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.wakeparkby.Activity.History.AdapterHistory;
import com.wakeparkby.Activity.History.AdapterHistoryArray;
import com.wakeparkby.Controller.HistoryController;
import com.wakeparkby.HTTPController.History;
import com.wakeparkby.Observer.Observer;
import com.wakeparkby.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FragmentHistory extends Fragment implements  AdapterView.OnItemClickListener {
    private ArrayList<History> historyArrayList = new ArrayList<>();
    HistoryController historyController = new HistoryController();
    ListView listView;
    RelativeLayout relativeLayoutProgressBarHistory;
    private String userId = "1";

    Observer observer = new Observer("History") {
        @Override
        public void update() {
            int n = observer.getStatus();
            if (n == 10) {
                if (observer.getId() == 5) {
                    updateHistoryList();
                    observer.setId(0);
                } else {
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_history, container, false);
        listView = (ListView) rootView.findViewById(R.id.listview);
        listView.setOnItemClickListener(this);
        relativeLayoutProgressBarHistory = rootView.findViewById(R.id.relativeLayoutProgressBarHistory);
        HistoryController historyController = new HistoryController(userId);
        listView.setVisibility(View.GONE);
        relativeLayoutProgressBarHistory.setVisibility(View.VISIBLE);
        return rootView;
    }

    public FragmentHistory() {
    }

    public static FragmentHistory newInstance() {
        return new FragmentHistory();
    }

    private void updateHistoryList() {
        historyArrayList = HistoryController.getListHistory();
        ArrayAdapter<History> adapter = new AdapterHistoryArray(getActivity(), 0, historyArrayList);
        listView.setAdapter(adapter);
        relativeLayoutProgressBarHistory.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        History history = historyArrayList.get(position);
        AdapterHistory adapterHistory = new AdapterHistory(history);
        String status = adapterHistory.getStatus();
        if (status.equals("BOOKED")) {
            String data = adapterHistory.getData();
            String time = adapterHistory.getTime();
            long yourmilliseconds = System.currentTimeMillis();
            SimpleDateFormat dataFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date resultDate = new Date(yourmilliseconds);
            String dataNow = dataFormat.format(resultDate);
            if (dataNow.equals(data)) {
                long milliseconds = System.currentTimeMillis();
                SimpleDateFormat timeHoursFormat = new SimpleDateFormat("HH");
                SimpleDateFormat timeMinutesFormat = new SimpleDateFormat("mm");
                Date resultTime = new Date(milliseconds);
                String hoursNow = timeHoursFormat.format(resultTime);
                String minutesNow = timeMinutesFormat.format(resultTime);
                int timeNow = Integer.valueOf(hoursNow) * 60 + Integer.valueOf(minutesNow);
                if (timeNow > Integer.valueOf(adapterHistory.getStartTime()) - 120) {
                    Toast.makeText(getContext(), "Отмена невозможна" + System.lineSeparator() + "Осталось меньше 2-x часов", Toast.LENGTH_LONG).show();
                } else {
                    String idHistory = adapterHistory.getHistoryId();
                    String location = adapterHistory.getLocationName();
                    String reversNumber = adapterHistory.getReversNumber();
                    System.out.print(idHistory);
                    createTwoButtonsAlertDialog("Отмена бронирования", "Отменить броинрование ?" + System.lineSeparator() + System.lineSeparator() + "Место: " + location + " ( " +
                            reversNumber + " реверс )" + System.lineSeparator() + "Дата: " + data + System.lineSeparator() + "Время: " + time, idHistory);
                }
            } else {
                String idHistory = adapterHistory.getHistoryId();
                String location = adapterHistory.getLocationName();
                String reversNumber = adapterHistory.getReversNumber();
                System.out.print(idHistory);
                createTwoButtonsAlertDialog("Отмена бронирования", "Отменить броинрование ?" + System.lineSeparator() + System.lineSeparator() + "Место: " + location + " ( " +
                        reversNumber + " реверс )" + System.lineSeparator() + "Дата: " + data + System.lineSeparator() + "Время: " + time, idHistory);
            }

        } else if (status.equals("MISSED")){
            Toast.makeText(getContext(), "Вы уже оменили бронирование", Toast.LENGTH_LONG).show();
        } else if (status.equals("VISITED")){
            Toast.makeText(getContext(), "Вы уже посетили вейкпарк", Toast.LENGTH_LONG).show();

        }


    }

    private void createTwoButtonsAlertDialog(String title, String content, String idHistory) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setNegativeButton("Вернуться",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                    }
                });
        builder.setPositiveButton("Отменить",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        historyController.deleteHistory(userId,idHistory);
                        Toast.makeText(getContext(), "Вы отменили бронирование", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        observer.removeFromList(observer);
    }
}