package com.wakeparkby.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wakeparkby.R;

/**
 * класс для работы с объектом интерфейса карты
 */
public class FragmentMaps extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    Button buttonAddCoordinates;
    private MapView mapView;
    private GoogleMap mMap;
    private Marker marker;
    private LatLng latLng;
    private String coordinates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);
        //    SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        //   mapFragment.getMapAsync(this);
        //   latLng = new LatLng(,)

        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        toolbar.setTitle("Карта");
        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);

        FloatingActionsMenu rightLabels = (FloatingActionsMenu) rootView.findViewById(R.id.right_labels);
        FloatingActionButton fab_drozdy = rootView.findViewById(R.id.fab_drozdy);
        FloatingActionButton fab_logoysk = rootView.findViewById(R.id.fab_logoysk);
        fab_drozdy.setOnClickListener(this);
        fab_logoysk.setOnClickListener(this);
        return rootView;

    }

    public static FragmentMaps newInstance() {
        return new FragmentMaps();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
   /*     if (latLng!=null){
            mMap.addMarker(new MarkerOptions().position(latLng).title("Место встречи"));
          //  mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            CameraUpdate centre = CameraUpdateFactory.newLatLng(latLng);
            mMap.moveCamera(centre);
            mMap.animateCamera(zoom);
        }
        else {*/
        LatLng minsk = new LatLng(53.907211, 27.558678);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(11);
        CameraUpdate centre = CameraUpdateFactory.newLatLng(minsk);
        mMap.moveCamera(centre);
        mMap.animateCamera(zoom);

        LatLng logoysk = new LatLng(54.181223, 27.810689);
        googleMap.addMarker(new MarkerOptions().position(logoysk)
                .title("Логойск"));
        LatLng drozdy = new LatLng(53.956229, 27.448999);
        googleMap.addMarker(new MarkerOptions().position(drozdy)
                .title("Дрозды"));

        //}
       /* mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker == null){
                    marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Место встречи"));
                    setLatLng(latLng);
                }
                else {
                    mMap.clear();
                    marker = null;
                }
            }
        });*/
    }


    public boolean onMarkerClick(Marker marker) {
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(getContext(), marker.getTitle() + " has been clicked " + clickCount + " times.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_drozdy:
                setLatLng(new LatLng(53.956229, 27.448999));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                CameraUpdate centre = CameraUpdateFactory.newLatLng(latLng);
                mMap.moveCamera(centre);
                mMap.animateCamera(zoom);
                break;
            case R.id.fab_logoysk:
                setLatLng(new LatLng(54.181223, 27.810689));
                zoom = CameraUpdateFactory.zoomTo(15);
                centre = CameraUpdateFactory.newLatLng(latLng);
                mMap.moveCamera(centre);
                mMap.animateCamera(zoom);
                break;
        }

    }
}

/*
mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            CameraUpdate centre = CameraUpdateFactory.newLatLng(latLng);
            mMap.moveCamera(centre);
            mMap.animateCamera(zoom);
 */