package com.app.sample.insta;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


public class ActivityOrderInfomation extends AppCompatActivity
{

    public static final String EXTRA_OBJCT = "com.app.sample.insta.OBJ";

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage, Order obj) {
        Intent intent = new Intent(activity, ActivityOrderInfomation.class);
        intent.putExtra(EXTRA_OBJCT, obj);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_OBJCT);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private FloatingActionButton fab;
    private View parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details_information);
        parent_view = findViewById(android.R.id.content);

        // animation transition
        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_OBJCT);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Order orderOBJ = (Order) getIntent().getSerializableExtra(EXTRA_OBJCT);
        fab = (FloatingActionButton) findViewById(R.id.fab);

     //   setupToolbar(Order.getName());

       // ((ImageView) findViewById(R.id.image)).setImageResource(recipe.getPhoto());

      //  LinearLayout ingredients = (LinearLayout) findViewById(R.id.ingredients);
        TextView order = (TextView) findViewById(R.id.order_details);
        order.setText("Order ID: " + orderOBJ.getID());

        TextView location = (TextView) findViewById(R.id.location_details);
        location.setText("Location Address: " + orderOBJ.getStreet() + '\n' +
                      "Location Suburb: " + orderOBJ.getSuburb());

      //  TextView client = (TextView) findViewById(R.id.order_client);
      //  order.setText("Order 1");



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                fabToggle();
            }
        });
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
        Toast.makeText(this, "Start route coming soon!!", Toast.LENGTH_SHORT).show();
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
}

