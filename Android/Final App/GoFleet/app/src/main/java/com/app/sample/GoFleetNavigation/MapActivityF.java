package com.app.sample.GoFleetNavigation;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.sample.GoFleetNavigation.R;
import com.app.sample.GoFleetNavigation.model.Order;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;

/**
 * Use Mapbox Java Services to request directions from the Mapbox Directions API and show the
 * route with a LineLayer.
 */
public class MapActivityF extends AppCompatActivity
{

    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_SOURCE_ID = "route-source-id";
    private static final String ICON_LAYER_ID = "icon-layer-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";
    private MapView mapView;
    private DirectionsRoute currentRoute;
    private MapboxDirections client;
    private Point origin;
    private Point destination;

    private List<Order> ordersLoad;
    private Order orderinfo;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        orderinfo = (Order) getIntent().getSerializableExtra("orderObj");

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_map);

        Application application = this.getApplication();
        ModelApi modelApi = ModelApi.getInstance(application);
        ordersLoad = modelApi.getOrders();

        final int[] ref = {0};

        // Setup the MapView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style)
                    {
                        // Set the origin location to the Alhambra landmark in Granada, Spain.
                        //origin = Point.fromLngLat(28.1918175, -25.925063);

                        if(Integer.parseInt(orderinfo.getSequenceNumber()) == 1)
                        {
                            origin = Point.fromLngLat(28.191817,-25.925063);
                        }
                        else if(Integer.parseInt(orderinfo.getSequenceNumber()) > 1)
                        {
                            ref[0] = Integer.parseInt(orderinfo.getSequenceNumber());
                            origin = Point.fromLngLat(ordersLoad.get(ref[0] -2).getLongitude(),ordersLoad.get(ref[0] -2).getLatitude());
                        }

                        // Set the destination location to the Plaza del Triunfo in Granada, Spain.
                        if(Integer.parseInt(orderinfo.getSequenceNumber()) == 1)
                        {
                            destination = Point.fromLngLat(ordersLoad.get(0).getLongitude(), ordersLoad.get(0).getLatitude());
                        }
                        else
                        {
                            destination = Point.fromLngLat(ordersLoad.get(ref[0]-1).getLongitude(), ordersLoad.get(ref[0]-1).getLatitude());
                        }



                        initSource(style);

                        initLayers(style);

                        // Get the directions route from the Mapbox Directions API
                        getRoute(mapboxMap, origin, destination);
                    }
                });
            }
        });
    }

    public void StartNavigation(View v)
    {
        Intent intent = new Intent(MapActivityF.this, WaypointNavigationActivity.class);
        intent.putExtra("origin",origin); //passes objects
        intent.putExtra("orderObj",orderinfo); //passes objects
        startActivity(intent);
        finish();

    }

    public void StartNavigationReal(View v)
    {

        Intent intent = new Intent(MapActivityF.this, WayPointNaviagtionRealStart.class);
        intent.putExtra("origin",origin); //passes objects
        intent.putExtra("orderObj",orderinfo); //passes objects
        startActivity(intent);
        finish();

    }

    /**
     * Add the route and marker sources to the map
     */
    private void initSource(@NonNull Style loadedMapStyle)
    {
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID));

        GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[] {
                Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude()))}));
        loadedMapStyle.addSource(iconGeoJsonSource);
    }

    private static final String ICON_GEOJSON_SOURCEE_ID = "icon-sourcee-id";

    private static final String ICON_GEOJSON_HOMME_ID = "icon-homee-id";

    /**
     * Add the route and marker icon layers to the map
     */
    private void initLayers(@NonNull Style loadedMapStyle) {
        LineLayer routeLayer = new LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID);

        // Add the LineLayer to the map. This layer will display the directions route.
        routeLayer.setProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(5f),
                lineColor(Color.parseColor("#FF6FE80C"))
        );
        loadedMapStyle.addLayer(routeLayer);

//        // Add the red marker icon image to the map
//        loadedMapStyle.addImage(RED_PIN_ICON_ID, BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.mapbox_marker_icon_default)));
//
//        // Add the red marker icon SymbolLayer to the map
//        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(iconImage(RED_PIN_ICON_ID)));

        loadedMapStyle.addImage("icon-image", BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));

        loadedMapStyle.addImage("icon-image-start", BitmapFactory.decodeResource(this.getResources(), R.drawable.map_marker_dark));


        // Add the source to the map
        loadedMapStyle.addSource(new GeoJsonSource(ICON_GEOJSON_SOURCEE_ID, Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude()))));
        loadedMapStyle.addLayer(new SymbolLayer("icon-layer-id", ICON_GEOJSON_SOURCEE_ID).withProperties(
                iconImage("icon-image"),
                iconSize(1.5f),
                iconAllowOverlap(true),
                iconIgnorePlacement(true),
                textField("Start"),
                iconOffset(new Float[] {0f, -7f})
        ));

        loadedMapStyle.addSource(new GeoJsonSource(ICON_GEOJSON_HOMME_ID, Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude()))));

        loadedMapStyle.addLayer(new SymbolLayer("icon-home-id", ICON_GEOJSON_HOMME_ID).withProperties(
                iconImage("icon-image-start"),
                iconSize(1f),
                iconAllowOverlap(true),
                iconIgnorePlacement(true),
                textField("Destination"),
                iconOffset(new Float[] {0f, -7f})
        ));



    }

    /**
     * Make a request to the Mapbox Directions API. Once successful, pass the route to the
     * route layer.
     * @param mapboxMap the Mapbox map object that the route will be drawn on
     * @param origin      the starting point of the route
     * @param destination the desired finish point of the route
     */
    private void getRoute(MapboxMap mapboxMap, Point origin, Point destination) {
        client = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(getString(R.string.mapbox_access_token))
                .build();

        client.enqueueCall(new Callback<DirectionsResponse>() {
            @SuppressLint("StringFormatInvalid")
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
// You can get the generic HTTP info about the response
                Timber.d("Response code: " + response.code());
                if (response.body() == null) {
                    Timber.e("No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().routes().size() < 1) {
                    Timber.e("No routes found");
                    return;
                }

                    // Get the directions route
                currentRoute = response.body().routes().get(0);

                // Make a toast which displays the route's distance
                Toast.makeText(MapActivityF.this, String.format(
                        getString(R.string.directions_activity_toast_message),
                        currentRoute.distance()), Toast.LENGTH_SHORT).show();

                if (mapboxMap != null) {
                    mapboxMap.getStyle(new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {

                            // Retrieve and update the source designated for showing the directions route
                            GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

                            // Create a LineString with the directions route's geometry and
                            // reset the GeoJSON source for the route LineLayer source
                            if (source != null) {
                                source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6));
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Timber.e("Error: " + throwable.getMessage());
                Toast.makeText(MapActivityF.this, "Error: " + throwable.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i = null;
        i = new Intent(this, ActivityOrderDetails.class);
        startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
// Cancel the Directions API request
        if (client != null) {
            client.cancelCall();
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}