package com.wakeparkby.Fragment;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.wakeparkby.Activity.MainMenu.MainMenuActivity;
import com.wakeparkby.Controller.BookingController;
import com.wakeparkby.R;

public class FragmentReverseCableSelection extends Fragment implements View.OnClickListener, MainMenuActivity.OnBackPressedListner {
    private Button firstCable;
    private Button secondCable;
    private Button thirdCable;
    private BookingController bookingController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_reverse_selection, container, false);
        firstCable = rootView.findViewById(R.id.buttonFirstReverse);
        String myInt = getArguments().getString("date");
        firstCable.setOnClickListener(this);
        secondCable = rootView.findViewById(R.id.buttonSecondReverse);
        secondCable.setOnClickListener(this);
        thirdCable = rootView.findViewById(R.id.buttonThirdReverse);
        thirdCable.setOnClickListener(this);
        if (getArguments().getString("place").equals("DROZDI")) {
            thirdCable.setVisibility(View.GONE);
        }
        RelativeLayout relativeLayoutCalendar = rootView.findViewById(R.id.relativeLayoutReversSelection);
        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        toolbar.setTitle("Выберите реверс");
        return rootView;
    }

    @Override
    public void onClick(View v) {
        BookingController bookingController;
        Fragment fragment = new FragmentChooseTime();
        Bundle args_fragment = new Bundle();
        args_fragment.putString("place", getArguments().getString("place"));
        args_fragment.putString("date", getArguments().getString("date"));

        switch (v.getId()) {
            case R.id.buttonFirstReverse:
                this.bookingController = new BookingController(getArguments().getString("place"), getArguments().getString("date"),
                        1);
                args_fragment.putInt("reverseCableNumber", 1);
                fragment.setArguments(args_fragment);
                ((MainMenuActivity) getActivity()).pushFragments(MainMenuActivity.TAB_HOME, fragment, true);
                break;
            case R.id.buttonSecondReverse:
                this.bookingController = new BookingController(getArguments().getString("place"), getArguments().getString("date"),
                        2);
                args_fragment.putInt("reverseCableNumber", 2);
                fragment.setArguments(args_fragment);
                ((MainMenuActivity) getActivity()).pushFragments(MainMenuActivity.TAB_HOME, fragment, true);
                break;
            case R.id.buttonThirdReverse:
                this.bookingController = new BookingController(getArguments().getString("place"), getArguments().getString("date"),
                        3);
                args_fragment.putInt("reverseCableNumber", 3);
                fragment.setArguments(args_fragment);
                ((MainMenuActivity) getActivity()).pushFragments(MainMenuActivity.TAB_HOME, fragment, true);
                break;
        }
    }


    @Override
    public boolean onBackPressed() {
        return false;
    }
}
