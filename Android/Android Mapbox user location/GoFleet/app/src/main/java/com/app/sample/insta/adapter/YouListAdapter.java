package com.app.sample.insta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.sample.insta.ActivityCamera;
import com.app.sample.insta.R;
import com.app.sample.insta.model.LottoTicket;

import java.util.ArrayList;
import java.util.List;

public class YouListAdapter extends RecyclerView.Adapter<YouListAdapter.ViewHolder>
{

    private List<LottoTicket> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    private String[] LN = new String[5];

   // private LottoTicket LT;

    private LottoTicket LT = ActivityCamera.LT;


    public interface OnItemClickListener
    {
        void onItemClick(View view, LottoTicket obj, int position); //change from freind to LottoTicket
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener = mItemClickListener;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public YouListAdapter(Context context, List<LottoTicket> items)
    {
        this.items = items;
        ctx = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public TextView name;
        public ImageView image;
        public LinearLayout lyt_parent;

        public TextView Num1;
        public TextView Num2;
        public TextView Num3;
        public TextView Num4;
        public TextView Num5;
        public TextView Num6;

        public TextView Date;

        public ViewHolder(View v)
        {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            image = (ImageView) v.findViewById(R.id.image);
            lyt_parent = (LinearLayout) v.findViewById(R.id.lyt_parent);

            Date = (TextView) v.findViewById(R.id.date);

            Num1 = v.findViewById(R.id.num1);
            Num2 = v.findViewById(R.id.num2);
            Num3 = v.findViewById(R.id.num3);
            Num4 = v.findViewById(R.id.num4);
            Num5 = v.findViewById(R.id.num5);
            Num6 = v.findViewById(R.id.num6);

/*

            if(LT != null)
            {

                LN = LT.getTBoards();

                String delim = ":";

                StringTokenizer tok = new StringTokenizer(LN[0], delim, true);

                String LottoLine = tok.nextToken();
                String LottoLineN = tok.nextToken();

                String LottoLineN2 = tok.nextToken();

                //   String LotoNums = " 01 17 20 23 26 29";

                // StringTokenizer tok2 = new StringTokenizer(LottoLineN2, " ", true);
                StringTokenizer tok2 = new StringTokenizer(LottoLineN2);


                //     String token = tok2.nextToken();

                //    System.out.println(token);

                Num1 = (TextView) v.findViewById(R.id.num1);

                Num1.setText(tok2.nextToken());

                Num2 = (TextView) v.findViewById(R.id.num2);
                Num2.setText(tok2.nextToken());

                Num3 = (TextView) v.findViewById(R.id.num3);
                Num3.setText(tok2.nextToken());

                Num4 = (TextView) v.findViewById(R.id.num4);
                Num4.setText(tok2.nextToken());

                Num5 = (TextView) v.findViewById(R.id.num5);
                Num5.setText(tok2.nextToken());

                Num6 = (TextView) v.findViewById(R.id.num6);
                Num6.setText(tok2.nextToken());

                image.setImageResource(R.drawable.dailylotto);
            }
            else if(items != null)
            {

                String delim = ":";

                //retrive items out of array list

                String lottoNum = "A06: 0 2 0 0 0 0";

                StringTokenizer tok = new StringTokenizer(lottoNum, delim, true);

                String LottoLine = tok.nextToken();
                String LottoLineN = tok.nextToken();

                String LottoLineN2 = tok.nextToken();

                //   String LotoNums = " 01 17 20 23 26 29";

                // StringTokenizer tok2 = new StringTokenizer(LottoLineN2, " ", true);
                StringTokenizer tok2 = new StringTokenizer(LottoLineN2);


                //     String token = tok2.nextToken();

                //    System.out.println(token);

                Num1 = (TextView) v.findViewById(R.id.num1);

                Num1.setText(tok2.nextToken());

                Num2 = (TextView) v.findViewById(R.id.num2);
                Num2.setText(tok2.nextToken());

                Num3 = (TextView) v.findViewById(R.id.num3);
                Num3.setText(tok2.nextToken());

                Num4 = (TextView) v.findViewById(R.id.num4);
                Num4.setText(tok2.nextToken());

                Num5 = (TextView) v.findViewById(R.id.num5);
                Num5.setText(tok2.nextToken());

                Num6 = (TextView) v.findViewById(R.id.num6);
                Num6.setText(tok2.nextToken());

                image.setImageResource(R.drawable.dailylotto);
            }
            else
            {

                String delim = ":";

                String lottoNum = "A06: 0 0 0 0 0 0";

                StringTokenizer tok = new StringTokenizer(lottoNum, delim, true);

                String LottoLine = tok.nextToken();
                String LottoLineN = tok.nextToken();

                String LottoLineN2 = tok.nextToken();

                //   String LotoNums = " 01 17 20 23 26 29";

                // StringTokenizer tok2 = new StringTokenizer(LottoLineN2, " ", true);
                StringTokenizer tok2 = new StringTokenizer(LottoLineN2);


                //     String token = tok2.nextToken();

                //    System.out.println(token);

                Num1 = (TextView) v.findViewById(R.id.num1);

                Num1.setText(tok2.nextToken());

                Num2 = (TextView) v.findViewById(R.id.num2);
                Num2.setText(tok2.nextToken());

                Num3 = (TextView) v.findViewById(R.id.num3);
                Num3.setText(tok2.nextToken());

                Num4 = (TextView) v.findViewById(R.id.num4);
                Num4.setText(tok2.nextToken());

                Num5 = (TextView) v.findViewById(R.id.num5);
                Num5.setText(tok2.nextToken());

                Num6 = (TextView) v.findViewById(R.id.num6);
                Num6.setText(tok2.nextToken());

                image.setImageResource(R.drawable.dailylotto);
            }*/

        }

    }


    @Override
    public YouListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_you, parent, false);

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        LottoTicket lt = items.get(position);

        final LottoTicket c = items.get(position);
        holder.name.setText(lt.getTicketType());

        holder.Date.setText(lt.getDateOfPurc());

        holder.Num1.setText(lt.getBoardText(0, 1));
        holder.Num2.setText(lt.getBoardText(0, 2));
        holder.Num3.setText(lt.getBoardText(0, 3));
        holder.Num4.setText(lt.getBoardText(0, 4));
        holder.Num5.setText(lt.getBoardText(0, 5));
        holder.Num6.setText(lt.getBoardText(0, 6));

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, c, position);

                };
            }
        });
    }

    public LottoTicket getItem(int position)
    {
        return items.get(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return items.size();
    }

}