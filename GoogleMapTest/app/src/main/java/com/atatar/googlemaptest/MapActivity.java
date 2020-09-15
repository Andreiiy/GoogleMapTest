package com.atatar.googlemaptest;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atatar.googlemaptest.adapters.CustomInfoWindowAdapter;
import com.atatar.googlemaptest.adapters.OnInfoWindowElemTouchListener;
import com.atatar.googlemaptest.jsonparser.JsonParser;
import com.atatar.googlemaptest.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private EditText et_location;
    private Button btn_search;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private User currentUser;
    private double currentLat = 0;
    private double currentLong = 0;
    private String searchString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        et_location = (EditText) findViewById(R.id.et_location);
        btn_search = (Button) findViewById(R.id.btn_search);

        Intent intent = getIntent();
       currentUser =  (User) intent.getSerializableExtra("user");
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                //map.clear();
                searchString = et_location.getText().toString();

                if (!searchString.isEmpty()) {
                    findPlace(searchString);
                }
            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        getDeviceLocation();
    }


    private void findPlace(String searchString){
        List<Address> addressList = null;

        Geocoder geocoder = new Geocoder(MapActivity.this);
        try {
            addressList = geocoder.getFromLocationName(searchString, 30);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addressList.size() > 0) {
            Address address = addressList.get(0);
            currentLat = address.getLatitude();
            currentLong = address.getLongitude();
            LatLng latLng = new LatLng(currentLat, currentLong);
            map.addMarker(new MarkerOptions().position(latLng).title(address.getAddressLine(0)));
            moveCamera(latLng, 7);

        } else {
            findPlaces();
        }
    }


    private void getDeviceLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLat = location.getLatitude();
                    currentLong = location.getLongitude();

                    map.addMarker(new MarkerOptions().position(new LatLng(currentLat, currentLong)).title("My location"));
                    moveCamera(new LatLng(currentLat, currentLong), 15);
                }
            }
        });

    }

    private void moveCamera(LatLng latLng, int zoom) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        map.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));
    }

    private void findPlaces() {

        String place = et_location.getText().toString().toLowerCase();
        et_location.setText("");

        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?" +
                "query="+ place +
                "&location=" + currentLat + "," + currentLong +
                "&key="+getResources().getString(R.string.map_key);

        new TaskPlace().execute(url);
    }

    public class TaskPlace extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            //Execute parser task
            new ParserTask().execute(s);
        }

        private String downloadUrl(String string) throws IOException {

            //Initialize url
            URL url = new URL(string);
            //Initialize connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            //Initialize input stream
            InputStream stream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder stringBuilder = new StringBuilder();
            // Initialize string variable
            String line = "";
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            String data = stringBuilder.toString();

            reader.close();

            return data;
        }

        public class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
            @Override
            protected List<HashMap<String, String>> doInBackground(String... strings) {
                JsonParser jsonParser = new JsonParser();
                List<HashMap<String, String>> mapList = null;
                JSONObject object = null;
                try {
                    object = new JSONObject(strings[0]);
                    mapList = jsonParser.parseResult(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return mapList;
            }

            @Override
            protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
                ArrayList<String> protectionWords = new ArrayList<String>(Arrays.asList("bar","pub","alcohol","beer","wine"));

                map.clear();

                for (int i = 0; i < hashMaps.size(); i++) {
                    //Initialize hash map
                    HashMap<String, String> hashMapList = hashMaps.get(i);

                    double latitude = Double.parseDouble(hashMapList.get("lat"));
                    double longitude = Double.parseDouble(hashMapList.get("lng"));
                    String name = hashMapList.get("name");
                    String address = hashMapList.get("address");

                    String title = name+"\n"+address;
                    if(!currentUser.checkAgeOfMajority() && protectionWords.contains(searchString.toLowerCase()))
                        title = "Staying in these places is prohibited by law";
                   //

                    map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(title));

                }
            }
        }
    }
}