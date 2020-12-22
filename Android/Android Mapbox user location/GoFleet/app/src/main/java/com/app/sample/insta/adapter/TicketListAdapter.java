package com.app.sample.insta.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.sample.insta.ActivityCamera;
import com.app.sample.insta.R;
import com.app.sample.insta.model.LottoTicket;

import java.util.ArrayList;
import java.util.List;

public class TicketListAdapter extends RecyclerView.Adapter<TicketListAdapter.ViewHolder>
{

    private String[] LN = new String[5];

    // private LottoTicket LT;

    private LottoTicket LT = ActivityCamera.LT;

    private List<LottoTicket> items_ticket = new ArrayList<>();

    public TicketListAdapter(List<LottoTicket> ticketitems)
    {
        this.items_ticket = ticketitems;
    }

    @Override
    public TicketListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ticket, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketListAdapter.ViewHolder holder, int position)
    {
        LottoTicket lt = items_ticket.get(position);

        holder.Num1.setText(lt.getBoardText(position, 1));
        holder.Num2.setText(lt.getBoardText(position, 2));
        holder.Num3.setText(lt.getBoardText(position, 3));
        holder.Num4.setText(lt.getBoardText(position, 4));
        holder.Num5.setText(lt.getBoardText(position, 5));
        holder.Num6.setText(lt.getBoardText(position, 6));

        switch(position) {
            case 0:

                holder.boardNum.setText("A");
                break;
            case 1:

                holder.boardNum.setText("B");
                break;
            case 2:

                holder.boardNum.setText("C");
                break;
            case 3:

                holder.boardNum.setText("D");
                break;
            case 4:

                holder.boardNum.setText("E");
                break;
            case 5:

                holder.boardNum.setText("F");
                break;
            case 6:

                holder.boardNum.setText("G");
                break;
            case 7:

                holder.boardNum.setText("H");
                break;
            case 8:

                holder.boardNum.setText("I");
                break;
            case 9:

                holder.boardNum.setText("J");
                break;
            default:
                // code block
        }


        //TODO switch from in 0 to A --DONE


    }

    @Override
    public int getItemCount()
    {
        return items_ticket.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView Num1;
        public TextView Num2;
        public TextView Num3;
        public TextView Num4;
        public TextView Num5;
        public TextView Num6;

        public TextView DrawDate;
        public TextView scanDate;

        public TextView estWinnings;

        public TextView boardNum;

        public ViewHolder(View v)
        {
            super(v);
            Num1 = v.findViewById(R.id.Tnum1);
            Num2 = v.findViewById(R.id.Tnum2);
            Num3 = v.findViewById(R.id.Tnum3);
            Num4 = v.findViewById(R.id.Tnum4);
            Num5 = v.findViewById(R.id.Tnum5);
            Num6 = v.findViewById(R.id.Tnum6);
            boardNum = v.findViewById(R.id.boardtype);

            DrawDate = v.findViewById(R.id.drawDate);

            scanDate = v.findViewById(R.id.ScanDate);

            estWinnings = v.findViewById(R.id.EstimateWinnings);

        }

    }


}
