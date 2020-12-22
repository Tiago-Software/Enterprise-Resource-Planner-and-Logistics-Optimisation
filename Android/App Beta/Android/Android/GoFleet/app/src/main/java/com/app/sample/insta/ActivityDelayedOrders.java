package com.app.sample.insta;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.app.sample.insta.adapter.OrderListAdapter;
import com.app.sample.insta.data.Tools;
import com.app.sample.insta.model.AbstractAPIListener;
import com.app.sample.insta.model.Order;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class ActivityDelayedOrders extends AppCompatActivity
{

    private View parent_view;
    private RadioButton rb1;
    private RadioButton rb2;

    private Spinner spinner;
    private TextInputEditText delayReason;
    private Order orderDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delayed_orders);
        parent_view = findViewById(android.R.id.content);

        Application application = this.getApplication();
        ModelApi modelApi = ModelApi.getInstance(application);
        final List<Order> ordersLoader = modelApi.getOrders();

        if(ordersLoader.isEmpty())
        {
            Toast.makeText(this,"No orders currently available",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ActivityDelayedOrders.this, ActivityMain.class));
            finish();
        }

        delayReason = (TextInputEditText) findViewById(R.id.ReasonForDelay);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbarDelay));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Delayed Orders");

        spinner = (Spinner) findViewById(R.id.time_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Delay_Time_array, android.R.layout.simple_spinner_item);

      // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner

        spinner.setEnabled(false);
        spinner.setClickable(false);
        spinner.setAdapter(adapter);

        rb1 = findViewById(R.id.radioTemp);
        rb2 = findViewById(R.id.radioPermanent);

        View.OnClickListener button1Listener = new View.OnClickListener() {
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        };

        View.OnClickListener button2Listener = new View.OnClickListener() {
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        };

        rb1.setOnClickListener(button1Listener);
        rb2.setOnClickListener(button2Listener);

    }

    public void DelayConfirm(View v)
    {

        Application application = this.getApplication();
        ModelApi modelApi = ModelApi.getInstance(application);

        if(spinner.isEnabled() && spinner.isClickable())
        {
            int spinint = spinner.getSelectedItemPosition();
            long EDT = 0;

            if(spinint == 0)
            {
                EDT = 1800;
            }
            else if(spinint == 1)
            {
                EDT = 3600;
            }
            else if(spinint == 2)
            {
                EDT = 5400;
            }
            else if(spinint == 3)
            {
                EDT = 7200;
            }

            modelApi.ReportTemporaryDelay(Integer.parseInt(orderDelay.getRoute_ID()),delayReason.getText().toString(),EDT);
        }
        else
        {
            modelApi.ReportPermanentDelay(Integer.parseInt(orderDelay.getRoute_ID()),delayReason.getText().toString());
        }

        Toast.makeText(this,"Delay Confirmed",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ActivityDelayedOrders.this, ActivityMain.class);
        startActivity(intent);
        finish();
    }

    public void onRadioButtonClicked(View view)
    {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId())
        {
            case R.id.radioTemp:
                if (checked)
                    spinner.setEnabled(true);
                    spinner.setClickable(true);
                    break;
            case R.id.radioPermanent:
                if (checked)
                    spinner.setEnabled(false);
                    spinner.setClickable(false);
                    break;
        }

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
    }


}
