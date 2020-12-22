package com.app.sample.insta;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.sample.insta.adapter.OrderListAdapter;
import com.app.sample.insta.model.AbstractAPIListener;
import com.app.sample.insta.model.Order;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

public class ActivityRouteFinished extends AppCompatActivity
{

    private ImageView imageView;
    private Order orderinfo;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_finished);   //order details
        imageView = (ImageView) findViewById(R.id.routeFinished);

        orderinfo = (Order) getIntent().getSerializableExtra("orderObj");

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_fin));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        //  LinearLayout ingredients = (LinearLayout) findViewById(R.id.ingredients);
        TextView order = (TextView) findViewById(R.id.order_details_fin);
        order.setText("Order ID: " + orderinfo.getID());

        TextView location = (TextView) findViewById(R.id.location_details_fin);
        location.setText("Location Address: " + orderinfo.getStreet() + '\n' +
                "Location Suburb: " + orderinfo.getSuburb());


        // Initialize the SDK
        Places.initialize(getApplicationContext(), "AIzaSyA6li206J2GgrnxbjSJ5ypv5kWQa7DyxIA");

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        // Define a Place ID.
       // final String placeId = orderinfo.getPlaceID();

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
            final PhotoMetadata photoMetadata = metadata.get(Integer.parseInt((String)orderinfo.getSequenceNumber()));

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

    }

    public void ScanClientInvoice(View v)
    {
        Intent intent = new Intent(ActivityRouteFinished.this, ActivityCamera.class);
        intent.putExtra("orderObj",orderinfo); //passes objects
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(ActivityRouteFinished.this, ActivityMain.class));
        finish();
    }
}
