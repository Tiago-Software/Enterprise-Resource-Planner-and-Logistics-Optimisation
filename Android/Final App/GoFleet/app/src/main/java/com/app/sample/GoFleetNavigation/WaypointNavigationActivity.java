package com.app.sample.GoFleetNavigation;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.app.sample.GoFleetNavigation.model.AbstractAPIListener;
import com.app.sample.GoFleetNavigation.model.Order;
import com.google.android.material.snackbar.Snackbar;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.services.android.navigation.ui.v5.NavigationView;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapbox.services.android.navigation.ui.v5.OnNavigationReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.listeners.NavigationListener;
import com.mapbox.services.android.navigation.ui.v5.listeners.RouteListener;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.services.android.navigation.v5.routeprogress.ProgressChangeListener;
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WaypointNavigationActivity extends AppCompatActivity implements OnNavigationReadyCallback,
        NavigationListener, RouteListener, ProgressChangeListener {

    private NavigationView navigationView;
    private boolean dropoffDialogShown;
    private Location lastKnownLocation;
    private Point origin;
    private List<Point> points = new ArrayList<>();
    private Order orderinfo;

    private static Boolean Complete;

    private ModelApi modelApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        setTheme(R.style.Theme_AppCompat_NoActionBar);
        super.onCreate(savedInstanceState);

        origin = (Point) getIntent().getSerializableExtra("origin");
        orderinfo = (Order) getIntent().getSerializableExtra("orderObj");
        CancelledOrder = false;

        points.add(Point.fromLngLat(orderinfo.getLongitude(), orderinfo.getLatitude()));

        setContentView(R.layout.activity_wapoint_navigation);
        navigationView = findViewById(R.id.navigationView);
        navigationView.onCreate(savedInstanceState);
        navigationView.initialize(this);

        Application application = this.getApplication();
        modelApi = ModelApi.getInstance(application);

        Complete = false;
    }

    public void FinishNavigation(View v)
    {
        Complete = true;
        modelApi.PostLocation(modelApi.getUser().getID(),orderinfo.getLongitude(), orderinfo.getLatitude());
        Intent intent = new Intent(WaypointNavigationActivity.this, ActivityRouteFinished.class);
        intent.putExtra("orderObj",orderinfo); //passes objects
        intent.putExtra("camScan",true); //passes objects
        startActivity(intent);
        finish();
    }

    public void DelayOrderFromNav(View v)
    {
        Intent intent = new Intent(WaypointNavigationActivity.this, ActivityDelayedOrders.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        navigationView.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        navigationView.onResume();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        navigationView.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        // If the navigation view didn't need to do anything, call super
        if (!navigationView.onBackPressed())
        {
            super.onBackPressed();
            startActivity(new Intent(WaypointNavigationActivity.this, ActivityOrderDetails.class));
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        navigationView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        navigationView.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        navigationView.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        navigationView.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        navigationView.onDestroy();
    }

    @Override
    public void onNavigationReady(boolean isRunning)
    {
        fetchRoute(origin,points.get(0));
    }

    @Override
    public void onCancelNavigation() {
        // Navigation canceled, finish the activity
        finish();
    }

    @Override
    public void onNavigationFinished()
    {
        // Intentionally empty

    }

    @Override
    public void onNavigationRunning()
    {
        // Intentionally empty
    }

    @Override
    public boolean allowRerouteFrom(Point offRoutePoint)
    {
        return true;
    }

    @Override
    public void onOffRoute(Point offRoutePoint)
    {

    }

    @Override
    public void onRerouteAlong(DirectionsRoute directionsRoute)
    {

    }

    @Override
    public void onFailedReroute(String errorMessage)
    {

    }

    @Override
    public void onArrival()
    {
        Complete = true;
        modelApi.PostLocation(modelApi.getUser().getID(),orderinfo.getLongitude(), orderinfo.getLatitude());
        Intent intent = new Intent(WaypointNavigationActivity.this, ActivityRouteFinished.class);
        intent.putExtra("orderObj",orderinfo); //passes objects
        intent.putExtra("camScan",true); //passes objects
        startActivity(intent);
        finish();
    }

    @Override
    public void onProgressChange(Location location, RouteProgress routeProgress)
    {
        lastKnownLocation = location;

        if(Complete == false)
        {
            Point point = getLastKnownLocation();
            modelApi.PostLocation(modelApi.getUser().getID(),point.longitude(),point.latitude());
        }

        if(CancelledOrder == false)
        {
            final List<Order> ordersLoad = modelApi.getOrders();

            modelApi.loadOrders(new AbstractAPIListener()
            {
                @Override
                public void onOrdersLoaded(List<Order> order)
                {
                    ordersLoad.clear();
                    ordersLoad.addAll(order);
                    isCancelled(ordersLoad);
                }
            });
        }

    }

    public static boolean CancelledOrder;

    public void isCancelled(List<Order> orders)
    {

    int count = 0;

        for(int i = 0; i < orders.size(); i++)
        {


            if (orderinfo.getID().equals(orders.get(i).getID()))
            {

                boolean cancel = orders.get(i).isCancelled();

                count = i;

                if (cancel == true)
                {
                    CancelledOrder = true;
                    // Notify user of cancellation
                    AlertDialog alertDialog = new AlertDialog.Builder(WaypointNavigationActivity.this).create();
                    alertDialog.setTitle("Cancellation");
                    alertDialog.setMessage("Click OK to start next route");
                    int finalCount = count;
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Redirect Route to next route

                                    Point point = getLastKnownLocation();
                                    origin = Point.fromLngLat(point.longitude(),point.latitude());

                                    if(finalCount <= orders.size())
                                    {

                                        Intent intent = getIntent();
                                        intent.putExtra("origin",origin); //passes objects
                                        intent.putExtra("orderObj",orders.get(finalCount +1)); //passes objects
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        Intent intent = new Intent(WaypointNavigationActivity.this, ActivityMain.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            });
                    alertDialog.show();

                }

            }



        }


    }

    private void startNavigation(DirectionsRoute directionsRoute)
    {
        NavigationViewOptions navigationViewOptions = setupOptions(directionsRoute);
        navigationView.startNavigation(navigationViewOptions);
    }

    private void showDropoffDialog()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage(getString(R.string.dropoff_dialog_text));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dropoff_dialog_positive_text),
                (dialogInterface, in) -> fetchRoute(getLastKnownLocation(), points.remove(0)));
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.dropoff_dialog_negative_text),
                (dialogInterface, in) -> {
// Do nothing
                });

        alertDialog.show();
    }

    private void fetchRoute(Point origin, Point destination)
    {
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .alternatives(true)
                .voiceUnits(DirectionsCriteria.METRIC)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response)
                    {

                        DirectionsResponse directionsResponse = response.body(); // distance and details

                        if (directionsResponse != null && !directionsResponse.routes().isEmpty())
                        {
                            startNavigation(directionsResponse.routes().get(0));
                        }

                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t)
                    {

                    }
                });
    }

    private NavigationViewOptions setupOptions(DirectionsRoute directionsRoute)
    {
        dropoffDialogShown = false;
        NavigationViewOptions.Builder options = NavigationViewOptions.builder();

        options.directionsRoute(directionsRoute)
                .navigationListener(this)
                .progressChangeListener(this)
                .routeListener(this)
                .shouldSimulateRoute(true);
        return options.build();
    }

    private Point getLastKnownLocation()
    {
        return Point.fromLngLat(lastKnownLocation.getLongitude(), lastKnownLocation.getLatitude());
    }
}
