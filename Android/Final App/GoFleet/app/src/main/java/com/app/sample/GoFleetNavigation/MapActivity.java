package com.app.sample.GoFleetNavigation;

import android.app.Application;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.sample.GoFleetNavigation.model.Order;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.BitmapFactory.decodeResource;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener
{
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private LocationComponent locationComponent;
    private MapboxNavigation mapboxNavigation;
    private NavigationMapRoute navigationMapRoute;

    private DirectionsRoute currentRoute;

    private SymbolManager symbolManager;
    private Symbol symbol;

    private Button startButton;

    private FloatingActionButton fab;

    private FloatingActionButton fab2;

    private Point originPoint;

    private Order orderinfo;

    public MapActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        orderinfo = (Order) getIntent().getSerializableExtra("orderObj");

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        fab = (FloatingActionButton) findViewById(R.id.fab2);

        mapboxNavigation= new MapboxNavigation(this, getString(R.string. mapbox_access_token));

        setContentView(R.layout.activity_map);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    public void StartNavigation(View v)
    {
        Intent intent = new Intent(MapActivity.this, WaypointNavigationActivity.class);
        intent.putExtra("origin",originPoint); //passes objects
        intent.putExtra("orderObj",orderinfo); //passes objects
        startActivity(intent);
        finish();

    }

    public void StartNavigationReal(View v)
    {

        Intent intent = new Intent(MapActivity.this, WayPointNaviagtionRealStart.class);
        intent.putExtra("origin",originPoint); //passes objects
        intent.putExtra("orderObj",orderinfo); //passes objects
        startActivity(intent);
        finish();

    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        MapActivity.this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(getString(R.string.navigation_guidance_day), new Style.OnStyleLoaded()
        {
            @Override
            public void onStyleLoaded(@NonNull Style style)
            {
                addDestinationIconLayer(style);
                //enableLocationComponent(style);
                Route();
            }
        });
    }

    //Adding destination marker
    public void addDestinationIconLayer(Style style) {
        List<Feature> markerCoordinates = new ArrayList<>();

//
//        Application application = this.getApplication();
//        ModelApi modelApi = ModelApi.getInstance(application);
//        final List<Order> ordersLoad = modelApi.getOrders();

        originPoint = Point.fromLngLat(28.191817,-25.925063);

//        if(Integer.parseInt(orderinfo.getSequenceNumber()) == 1)
//        {
//            originPoint = Point.fromLngLat(28.191817,-25.925063);
//        }
//        else if(Integer.parseInt(orderinfo.getSequenceNumber()) > 1)
//        {
//            int ref = Integer.parseInt(orderinfo.getSequenceNumber()); // 2 == 1
//            originPoint = Point.fromLngLat(ordersLoad.get(ref).getLongitude(),ordersLoad.get(ref).getLatitude());
//        }

        style.addImage("destination-icon-id", BitmapFactory.decodeResource(MapActivity.this.getResources(), R.drawable.mapbox_marker_icon_default));

        style.addSource(new GeoJsonSource("destination-source-id", FeatureCollection.fromFeatures(markerCoordinates)));

        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id","destination-source-id");

        destinationSymbolLayer.withProperties(iconImage("destination-icon-id"),iconAllowOverlap(true),iconIgnorePlacement(true));

        style.addLayer(destinationSymbolLayer);


    }

        public void Route()
    {

      //  locationComponent = mapboxMap.getLocationComponent();

    //    Point destinationPoint = Point.fromLngLat(28.082472,-26.200667);  mock

        Point destinationPoint = Point.fromLngLat(orderinfo.getLongitude(),orderinfo.getLatitude());

     //   originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),locationComponent.getLastKnownLocation().getLatitude()); for real location

      //  originPoint = Point.fromLngLat(28.077869415283203,-26.19801902770996); mock

        originPoint = Point.fromLngLat(28.191817,-25.925063);

//        Application application = this.getApplication();
//        ModelApi modelApi = ModelApi.getInstance(application);
//        final List<Order> ordersLoad = modelApi.getOrders();
//
//        if(Integer.parseInt(orderinfo.getSequenceNumber()) == 1)
//        {
//            originPoint = Point.fromLngLat(28.191817,-25.925063);
//        }
//        else if(Integer.parseInt(orderinfo.getSequenceNumber()) > 1)
//        {
//            int ref = Integer.parseInt(orderinfo.getSequenceNumber()); // 2 == 1
//            originPoint = Point.fromLngLat(ordersLoad.get(ref).getLongitude(),ordersLoad.get(ref).getLatitude());
//        }


        GeoJsonSource source = mapboxMap.getStyle().getSourceAs("destination-source-id");

        if(source != null)
        {
            source.setGeoJson(Feature.fromGeometry(destinationPoint));
        }

       // Log.i("Access token ", Mapbox.getAccessToken());

        NavigationRoute.builder(this)
                       .accessToken(Mapbox.getAccessToken())
                       .origin(originPoint)
                       .destination(destinationPoint)
                       .build()
                       .getRoute(new Callback<DirectionsResponse>() {
                           @Override
                           public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response)
                           {

                                // Route fetched from NavigationRoute
                               currentRoute = response.body().routes().get(0);

                               // Draw the route on the map
                               if (navigationMapRoute != null)
                               {
                                   navigationMapRoute.removeRoute();
                               }
                               else
                               {
                                   navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                               }

                               navigationMapRoute.addRoute(currentRoute);

                           }

                           @Override
                           public void onFailure(Call<DirectionsResponse> call, Throwable t)
                           {
                                    
                           }
                       });

    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            locationComponent = mapboxMap.getLocationComponent();

            // Activate with options
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        }
        else
        {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain)
    {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted)
    {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        permissionsManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mapView.onDestroy();
        mapboxNavigation.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i = null;
        i = new Intent(this, ActivityOrderDetails.class);
        startActivity(i);
    }


}
