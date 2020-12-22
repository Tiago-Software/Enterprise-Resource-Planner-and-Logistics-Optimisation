package com.app.sample.insta.model;

import java.util.ArrayList;
import java.util.Date;

public class TicketFirestoreModel {
    private String documentID;
    private ArrayList<String> Boards;
    private Date Draw_Date;
    private Date Purchase_Date;
    private String Ticket_Type;
    private int EstWinnings;

    public TicketFirestoreModel(){ }

    public TicketFirestoreModel(String documentID, ArrayList<String> boards, Date Draw_Date, Date Purchase_Date, String Ticket_Type, int EstWinnings)
    {
        this.documentID = documentID;
        this.Boards = boards;
        this.Draw_Date = Draw_Date;
        this.Purchase_Date = Purchase_Date;
        this.Ticket_Type = Ticket_Type;
        this.EstWinnings = EstWinnings;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public ArrayList<String> getBoards() {
        return Boards;
    }

    public void setBoards(ArrayList<String> boards) {
        Boards = boards;
    }

    public Date getDraw_Date() {
        return Draw_Date;
    }

    public void setDraw_Date(Date draw_Date) {
        Draw_Date = draw_Date;
    }

    public Date getPurchase_Date() {
        return Purchase_Date;
    }

    public void setPurchase_Date(Date purchase_Date) {
        Purchase_Date = purchase_Date;
    }

    public String getTicket_Type() {
        return Ticket_Type;
    }

    public void setTicket_Type(String ticket_Type) {
        Ticket_Type = ticket_Type;
    }

    public int getEstWinnings()
    {
        return EstWinnings;
    }

    public void setEstWinnings(int estWinnings) {
        EstWinnings = estWinnings;
    }
}
