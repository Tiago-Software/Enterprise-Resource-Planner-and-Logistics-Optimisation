package com.app.sample.insta.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.sample.insta.ActivityHelp;
import com.app.sample.insta.ActivityLogin;
import com.app.sample.insta.ActivityOrderDetails;
import com.app.sample.insta.R;
import com.app.sample.insta.adapter.ResultsListAdapter;
import com.app.sample.insta.adapter.YouListAdapter;
import com.app.sample.insta.data.Constant;
import com.app.sample.insta.model.Feed;
import com.app.sample.insta.model.Friend;
import com.app.sample.insta.model.LottoTicket;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class PageHomeFragment extends Fragment {

    private View view;
    private ProgressBar progressbar;
    private RecyclerView recyclerView;
    private ResultsListAdapter mAdapter;
    private TabLayout home_tabs;

    private List<LottoTicket> items_you = new ArrayList<>();
    private List<Friend> items_following = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.orders, container, false);

//        Intent i = null;
//        i = new Intent(this, ActivityOrderDetails.class);
//        startActivity(i);



/*        List<Friend> items = Constant.getFriendsData(getActivity());


        List<LottoTicket> item = new ArrayList<>();

        LottoTicket LT1 = new LottoTicket();
        String[] Board = new String[1];
        Board[0] = "A00: 0 0 0 0 0 0";
        LT1.setTBoards(Board);

        item.add(LT1);

        LottoTicket LT2 = new LottoTicket();
        LT2.setTBoards(Board);

        item.add(LT2);

        items_you = item.subList(0,item.size());
        items_following = items.subList(0, 1);

        // activate fragment menu
        setHasOptionsMenu(true);
        progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        home_tabs = (TabLayout) view.findViewById(R.id.home_tabs);
        home_tabs.addTab(home_tabs.newTab().setText("Results"),true);
        home_tabs.addTab(home_tabs.newTab().setText("Winning"));

        home_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    showResults(); //results
                }else if(tab.getPosition()==1){
                    showListYou(); // winning
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}

        });*/



//        recyclerView.setHasFixedSize(true);

        if (!taskRunning) {
//            new DummyFeedLoader().execute("");
        }
        return view;
    }


    private void showListYou(){
        YouListAdapter mAdapter = new YouListAdapter(getActivity(), items_you);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new YouListAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, LottoTicket obj, int position) {

            }
        });
    }

    private void showResults()
    {
        ResultsListAdapter mAdapter = new ResultsListAdapter(getActivity(), items_following);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new YouListAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, LottoTicket obj, int position) {

            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_logout: {

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setCancelable(false);
                dialog.setTitle("Logout");
                dialog.setMessage("Do you wish to logout?");
                dialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        signOut();
                        Intent i = new Intent(getActivity(), ActivityLogin.class);
                        startActivity(i);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });
                dialog.create();
                dialog.show();

                return true;
            } case R.id.action_settings: {
                Snackbar.make(view, "Setting Clicked", Snackbar.LENGTH_SHORT).show();
                return true;
            } case R.id.action_help: {
                Intent i = new Intent(getActivity(), ActivityHelp.class);
                startActivity(i);
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean taskRunning = false;

    private class DummyFeedLoader extends AsyncTask<String, String, String> {
        private String status = "";
        private List<Feed> items = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            taskRunning = true;
            items.clear();
//            progressbar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Thread.sleep(500);
                items = Constant.getRandomFeed(getActivity());
                status = "success";
            } catch (Exception e) {
                status = "failed";
            }
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            progressbar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            if (status.equals("success")) {
                //set data and list adapter
                mAdapter = new ResultsListAdapter(getActivity(), items_following);
                recyclerView.setAdapter(mAdapter);
            }
            taskRunning = false;
            super.onProgressUpdate(values);
        }
    }
    public void signOut()
    {

    }


}
