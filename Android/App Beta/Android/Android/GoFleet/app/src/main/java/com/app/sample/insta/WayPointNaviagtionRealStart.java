package com.app.sample.insta;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.app.sample.insta.model.Order;
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

public class WayPointNaviagtionRealStart extends AppCompatActivity implements OnNavigationReadyCallback,
        NavigationListener, RouteListener, ProgressChangeListener
{


    private NavigationView navigationView;
    private boolean dropoffDialogShown;
    private Location lastKnownLocation;
    private Point origin;
    private List<Point> points = new ArrayList<>();
    private Order orderinfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        setTheme(R.style.Theme_AppCompat_NoActionBar);
        super.onCreate(savedInstanceState);

        origin = (Point) getIntent().getSerializableExtra("origin");
        orderinfo = (Order) getIntent().getSerializableExtra("orderObj");

        points.add(Point.fromLngLat(orderinfo.getLongitude(), orderinfo.getLatitude()));
        setContentView(R.layout.activity_wapoint_navigation);
        navigationView = findViewById(R.id.navigationView);
        navigationView.onCreate(savedInstanceState);
        navigationView.initialize(this);
    }

    public void FinishNavigation(View v)
    {
        Intent intent = new Intent(WayPointNaviagtionRealStart.this, ActivityRouteFinished.class);
        intent.putExtra("orderObj",orderinfo); //passes objects
        startActivity(intent);
        finish();
    }

    public void DelayOrderFromNav(View v)
    {
        Intent intent = new Intent(WayPointNaviagtionRealStart.this, ActivityDelayedOrders.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        navigationView.onStart();
    }

    @Override
    public void onResume() {
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
            startActivity(new Intent(WayPointNaviagtionRealStart.this, ActivityOrderDetails.class));
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        navigationView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        navigationView.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        navigationView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        navigationView.onStop();
    }

    @Override
    protected void onDestroy() {
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
    public void onNavigationRunning() {
        // Intentionally empty
    }

    @Override
    public boolean allowRerouteFrom(Point offRoutePoint) {
        return true;
    }

    @Override
    public void onOffRoute(Point offRoutePoint) {

    }

    @Override
    public void onRerouteAlong(DirectionsRoute directionsRoute) {

    }

    @Override
    public void onFailedReroute(String errorMessage) {

    }

    @Override
    public void onArrival() {
//        if (!dropoffDialogShown && !points.isEmpty())
//        {
//            showDropoffDialog();
//            dropoffDialogShown = true; // Accounts for multiple arrival events
//            Toast.makeText(this, "You have arrived!", Toast.LENGTH_SHORT).show();
//        }
        Intent intent = new Intent(WayPointNaviagtionRealStart.this, ActivityRouteFinished.class);
        intent.putExtra("orderObj",orderinfo); //passes objects
        startActivity(intent);
        finish();
    }

    @Override
    public void onProgressChange(Location location, RouteProgress routeProgress) {
        lastKnownLocation = location;
    }

    private void startNavigation(DirectionsRoute directionsRoute) {
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
                .voiceUnits(DirectionsCriteria.METRIC)
                .destination(destination)
                .alternatives(true)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response)
                    {
                        DirectionsResponse directionsResponse = response.body();
                        if (directionsResponse != null && !directionsResponse.routes().isEmpty()) {
                            startNavigation(directionsResponse.routes().get(0));
                        }
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t)
                    {

                    }
                });
    }

    private NavigationViewOptions setupOptions(DirectionsRoute directionsRoute) {
        dropoffDialogShown = false;

        NavigationViewOptions.Builder options = NavigationViewOptions.builder();
        options.directionsRoute(directionsRoute)
                .navigationListener(this)
                .progressChangeListener(this)
                .routeListener(this)
                .shouldSimulateRoute(false);
        return options.build();
    }

    private Point getLastKnownLocation() {
        return Point.fromLngLat(lastKnownLocation.getLongitude(), lastKnownLocation.getLatitude());
    }
}
