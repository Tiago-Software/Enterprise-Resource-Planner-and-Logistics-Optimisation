package com.app.sample.GoFleetNavigation;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.sample.GoFleetNavigation.adapter.OrderListAdapter;
import com.app.sample.GoFleetNavigation.adapter.PastOrderListAdapter;
import com.app.sample.GoFleetNavigation.model.AbstractAPIListener;
import com.app.sample.GoFleetNavigation.model.Order;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ActivityPastOrderDetails extends AppCompatActivity
{

    public static final String EXTRA_OBJCT = "com.app.sample.GoFleetNavigation.OBJ";

    public RecyclerView recyclerView;
    private PastOrderListAdapter mAdapter;

    private OrderListAdapter mAdapterOrder;

    private String orderRead;
    private View parent_view;
    private Boolean CamScanner = false;

    private Boolean DelayedOrders = false;

    private static Boolean CompleteRoute = false;

    private List<Order> ordersLoad;

    private boolean open = false;

    private boolean one;

    private List<Order> Routes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details_past);   //order details
        parent_view = findViewById(android.R.id.content);

        fabPast = findViewById(R.id.fabMapPast);


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


        Application application = this.getApplication();
        ModelApi modelApi = ModelApi.getInstance(application);

        ordersLoad = new ArrayList<>();
        Routes = new ArrayList<>();

        one = false;


        modelApi.loadPastOrders(new AbstractAPIListener()
        {
            @Override
            public void onPastOrdersLoaded(List<Order> order)
            {
                ordersLoad.clear();
                ordersLoad.addAll(order);


                                                                                      //0,  1,  2,  3, 4
                   // int routeID = Integer.parseInt(ordersLoad.get(0).getRoute_ID()); //33, 33, 33, 33, 34, 34, 38, 38

                    Routes.add(ordersLoad.get(0));


                    for(int j = 1; j < ordersLoad.size(); j++)
                    {
                        int routeID = Integer.parseInt(ordersLoad.get(j-1).getRoute_ID()); //33, 33, 33, 33, 34, 34, 38, 38
                        int routeID2 = Integer.parseInt(ordersLoad.get(j).getRoute_ID());

                        if(routeID != routeID2)
                        {
                            Routes.add(ordersLoad.get(j)); // 33, 34, 38

                            one = true;
                        }

                    }

                    if(one == false)
                    {
                        Routes.add(ordersLoad.get(0));
                    }


                mAdapter.notifyDataSetChanged();
            }
        });


        mAdapter = new PastOrderListAdapter(this, Routes);


        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new PastOrderListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Order obj, int position) //passes current order object to next activity
            {
                ordersLoaded = RouteOrders(Integer.parseInt(obj.getRoute_ID()));
                refreshView(ordersLoaded);
                open = true;
                TextView routeNum = findViewById(R.id.RoutePast);
                routeNum.setVisibility(View.VISIBLE);
                routeNum.setText("Route " + obj.getRoute_ID());

                fabPast.setVisibility(parent_view.VISIBLE);
                fabPast.setClickable(true);
                posss = position;
            }
        });
    }

    private int posss;
    private List<Order> ordersLoaded;
    private FloatingActionButton fabPast;
    private boolean overview = false;

    public void StartMapOverviewPast(View v)
    {
        onPause();
        overview = true;
        Intent intent = new Intent(ActivityPastOrderDetails.this, ActivityRouteShowCase.class);
        Bundle args = new Bundle();
        args.putSerializable("ARRAYLIST",(Serializable)ordersLoaded);
        intent.putExtra("BUNDLE",args);

        startActivity(intent);


    }

    public void refreshView(List<Order> orders)
    {
        mAdapterOrder = new OrderListAdapter(this, orders);

        recyclerView.setAdapter(mAdapterOrder);
    }

    public List<Order> RouteOrders(int routeIDObj)
    {
        List<Order> Orders = new ArrayList<>();


        for(int j = 0; j < ordersLoad.size(); j++)
        {

            int routeID = Integer.parseInt(ordersLoad.get(j).getRoute_ID()); //33, 33, 33, 33, 34

            if(routeID == routeIDObj)
            {
                Orders.add(ordersLoad.get(j));
            }

        }

        return Orders;
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

    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onBackPressed()
    {
      //  super.onBackPressed();

        if(open == true)
        {
            fabPast.setVisibility(parent_view.GONE);
            recyclerView.setAdapter(mAdapter);
            TextView routeNum = findViewById(R.id.RoutePast);
            routeNum.setVisibility(View.INVISIBLE);

            open = false;

        }
        else
        {
            Intent i = null;
            i = new Intent(this, ActivityMain.class);
            startActivity(i);
            open = false;
        }


    }

}
