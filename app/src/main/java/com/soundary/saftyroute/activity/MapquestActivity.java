package com.soundary.saftyroute.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapquest.mapping.MapQuest;
import com.mapquest.mapping.maps.MapView;
import com.soundary.saftyroute.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapquestActivity extends AppCompatActivity {

    private MapView mMapView;
    private MapboxMap mMapboxMap;
    private final LatLng MAPQUEST_HEADQUARTERS_LOCATION = new LatLng(39.750307, -104.999472);
    String sourceString, destinationString;
    private ArrayList<LatLng> MarkerPoints = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapQuest.start(getApplicationContext());
        setContentView(R.layout.activity_mapquest);

        mMapView = (MapView) findViewById(R.id.mapquestMapView);
        mMapView.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        sourceString = bundle.getString("source");
        destinationString = bundle.getString("destination");

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;
                mMapView.setStreetMode();
                mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MAPQUEST_HEADQUARTERS_LOCATION, 12));
//                addMarker(mapboxMap);
//                searchLocation(mapboxMap);
                mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        Toast.makeText(MapquestActivity.this, marker.getTitle(), Toast.LENGTH_LONG).show();
                        return true;
                    }
                });

                mapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng point) {
                        // Already two locations
                        if (MarkerPoints.size() > 1) {
                            MarkerPoints.clear();
                            mMapboxMap.clear();
                        }

                        // Adding new item to the ArrayList
                        MarkerPoints.add(point);

                        // Creating MarkerOptions
                        MarkerOptions options = new MarkerOptions();

                        // Setting the position of the marker
                        options.position(point);

                        /**
                         * For the start location, the color of marker is GREEN and
                         * for the end location, the color of marker is RED.
                         */
//                        if (MarkerPoints.size() == 1) {
//                            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                        } else if (MarkerPoints.size() == 2) {
//                            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                        }


                        // Add new marker to the Google Map Android API V2
                        mMapboxMap.addMarker(options);

                        // Checks, whether start and end locations are captured
                        if (MarkerPoints.size() >= 2) {
                            LatLng origin = MarkerPoints.get(0);
                            LatLng dest = MarkerPoints.get(1);

                            Geocoder geocoder;
                            List<Address> sourceAddresses,destinationAddress;
                            geocoder = new Geocoder(MapquestActivity.this, Locale.getDefault());

                            try {
                                sourceAddresses = geocoder.getFromLocation(origin.getLatitude(), origin.getLongitude(), 1);
                                String address = sourceAddresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = sourceAddresses.get(0).getLocality();
                                String state = sourceAddresses.get(0).getAdminArea();
                                String country = sourceAddresses.get(0).getCountryName();
                                String postalCode = sourceAddresses.get(0).getPostalCode();
                                String knownName = sourceAddresses.get(0).getFeatureName();
                                sourceString=address+","+city+","+state+","+country+","+postalCode+","+knownName;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                destinationAddress = geocoder.getFromLocation(dest.getLatitude(), dest.getLongitude(), 1);
                                String address = destinationAddress.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = destinationAddress.get(0).getLocality();
                                String state = destinationAddress.get(0).getAdminArea();
                                String country = destinationAddress.get(0).getCountryName();
                                String postalCode = destinationAddress.get(0).getPostalCode();
                                String knownName = destinationAddress.get(0).getFeatureName();
                                destinationString=address+","+city+","+state+","+country+","+postalCode+","+knownName;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            // Getting URL to the Google Directions API
                            String url = getUrl(origin, dest);
//                            Log.d("onMapClick", url.toString());
                            FetchUrl FetchUrl = new FetchUrl();

                            // Start downloading json data from Google Directions API
                            FetchUrl.execute(url);
                            //move map camera
                            mMapboxMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                            mMapboxMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                        }


                    }

                });
            }
        });
    }

    private void addMarker(MapboxMap mapboxMap) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(MAPQUEST_HEADQUARTERS_LOCATION);
        markerOptions.title("MapQuest");
        markerOptions.snippet("Welcome to Denver!");
        mapboxMap.addMarker(markerOptions);
    }

    public void searchLocation(MapboxMap mMapboxMap) {
//        EditText locationSearch = (EditText) findViewById(R.id.editText);
//        String location = sourceString;
        List<Address> addressList = null;

        //source
        Geocoder geocoder = new Geocoder(this);
        try {
            addressList = geocoder.getFromLocationName(sourceString, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address = addressList.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        mMapboxMap.addMarker(new MarkerOptions().position(latLng).title(sourceString));
        mMapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        Toast.makeText(getApplicationContext(), address.getLatitude() + " " + address.getLongitude(), Toast.LENGTH_LONG).show();

        //destination
        try {
            addressList = geocoder.getFromLocationName(destinationString, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Address destinationaddress = addressList.get(0);
        LatLng destinationlatLng = new LatLng(destinationaddress.getLatitude(), destinationaddress.getLongitude());
        mMapboxMap.addMarker(new MarkerOptions().position(destinationlatLng).title(destinationString));
        mMapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        Toast.makeText(getApplicationContext(), destinationaddress.getLatitude() + " " + destinationaddress.getLongitude(), Toast.LENGTH_LONG).show();
        String url = getUrl(latLng, destinationlatLng);
        FetchUrl FetchUrl = new FetchUrl();

        // Start downloading json data from Google Directions API
        FetchUrl.execute(url);
    }


    private String getUrl(LatLng origin, LatLng dest) {

        Log.e("TAG", "address" + sourceString + "detination : " + destinationString);

// Origin of route
        String str_origin = "origin=" + origin.getLatitude() + "," + origin.getLatitude();

// Destination of route
        String str_dest = "destination=" + dest.getLatitude() + "," + dest.getLongitude();

// Sensor enabled
        String sensor = "sensor=false";

// Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

// Output format
        String output = "json";

        //key
        String key = "?key=26HrKEQBSOAk4GA2RMiz10VeZCLDkTzc";

        Log.e("TAG ", "getUrl: " + origin + "destination : " + dest);

// Building the url to the web service
        String url = "https://www.mapquestapi.com/directions/v2/route" + key + "&from=" + sourceString + "&to=" + destinationString + "&outFormat=json&ambiguities=check&routeType=fastest&doReverseGeocode=false&enhancedNarrative=false&avoidTimedConditions=false";
        Log.e("TAG", "getUrl: " + url);

        return url;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    //fetch class
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }


    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMapboxMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

    public class DataParser {

        /**
         * Receives a JSONObject and returns a list of lists containing latitude and longitude
         */
        public List<List<HashMap<String, String>>> parse(JSONObject jObject) {

            List<List<HashMap<String, String>>> routes = new ArrayList<>();
            JSONArray jRoutes;
            JSONArray jLegs;
            JSONArray jSteps;

            try {

                jRoutes = jObject.getJSONArray("collections");
                List path = new ArrayList<>();
                /** Traversing all routes */
                for (int i = 0; i < jRoutes.length(); i++) {
                    jLegs = (JSONArray) jRoutes.get(i);

                    Log.e("tag", "jLegs: " + jLegs.length());
                    /** Traversing all legs */
//                    for(int j=0;j<jLegs.length();j++){

//                        jSteps = (JSONObject) jLegs.get(j);
//                        Log.e("tag", "jSteps: "+jSteps.length());
                    /** Traversing all steps */
                    for (int k = 0; k < jLegs.length(); k++) {

                        String polyline = "";
                        polyline = ((JSONObject) ((JSONObject) jLegs.get(k)).get("displayLatLng")).get("lat").toString() + "," +
                                ((JSONObject) ((JSONObject) jLegs.get(k)).get("displayLatLng")).get("lng").toString();
                        String lat = ((JSONObject) ((JSONObject) jLegs.get(k)).get("displayLatLng")).get("lat").toString();
                        String lng = ((JSONObject) ((JSONObject) jLegs.get(k)).get("displayLatLng")).get("lng").toString();
                        Log.e("tag", "jLegs array: " + polyline);
//                            List<LatLng> list = new ArrayList<>();
//                            Log.e("TAG", "list: "+ list.size());
                        /** Traversing all points */
//                            for(int l=0;l<list.size();l++){
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("lat", lat);
                        hm.put("lng", lng);
                        path.add(hm);
//                            }
                    }
//                    }
                }
                routes.add(path);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("TAG", "parse: " + e.getLocalizedMessage());
            } catch (Exception e) {
                Log.e("TAG", "parse: " + e.getLocalizedMessage());
            }


            return routes;
        }


        /**
         * Method to decode polyline points
         * Courtesy : https://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
         */
        private List<LatLng> decodePoly(String encoded) {

            List<LatLng> poly = new ArrayList<>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }

            return poly;
        }
    }

}