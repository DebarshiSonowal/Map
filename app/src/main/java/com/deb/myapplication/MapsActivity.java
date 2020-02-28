package com.deb.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, SeekBar.OnSeekBarChangeListener {
    //Initialise Variable
    GoogleMap mGoogleMap;
    CheckBox mCheckBox;
    SeekBar seekred,seekgreen,seekblue;
    Button btDraw,btClear;
    Polygon mPolygon  = null;
    List<LatLng> mLatLngs = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();
    Location mLocation;
    FusedLocationProviderClient mFusedLocationProviderClient;
    private  static  final int Request_Code=101;


    int red=0,green=0,blue=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Assign Variable
        mCheckBox = findViewById(R.id.check_box);
        seekred = findViewById(R.id.seek_red);
        seekblue = findViewById(R.id.seek_blue);
        seekgreen = findViewById(R.id.seek_green);
        btDraw = findViewById(R.id.bt_draw);
        btClear = findViewById(R.id.bt_clear);

        //Intialize Support map
        SupportMapFragment  supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (mPolygon == null) return;;

                    mPolygon.setFillColor(Color.rgb(red,green,blue));
                }else {
                    mPolygon.setFillColor(Color.TRANSPARENT);
                }
            }
        });
        btDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPolygon != null) mPolygon.remove();
                PolygonOptions polygonOptions = new PolygonOptions().addAll(mLatLngs).clickable(true);

                mPolygon = mGoogleMap.addPolygon(polygonOptions);

                mPolygon.setStrokeColor(Color.rgb(red,green,blue));
                if(mCheckBox.isChecked())
                {
                    mPolygon.setFillColor(Color.rgb(red,green,blue));
                }
            }
        });
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPolygon != null) mPolygon.remove();
                for(Marker marker:markerList) marker.remove();
                mLatLngs.clear();
                markerList.clear();
                mCheckBox.setChecked(false);
                seekred.setProgress(0);
                seekgreen.setProgress(0);
                seekblue.setProgress(0);
            }
        });

        seekred.setOnSeekBarChangeListener(this);
        seekgreen.setOnSeekBarChangeListener(this);
        seekblue.setOnSeekBarChangeListener(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        mGoogleMap .setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                Marker marker = mGoogleMap.addMarker(markerOptions);
                mLatLngs.add(latLng);
                markerList.add(marker);
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.seek_red:
                red =i ;
                break;
            case R.id.seek_green:
                green = i;
                break;

            case R.id.seek_blue:
                blue = i;
                break;
        }
        if (mPolygon != null) {
            mPolygon.setStrokeColor(Color.rgb(red, green, blue));
            if (mCheckBox.isChecked()) {
                mPolygon.setFillColor(Color.rgb(red, green, blue));
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
