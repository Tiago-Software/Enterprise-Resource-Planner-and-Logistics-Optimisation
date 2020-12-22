package com.app.sample.GoFleetNavigation;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.sample.GoFleetNavigation.adapter.OrderListAdapter;
import com.app.sample.GoFleetNavigation.model.AbstractAPIListener;
import com.app.sample.GoFleetNavigation.model.Order;
import com.google.android.material.snackbar.Snackbar;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.optimization.v1.MapboxOptimization;
import com.mapbox.api.optimization.v1.models.OptimizationResponse;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

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
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;

public class ActivityOrderDetails extends AppCompatActivity implements
        MapboxMap.OnMapClickListener, MapboxMap.OnMapLongClickListener
{

    public static final String EXTRA_OBJCT = "com.app.sample.GoFleetNavigation.OBJ";

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage, Order obj) {
        Intent intent = new Intent(activity, ActivityOrderDetails.class);
        intent.putExtra(EXTRA_OBJCT, obj);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_OBJCT);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    public RecyclerView recyclerView;
    private OrderListAdapter mAdapter;
    private String orderRead;
    private View parent_view;
    private Boolean CamScanner = false;

    private Boolean DelayedOrders = false;

    private static Boolean CompleteRoute = false;

    public TextView ETAtime;

    public TextView CurrentRoute;

    public List<Order> ordersLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token)); //mapbox init

        setContentView(R.layout.order_details);   //order details
        parent_view = findViewById(android.R.id.content);


        if(getIntent().getSerializableExtra("DelayedOrders") != null)
        {
            DelayedOrders = (Boolean) getIntent().getSerializableExtra("DelayedOrders");
        }

        if(getIntent().getSerializableExtra("CamScan") != null)
        {
            CamScanner = (Boolean) getIntent().getSerializableExtra("CamScan");
        }

        ETAtime = (TextView) findViewById(R.id.ETAtime);

        CurrentRoute = (TextView) findViewById(R.id.CurrentOrder);

        // animation transition
        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_OBJCT);

        // category = (Category) getIntent().getSerializableExtra(EXTRA_OBJCT);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        //  LinearLayoutManager mLayoutManager = new GridLayoutManager(this, Tools.getGridSpanCount(this));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        //  List<Order> orders = new ArrayList<>();

//        Order g = new Order();
//
//        orders.add(g);

        //Recycler view configure

        Application application = this.getApplication();
        ModelApi modelApi = ModelApi.getInstance(application);


        ordersLoad = modelApi.getOrders();

        modelApi.loadOrders(new AbstractAPIListener()
        {
            @Override
            public void onOrdersLoaded(List<Order> order)
            {
                ordersLoad.clear();
                ordersLoad.addAll(order);

                loadOrderCount();

                mAdapter.notifyDataSetChanged();
            }
        });


        mAdapter = new OrderListAdapter(this, ordersLoad);

        if(CheckIfOrdersComplete(ordersLoad).equals(true))
        {
            orderRead = ordersLoad.get(0).getRoute_ID(); //used for route num confirmation
            CompleteRoute();

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run()
//                {
//
//                }
//            }, 1000); // Millisecond 1000 = 1 sec
        }
        else if(ordersLoad.isEmpty())
        {
            Toast.makeText(this,"No orders currently available",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ActivityOrderDetails.this, ActivityMain.class));
            finish();
        }


        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OrderListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Order obj, int position) //passes current order object to next activity
            {

                if(DelayedOrders == true)
                {
                    Intent intent = new Intent(ActivityOrderDetails.this, ActivityDelayedOrders.class);
                    intent.putExtra("OrderObjDelay",obj);
                    startActivity(intent);
                    DelayedOrders = false;
                    finish();
                }
                else
                {
                    ActivityOrderInfomation.navigate(ActivityOrderDetails.this, v.findViewById(R.id.image), obj,CamScanner, ordersLoad);
                    finish();
                }

            }
        });


        // Setup the MapView
        mapView = findViewById(R.id.mapViewOrders);
        mapView.onCreate(savedInstanceState);

        origin = Point.fromLngLat(28.191817,-25.925063); //phaki branch
        stops.add(origin);

        if(ordersLoad != null)
        {

            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull MapboxMap mapboxMap) {
                    mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style)
                        {
                                    // Add origin and destination to the mapboxMap
                                    initMarkerIconSymbolLayer(style);
                                    //       enableLocationComponent(style);

                                    style = mapboxMap.getStyle();
                                    if (style != null)
                                    {

                                        if(ordersLoad != null)
                                        {
                                            for(int i = 0; i < ordersLoad.size(); i++)
                                            {
                                                LatLng point = new LatLng();
                                                point.setLongitude(ordersLoad.get(i).getLongitude());
                                                point.setLatitude(ordersLoad.get(i).getLatitude());
                                                addDestinationMarker(style, point,i);
                                                addPointToStopsList(point);

                                            }
                                        }
                                        else
                                        {
                                            for(int i = 0; i < ordersLoad.size(); i++)
                                            {
                                                LatLng point = new LatLng();
                                                point.setLongitude(ordersLoad.get(i).getLongitude());
                                                point.setLatitude(ordersLoad.get(i).getLatitude());
                                                addDestinationMarker(style, point,i);
                                                addPointToStopsList(point);

                                            }
                                        }



                                    }



                                    getOptimizedRoute(style, stops);

                                    initOptimizedRouteLineLayer(style);

                                }

                    });
                }
            });
        }





    }


    //Mapbox varaibles
    private static final String ICON_GEOJSON_SOURCE_ID = "icon-source-id";

    private static final String ICON_GEOJSON_HOME_ID = "icon-home-id";

    private static final String FIRST = "first";
    private static final String ANY = "any";
    private static final String TEAL_COLOR = "#23D2BE";
    private static final float POLYLINE_WIDTH = 5;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private DirectionsRoute optimizedRoute;
    private MapboxOptimization optimizedClient;
    private List<Point> stops = new ArrayList<>();
    private Point origin;

    private void initMarkerIconSymbolLayer(@NonNull Style loadedMapStyle)
    {
        // Add the marker image to map
        //icon load set
        loadedMapStyle.addImage("icon-image", BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));

        loadedMapStyle.addImage("icon-image-start", BitmapFactory.decodeResource(this.getResources(), R.drawable.map_marker_dark));


        // Add the source to the map
        loadedMapStyle.addSource(new GeoJsonSource(ICON_GEOJSON_SOURCE_ID, Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude()))));



        loadedMapStyle.addSource(new GeoJsonSource(ICON_GEOJSON_HOME_ID)); //, Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude()))));

        loadedMapStyle.addLayer(new SymbolLayer("icon-home-id", ICON_GEOJSON_HOME_ID).withProperties(
                iconImage("icon-image-start"),
                iconSize(1f),
                iconAllowOverlap(true),
                iconIgnorePlacement(true),
                textField("Destination"),
                iconOffset(new Float[] {0f, -7f})
        ));

        loadedMapStyle.addLayer(new SymbolLayer("icon-layer-id", ICON_GEOJSON_SOURCE_ID).withProperties(
                iconImage("icon-image"),
                iconSize(1.5f),
                iconAllowOverlap(true),
                iconIgnorePlacement(true),
                textField("Start"),
                iconOffset(new Float[] {0f, 15f})
        ));


    }


    private void initOptimizedRouteLineLayer(@NonNull Style loadedMapStyle)
    {
        loadedMapStyle.addSource(new GeoJsonSource("optimized-route-source-id"));
        loadedMapStyle.addLayerBelow(new LineLayer("optimized-route-layer-id", "optimized-route-source-id")
                .withProperties(
                        lineColor(Color.parseColor("#FF6FE80C")),
                        lineWidth(POLYLINE_WIDTH)
                ), "icon-layer-id");
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
// Optimization API is limited to 12 coordinate sets
        return false;
    }

    @Override
    public boolean onMapLongClick(@NonNull LatLng point) {

        return false;
    }

    private void resetDestinationMarkers(@NonNull Style style) {
        GeoJsonSource optimizedLineSource = style.getSourceAs(ICON_GEOJSON_SOURCE_ID);
        if (optimizedLineSource != null) {
            optimizedLineSource.setGeoJson(Point.fromLngLat(origin.longitude(), origin.latitude()));
        }
    }

    private void removeOptimizedRoute(@NonNull Style style) {
        GeoJsonSource optimizedLineSource = style.getSourceAs("optimized-route-source-id");
        if (optimizedLineSource != null) {
            optimizedLineSource.setGeoJson(FeatureCollection.fromFeatures(new Feature[] {}));
        }
    }

    private boolean alreadyTwelveMarkersOnMap() {
        return stops.size() == 12;
    }

    private void addDestinationMarker(@NonNull Style style, LatLng point, int num)  //adds points
    {
        List<Feature> destinationMarkerList = new ArrayList<>();

        for (Point singlePoint : stops)
        {
            destinationMarkerList.add(Feature.fromGeometry(Point.fromLngLat(singlePoint.longitude(), singlePoint.latitude())));
        }

        GeoJsonSource iconSource = null;

        destinationMarkerList.add(Feature.fromGeometry(Point.fromLngLat(point.getLongitude(), point.getLatitude())));

//        if(num == 1)
//        {
//            iconSource = null;
//        }
//

        iconSource = style.getSourceAs(ICON_GEOJSON_HOME_ID); //icon set


        if (iconSource != null)
        {
            iconSource.setGeoJson(FeatureCollection.fromFeatures(destinationMarkerList));
        }
    }

    private void addPointToStopsList(LatLng point) {
        stops.add(Point.fromLngLat(point.getLongitude(), point.getLatitude()));
    }

    private void addFirstStopToStopsList()
    {
// Set first stop
        origin = Point.fromLngLat(28.191817,-25.925063); //phaki branch
        stops.add(origin);
    }

    private void getOptimizedRoute(@NonNull final Style style, List<Point> coordinates)
    {
        optimizedClient = MapboxOptimization.builder()
                .source(FIRST)
                .destination(ANY)
                .coordinates(coordinates)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.mapbox_access_token))
                .build();

        optimizedClient.enqueueCall(new Callback<OptimizationResponse>() {
            @Override
            public void onResponse(Call<OptimizationResponse> call, Response<OptimizationResponse> response) {

                if (response.body() != null)
                {
                    List<DirectionsRoute> routes = response.body().trips();

                    if (routes != null)
                    {
                        // Get most optimized route from API response
                        optimizedRoute = routes.get(0);
                        drawOptimizedRoute(style, optimizedRoute);


                    }
                }
            }

            @Override
            public void onFailure(Call<OptimizationResponse> call, Throwable throwable) {
                Timber.d("Error: %s", throwable.getMessage());
            }
        });
    }

    private void drawOptimizedRoute(@NonNull Style style, DirectionsRoute route) {
        GeoJsonSource optimizedLineSource = style.getSourceAs("optimized-route-source-id");
        if (optimizedLineSource != null) {
            optimizedLineSource.setGeoJson(FeatureCollection.fromFeature(Feature.fromGeometry(
                    LineString.fromPolyline(route.geometry(), PRECISION_6))));
        }
    }


    public void StartMapOverview(View v)
    {

        Intent intent = new Intent(ActivityOrderDetails.this, ActivityRouteShowCase.class);
        startActivity(intent);

    }

    public void CompleteRoute()
    {
        Toast.makeText(this,"Orders Complete",Toast.LENGTH_SHORT).show();

        AlertDialog alertDialog = new AlertDialog.Builder(ActivityOrderDetails.this).create();
        alertDialog.setTitle("Route Complete");
        alertDialog.setMessage("Route complete! Click OK to proceed");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(ActivityOrderDetails.this, ActivityMain.class));
                        finish();
                    }
                });

        alertDialog.show();
        Application application = this.getApplication();
        ModelApi modelApi = ModelApi.getInstance(application);
        modelApi.CompleteRoute(Integer.parseInt(orderRead));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }else{
            Snackbar.make(parent_view, item.getTitle() + " clicked", Snackbar.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_orders, menu);
        return true;
    }

    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i=0; i<menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }


    @Override
    protected void onResume()
    {
        super.onResume();
   //     UpdateCurrentOrder();

        //loadOrderCount();
    }

    public void UpdateCurrentOrder()
    {
        Application application = this.getApplication();
        ModelApi modelApi = ModelApi.getInstance(application);

        CurrentRoute.setText("Current Order: " + modelApi.getSequenceNumber());
    }

    public void loadOrderCount()
    {
        Application application = this.getApplication();
        ModelApi modelApi = ModelApi.getInstance(application);
        List<Order> ordersLoad = modelApi.getOrders();

        long minutes = 0;


        if(ordersLoad.isEmpty() == false)
        {
            minutes = TimeUnit.SECONDS.toMinutes(ordersLoad.get(0).getEta_Sec());;
        }


        ETAtime.setText("Route Total ETA: " + minutes + " minutes");


        int count = 1;

        if(ordersLoad.isEmpty() == false)
        {
            for(int i = 0; i < ordersLoad.size();i++)
            {

                if(ordersLoad.get(i).isDelivered() == true) //IF dellivered
                {
                    count++;
                }

                if(ordersLoad.get(i).isCancelled() == true) //IF Cancelled
                {
                    count++;
                }

                List<Integer> delayNum = null;
                int orderID = 0;

                if(ordersLoad.isEmpty() != true)
                {
                    delayNum = ordersLoad.get(0).getList();

                }

                orderID = Integer.parseInt(ordersLoad.get(i).getID());

                int delaynumm = 0;

                if(delayNum.isEmpty() != true)
                {
                    for(int j = 0; j < delayNum.size();j++)
                    {
                        delaynumm = delayNum.get(j).intValue();

                        if(orderID == delaynumm) //if OrderID == delayNum ID
                        {
                            count++;
                        }
                    }
                }




            }


            if(count == 0)
            {

                CurrentRoute.setText("Current Order: " + ordersLoad.get(0).getSequenceNumber());
                modelApi.setSequenceNumber(1);
            }
            else if(count-1 == ordersLoad.size())
            {
                CurrentRoute.setText("Orders Complete");
            }
            else
            {
                CurrentRoute.setText("Current Order: " + count);
                modelApi.setSequenceNumber(count);
            }


        }
    }

    public Boolean CheckIfOrdersComplete(List<Order> orders) //checks if orders are complete
    {

        int Count = 0;
        Boolean complete = false;

        for(int i = 0; i < orders.size();i++)
        {
            Order listOrder = orders.get(i);

            if(listOrder.isDelivered() == true)
            {
                Count++;
            }

            if(listOrder.getList() != null)
            {
                for(int j = 0;j < listOrder.getList().size();j++)
                {
                    int delayNum = listOrder.getList().get(j);

                    if(Integer.parseInt(orders.get(i).getID()) == delayNum) //checks for delays
                    {
                        Count++;
                    }

                }
            }

            if(Count == orders.size())
            {
                complete = true;
            }
        }

        return complete;

    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i = null;
        i = new Intent(this, ActivityMain.class);
        startActivity(i);
        finish();
    }

    //mapbox
    @Override
    protected void onDestroy() {
        super.onDestroy();
// Cancel the directions API request
        if (optimizedClient != null) {
            optimizedClient.cancelCall();
        }
        if (mapboxMap != null) {
            mapboxMap.removeOnMapClickListener(this);
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

}

