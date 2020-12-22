package com.app.sample.insta;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.app.sample.insta.model.Order;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.List;


public class ActivityOrderInfomation extends AppCompatActivity
{

    public static final String EXTRA_OBJCT = "com.app.sample.insta.OBJ";

    public static Order orderInfo;
    private static Boolean camScan = false;

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage, Order obj, Boolean CamScan) //passes order obj
    {
        Intent intent = new Intent(activity, ActivityOrderInfomation.class);
        intent.putExtra(EXTRA_OBJCT, obj);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_OBJCT);
        ActivityCompat.startActivity(activity, intent, options.toBundle());

        orderInfo = obj;
        camScan = CamScan;
    }

    private FloatingActionButton fab;
    private FloatingActionButton fabScan;
    private View parent_view;
    private Button btnDelayOrder;
    private Order orderOBJ;
    private ImageView imageView;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details_information);
        parent_view = findViewById(android.R.id.content);

        imageView = (ImageView) findViewById(R.id.routeImage);

        // animation transition
        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_OBJCT);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        orderOBJ = (Order) getIntent().getSerializableExtra(EXTRA_OBJCT);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabScan = (FloatingActionButton) findViewById(R.id.fabScan);

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

        if(camScan == true)
        {
            fab.setVisibility(0);
            fab.setClickable(false);

            fabScan.setVisibility(1);
            fabScan.setClickable(true);
        }

        if(orderOBJ.isDelivered() == true)
        {
            int nextOrder =  Integer.parseInt(orderOBJ.getSequenceNumber())+1;
            String temp ="Order has already been delivered ... Please start order:" + nextOrder;

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    showToast(temp);
                }
            });

        }
        else
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





        fabScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ActivityOrderInfomation.this, ActivityCamera.class);
                intent.putExtra("orderObj",orderInfo); //passes objects
                intent.putExtra("CamScan",camScan); //passes objects
                startActivity(intent);
                finish();
            }
        });
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
        modelApi.ActivateRoute(Integer.parseInt(orderOBJ.getRoute_ID()));

        //start Map
        Intent intent = new Intent(ActivityOrderInfomation.this, MapActivity.class);
        intent.putExtra("orderObj",orderInfo); //passes objects
        startActivity(intent);
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
    }
}

