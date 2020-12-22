package com.app.sample.insta;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.sample.insta.adapter.OrderListAdapter;
import com.app.sample.insta.data.Tools;
import com.app.sample.insta.model.AbstractAPIListener;
import com.app.sample.insta.model.Order;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ActivityOrderDetails extends AppCompatActivity
{

    public static final String EXTRA_OBJCT = "com.app.sample.insta.OBJ";

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

    private static Boolean CompleteRoute = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);   //order details
        parent_view = findViewById(android.R.id.content);


        if(getIntent().getSerializableExtra("CamScan") != null)
        {
            CamScanner = (Boolean) getIntent().getSerializableExtra("CamScan");
        }

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
        final List<Order> ordersLoad = modelApi.getOrders();

        modelApi.loadOrders(new AbstractAPIListener()
        {
            @Override
            public void onOrdersLoaded(List<Order> order)
            {
                ordersLoad.clear();
                ordersLoad.addAll(order);
                mAdapter.notifyDataSetChanged();
            }
        });


        mAdapter = new OrderListAdapter(this, ordersLoad);

        if(CheckIfOrdersComplete(ordersLoad).equals(true))
        {
            orderRead = ordersLoad.get(0).getRoute_ID();
            CompleteRoute();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Magic here
                    startActivity(new Intent(ActivityOrderDetails.this, ActivityMain.class));
                    finish();
                }
            }, 1000); // Millisecond 1000 = 1 sec
            Toast.makeText(this,"No orders currently available",Toast.LENGTH_SHORT).show();
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
                ActivityOrderInfomation.navigate(ActivityOrderDetails.this, v.findViewById(R.id.image), obj,CamScanner);
                finish();
            }
        });
    }

    public void CompleteRoute()
    {
        Toast.makeText(this,"Orders Complete",Toast.LENGTH_SHORT).show();
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

        //      final MenuItem searchItem = menu.findItem(R.id.action_search);
//
//        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setIconified(false);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                try {
//                    mAdapter.getFilter().filter(s);
//                } catch (Exception e) {
//                }
//                return true;
//            }
//        });
//        // Detect SearchView icon clicks
//        searchView.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setItemsVisibility(menu, searchItem, false);
//            }
//        });
//
//        // Detect SearchView close
//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                setItemsVisibility(menu, searchItem, true);
//                return false;
//            }
//        });
//        searchView.onActionViewCollapsed();
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

        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OrderListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Order obj, int position) {
                ActivityOrderInfomation.navigate(ActivityOrderDetails.this, v.findViewById(R.id.image), obj,CamScanner);
            }
        });

    }

    public Boolean CheckIfOrdersComplete(List<Order> orders)
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

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }


}

