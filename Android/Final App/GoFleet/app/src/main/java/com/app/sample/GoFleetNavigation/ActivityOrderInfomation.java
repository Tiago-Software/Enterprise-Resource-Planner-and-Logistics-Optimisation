package com.app.sample.GoFleetNavigation;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.app.sample.GoFleetNavigation.model.Order;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.mlkit.md.LiveBarcodeScanningActivity;
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
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.Collections;
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
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;


public class ActivityOrderInfomation extends AppCompatActivity
{

    public static final String EXTRA_OBJCT = "com.app.sample.GoFleetNavigation.OBJ";

    public static Order orderInfo;
    private static Boolean camScan = false;

    //mapbox
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

    private static List<Order> ordersLoaderInfo;

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage, Order obj, Boolean CamScan, List<Order> ordersLoader) //passes order obj
    {
        Intent intent = new Intent(activity, ActivityOrderInfomation.class);
        intent.putExtra(EXTRA_OBJCT, obj);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_OBJCT);
        ActivityCompat.startActivity(activity, intent, options.toBundle());

        orderInfo = obj;
        camScan = CamScan;
        ordersLoaderInfo = ordersLoader;
    }

    private FloatingActionButton fab;
    private FloatingActionButton fabScan;
    private FloatingActionButton fabInvalid;
    private FloatingActionButton fabComplete;

    private FloatingActionButton fabreal;

    private View parent_view;
    private Button btnDelayOrder;
    private Order orderOBJ;
    private ImageView imageView;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details_information);

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        parent_view = findViewById(android.R.id.content);

        imageView = (ImageView) findViewById(R.id.routeImage);

        // animation transition
        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_OBJCT);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Information");

        orderOBJ = (Order) getIntent().getSerializableExtra(EXTRA_OBJCT);

        fab = findViewById(R.id.fab);
        fabScan = findViewById(R.id.fabScan);
        fabInvalid = findViewById(R.id.fabInvalid);
        fabComplete = findViewById(R.id.fabCompleted);

        fabreal = findViewById(R.id.fabreal);

     //   setupToolbar(Order.getName());

       // ((ImageView) findViewById(R.id.image)).setImageResource(recipe.getPhoto());

      //  LinearLayout ingredients = (LinearLayout) findViewById(R.id.ingredients);
        TextView order = (TextView) findViewById(R.id.order_details);
        order.setText("Order ID: " + orderOBJ.getID());

        TextView location = (TextView) findViewById(R.id.location_details);
        location.setText("Location Address: " + orderOBJ.getStreet() + '\n' +
                      "Location Suburb: " + orderOBJ.getSuburb());

        TextView clientBusName = (TextView) findViewById(R.id.client_business_name);
        clientBusName.setText(orderOBJ.getClient_Business_Name());


        TextView clientNum = (TextView) findViewById(R.id.ClientNumber);
        clientNum.setText(orderOBJ.getClientNum());

        TextView clientBricks = (TextView) findViewById(R.id.clientOrder_Brick);
        clientBricks.setText("Clay Brick: " + orderOBJ.getBrickQ());

        TextView clientCement = (TextView) findViewById(R.id.clientOrder_Cement);
        clientCement.setText("Cement bags: " + orderOBJ.getCementQ());

        TextView clientSand = (TextView) findViewById(R.id.clientOrder_Sand);
        clientSand.setText("Fine Sand: " + orderOBJ.getSandQ());



      //  TextView client = (TextView) findViewById(R.id.order_client);
      //  order.setText("Order 1");

        // Initialize the SDK
        Places.initialize(getApplicationContext(), "AIzaSyA6li206J2GgrnxbjSJ5ypv5kWQa7DyxIA");

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        // Define a Place ID.
        //  final String placeId = orderinfo.getPlaceID();

     //   final String placeId = orderOBJ.getPlaceID();

        final String placeId = "ChIJ_eDZDWxZlR4RZ6Ix7Cp9pjs";

        // Specify fields. Requests for photos must always have the PHOTO_METADATAS field.
        final List<Place.Field> fields = Collections.singletonList(Place.Field.PHOTO_METADATAS);

        // Get a Place object (this example uses fetchPlace(), but you can also use findCurrentPlace())
        final FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, fields);

        placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
            final Place place = response.getPlace();

            // Get the photo metadata.
            final List<PhotoMetadata> metadata = place.getPhotoMetadatas();
            if (metadata == null || metadata.isEmpty())
            {
                Toast.makeText(this,"No location photo available",Toast.LENGTH_SHORT).show();
                Log.w("TAG", "No photo metadata.");
                return;
            }
            final PhotoMetadata photoMetadata = metadata.get(Integer.parseInt((String)orderOBJ.getSequenceNumber()));

            // Get the attribution text.
            final String attributions = photoMetadata.getAttributions();

            // Create a FetchPhotoRequest.
            final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(800) // Optional.
                    .setMaxHeight(600) // Optional.
                    .build();
            placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                Bitmap bitmap = fetchPhotoResponse.getBitmap();
                imageView.setImageBitmap(bitmap);
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    final ApiException apiException = (ApiException) exception;
                    Log.e("TAG", "Place not found: " + exception.getMessage());
                    final int statusCode = apiException.getStatusCode();
                    // TODO: Handle error with given status code.
                }
            });
        });

        if(camScan == true && orderOBJ.isDelivered() == false)
        {
            fab.setVisibility(0);
            fab.setClickable(false);

            fabScan.setVisibility(1);
            fabScan.setClickable(true);

            fabreal.setVisibility(parent_view.GONE);
            fabreal.setClickable(false);

            fabScan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(ActivityOrderInfomation.this, LiveBarcodeScanningActivity.class);
                    intent.putExtra("orderObj",orderInfo); //passes objects
                    intent.putExtra("CamScan",camScan); //passes objects
                    startActivity(intent);
                    finish();
                }
            });
        }

        boolean postponed = isOnPostPonedList();

        if(postponed)
        {
            fab.setVisibility(0);
            fab.setClickable(false);

            fabScan.setVisibility(0);
            fabScan.setClickable(false);

            fabInvalid.setVisibility(1);
            fabInvalid.setClickable(true);

            int nextOrder =  Integer.parseInt(orderOBJ.getSequenceNumber())+1;
            String temp ="Order has been postponed";

            fabInvalid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    showToast(temp);
                }
            });
        }

        if(orderOBJ.isCancelled() == true)
        {
            fab.setVisibility(0);
            fab.setClickable(false);

            fabScan.setVisibility(0);
            fabScan.setClickable(false);

            fabInvalid.setVisibility(1);
            fabInvalid.setClickable(true);

            int nextOrder =  Integer.parseInt(orderOBJ.getSequenceNumber())+1;
            String temp ="Order has been cancelled";

            fabInvalid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    showToast(temp);
                }
            });
        }

        if(orderOBJ.isDelivered() == true)
        {
            fab.setVisibility(0);
            fab.setClickable(false);

            fabScan.setVisibility(0);
            fabScan.setClickable(false);

            fabInvalid.setVisibility(0);
            fabInvalid.setClickable(false);

            fabComplete.setVisibility(1);
            fabComplete.setClickable(true);


            String temp = "Order has already been delivered";

            fabComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    showToast(temp);
                }
            });

        }
        else if(orderOBJ.isDelivered() == false)
        {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    fabToggle();
                    finish();
                }
            });

        }

        //mapbox

        final int[] ref = {0};

        // Setup the MapView
        mapView = findViewById(R.id.mapViewRoute);
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

                        if(Integer.parseInt(orderOBJ.getSequenceNumber()) == 1)
                        {
                            origin = Point.fromLngLat(28.191817,-25.925063);
                        }
                        else if(Integer.parseInt(orderOBJ.getSequenceNumber()) > 1)
                        {
                            ref[0] = Integer.parseInt(orderOBJ.getSequenceNumber());
                            origin = Point.fromLngLat(ordersLoaderInfo.get(ref[0] -2).getLongitude(),ordersLoaderInfo.get(ref[0] -2).getLatitude());
                        }

                        // Set the destination location to the Plaza del Triunfo in Granada, Spain.
                        if(Integer.parseInt(orderOBJ.getSequenceNumber()) == 1)
                        {
                            destination = Point.fromLngLat(ordersLoaderInfo.get(0).getLongitude(), ordersLoaderInfo.get(0).getLatitude());
                        }
                        else
                        {
                            destination = Point.fromLngLat(ordersLoaderInfo.get(ref[0]-1).getLongitude(), ordersLoaderInfo.get(ref[0]-1).getLatitude());
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

    public void StartNavigationReal(View v)
    {

        Intent intent = new Intent(ActivityOrderInfomation.this, WayPointNaviagtionRealStart.class);
        intent.putExtra("origin",origin); //passes objects
        intent.putExtra("orderObj",orderInfo); //passes objects
        startActivity(intent);
        finish();

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

//                // Make a toast which displays the route's distance
//                Toast.makeText(ActivityOrderInfomation.this, String.format(
//                        getString(R.string.directions_activity_toast_message),
//                        currentRoute.distance()), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(ActivityOrderInfomation.this, "Error: " + throwable.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean isOnPostPonedList()
    {

        for(int i = 0;i < orderOBJ.getList().size();i++)
        {
            int delayNum = orderOBJ.getList().get(i);

            if(Integer.parseInt(orderOBJ.getID()) == delayNum)
            {
                return true;
            }

        }

        return false;
    }

    private void showToast(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void DelayOrder(View v)
    {
        Intent intent = new Intent(ActivityOrderInfomation.this, ActivityDelayedOrders.class);
        //intent.putExtra("orderObj",orderinfo); //passes objects
        startActivity(intent);
        finish();
    }

    public void CallClient(View v)
    {

    }

    private void setupToolbar(String name) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

      //  ((TextView) findViewById(R.id.toolbar_title)).setText(name);
    }

    private void fabToggle()
    {
        //activate route
        Application application = this.getApplication();
        ModelApi modelApi = ModelApi.getInstance(application);

        if(modelApi.getSequenceNumber() != Integer.parseInt(orderOBJ.getSequenceNumber())) //quick fix for validation of not starting the current order
        {
            Toast.makeText(this,"This is not the current Order ... please start order " + modelApi.getSequenceNumber(),Toast.LENGTH_SHORT).show();
            Intent intent = getIntent();
           // finish();
            startActivity(intent);
        }
        else if(modelApi.getSequenceNumber() == Integer.parseInt(orderOBJ.getSequenceNumber()))
        {
            modelApi.ActivateRoute(Integer.parseInt(orderOBJ.getRoute_ID()));

            Intent intent = new Intent(ActivityOrderInfomation.this, WaypointNavigationActivity.class);
            intent.putExtra("origin",origin); //passes objects
            intent.putExtra("orderObj",orderInfo); //passes objects
            startActivity(intent);
            finish();
        }



//        //start Map
//        Intent intent = new Intent(ActivityOrderInfomation.this, MapActivityF.class);
//        intent.putExtra("orderObj",orderInfo); //passes objects
//        startActivity(intent);

        //new

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }else{
            Snackbar.make(parent_view, item.getTitle()+" clicked", Snackbar.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_orders, menu);
        return true;
    }

    public void onBackPressed()
    {
    super.onBackPressed();
    //logic for button

        if(camScan == true)
        {
            Intent i = null;
            i = new Intent(this, ActivityOrderDetails.class);
            i.putExtra("CamScan",camScan); //passes objects
            startActivity(i);
            camScan = false;
        }
        else
        {
            Intent i = null;
            i = new Intent(this, ActivityOrderDetails.class);
            startActivity(i);
        }


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

