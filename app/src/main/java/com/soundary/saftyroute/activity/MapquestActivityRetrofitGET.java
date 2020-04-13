package com.soundary.saftyroute.activity;


import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapquest.mapping.MapQuest;
import com.mapquest.mapping.maps.MapView;
import com.soundary.saftyroute.R;
import com.soundary.saftyroute.pojo.AlternateRoute;
import com.soundary.saftyroute.pojo.AlternateRoutesResponse;
import com.soundary.saftyroute.pojo.Leg;
import com.soundary.saftyroute.pojo.Leg_;
import com.soundary.saftyroute.pojo.Maneuver;
import com.soundary.saftyroute.pojo.Maneuver_;
import com.soundary.saftyroute.pojo.RequestLocation;
import com.soundary.saftyroute.pojo.Shape;
import com.soundary.saftyroute.retrofit.GetDataService;
import com.soundary.saftyroute.retrofit.RetrofitClientInstance;

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
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapquestActivityRetrofitGET extends AppCompatActivity {

    private MapView mMapView;
    private MapboxMap mMapboxMap;
    private final LatLng MAPQUEST_HEADQUARTERS_LOCATION = new LatLng(10.969957,76.956184);
    ArrayList<LatLng> dangerPoints = new ArrayList<>();
    String sourceString,destinationString;
    private ArrayList<LatLng> MarkerPoints=new ArrayList<>();
    private String TAG=MapquestActivityRetrofitGET.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapQuest.start(getApplicationContext());
        setContentView(R.layout.activity_mapquest);
        LatLng gopalapuram=new LatLng(10.992527,76.951492);
        LatLng vgcentre=new LatLng(11.017129,76.96492);
        dangerPoints.add(MAPQUEST_HEADQUARTERS_LOCATION);
        dangerPoints.add(gopalapuram);
        dangerPoints.add(vgcentre);
        mMapView = (MapView) findViewById(R.id.mapquestMapView);
        mMapView.onCreate(savedInstanceState);

        Bundle bundle=getIntent().getExtras();
        sourceString=bundle.getString("source");
        destinationString=bundle.getString("destination");

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;
                mMapView.setStreetMode();
                mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MAPQUEST_HEADQUARTERS_LOCATION, 13));
                searchLocation(mapboxMap);
                mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        Toast.makeText(MapquestActivityRetrofitGET.this, marker.getTitle(), Toast.LENGTH_LONG).show();
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
                            geocoder = new Geocoder(MapquestActivityRetrofitGET.this, Locale.getDefault());

                            try {
                                sourceAddresses = geocoder.getFromLocation(origin.getLatitude(), origin.getLongitude(), 1);
                                String address = sourceAddresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = sourceAddresses.get(0).getLocality();
                                String state = sourceAddresses.get(0).getAdminArea();
                                String country = sourceAddresses.get(0).getCountryName();
                                String postalCode = sourceAddresses.get(0).getPostalCode();
                                String knownName = sourceAddresses.get(0).getFeatureName();
//                                sourceString=address+","+city+","+state+","+country+","+postalCode+","+knownName;
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
//                                destinationString=address+","+city+","+state+","+country+","+postalCode+","+knownName;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            List<String> location=new ArrayList<>();
                            String str_origin = origin.getLatitude()+","+origin.getLongitude();
//                            String str_origin = "11.001355"+","+"76.95993763494403";
// Destination of route
                            String str_dest = dest.getLatitude()+","+dest.getLongitude();
//                            String str_dest = "11.009758422710263"+","+ "76.94394936132437";
                            location.add(str_origin);
                            location.add(str_dest);
                            JSONArray jsArray = new JSONArray();
                            for (int i = 0; i < location.size(); i++) {
                                jsArray.put(location.get(i));
                            };
                            Log.e(TAG, "onMapClick: location : "+ jsArray );

                            RequestLocation requestLocation=new RequestLocation(location,3,100);
                            Log.e(TAG, "onMapClick: requestLocation : "+new Gson().toJson(requestLocation ));


                            /*Create handle for the RetrofitInstance interface*/
                            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                            Call<AlternateRoutesResponse> call = service.getRoute("42K0L9WTmAuyBS6L8jD8gadFz5KjW3Yu",str_origin,str_dest,
                                    "json","check","fastest",3,25,false,false,
                                    false,"M");
                            Log.e(TAG, "onMapClick: "+call.request() );
                            call.enqueue(new Callback<AlternateRoutesResponse>() {
                                @Override
                                public void onResponse(Call<AlternateRoutesResponse> call, Response<AlternateRoutesResponse> response) {


                                    try {
                                        List<AlternateRoute> alternateRouteList= response.body().getRoute().getAlternateRoutes();
//                                        ArrayList<LatLng> points=new ArrayList<>();
                                        PolylineOptions lineOptions = null;
                                        Log.e(TAG, "onResponse: "+ response.body().getRoute().getAlternateRoutes());
                                    if (alternateRouteList!=null) {
                                        for (AlternateRoute alternateRoute : alternateRouteList) {
                                            List path = new ArrayList<>();
                                            ArrayList<LatLng> points = new ArrayList<>();
                                            lineOptions = new PolylineOptions();
                                            List<Leg_> leg_list = alternateRoute.getRoute().getLegs();
                                            for (Leg_ leg_ : leg_list) {
                                                List<Maneuver_> maneuver_list = leg_.getManeuvers();
                                                for (Maneuver_ maneuver : maneuver_list) {

                                                    List<LatLng> list = new ArrayList<>();
                                                    LatLng latLng = new LatLng(maneuver.getStartPoint().getLat(), maneuver.getStartPoint().getLng());
                                                    Log.e(TAG, "onResponse: distance to :  " + latLng.distanceTo(MAPQUEST_HEADQUARTERS_LOCATION));
                                                    list.add(latLng);
                                                    points.add(latLng);
                                                    // Adding all the points in the route to LineOptions
//                                                    lineOptions.addAll(list);
//                                                    lineOptions.width(10);
//                                                    lineOptions.color(Color.RED);
                                                    /** Traversing all points */
//                                                    for(int l=0;l<list.size();l++){
//                                                        HashMap<String, String> hm = new HashMap<>();
//                                                        hm.put("lat", Double.toString((list.get(l)).getLatitude()) );
//                                                        hm.put("lng", Double.toString((list.get(l)).getLatitude()) );
//                                                        path.add(hm);
//                                                    }
                                                }
                                            }
                                            Random rand = new Random();
                                            // Java 'Color' class takes 3 floats, from 0 to 1.
                                            Log.e(TAG, "parse: latLng : " + points);
                                            float r = rand.nextFloat();
                                            float g = rand.nextFloat();
                                            float b = rand.nextFloat();
                                            float a = rand.nextFloat();
                                            int colors = 0;
                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                colors = Color.rgb(r, g, b);
                                            }else{
                                                colors = Color.GREEN;
                                            }
                                            lineOptions.addAll(points);
                                            lineOptions.width(10);
                                            lineOptions.color(colors);
                                            // Drawing polyline in the Google Map for the i-th route
                                            if (lineOptions != null) {
                                                mMapboxMap.addPolyline(lineOptions);
                                            } else {
                                                Log.d("onPostExecute", "without Polylines drawn");

                                            }
//                                            break;
                                        }
                                    }else {
                                        Log.e(TAG, "onResponse: route : "+ response.body().getRoute());
                                        List path = new ArrayList<>();
                                        ArrayList<LatLng> points = new ArrayList<>();
                                        lineOptions = new PolylineOptions();
                                        List<Leg> leg_list = response.body().getRoute().getLegs();
                                        for (Leg leg_ : leg_list) {
                                            List<Maneuver> maneuver_list = leg_.getManeuvers();
                                            for (Maneuver maneuver : maneuver_list) {

                                                List<LatLng> list = new ArrayList<>();
                                                LatLng latLng = new LatLng(maneuver.getStartPoint().getLat(), maneuver.getStartPoint().getLng());
                                                Log.e(TAG, "onResponse: distance to :  " + latLng.distanceTo(MAPQUEST_HEADQUARTERS_LOCATION));
                                                list.add(latLng);
                                                points.add(latLng);
                                                // Adding all the points in the route to LineOptions
//                                                    lineOptions.addAll(list);
//                                                    lineOptions.width(10);
//                                                    lineOptions.color(Color.RED);
                                                /** Traversing all points */
//                                                    for(int l=0;l<list.size();l++){
//                                                        HashMap<String, String> hm = new HashMap<>();
//                                                        hm.put("lat", Double.toString((list.get(l)).getLatitude()) );
//                                                        hm.put("lng", Double.toString((list.get(l)).getLatitude()) );
//                                                        path.add(hm);
//                                                    }
                                            }
                                        }
                                        Random rand = new Random();
                                        // Java 'Color' class takes 3 floats, from 0 to 1.
                                        Log.e(TAG, "parse: latLng : " + points);
                                        float r = rand.nextFloat();
                                        float g = rand.nextFloat();
                                        float b = rand.nextFloat();
                                        float a = rand.nextFloat();
                                        int colors = 0;
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                            colors = Color.rgb(r, g, b);
                                        }else {
                                            colors = Color.GREEN;
                                        }
                                        lineOptions.addAll(points);
                                        lineOptions.width(10);
                                        lineOptions.color(colors);
                                        // Drawing polyline in the Google Map for the i-th route
                                        if (lineOptions != null) {
                                            mMapboxMap.addPolyline(lineOptions);
                                        } else {
                                            Log.d("onPostExecute", "without Polylines drawn");

                                        }
                                    }

//
//                                        // Traversing through all the routes
//                                        for (int i = 0; i < result.size(); i++) {
//                                            points = new ArrayList<>();
//                                            lineOptions = new PolylineOptions();
//
//                                            // Fetching i-th route
//                                            List<HashMap<String, String>> path = result.get(i);
//
//                                            // Fetching all the points in i-th route
//                                            for (int j = 0; j < path.size(); j++) {
//                                                HashMap<String, String> point = path.get(j);
//
//                                                double lat = Double.parseDouble(point.get("lat"));
//                                                double lng = Double.parseDouble(point.get("lng"));
//                                                LatLng position = new LatLng(lat, lng);
//
//                                                points.add(position);
//                                            }
//
//                                            // Adding all the points in the route to LineOptions
//                                            lineOptions.addAll(points);
//                                            lineOptions.width(10);
//                                            lineOptions.color(Color.RED);
//
//                                            Log.d("onPostExecute","onPostExecute lineoptions decoded");
//
//                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.e(TAG, "parse: "+e.getLocalizedMessage() );
                                    }

                                }

                                @Override
                                public void onFailure(Call<AlternateRoutesResponse> call, Throwable t) {
                                    Log.e(TAG, "onFailure: "+t.getLocalizedMessage() );
                                    Toast.makeText(MapquestActivityRetrofitGET.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                                }
                            });

//                            // Getting URL to the Google Directions API
//                            String url = getUrl(origin, dest);
////                            Log.d("onMapClick", url.toString());
//                            FetchUrl FetchUrl = new FetchUrl();
//
//                            // Start downloading json data from Google Directions API
//                            FetchUrl.execute(url);
                            //move map camera
                            mMapboxMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                            mMapboxMap.animateCamera(CameraUpdateFactory.zoomTo(13));
                        }


                    }

                });
            }
        });
    }

    private void addMarker(MapboxMap mapboxMap,LatLng MAPQUEST_HEADQUARTERS_LOCATION) {
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
        mMapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        Toast.makeText(getApplicationContext(),address.getLatitude()+" "+address.getLongitude(),Toast.LENGTH_LONG).show();

        //destination
        try {
            addressList = geocoder.getFromLocationName(destinationString, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Address destinationaddress = addressList.get(0);
        LatLng destinationlatLng = new LatLng(destinationaddress.getLatitude(), destinationaddress.getLongitude());
        mMapboxMap.addMarker(new MarkerOptions().position(destinationlatLng).title(destinationString));
        mMapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
//        Toast.makeText(getApplicationContext(),destinationaddress.getLatitude()+" "+destinationaddress.getLongitude(),Toast.LENGTH_LONG).show();
//        String url = getUrl(latLng, destinationlatLng);
//        FetchUrl FetchUrl = new FetchUrl();

        // Start downloading json data from Google Directions API
//        FetchUrl.execute(url);



        List<String> location=new ArrayList<>();
        String str_origin = latLng.getLatitude()+","+latLng.getLongitude();
//                String str_origin =  "Denver, CO";
//                            String str_origin = "10.994793199685589"+","+"76.95993763494403";
// Destination of route
        String str_dest = destinationlatLng.getLatitude()+","+destinationlatLng.getLongitude();
//                String str_dest =   "Golden, CO";
//                            String str_dest = "11.009758422710263"+","+ "76.94394936132437";
        location.add(str_origin);
        location.add(str_dest);
        JSONArray jsArray = new JSONArray();
        for (int i = 0; i < location.size(); i++) {
            jsArray.put(location.get(i));
        }
        Log.e(TAG, "onMapClick: location : "+ jsArray );

        RequestLocation requestLocation=new RequestLocation(location,3,100);
        Log.e(TAG, "onMapClick: requestLocation : "+new Gson().toJson(requestLocation) );


        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<AlternateRoutesResponse> call = service.getRoute("42K0L9WTmAuyBS6L8jD8gadFz5KjW3Yu",str_origin,str_dest,
                "json","ignore","fastest",3,25,false,false,
                false,"M");
        Log.e(TAG, "onMapClick: "+call.request() );
        call.enqueue(new Callback<AlternateRoutesResponse>() {
            @Override
            public void onResponse(Call<AlternateRoutesResponse> call, Response<AlternateRoutesResponse> response) {


                try {
                    List<AlternateRoute> alternateRouteList= response.body().getRoute().getAlternateRoutes();
//                                        ArrayList<LatLng> points=new ArrayList<>();
                    PolylineOptions lineOptions = null;
                    PolygonOptions polygon = new PolygonOptions();
                    ArrayList<Point> routeCoordinates = new ArrayList<Point>();

                    Log.e(TAG, "onResponse: "+ new Gson().toJson(alternateRouteList));
                    System.out.println(new Gson().toJson(response.body()));
                    if (alternateRouteList!=null) {
                        for (AlternateRoute alternateRoute : alternateRouteList) {
                            List path = new ArrayList<>();
                            ArrayList<LatLng> points = new ArrayList<>();
                            Random rand = new Random();
                            // Java 'Color' class takes 3 floats, from 0 to 1.

                            float r = rand.nextFloat();
                            float g = rand.nextFloat();
                            float b = rand.nextFloat();
                            float a = rand.nextFloat();
                            int colors = 0;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                colors = Color.rgb(r, g, b);
                            }else {
                                colors = Color.GREEN;
                            }
//                            int dangerColor;
                            lineOptions = new PolylineOptions();
                            List<Double> shapePoints = alternateRoute.getRoute().getShape().getShapePoints();
                            // get every other shape point
                            int pointcount = shapePoints.size() / 2;

                            // create a shape point list


                            // fill list with every even value as lat and odd value as lng
                            for (int point = 0; point < pointcount; point = point + 1) {
                                points.add(new LatLng(
                                        (double) shapePoints.get(point * 2),
                                        (double) shapePoints.get(point * 2 + 1)
                                ));
                                LatLng latLng = new LatLng((double) shapePoints.get(point * 2),(double) shapePoints.get(point * 2 + 1));
                                for (LatLng dpoint: dangerPoints) {
                                    int distance = (int) latLng.distanceTo(dpoint);
                                    if (distance == 0) {
                                        addMarker(mMapboxMap, latLng);
                                        colors = Color.RED;
                                    }
                                }
                            }


//                            List<Leg_> leg_list = alternateRoute.getRoute().getLegs();
//                            for (Leg_ leg_ : leg_list) {
//                                List<Maneuver_> maneuver_list = leg_.getManeuvers();
//                                for (Maneuver_ maneuver : maneuver_list) {
//
//                                    List<LatLng> list = new ArrayList<>();
//                                    LatLng latLng = new LatLng(maneuver.getStartPoint().getLat(), maneuver.getStartPoint().getLng());
//                                    Log.e(TAG, "onResponse latLng : "+ latLng);
////                                    list.add(latLng);
////                                    points.add(latLng);
//                                    routeCoordinates.add(Point.fromLngLat(maneuver.getStartPoint().getLng(),maneuver.getStartPoint().getLat()));
////                                    Log.e(TAG, "onResponse: distance to :  " + (int)latLng.distanceTo(MAPQUEST_HEADQUARTERS_LOCATION));
////                                    int distance= (int)latLng.distanceTo(MAPQUEST_HEADQUARTERS_LOCATION);
////                                    if(distance==0){
////                                        colors=Color.RED;
////                                    }
//                                    // Adding all the points in the route to LineOptions
////                                                    lineOptions.addAll(list);
////                                                    lineOptions.width(10);
////                                                    lineOptions.color(Color.RED);
//                                    /** Traversing all points */
////                                                    for(int l=0;l<list.size();l++){
////                                                        HashMap<String, String> hm = new HashMap<>();
////                                                        hm.put("lat", Double.toString((list.get(l)).getLatitude()) );
////                                                        hm.put("lng", Double.toString((list.get(l)).getLatitude()) );
////                                                        path.add(hm);
////                                                    }
//                                }
//                            }
                            Log.e(TAG, "parse: latLng : " + points);
                            lineOptions.addAll(points);
                            lineOptions.width(5);
                            lineOptions.color(colors);
                            mMapboxMap.addPolyline(lineOptions);
                            polygon.addAll(points);
                            polygon.fillColor(colors);
                            polygon.strokeColor(Color.BLACK);

                            // Drawing polyline in the Google Map for the i-th route
//                            if (polygon != null) {
//                                mMapboxMap.addPolygon(polygon);
//                            } else {
//                                Log.d("onPostExecute", "without Polylines drawn");
//
//                            }
//                                            break;
                        }
                    }else {
//                        Log.e(TAG, "onResponse: "+ new Gson().toJson(response.body().getRoute()));
                        List path = new ArrayList<>();
                        ArrayList<LatLng> points = new ArrayList<>();
                        lineOptions = new PolylineOptions();

                        List<Double> shapePoints = response.body().getRoute().getShape().getShapePoints();
                        Log.e(TAG, "onResponse: shapePoints "+new Gson().toJson(shapePoints) );
                        // get every other shape point
                        int pointcount = shapePoints.size() / 2;
//                        Log.e(TAG, "onResponse: pointcount "+pointcount );
                        // create a shape point list
                        Random rand = new Random();
                        // Java 'Color' class takes 3 floats, from 0 to 1.
//                        Log.e(TAG, "parse: latLng : " + points);
                        float r = rand.nextFloat();
                        float g = rand.nextFloat();
                        float b = rand.nextFloat();
                        float a = rand.nextFloat();
                        int colors = 0;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            colors = Color.rgb(r, g, b);
                        }else {
                            colors = Color.GREEN;
                        }

                        // fill list with every even value as lat and odd value as lng
                        for (int point = 0; point < pointcount; point = point + 1) {
                            Log.e(TAG, "onResponse: point : "+point );
                            points.add(new LatLng(
                                    (double) shapePoints.get(point * 2),
                                    (double) shapePoints.get(point * 2 + 1)
                            ));
                            LatLng latLng = new LatLng((double) shapePoints.get(point * 2),(double) shapePoints.get(point * 2 + 1));
                            for (LatLng dpoint: dangerPoints) {
                                int distance = (int) latLng.distanceTo(dpoint);
                                Log.e(TAG, "onResponse: distance : " + distance);
                                if (distance == 0) {
                                    addMarker(mMapboxMap, latLng);
                                    colors = Color.RED;
                                }
                            }
                            routeCoordinates.add(Point.fromLngLat((double) shapePoints.get(point * 2 + 1),(double) shapePoints.get(point * 2)));
                        }

//                        List<Leg> leg_list = response.body().getRoute().getLegs();
//                        for (Leg leg_ : leg_list) {
//                            List<Maneuver> maneuver_list = leg_.getManeuvers();
//                            for (Maneuver maneuver : maneuver_list) {
//
//                                List<LatLng> list = new ArrayList<>();
//                                LatLng latLng = new LatLng(maneuver.getStartPoint().getLat(), maneuver.getStartPoint().getLng());
//                                Log.e(TAG, "onResponse latLng : "+ latLng);
////                                list.add(latLng);
////                                points.add(latLng);
//                                routeCoordinates.add(Point.fromLngLat(maneuver.getStartPoint().getLng(),maneuver.getStartPoint().getLat()));
////                                Log.e(TAG, "onResponse: distance to :  " + latLng.distanceTo(MAPQUEST_HEADQUARTERS_LOCATION));
//
//                                // Adding all the points in the route to LineOptions
////                                                    lineOptions.addAll(list);
////                                                    lineOptions.width(10);
////                                                    lineOptions.color(Color.RED);
//                                /** Traversing all points */
////                                                    for(int l=0;l<list.size();l++){
////                                                        HashMap<String, String> hm = new HashMap<>();
////                                                        hm.put("lat", Double.toString((list.get(l)).getLatitude()) );
////                                                        hm.put("lng", Double.toString((list.get(l)).getLatitude()) );
////                                                        path.add(hm);
////                                                    }
//                            }
//                        }

                        lineOptions.addAll(points);
                        lineOptions.width(5);
                        lineOptions.color(colors);
                        mMapboxMap.addPolyline(lineOptions);

                        polygon.addAll(points);
                        polygon.fillColor(colors);
                        polygon.strokeColor(colors);


                        // Create the LineString from the list of coordinates and then make a GeoJSON
// FeatureCollection so we can add the line to our map as a layer.
//                        LineString lineString = LineString.fromLngLats(routeCoordinates);
//
//                        FeatureCollection featureCollection =
//                                FeatureCollection.fromFeatures(new Feature[]{Feature.fromGeometry(lineString)});
//
//                        Source geoJsonSource = new GeoJsonSource("line-source", featureCollection);
//
//                        mMapboxMap.addSource(geoJsonSource);

//                        LineLayer lineLayer = new LineLayer("linelayer", "line-source");


// The layer properties for our line. This is where we make the line red, set its width, etc
//                        lineLayer.setProperties(
//                                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
//                                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
//                                PropertyFactory.lineWidth(2f),
//                                PropertyFactory.lineColor(Color.parseColor("#f20b0d"))
//                        );
////
//                        mMapboxMap.addLayer(lineLayer);

//                        // Drawing polyline in the Google Map for the i-th route
//                        if (polygon != null) {
//                            mMapboxMap.addPolygon(polygon);
//                        } else {
//                            Log.d("onPostExecute", "without Polylines drawn");
//
//                        }
                    }

//
//                                        // Traversing through all the routes
//                                        for (int i = 0; i < result.size(); i++) {
//                                            points = new ArrayList<>();
//                                            lineOptions = new PolylineOptions();
//
//                                            // Fetching i-th route
//                                            List<HashMap<String, String>> path = result.get(i);
//
//                                            // Fetching all the points in i-th route
//                                            for (int j = 0; j < path.size(); j++) {
//                                                HashMap<String, String> point = path.get(j);
//
//                                                double lat = Double.parseDouble(point.get("lat"));
//                                                double lng = Double.parseDouble(point.get("lng"));
//                                                LatLng position = new LatLng(lat, lng);
//
//                                                points.add(position);
//                                            }
//
//                                            // Adding all the points in the route to LineOptions
//                                            lineOptions.addAll(points);
//                                            lineOptions.width(10);
//                                            lineOptions.color(Color.RED);
//
//                                            Log.d("onPostExecute","onPostExecute lineoptions decoded");
//
//                                        }
//                    mMapboxMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                    mMapboxMap.animateCamera(CameraUpdateFactory.zoomTo(13));

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "parse: "+e.getLocalizedMessage() );
                }

            }

            @Override
            public void onFailure(Call<AlternateRoutesResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getLocalizedMessage() );
                Toast.makeText(MapquestActivityRetrofitGET.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private String getUrl(LatLng origin, LatLng dest){

        Log.e("TAG","address" + sourceString + "detination : " + destinationString);

// Origin of route
        String str_origin = "origin="+origin.getLatitude()+"%2C"+origin.getLatitude();

// Destination of route
        String str_dest = "destination="+dest.getLatitude()+"%2C"+dest.getLongitude();

// Sensor enabled
        String sensor = "sensor=false";

// Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

// Output format
        String output = "json";

        //key
        String key="?key=42K0L9WTmAuyBS6L8jD8gadFz5KjW3Yu";

// Building the url to the web service
        String url = "https://www.mapquestapi.com/directions/v2/alternateroutes"+key+"&from="+str_origin+"&to="+str_dest+"&outFormat=json&ambiguities=ignore&routeType=fastest&maxRoutes=3&timeOverage=25&doReverseGeocode=false&enhancedNarrative=false&avoidTimedConditions=false&unit=M";
        Log.e(TAG, "getUrl: "+url );
        return url;
    }

    @Override
    public void onResume()
    { super.onResume();
    mMapView.onResume();
    }

    @Override
    public void onPause()
    { super.onPause();
    mMapView.onPause();
    }

    @Override
    protected void onDestroy()
    { super.onDestroy();
    mMapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    { super.onSaveInstanceState(outState);
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
            Log.d("Exception", e.getLocalizedMessage());
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
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.getLocalizedMessage());
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

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                mMapboxMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");

            }
        }
    }

    public class DataParser {

        /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
        public List<List<HashMap<String,String>>> parse(JSONObject jObject){

            List<List<HashMap<String, String>>> routes = new ArrayList<>() ;
            JSONArray jRoutes;
            JSONArray jLegs;
            JSONArray jSteps;


            try {

                jRoutes = jObject.getJSONArray("route");
                Log.e(TAG, "jRoutes: "+jRoutes );
                /** Traversing all routes */
                for(int i=0;i<jRoutes.length();i++){
                    jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                    List path = new ArrayList<>();

                    /** Traversing all legs */
                    for(int j=0;j<jLegs.length();j++){
                        jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("maneuvers");

                        /** Traversing all steps */
                        for(int k=0;k<jSteps.length();k++){
                            JSONObject polyline ;
                            polyline = (JSONObject) ((JSONObject)jSteps.get(k)).get("startPoint");
                            Log.e(TAG, "parse: polyline : "+polyline );
//                            List<LatLng> list = decodePoly(polyline);
                            List<LatLng> list = new ArrayList<>();
                            LatLng latLng=new LatLng(polyline.getDouble("lat"),polyline.getDouble("lng"));
                            Log.e(TAG, "parse: latLng : "+latLng );
                            list.add(latLng);
                            /** Traversing all points */
                            for(int l=0;l<list.size();l++){
                                HashMap<String, String> hm = new HashMap<>();
                                hm.put("lat", Double.toString((list.get(l)).getLatitude()) );
                                hm.put("lng", Double.toString((list.get(l)).getLatitude()) );
                                path.add(hm);
                            }
                        }
                        Log.e(TAG, "parse: "+path );
                        routes.add(path);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "parse: "+e.getLocalizedMessage() );
            }catch (Exception e){
                Log.e(TAG, "parse: "+e.getLocalizedMessage() );
            }


            return routes;
        }


        /**
         * Method to decode polyline points
         * Courtesy : https://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
         * */
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
