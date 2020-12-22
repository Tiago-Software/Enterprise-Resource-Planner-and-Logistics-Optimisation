package com.app.sample.insta.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.app.sample.insta.R;
import com.app.sample.insta.adapter.FollowingListAdapter;
import com.app.sample.insta.adapter.YouListAdapter;
import com.app.sample.insta.data.Constant;
import com.app.sample.insta.model.Friend;
import com.app.sample.insta.model.LottoTicket;
import com.app.sample.insta.model.TicketFirestoreModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PageFriendFragment extends Fragment {

    private View view;
    private TabLayout friend_tabs;
    private RecyclerView recyclerView;

    private List<LottoTicket> items_ticket = new ArrayList<>();

    private List<Friend> items_following = new ArrayList<>();



   // private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.page_fragment_friend, container, false);

        // activate fragment menu
        setHasOptionsMenu(true);

        List<Friend> items = Constant.getFriendsData(getActivity());

        List<LottoTicket> item = new ArrayList<>();

       // getDocuments(); //DB extraction

        items_ticket = item.subList(0,item.size());

        items_following = items.subList(1, items.size()-1);

        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        friend_tabs = view.findViewById(R.id.friend_tabs);
        friend_tabs.addTab(friend_tabs.newTab().setText("Past"), true);
        friend_tabs.addTab(friend_tabs.newTab().setText("Present"));

        friend_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    showListFollowing(); //past
                }else if(tab.getPosition()==1){
                    showListYou();  // Present
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }

        });
        return view;
    }

    private void showListFollowing(){
        FollowingListAdapter mAdapter = new FollowingListAdapter(getActivity(), items_following);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new FollowingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Friend obj, int position) {

            }
        });
    }

    private void showListYou()
    {
        //need to pass documents as objects to you list adapter
        YouListAdapter mAdapter = new YouListAdapter(getActivity(), items_ticket);

        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new YouListAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, LottoTicket obj, int position) {
//                Intent intent = new Intent(getActivity(), ActivityTicket.class);
//                intent.putExtra("LottoTicketObj", obj); //passes objects
//                startActivity(intent);
            }

        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_friend, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Snackbar.make(view,item.getTitle() + " Coming Soon", Snackbar.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

 /*   public void getDocuments()
    {

      //  firebaseAuth = FirebaseAuth.getInstance();

    //    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        database = FirebaseFirestore.getInstance();

      //  String uID = firebaseUser.getUid();

            database.collection("Users").document(uID).collection("tickets").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.isEmpty()){
                        Log.d("ActivityTicket", "onSuccess: LIST EMPTY");
                        return;
                    }else{
                        int count = 0;
                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            if (documentSnapshot.exists()){
                                // TODO: data conversions
                                TicketFirestoreModel ticketFirestoreModel = documentSnapshot.toObject(TicketFirestoreModel.class);
                                //document snapshot Id
                                String ticketID = documentSnapshot.getId();

                                //String ticketID = ticketFirestoreModel.getDocumentID();
                                String tType= ticketFirestoreModel.getTicket_Type();
                                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                                String Draw_date = df.format(ticketFirestoreModel.getDraw_Date());
                                String Purchase_Date = df.format(ticketFirestoreModel.getPurchase_Date());

                                //  double EstimatedWinnings = ticketFirestoreModel.getEstWinnings();


                                // declaration and initialise String Array
                                ArrayList<String> arrayList = ticketFirestoreModel.getBoards();
                                String str[] = new String[arrayList.size()];
                                // ArrayList to Array Conversion
                                for (int j = 0; j < arrayList.size(); j++) {

                                    // Assign each value to String array
                                    str[j] = arrayList.get(j);
                                    System.out.println(Arrays.toString(str));
                                }
                                //object created and read in from database
                                LottoTicket LT = new LottoTicket(ticketID,tType,Purchase_Date,Draw_date,str);
                                items_ticket.add(LT); //adding multiple objects to array list

                            }
                            count++;
                        }
                        System.out.println(count);
                    }
                }
            });
        }*/






}
