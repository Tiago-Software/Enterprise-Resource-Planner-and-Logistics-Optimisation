package com.app.sample.insta.model;

import java.io.Serializable;
import java.util.StringTokenizer;

public class LottoTicket implements Serializable
{
    private String TicketID;
    private String TicketType;
    private String DateOfPurc;
    private String DateOfDraw;

    private String[] TBoards;

    private int MatchRate;
    private String TicketPrice;

   // private double EstWinnings;

    public LottoTicket()
    {

    }

    public LottoTicket(String ticketID,String ticketType,String dateOfPurc,String dateOfDraw,String[] tBoards)
    {
            this.TicketID = ticketID;
            this.TicketType = ticketType;
            this.DateOfPurc = dateOfPurc;
            this.DateOfDraw = dateOfDraw;
            this.TBoards = tBoards;
         //   this.EstWinnings = estWinnings;
    }

    public String getTicketID()
    {
        return TicketID;
    }

    public void setTicketID(String ticketID)
    {
        TicketID = ticketID;
    }

    public String getTicketType()
    {
        return TicketType;
    }

    public void setTicketType(String ticketType)
    {
        TicketType = ticketType;
    }

    public String getDateOfPurc()
    {
        return DateOfPurc;
    }

    public void setDateOfPurc(String dateOfPurc)
    {
        DateOfPurc = dateOfPurc;
    }

    public String getDateOfDraw()
    {
        return DateOfDraw;
    }

    public void setDateOfDraw(String dateOfDraw)
    {
        DateOfDraw = dateOfDraw;
    }


    public String[] getTBoards()
    {
        return TBoards;
    }

    public void setTBoards(String[] TBoards)
    {
        this.TBoards = TBoards;
    }

    public String setTBoardEx(int i)
    {

        String Board = this.TBoards[i];

        return Board;
    }

    public String getBoardText(int id, int pos)
    {
        String Text = "";


        String Board = this.TBoards[id];

        String delim = ":";

        StringTokenizer tok = new StringTokenizer(Board, delim, true);

        String LottoLine = tok.nextToken();
        String LottoLineN = tok.nextToken();

        String LottoLineN2 = tok.nextToken();

        StringTokenizer tok2 = new StringTokenizer(LottoLineN2);

        for(int i = 0; i < pos;i++)
        {
            Text = tok2.nextToken();
        }

        return Text;
    }

    public int getMatchRate()
    {
        return MatchRate;
    }

    public void setMatchRate(int matchRate)
    {
        MatchRate = matchRate;
    }


    public String getTicketPrice()
    {
        return TicketPrice;
    }

    public void setTicketPrice(String ticketPrice)
    {
        TicketPrice = ticketPrice;
    }


/*    public double getEstWinnings()
    {
        return EstWinnings;
    }

    public void setEstWinnings(double estWinnings)
    {
        EstWinnings = estWinnings;
    }*/







}
