package com.app.sample.GoFleetNavigation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.sample.GoFleetNavigation.R;
import com.google.android.material.tabs.TabLayout;

public class PageHomeFragment extends Fragment {

    private View view;
    private ProgressBar progressbar;
    private RecyclerView recyclerView;
    private TabLayout home_tabs;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.orders, container, false);

//        Intent i = null;
//        i = new Intent(this, ActivityOrderDetails.class);
//        startActivity(i);


        return view;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.action_logout: {
//
//                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
//                dialog.setCancelable(false);
//                dialog.setTitle("Logout");
//                dialog.setMessage("Do you wish to logout?");
//                dialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which)
//                    {
//                        signOut();
//                        Intent i = new Intent(getActivity(), ActivityLogin.class);
//                        startActivity(i);
//                    }
//                });
//                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
//                {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which)
//                    {
//                        dialog.cancel();
//                    }
//                });
//                dialog.create();
//                dialog.show();
//
//                return true;
//            } case R.id.action_settings: {
//                Snackbar.make(view, "Setting Clicked", Snackbar.LENGTH_SHORT).show();
//                return true;
//            } case R.id.action_help: {
//                Intent i = new Intent(getActivity(), ActivityHelp.class);
//                startActivity(i);
//                return true;
//            }
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void signOut()
    {

    }


}
