package com.app.sample.GoFleetNavigation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.app.sample.GoFleetNavigation.R;
import com.app.sample.GoFleetNavigation.model.Order;
import com.balysv.materialripple.MaterialRippleLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> implements Filterable
{

    private List<Order> original_items = new ArrayList<>();
    private List<Order> filtered_items = new ArrayList<>();
    private ItemFilter mFilter = new ItemFilter();

    public static Boolean Complete;

    private Context ctx;

    // for item click listener
    private OnItemClickListener mOnItemClickListener;
    private boolean clicked = false;

    public interface OnItemClickListener {
        void onItemClick(View view, Order obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  //items
    {
        // each data item is just a string in this case
        public TextView orderID;
        public TextView location;
        public CheckBox checkBox;
        public CheckBox chckbxPostponed;
        public CheckBox chckbxCancelled;

        public MaterialRippleLayout lyt_parent;

        public TextView ClientBusName;
        public TextView ClientAddress;


        public TextView DeliverByDate;

        public ViewHolder(View v) {
            super(v);
            orderID = (TextView) v.findViewById(R.id.order);
           // location = (TextView) v.findViewById(R.id.location);
            checkBox = (CheckBox) v.findViewById(R.id.chckbxComplete);

            chckbxPostponed = (CheckBox) v.findViewById(R.id.chckbxDelayed);

            chckbxCancelled = (CheckBox) v.findViewById(R.id.chckbxCanceled);

            ClientBusName = (TextView) v.findViewById(R.id.ClientBusname);

            ClientAddress = (TextView) v.findViewById(R.id.ClientStreet);



            DeliverByDate = (TextView) v.findViewById(R.id.DeliverByDate);

            lyt_parent = (MaterialRippleLayout) v.findViewById(R.id.lyt_parent);
        }
    }

    public Filter getFilter() {
        return mFilter;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public OrderListAdapter(Context ctx, List<Order> items) {
        this.ctx = ctx;
        original_items = items;
        filtered_items = items;
    }

    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  //item order
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {

        final Order p = original_items.get(position);

            holder.orderID.setText(p.getSequenceNumber());
//            holder.location.setText(p.getStreet());

        holder.ClientBusName.setText("Client: " +p.getClient_Business_Name());
        holder.ClientAddress.setText("Location Suburb: " + p.getSuburb());

        holder.DeliverByDate.setText(p.getDeliverByDate());

        if(p.isDelivered() == true)
        {
            holder.checkBox.setChecked(true);
        }

        if(p.isCancelled() == true)
        {
            holder.chckbxCancelled.setChecked(true);
        }

        if(p.getList() != null)
        {
            for(int i = 0;i < p.getList().size();i++)
            {
                int delayNum = p.getList().get(i);

                if(Integer.parseInt(p.getID()) == delayNum)
                {
                    holder.chckbxPostponed.setChecked(true);
                }

            }
        }



        setAnimation(holder.lyt_parent, position);
        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!clicked && mOnItemClickListener != null) {
                    clicked = true;
                    mOnItemClickListener.onItemClick(view, p, position);
                }
            }
        });
        clicked = false;

    }

    //Here is the key method to apply the animation
    private int lastPosition = -1;

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
            Animation animation = AnimationUtils.loadAnimation(ctx, R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return filtered_items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String query = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<Order> list = original_items;
            final List<Order> result_list = new ArrayList<>(list.size());

//
//            for (int i = 0; i < list.size(); i++)
//            {
//                int str_title = list.get(i).getID();
//                String str_cat = list.get(i).getCategory().getName();
//                if (str_title.toLowerCase().contains(query) || str_cat.toLowerCase().contains(query)) {
//                    result_list.add(list.get(i));
//                }
//            }

            results.values = result_list;
            results.count = result_list.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filtered_items = (List<Order>) results.values;
            notifyDataSetChanged();
        }

    }
}
