package com.app.sample.GoFleetNavigation.model;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order implements Serializable
{
    private String ID;
    private String street;
    private String suburb;
    private String sequenceNumber;
    private String placeID;
    private double longitude;
    private double latitude;
    private boolean delivered;
    private String route_ID;
    private long eta_Sec;
    private String client_Business_Name;
    private String deliverByDate;



    private String ClientNum;

    private int brickQ;
    private int cementQ;
    private int sandQ;

    private boolean isCancelled;


    private List<Integer> list;

    public Order()
    {

    }

    public Order(String ID, String street, String subrub, String sequenceNumber, String placeID, double longitude,
                 double latitude, boolean delivered, String route_ID, long eta_Sec ,String client_Business_Name,List<Integer> list_delays,
                 int Brickq, int Cementq, int SandQ, String dateDeliverby, String ClientNumber, boolean cancelled)
    {
        this.ID = ID;
        this.street = street;
        this.suburb = subrub;
        this.sequenceNumber = sequenceNumber;
        this.placeID = placeID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.delivered = delivered;
        this.route_ID = route_ID;
        this.eta_Sec = eta_Sec;
        this.client_Business_Name = client_Business_Name;
        this.list = list_delays;
        this.brickQ = Brickq;
        this.sandQ = SandQ;
        this.cementQ = Cementq;
        this.deliverByDate = dateDeliverby;
        this.ClientNum = ClientNumber;
        this.isCancelled = cancelled;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String subrub) {
        this.suburb = subrub;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isDelivered()
    {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public String getRoute_ID() {
        return route_ID;
    }

    public void setRoute_ID(String route_ID) {
        this.route_ID = route_ID;
    }

    public long getEta_Sec() {
        return eta_Sec;
    }

    public void setEta_Sec(long eta_Sec) {
        this.eta_Sec = eta_Sec;
    }

    public String getClient_Business_Name() {
        return client_Business_Name;
    }

    public void setClient_Business_Name(String client_Business_Name) {
        this.client_Business_Name = client_Business_Name;
    }

    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    public int getBrickQ() {
        return brickQ;
    }

    public void setBrickQ(int brickQ) {
        this.brickQ = brickQ;
    }

    public int getCementQ() {
        return cementQ;
    }

    public void setCementQ(int cementQ) {
        this.cementQ = cementQ;
    }

    public int getSandQ() {
        return sandQ;
    }

    public void setSandQ(int sandQ) {
        this.sandQ = sandQ;
    }

    public String getDeliverByDate() {
        return deliverByDate;
    }

    public void setDeliverByDate(String deliverByDate) {
        this.deliverByDate = deliverByDate;
    }

    public String getClientNum() {
        return ClientNum;
    }

    public void setClientNum(String clientNum) {
        ClientNum = clientNum;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }


    public static Order getOrder(JSONObject jsonObject, JSONObject jsonObject2, JSONObject jsonObject3, JSONObject jsonObject4) throws JSONException //takes json object and given back user object
    {
        String id = jsonObject.getString("order_Id");
        String street = jsonObject3.getString("location_Street");
        String subrub = jsonObject3.getString("location_Suburb");
        String sequence_number = jsonObject.getString("order_Sequence_Number");
        String place_id = jsonObject3.getString("location_Places_Id");
        double location_longtitude = jsonObject3.getDouble("location_Longitude");
        double location_latitude = jsonObject3.getDouble("location_Latitude");;

        boolean order_delivered = jsonObject.getBoolean("order_Delivered");

        boolean order_Cancelled= jsonObject.getBoolean("order_Cancelled");

        String routeID = jsonObject4.getString("route_Id");
        long eta_Sec = jsonObject4.getLong("route_ETA_In_Seconds");

        String clientBN = jsonObject2.getString("client_Business_Name");
        String ClientNumber = jsonObject2.getString("client_Contact_Number");

        String date = jsonObject.getString("order_Deliver_By_Date");

        JSONArray jaary =  jsonObject4.getJSONArray("postponedOrderIds"); // 22 and 30

        List<Integer> list = new ArrayList<>();

        for(int i = 0; i < jaary.length();i++) //loads delayed orders
        {
            list.add(jaary.getInt(i));
        }

        JSONArray jaaryQuantity =  jsonObject.getJSONArray("items");

        List<String> listQuantitys = new ArrayList<>();

        for(int i = 0; i < jaaryQuantity.length();i++) //loads delayed orders
        {
            JSONObject jj = jaaryQuantity.getJSONObject(i);

            String gg = jj.getString("quantity_Ordered");

            listQuantitys.add(gg);
        }

        int BrickQ = 0;
        int SandQ = 0;
        int CementQ = 0;

        if(listQuantitys != null)
        {
            for(int i = 0; i < listQuantitys.size();i++)
            {
                if(i == 0)
                {
                    BrickQ = Integer.parseInt(listQuantitys.get(0));
                }
                else if(i == 1)
                {
                    SandQ = Integer.parseInt(listQuantitys.get(1));
                }
                else if(i == 2)
                {
                    CementQ = Integer.parseInt(listQuantitys.get(2));
                }
            }
        }

        Order order = new Order(id,street,subrub,sequence_number,place_id,location_longtitude,location_latitude,order_delivered,routeID,eta_Sec,clientBN,list,BrickQ,CementQ,SandQ,date,ClientNumber, order_Cancelled);

        return order;

    }


    public static List<Order> getOrders(JSONObject jsonObject) throws  JSONException
    {
        List<Order> orders = new ArrayList<>();

        for(int i = 0; i != jsonObject.getJSONArray("orders").length(); i++)
        {
            JSONArray jsonObject1 = jsonObject.getJSONArray("orders"); //first get json array out of object

            JSONObject jsonObject2 = jsonObject1.getJSONObject(i); // retrieve json object out of json array ... entire json object ... Order details

            JSONObject jsonObject3 = jsonObject2.getJSONObject("client"); //target json object ... 1 order object at a time

            JSONObject jsonObject4 = jsonObject2.getJSONObject("location"); //target json object ... 1 order object at a time

         //   JSONArray jsonObject5 = jsonObject2.getJSONArray("items");
//
//            JSONObject jsonObject6 = jsonObject5.getJSONObject(i);
//
//            JSONObject jsonObject7 = jsonObject6.get("Bricks")

            Order order = Order.getOrder(jsonObject2,jsonObject3,jsonObject4, jsonObject);

            // jsonObject 2 - Order details ... jsonObject 3 - client details ... jsonobject 4 - location ... jSon Object - order deets

            orders.add(order);

        }

        return orders;
    }


    public static List<Order> getPastOrders(JSONArray jsonArray) throws  JSONException
    {
        List<Order> orders = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++) //runs twice for 2 objects
        {

            for(int j = 0; j < jsonArray.getJSONObject(i).getJSONArray("orders").length(); j++) //needs to run as many times as there are orders
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i); //  Order details

                JSONArray jsonArray2 = jsonObject.getJSONArray("orders"); // orderID - client details

                JSONObject jsonObject3 = jsonArray2.getJSONObject(j); //

                JSONObject jsonObject4 = jsonObject3.getJSONObject("client"); //client

                JSONObject jsonObject5 = jsonObject3.getJSONObject("location"); //location

                Order order = Order.getPastOrder(jsonObject3,jsonObject4,jsonObject5,jsonObject);

                // jsonObject 2 - Order details ... jsonObject 3 - client details ... jsonobject 4 - location ... jSon Object - order deets

                orders.add(order);
            }



        }

        return orders;
    }

    public static Order getPastOrder(JSONObject jsonObject, JSONObject jsonObject2,JSONObject jsonObject3, JSONObject jsonObject4) throws JSONException //takes json object and given back user object
    {
        String id = jsonObject.getString("order_Id");
        String street = jsonObject3.getString("location_Street");
        String subrub = jsonObject3.getString("location_Suburb");
        String sequence_number = jsonObject.getString("order_Sequence_Number");
        String place_id = jsonObject3.getString("location_Places_Id");
        double location_longtitude = jsonObject3.getDouble("location_Longitude");
        double location_latitude = jsonObject3.getDouble("location_Latitude");;

        boolean order_delivered = jsonObject.getBoolean("order_Delivered");
        boolean order_Cancelled= jsonObject.getBoolean("order_Cancelled");

        String routeID = jsonObject4.getString("route_Id");
        long eta_Sec = jsonObject4.getLong("route_ETA_In_Seconds");

        String clientBN = jsonObject2.getString("client_Business_Name");

        String date = jsonObject.getString("order_Deliver_By_Date");


        List<Integer> list = new ArrayList<>();

        list = null;


        Order order = new Order(id,street,subrub,sequence_number,place_id,location_longtitude,location_latitude,order_delivered,routeID,eta_Sec,clientBN,list,0,0,0,date,null,order_Cancelled);

        return order;

    }




}
