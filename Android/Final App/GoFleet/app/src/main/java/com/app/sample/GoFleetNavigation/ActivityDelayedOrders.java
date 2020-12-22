package com.app.sample.GoFleetNavigation;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.app.sample.GoFleetNavigation.model.Order;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class ActivityDelayedOrders extends AppCompatActivity
{

    private View parent_view;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;

    private Button delayOrderSelect;

    private Spinner spinner;
    private Order orderDelay;

    private Spinner Delayspinner;

    private Order orderObj;

    private TextView txtorderID;

    private MaterialCardView SelectOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delayed_orders);
        parent_view = findViewById(android.R.id.content);

        Application application = this.getApplication();
        ModelApi modelApi = ModelApi.getInstance(application);
        final List<Order> ordersLoader = modelApi.getOrders();

        if(ordersLoader.isEmpty() == false)//bug fix
        {
            orderDelay = ordersLoader.get(0);
        }

        if(ordersLoader.isEmpty())
        {
            Toast.makeText(this,"No orders currently available",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ActivityDelayedOrders.this, ActivityMain.class));
            finish();
        }


        setSupportActionBar((Toolbar) findViewById(R.id.toolbarDelay));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Delayed Orders");

        spinner = (Spinner) findViewById(R.id.time_spinner);
        Delayspinner = (Spinner) findViewById(R.id.delay_spinner);
        delayOrderSelect = (Button) findViewById(R.id.btnOrderSelect);

        txtorderID = (TextView) findViewById(R.id.orderIdDelay);

        SelectOrder = (MaterialCardView) findViewById(R.id.OrderSelectCard);

        // Create an ArrayAdapter using the string array and a default spinner layout

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Delay_Time_array, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adapterDelay = ArrayAdapter.createFromResource(this, R.array.Category_PDelay_Reason, android.R.layout.simple_spinner_item);

      // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapterDelay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner

        spinner.setEnabled(false);
        spinner.setClickable(false);
        spinner.setAdapter(adapter);

        Delayspinner.setAdapter(adapterDelay);

        rb1 = findViewById(R.id.radioTemp);
        rb2 = findViewById(R.id.radioPermanent);
        rb3 = findViewById(R.id.radioOrderDelay);

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

        View.OnClickListener button3Listener = new View.OnClickListener() {
            public void onClick(View v)
            {
                onRadioButtonClicked(v);
            }
        };

        rb1.setOnClickListener(button1Listener);
        rb2.setOnClickListener(button2Listener);
        rb3.setOnClickListener(button3Listener);

    }

    public void DelayConfirm(View v)
    {

        Application application = this.getApplication();
        ModelApi modelApi = ModelApi.getInstance(application);

        if(rb1.isChecked())
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

            modelApi.ReportTemporaryDelay(Integer.parseInt(orderDelay.getRoute_ID()),Delayspinner.getSelectedItem().toString(),EDT);
        }
        else if(rb2.isChecked())
        {
            modelApi.ReportPermanentDelay(Integer.parseInt(orderDelay.getRoute_ID()),Delayspinner.getSelectedItem().toString());
        }
        else if(rb3.isChecked())
        {
            modelApi.ReportDelayedOrder(Integer.parseInt(orderObj.getID()),Delayspinner.getSelectedItem().toString());
        }

        AlertDialog alertDialog = new AlertDialog.Builder(ActivityDelayedOrders.this).create();
        alertDialog.setTitle("Delay");
        alertDialog.setMessage("Delay has been noted! Click OK to Proceed");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ActivityDelayedOrders.this, ActivityOrderDetails.class);
                        startActivity(intent);
                        finish();
                    }
                });
        alertDialog.show();


    }

    public void SelectOrderDelay(View v)
    {
        onPause();
        Intent intent = new Intent(ActivityDelayedOrders.this, ActivityOrderDetails.class);
        intent.putExtra("DelayedOrders",true);
        startActivity(intent);
    }

    public void onRadioButtonClicked(View view)
    {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId())
        {
            case R.id.radioTemp:
                SelectOrder.setVisibility(View.GONE);
                if (checked)
                    delayOrderSelect.setEnabled(false);
                    spinner.setEnabled(true);
                    spinner.setClickable(true);

                    ArrayAdapter<CharSequence> adapterDelay = ArrayAdapter.createFromResource(this, R.array.Category_Delay_Reason, android.R.layout.simple_spinner_item);
                    adapterDelay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Delayspinner.setAdapter(adapterDelay);
                    break;
            case R.id.radioPermanent:
                SelectOrder.setVisibility(View.GONE);
                if (checked)
                    delayOrderSelect.setEnabled(false);
                    spinner.setEnabled(false);
                    spinner.setClickable(false);

                ArrayAdapter<CharSequence> adapterDelayP = ArrayAdapter.createFromResource(this, R.array.Category_PDelay_Reason, android.R.layout.simple_spinner_item);
                adapterDelayP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Delayspinner.setAdapter(adapterDelayP);

                break;
            case R.id.radioOrderDelay:
                rb3.setChecked(true);
                if(checked)
                {
                    spinner.setEnabled(false);
                    spinner.setClickable(false);
                    delayOrderSelect.setEnabled(true);
                    ArrayAdapter<CharSequence> adapterDelaySingle = ArrayAdapter.createFromResource(this, R.array.Order_delay_reasons_single, android.R.layout.simple_spinner_item);
                    adapterDelaySingle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Delayspinner.setAdapter(adapterDelaySingle);
                    SelectOrder.setVisibility(View.VISIBLE);
                }

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
    protected void onResume() {
        super.onResume();

        if (getIntent().getSerializableExtra("OrderObjDelay") != null)
        {
            orderObj = (Order) getIntent().getSerializableExtra("OrderObjDelay");

            Boolean valid = Valid();

            if(valid == true)
            {
                Toast.makeText(this,"Order invalid",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ActivityDelayedOrders.this, ActivityDelayedOrders.class);
                startActivity(intent);
                finish();
            }
            else
            {
                SelectOrder.setVisibility(View.VISIBLE);
                txtorderID.setText("Order ID: " + orderObj.getID());
                rb3.setChecked(true);
                ArrayAdapter<CharSequence> adapterDelaySingle = ArrayAdapter.createFromResource(this, R.array.Order_delay_reasons_single, android.R.layout.simple_spinner_item);
                adapterDelaySingle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Delayspinner.setAdapter(adapterDelaySingle);
            }

        }

    }

    public Boolean Valid() //exception handling for delayed orders
    {
        Boolean notValid = false;

        if(orderObj.isDelivered() == true)
        {
            notValid = true;
        }

        if(orderObj.getList() != null)
        {
            for(int i = 0;i < orderObj.getList().size();i++)
            {
                int delayNum = orderObj.getList().get(i);

                if(Integer.parseInt(orderObj.getID()) == delayNum)
                {
                    notValid = true;
                }

            }
        }

        return notValid;

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i = null;
        i = new Intent(this, ActivityMain.class);
        startActivity(i);
    }


}
